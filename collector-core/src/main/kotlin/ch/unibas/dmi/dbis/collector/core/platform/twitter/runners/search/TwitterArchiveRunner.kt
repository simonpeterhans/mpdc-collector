@file:Suppress("DuplicatedCode")

package ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.search

import ch.unibas.dmi.dbis.collector.core.model.data.ModelEntity
import ch.unibas.dmi.dbis.collector.core.model.data.Post
import ch.unibas.dmi.dbis.collector.core.model.flow.ModelStream
import ch.unibas.dmi.dbis.collector.core.model.flow.QueryState
import ch.unibas.dmi.dbis.collector.core.model.flow.StreamElement
import ch.unibas.dmi.dbis.collector.core.model.flow.StreamStatus
import ch.unibas.dmi.dbis.collector.core.model.query.TemporalType
import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.TwitterApiClient
import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.endpoints.TwitterApiOptions
import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.endpoints.TwitterLookupApi
import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.endpoints.TwitterSearchApi
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.responses.TweetSearchResponse
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet.TwitterTweetSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.RecursiveTweetFetcher
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.TwitterConverter
import ch.unibas.dmi.dbis.collector.core.query.FetchStatus
import ch.unibas.dmi.dbis.collector.core.query.QueryRunner
import ch.unibas.dmi.dbis.collector.core.query.RequestResultWithStatus
import mu.KotlinLogging
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.math.max

private val logger = KotlinLogging.logger {}

