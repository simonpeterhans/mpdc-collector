package ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream

import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterSubQuery
import mu.KotlinLogging
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingQueue

private val logger = KotlinLogging.logger {}

abstract class TwitterStreamReaderManager<T : TwitterSubQuery> {

    private val streamReaderMap = ConcurrentHashMap<String, TwitterStreamReader<T>>()

    abstract fun createStreamReader(apiKey: String): TwitterStreamReader<T>

    @Synchronized
    fun forceRemoveStreamReader(reader: TwitterStreamReader<T>) {
        if (streamReaderMap.containsValue(reader)) {
            streamReaderMap.remove(reader.apiKey)
        }
    }

    @Synchronized
    fun subscribe(query: T): LinkedBlockingQueue<TweetStreamElement> {
        val streamReader = streamReaderMap[query.apiKey]

        logger.info {
            "Subscribing query ${query.id} to the Twitter stream of API key ${query.apiKey}."
        }

        return if (streamReader == null || !streamReader.isRunning()) {
            logger.info { "Creating new Twitter stream reader for API key ${query.apiKey}." }
            // No reader found, create new.
            val newStreamReader = createStreamReader(query.apiKey)
            streamReaderMap[query.apiKey] = newStreamReader

            // Subscribe with query.
            newStreamReader.subscribe(query)

            // Actually start the thread.
            newStreamReader.start()

            // Return the stream.
            newStreamReader.getStream(query)!!
        } else {
            logger.info { "Using existing Twitter stream reader for API key ${query.apiKey}." }

            // Stream reader exists, subscribe.
            streamReader.subscribe(query)

            // Return the stream.
            streamReader.getStream(query)!!
        }
    }

    @Synchronized
    fun unsubscribe(query: T) {
        val streamReader = streamReaderMap[query.apiKey] ?: return // Return if not found.

        logger.info {
            "Unsubscribing query ${query.id} from the Twitter stream of API key ${query.apiKey}"
        }

        streamReader.unsubscribe(query)

        if (!streamReader.isRunning()) {
            streamReaderMap.remove(query.apiKey)
        }
    }

}
