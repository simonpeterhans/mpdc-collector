package ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream.sample

import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.TwitterApiClient
import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.endpoints.TwitterApiOptions
import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.endpoints.TwitterSampleStreamApi
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.responses.TweetStreamResponse
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.sample.TwitterSampleSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream.RawTwitterStream
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream.TweetStreamElement
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream.TwitterStreamReader
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import java.io.InputStream
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

private val logger = KotlinLogging.logger {}

class TwitterSampleStreamReader(
    apiKey: String
) : TwitterStreamReader<TwitterSampleSubQuery>(apiKey) {

    private val client: TwitterApiClient = TwitterApiClient(apiKey)

    private val streamApi: TwitterSampleStreamApi = client.sampleStreamApi()

    private val queryStreams: ConcurrentHashMap<Long, LinkedBlockingQueue<TweetStreamElement>> =
        ConcurrentHashMap<Long, LinkedBlockingQueue<TweetStreamElement>>()

    private val mapper: ObjectMapper = jacksonObjectMapper().findAndRegisterModules()

    private val isRunning = AtomicBoolean(true)

    override fun isRunning() = isRunning.get()

    @Synchronized
    override fun getStream(query: TwitterSampleSubQuery): LinkedBlockingQueue<TweetStreamElement>? {
        return queryStreams[query.id!!]
    }

    @Synchronized
    override fun subscribe(query: TwitterSampleSubQuery): Boolean {
        // Already done or subscribed, return early.
        if (!isRunning.get() || queryStreams[query.id!!] != null) {
            return false
        }

        val stream = LinkedBlockingQueue<TweetStreamElement>()
        queryStreams[query.id!!] = stream

        return true
    }

    @Synchronized
    override fun unsubscribe(query: TwitterSampleSubQuery): Boolean {
        /*
         * We're already done (e.g., because something went wrong) - cleanup happens
         * before the stream, not after.
         */
        if (!isRunning.get()) {
            return false
        }

        val mapping = queryStreams.remove(query.id!!)

        if (mapping != null) {
            // Quit if we have no more subscribers.
            if (queryStreams.isEmpty()) {
                isRunning.set(false)
            }
        }

        return true
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

                queryStreams.values.forEach {
                    it.add(
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
        val req = streamApi.sampleStream(
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
                logger.error { "Error while running Twitter sample stream reader!" }
                logger.error { e.printStackTrace() }
            } finally {
                // Clean up in case the Twitter stream was terminated (idempotent operation).
                TwitterSampleStreamReaderManager.forceRemoveStreamReader(this)

                /*
                 * At the moment it's probably safer to do a pre-emptive clean-up as it's not safe:
                 * A new StreamReader for the same API key might be starting and might have its
                 * rules deleted when interleaving with this one.
                 */
                // clearAllRules()
            }
        }

        logger.info { "Closed Twitter sample stream reader for API key $apiKey." }
    }

}
