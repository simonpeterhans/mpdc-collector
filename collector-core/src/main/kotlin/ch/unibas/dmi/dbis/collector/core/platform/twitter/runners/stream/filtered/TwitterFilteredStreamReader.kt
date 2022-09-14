package ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream.filtered

import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.TwitterApiClient
import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.endpoints.TwitterApiOptions
import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.endpoints.TwitterFilteredStreamApi
import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.endpoints.TwitterFilteredStreamRulesApi
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.requests.AddStreamRuleRequest
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.requests.DeleteStreamRuleRequest
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.responses.AddStreamRuleResponse
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.responses.DeleteStreamRuleResponse
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.responses.StreamShowRuleResponse
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.responses.TweetStreamResponse
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.rules.Rule
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.rules.RuleDeleteRequest
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet.TwitterTweetSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream.RawTwitterStream
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream.TweetStreamElement
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream.TwitterStreamReader
import ch.unibas.dmi.dbis.collector.core.query.FetchStatus
import ch.unibas.dmi.dbis.collector.core.query.RequestResultWithStatus
import ch.unibas.dmi.dbis.collector.core.query.executeRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import java.io.InputStream
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

private val logger = KotlinLogging.logger {}

class TwitterFilteredStreamReader(
    apiKey: String
) : TwitterStreamReader<TwitterTweetSubQuery>(apiKey) {

    private val client: TwitterApiClient = TwitterApiClient(apiKey)

    private val streamApi: TwitterFilteredStreamApi = client.filteredStreamApi()
    private val streamRulesApi: TwitterFilteredStreamRulesApi = client.filteredStreamRules()

    private val queryStreams: ConcurrentHashMap<Long, LinkedBlockingQueue<TweetStreamElement>> =
        ConcurrentHashMap<Long, LinkedBlockingQueue<TweetStreamElement>>()

    private val mapper: ObjectMapper = jacksonObjectMapper().findAndRegisterModules()

    private val isRunning = AtomicBoolean(true)

    // We don't allow to instantiate this without clearing all rules.
    init {
        clearAllRules()
    }

    override fun isRunning() = isRunning.get()

    @Synchronized
    override fun getStream(query: TwitterTweetSubQuery): LinkedBlockingQueue<TweetStreamElement>? {
        return queryStreams[query.id!!]
    }

    @Synchronized
    override fun subscribe(query: TwitterTweetSubQuery): Boolean {
        // Already done or subscribed, return early.
        if (!isRunning.get() || queryStreams[query.id!!] != null) {
            return false
        }

        val stream = LinkedBlockingQueue<TweetStreamElement>()
        queryStreams[query.id!!] = stream

        addNewRules(query)

        return true
    }

    @Synchronized
    override fun unsubscribe(query: TwitterTweetSubQuery): Boolean {
        /*
         * We're already done (e.g., because something went wrong) - cleanup happens
         * before the stream, not after.
         */
        if (!isRunning.get()) {
            return false
        }

        val mapping = queryStreams.remove(query.id!!)

        if (mapping != null) {
            // Clear rules if we actually removed something.
            clearRules(query)

            // Quit if we have no more subscribers.
            if (queryStreams.isEmpty()) {
                isRunning.set(false)
            }
        }

        return true
    }

    private fun deleteRuleList(
        rules: List<Rule>
    ): RequestResultWithStatus<DeleteStreamRuleResponse> {
        val ruleDeletion = DeleteStreamRuleRequest(RuleDeleteRequest(rules.map { it.id!! }))
        val delReq = streamRulesApi.deleteStreamRules(ruleDeletion, false)
        return executeRequest(delReq, DeleteStreamRuleResponse::class.java, mapper, apiKey)
    }

    private fun clearAllRules(): Boolean {
        logger.info { "Clearing all rules for API key $apiKey." }

        val listReq = streamRulesApi.showStreamRules()
        val listRes = executeRequest(listReq, StreamShowRuleResponse::class.java, mapper, apiKey)

        if (listRes.response?.data == null) {
            return false
        }

        val rules = listRes.response.data

        if (rules.isEmpty()) {
            return true
        }

        val delRes = deleteRuleList(rules)

        return delRes.status == FetchStatus.SUCCESS
    }

    private fun clearRules(query: TwitterTweetSubQuery): Boolean {
        val listReq = streamRulesApi.showStreamRules()
        val listRes = executeRequest(listReq, StreamShowRuleResponse::class.java, mapper, query)

        if (listRes.response?.data == null) {
            return false
        }

        val rules = listRes.response.data

        val matchingRules = rules.filter { it.label == query.id!!.toString() }

        if (matchingRules.isEmpty()) {
            logger.info { "No matching rules found to delete for query with ID ${query.id}." }
            return true
        }

        val delRes = deleteRuleList(rules)

        return delRes.status == FetchStatus.SUCCESS
    }

    private fun addNewRules(query: TwitterTweetSubQuery): Boolean {
        val queryString = query.tweetQueryData.queryString

        // Add query rule.
        val req = streamRulesApi.addStreamRules(
            AddStreamRuleRequest(
                listOf(Rule(value = queryString, id = null, label = query.id!!.toString()))
            )
        )

        val res = executeRequest(req, AddStreamRuleResponse::class.java, mapper, query)

        return res.status == FetchStatus.SUCCESS
    }

    private fun readStream(rawTwitterStream: RawTwitterStream) {
        while (isRunning.get() && rawTwitterStream.isRunning()) {
            val twitterStreamElement = rawTwitterStream.buffer.poll(60, TimeUnit.SECONDS)

            if (twitterStreamElement == null) {
                logger.info { "No stream response in 60 seconds, terminating TwitterStreamReader." }
                break
            }

            if (twitterStreamElement.response.isBlank()) {
                // Ping, send to all streams - received every 20 seconds.
                queryStreams.values.forEach {
                    it.add(TweetStreamElement(twitterStreamElement.timestamp))
                }
            } else {
                /*
                 * Actual response, parse response, check which rule we matched, add to
                 * corresponding stream.
                 */
                val parsedResponse = mapper.readValue(
                    twitterStreamElement.response,
                    TweetStreamResponse::class.java
                )

                val matches = mutableListOf<Long>()
                // Ignore results that do not match any rule.
                parsedResponse.matchingRules?.forEach {
                    if (!it.label.isNullOrBlank()) {
                        matches.add(it.label.toLong())
                    }
                }

                matches.distinct().forEach {
                    val correspondingQueue = queryStreams[it]

                    correspondingQueue?.add(
                        TweetStreamElement(
                            twitterStreamElement.timestamp,
                            parsedResponse.data,
                            parsedResponse.includes
                        )
                    )
                }
            }
        }

        // Don't allow new threads to register since we're done.
        this.isRunning.set(false)
    }

    private fun getStream(backfill: Int? = null): InputStream {
        val req = streamApi.filteredStream(
            expansions = TwitterApiOptions.allExpansions,
            tweetFields = TwitterApiOptions.allTweetFields,
            userFields = TwitterApiOptions.allUserFields,
            mediaFields = TwitterApiOptions.allMediaFields,
            pollFields = TwitterApiOptions.allPollFields,
            placeFields = TwitterApiOptions.allPlaceFields,
            backfillMinutes = backfill
        )

        val res = req.execute()

        return res.body()?.byteStream()!!
    }

    override fun run() {
        // Rules were cleared initially!
        client.use {
            try {
                val stream = getStream()

                RawTwitterStream(stream, apiKey).use {
                    it.start()
                    readStream(it)
                }
            } catch (e: Exception) {
                /*
                 * TODO Narrow down exceptions.
                 * This should only happen if we fail to obtain the stream or fail to parse a
                 * response message received from the stream.
                 */
                logger.error { "Error while running Twitter filtered stream reader!" }
                logger.error { e.printStackTrace() }
            } finally {
                // Clean up in case the Twitter stream was terminated (idempotent operation).
                TwitterFilteredStreamReaderManager.forceRemoveStreamReader(this)

                /*
                 * At the moment it's probably safer to do a pre-emptive clean-up as it's not safe:
                 * A new StreamReader for the same API key might be starting and might have its
                 * rules deleted when interleaving with this one.
                 */
                // clearAllRules()
            }
        }

        logger.info { "Closed Twitter filtered stream reader for API key $apiKey." }
    }

}