class TwitterArchiveRunner(
    override val query: TwitterTweetSubQuery
) : QueryRunner() {

    companion object {

        // TODO Use caching here to avoid duplicates after the N pages.
        private const val PAGES_PER_QUERY = 10

    }

    private data class IntervalSearchResult(
        val fetchStatus: FetchStatus,
        val statusText: String,
        val queryFinished: Boolean,
        val oldestTimestamp: Instant,
        val posts: List<Post>
    )

    override val stream = ModelStream<ModelEntity>(query)
    override val client: TwitterApiClient = TwitterApiClient(query.apiKey)

    override var state = QueryState()

    private val searchApi: TwitterSearchApi = client.searchApi()
    private val lookupApi: TwitterLookupApi = client.lookupApi()
    private val recursiveTweetFetcher =
        RecursiveTweetFetcher(lookupApi, query, mapper, query.referencedTweetsDepth)

    private val delayMs = TwitterSubQuery.DEFAULT_DELAY_MS + 60_000 * query.queryDelayMinutes
    private val streamIntervalMs = TwitterSubQuery.DEFAULT_STREAM_INTERVAL_MS

    /*
     * We cannot use since_id/until_id since we don't since_id until we're done and the API only
     * allows the usage of either from/to or since_id/until_id, but no combination.
     * This means we have to paginate via nextToken instead in the same query.
     * If Twitter gets slower to look up results further down the query (i.e., older tweets),
     * decrease PAGES_PER_QUERY.
     */
    private fun performSearch(
        from: Instant,
        to: Instant,
        nextToken: String?
    ): RequestResultWithStatus<TweetSearchResponse> {
        // Build and execute request.
        val req = searchApi.tweetSearch(
            query = query.tweetQueryData.queryString,
            startTime = from,
            endTime = to,
            nextToken = nextToken,
            expansions = TwitterApiOptions.allExpansions,
            tweetFields = TwitterApiOptions.allTweetFields,
            userFields = TwitterApiOptions.allUserFields,
            mediaFields = TwitterApiOptions.allMediaFields,
            pollFields = TwitterApiOptions.allPollFields,
            placeFields = TwitterApiOptions.allPlaceFields
        )

        return executeApiRequest(req, TweetSearchResponse::class.java)
    }

    private fun multiPageSearchForInterval(
        from: Instant,
        to: Instant
    ): IntervalSearchResult {
        // Clear fetcher.
        recursiveTweetFetcher.clear()

        val posts = mutableListOf<Post>()

        var oldestTimestamp: Instant = to
        var pageCount = 0
        var nextToken: String? = null
        var done = false

        while (pageCount < PAGES_PER_QUERY) {
            val rr = performSearch(from, to, nextToken)

            if (rr.status != FetchStatus.SUCCESS || rr.response == null) {
                return IntervalSearchResult(rr.status, rr.statusText, false, oldestTimestamp, posts)
            }

            val response = rr.response

            // We're done if we obtained a page without a pagination token (or no results).
            nextToken = response.meta?.nextToken
            done = nextToken == null
            pageCount++

            // If we matched no tweets, the data is actually null (...).
            if (done || response.data == null) {
                break
            }

            // Convert obtained posts.
            val conversionResult =
                TwitterConverter.getPostsFromRaw(response.data, response.expansions, mapper)

            // Add posts to fetcher for nested tweets.
            recursiveTweetFetcher.addPosts(conversionResult.posts)

            // Perform lookup if we have enough references.
            val lookedUpPosts = recursiveTweetFetcher.efficientLookup()

            // Update timestamp and parameters.
            oldestTimestamp = conversionResult.oldestPost?.platformTimestamp ?: oldestTimestamp

            // Add obtained posts to list of posts we have obtained so far.
            posts.addAll(conversionResult.posts + lookedUpPosts)
        }

        // Fetch remaining references.
        posts.addAll(recursiveTweetFetcher.lookupAllRemaining())

        return IntervalSearchResult(FetchStatus.SUCCESS, "", done, oldestTimestamp, posts)
    }

    private fun search(from: Instant = query.interval.start, to: Instant = query.interval.end) {
        logger.info { "Performing multi-page search for interval $from to $to." }

        var currentToTimestamp = to

        do {
            val searchResult = multiPageSearchForInterval(from, currentToTimestamp)

            // Update timestamp.
            currentToTimestamp = searchResult.oldestTimestamp

            // Only update checkpoint timestamp for search.
            val nextCheckpoint = if (query.temporalType == TemporalType.SEARCH) {
                searchResult.oldestTimestamp
            } else {
                state.lastCheckpointTimestamp
            }

            // Stream.
            state = if (searchResult.fetchStatus == FetchStatus.SUCCESS) {
                QueryState(StreamStatus.RUNNING, searchResult.statusText, nextCheckpoint)
            } else {
                QueryState(StreamStatus.ERROR_INTERRUPT, searchResult.statusText, nextCheckpoint)
            }

            stream.put(StreamElement(searchResult.posts, state))
        } while (!searchResult.queryFinished
            && currentToTimestamp.isAfter(from)
            && state.streamStatus == StreamStatus.RUNNING
        )
    }

    private fun stream() {
        // Stores the most recent timestamp used for checkpointing.
        while (
            state.lastCheckpointTimestamp == null
            || query.interval.end.isAfter(state.lastCheckpointTimestamp)
        ) {
            // Track instant.
            val lastInstant = Instant.now()

            // Subtract the delay from the next timestamp.
            val currentCheckpointTimestamp = lastInstant.minusMillis(delayMs)

            /*
             * Perform search within the interval. Example:
             * [10:00, 10:05] at 10:20 for a stream with delay 15 min and intervals of 5 mins.
             * Note that the search already fills the object stream!
             */
            search(
                state.lastCheckpointTimestamp ?: query.interval.start,
                currentCheckpointTimestamp
            )

            // Update state.
            state = QueryState(state.streamStatus, state.statusText, currentCheckpointTimestamp)

            // Add the element to the stream.
            stream.put(StreamElement(emptyList(), state))

            // Wait until the interval duration has passed to adhere to the stream delay.
            val maxSleep = streamIntervalMs - ChronoUnit.MILLIS.between(lastInstant, Instant.now())
            sleep(max(0L, maxSleep))
        }
    }

    override fun waitForQueryStart() {
        when (query.temporalType) {
            TemporalType.SEARCH -> {
                val sleepTimeMs =
                    ChronoUnit.MILLIS.between(Instant.now(), query.interval.end) + delayMs

                if (sleepTimeMs > 0) {
                    // If we don't end up in this block, we're ready to go, otherwise wait.
                    logger.info { "Delay until query start: ${sleepTimeMs}ms." }
                    sleep(sleepTimeMs)
                }
            }

            TemporalType.STREAM -> {
                val sleepTimeMs = ChronoUnit.MILLIS.between(
                    Instant.now(),
                    query.interval.start
                ) + delayMs + streamIntervalMs

                // Wait until the query is to actually start.
                if (sleepTimeMs > 0) {
                    /*
                     * If we don't end up in this block, query.interval.start is in the past and is at most
                     * the instant at now - delayMs - streamIntervalMs.
                     */
                    logger.info { "Delay until query start: ${sleepTimeMs}ms." }
                    sleep(sleepTimeMs)
                }
            }
        }
    }

    override fun executeQuery() {
        when (query.temporalType) {
            TemporalType.SEARCH -> search()
            TemporalType.STREAM -> stream()
        }

        // Done without interrupt, finalize stream state.
        updateStreamState(StreamStatus.DONE)
    }

}
