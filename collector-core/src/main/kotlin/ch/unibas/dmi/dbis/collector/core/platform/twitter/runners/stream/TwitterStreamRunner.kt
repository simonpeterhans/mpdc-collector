package ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream

import ch.unibas.dmi.dbis.collector.core.model.data.ModelEntity
import ch.unibas.dmi.dbis.collector.core.model.data.Post
import ch.unibas.dmi.dbis.collector.core.model.flow.ModelStream
import ch.unibas.dmi.dbis.collector.core.model.flow.QueryState
import ch.unibas.dmi.dbis.collector.core.model.flow.StreamElement
import ch.unibas.dmi.dbis.collector.core.model.flow.StreamStatus
import ch.unibas.dmi.dbis.collector.core.model.query.TemporalType
import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.TwitterApiClient
import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.endpoints.TwitterLookupApi
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.RecursiveTweetFetcher
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.TwitterConverter
import ch.unibas.dmi.dbis.collector.core.query.QueryRunner
import mu.KotlinLogging
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

private val logger = KotlinLogging.logger {}

class TwitterStreamRunner<T : TwitterSubQuery>(
    override val query: T,
    referencedTweetsDepth: Int, // TODO This probably belongs into the sub query.
    private val manager: TwitterStreamReaderManager<T>
) : QueryRunner() {

    companion object {

        // Wait no longer than this until we assume something went wrong with the stream.
        const val POLL_TIMEOUT_SECONDS: Long = 60

        // Add some delay to the checkpoint just in case.
        const val UPDATE_TIMESTAMP_DELAY_SECONDS: Long = 60

        // 1 element every 20 seconds at most.
        const val STREAM_ELEMENT_INTERVAL_SECONDS: Long = 2

    }

    init {
        if (query.temporalType == TemporalType.SEARCH) {
            throw IllegalArgumentException(
                "TwitterStreamRunner cannot handle search queries (ID: ${query.id})!"
            )
        }
    }

    override val stream = ModelStream<ModelEntity>(query)
    override val client: TwitterApiClient = TwitterApiClient(query.apiKey)

    override var state = QueryState()

    private val lookupApi: TwitterLookupApi = client.lookupApi()
    private val recursiveTweetFetcher =
        RecursiveTweetFetcher(lookupApi, query, mapper, referencedTweetsDepth)

    private fun processObtainedPosts(posts: List<Post>, newestTimestamp: Instant) {
        // Get all references (and their nested references) recursively.
        val nestedPosts = recursiveTweetFetcher.lookupAllRemaining()

        // Clean up tweet fetcher after we got all posts.
        recursiveTweetFetcher.clear()

        // Update stream with posts and query state.
        val allPosts = posts + nestedPosts
        state = QueryState(StreamStatus.RUNNING, "", newestTimestamp)
        stream.put(StreamElement(allPosts, state))
    }

    private fun processStream(tweetStream: LinkedBlockingQueue<TweetStreamElement>) {
        val postBuffer = mutableListOf<Post>()
        var lastStreamElementSent = Instant.now()
        var newestTimestamp: Instant = Instant.now()

        while (newestTimestamp.isBefore(query.interval.end)) {
            // Check if we have been stopped in the meanwhile.
            if (!isQueryRunning.get()) {
                throw InterruptedException()
            }

            /*
             * This should not block for more than 20 seconds at most (heartbeat).
             * If the timeout is triggered, something went wrong.
             */
            val tse = tweetStream.poll(POLL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                ?: throw TimeoutException()

            // Unless the stream closes, we will receive a heartbeat from Twitter every 20 seconds.
            if (tse.tweet == null) {
                // Heartbeat; give some margin to account for possible stream delay.
                val updatedTimestamp = Instant.now().minusSeconds(UPDATE_TIMESTAMP_DELAY_SECONDS)

                if (updatedTimestamp.isAfter(newestTimestamp)) {
                    newestTimestamp = updatedTimestamp
                }

                logger.info { "Received ping from Twitter stream for query with ID ${query.id}." }
            } else {
                // Should only contain a single tweet (since we only received 1 from the stream).
                val result =
                    TwitterConverter.getPostsFromRaw(listOf(tse.tweet), tse.expansions, mapper)

//                logger.info { "Received post from Twitter stream for query with ID ${query.id}" }

                // Add post.
                postBuffer.addAll(result.posts)

                // Set new timestamp (will always be non-null since we gave in 1 tweet).
                if (result.newestPost != null) {
                    newestTimestamp = result.newestPost.platformTimestamp
                }

                // Register posts, so we can grab the references later on.
                recursiveTweetFetcher.addPosts(result.posts)
            }

            // Don't send the posts yet if the last message was sent too recently.
            val potentialStreamTime = Instant.now()
            if (lastStreamElementSent.plusSeconds(STREAM_ELEMENT_INTERVAL_SECONDS)
                    .isAfter(potentialStreamTime)
            ) {
                // Not enough seconds have elapsed since we sent the last element.
                continue
            }

            processObtainedPosts(postBuffer, newestTimestamp)

            // Reset post buffer.
            postBuffer.clear()

            // Update lookup time.
            lastStreamElementSent = potentialStreamTime
        }

        // Process one last time when exiting.
        processObtainedPosts(postBuffer, newestTimestamp)
    }

    override fun waitForQueryStart() {
        val sleepTimeMs = ChronoUnit.MILLIS.between(Instant.now(), query.interval.start)

        // Wait until the query is to actually start.
        if (sleepTimeMs > 0) {
            /*
             * If we don't end up in this block, query.interval.start is in the past and is at
             * most the instant at now - delayMs - streamIntervalMs.
             */
            logger.info { "Delay until query start: ${sleepTimeMs}ms." }
            sleep(sleepTimeMs)
        } else {
            logger.info {
                "The Twitter streaming endpoint is real-time and does not allow lookup. " +
                    "The query targeting this endpoint has starting timestamp " +
                    "${query.interval.start} - posts before now (${Instant.now()}) will be missed!"
            }
        }
    }

    override fun stopQuery() {
        if (isQueryRunning.get()) {
            logger.info { "Interrupting query with ID $query.id}." }
            isQueryRunning.set(false)
        } else {
            logger.info { "Cannot stop query with ID ${query.id} (already stopped)." }
        }
    }

    override fun executeQuery() {
        // TODO Consider making query runners closeable.
        // Exception to propagate because, in any case, we need to unsubscribe before returning.
        var exception: Exception? = null

        try {
            // Get the stream from the Twitter stream reader.
            val stream = manager.subscribe(query)

            // Process the stream until we're done.
            processStream(stream)

            // If we got here without exception, we're done.
            updateStreamState(StreamStatus.DONE)
            logger.info { "TwitterStreamRunner ending without exception." }
        } catch (e: InterruptedException) {
            exception = e
            logger.info { "TwitterStreamRunner ending with interrupt." }
        } catch (e: Exception) {
            exception = e
            logger.info { "TwitterStreamRunner ending with unknown exception." }
            logger.info { e.printStackTrace() }
        } finally {
            // In case something went wrong (idempotent operation, we can't get interrupted twice).
            manager.unsubscribe(query)
        }

        if (exception != null) {
            throw exception
        }
    }

}
