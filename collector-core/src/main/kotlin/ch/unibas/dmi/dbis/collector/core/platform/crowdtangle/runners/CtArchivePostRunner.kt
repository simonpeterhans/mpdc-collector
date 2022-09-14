@file:Suppress("DuplicatedCode")

package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.runners

import ch.unibas.dmi.dbis.collector.core.model.data.ModelEntity
import ch.unibas.dmi.dbis.collector.core.model.flow.QueryState
import ch.unibas.dmi.dbis.collector.core.model.flow.StreamElement
import ch.unibas.dmi.dbis.collector.core.model.flow.StreamStatus
import ch.unibas.dmi.dbis.collector.core.model.query.TemporalType
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.api.endpoints.CtPostApi
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.responses.PostSearchResponse
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.CtSubQuery
import ch.unibas.dmi.dbis.collector.core.query.FetchStatus
import ch.unibas.dmi.dbis.collector.core.query.QueryRunner
import ch.unibas.dmi.dbis.collector.core.query.RequestResultWithStatus
import mu.KotlinLogging
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.math.max

private val logger = KotlinLogging.logger {}

abstract class CtArchivePostRunner(
    queryDelayMinutes: Int
) : QueryRunner() {

    companion object {

        // TODO Use caching here to avoid duplicates after the N pages.
        private const val PAGES_PER_QUERY = 6

    }

    private val delayMs = CtSubQuery.DEFAULT_DELAY_MS + 60_000 * queryDelayMinutes
    private val streamIntervalMs = CtSubQuery.DEFAULT_STREAM_INTERVAL_MS

    abstract fun performSearch(
        from: Instant,
        to: Instant,
        offset: Int
    ): RequestResultWithStatus<PostSearchResponse>

    private fun search(from: Instant = query.interval.start, to: Instant = query.interval.end) {
        logger.info { "Performing multi-page search for interval $from to $to." }

        var oldestTimestamp: Instant = to // Serves as checkpoint for the state in search queries.
        var currentToTimestamp = oldestTimestamp
        var offset = 0
        var pageCount = 0
        var done = false

        do {
            // Contains the timestamp for the next checkpoint; is only updated for search queries.
            var nextStateCheckpoint: Instant? = state.lastCheckpointTimestamp

            if (pageCount >= PAGES_PER_QUERY) {
                currentToTimestamp = oldestTimestamp
                offset = 0
                pageCount = 0
            }

            logger.info { "Searching for $from to $currentToTimestamp. Offset: $offset" }

            val rr = performSearch(from, currentToTimestamp, offset)

            val posts: List<ModelEntity> = when (rr.status) {
                FetchStatus.SUCCESS -> {
                    val rawPosts = rr.response?.result?.posts ?: emptyList()
                    val cr = CtConverter.getModelElementsFromPosts(rawPosts, mapper)

                    // Obtain the oldest (last) element of the results to set the next timestamp.
                    oldestTimestamp = cr.oldestPost?.platformTimestamp ?: oldestTimestamp

                    logger.info { "Oldest timestamp: $oldestTimestamp." }

                    // Only advance checkpoint for search queries.
                    if (query.temporalType == TemporalType.SEARCH && cr.oldestPost != null) {
                        // Designate the timestamp of the oldest element obtained as next checkpoint.
                        nextStateCheckpoint = oldestTimestamp
                    }

                    // We're done if we obtained a page without the maximum number of results.
                    offset += rawPosts.size
                    done = rawPosts.size < CtPostApi.DEFAULT_NUM_POSTS_PER_REQUEST
                    pageCount++

                    cr.posts
                }

                else -> {
                    emptyList()
                }
            }

            state = if (rr.status == FetchStatus.SUCCESS) {
                QueryState(StreamStatus.RUNNING, rr.statusText, nextStateCheckpoint)
            } else {
                QueryState(StreamStatus.ERROR_INTERRUPT, rr.statusText, nextStateCheckpoint)
            }

            stream.put(StreamElement(posts, state))
        } while (!done && state.streamStatus == StreamStatus.RUNNING)
    }

    private fun stream() {
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
