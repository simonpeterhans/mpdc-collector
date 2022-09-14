package ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream

import mu.KotlinLogging
import java.io.BufferedReader
import java.io.Closeable
import java.io.InputStream
import java.io.InputStreamReader
import java.time.Instant
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

private val logger = KotlinLogging.logger {}

class RawTwitterStream(
    inStream: InputStream,
    private val apiKey: String,
    private val criticalSize: Int = 10_000
) : Closeable, Thread() {

    data class TwitterStreamResult(
        val response: String,
        val timestamp: Instant = Instant.now()
    )

    private val isRunning = AtomicBoolean(true)
    private val isClosed = AtomicBoolean(false)

    val buffer = ArrayBlockingQueue<TwitterStreamResult>(criticalSize)

    private val isr = InputStreamReader(inStream)
    private val reader = BufferedReader(isr)

    fun isRunning() = isRunning.get()

    private fun readStream() {
        while (isRunning.get()) {
            // This blocks for at most 20 seconds between heartbeats.
            val json: String? = reader.readLine()

            if (json == null) {
                this.isRunning.set(false)
                logger.info { "Twitter stream closed for API key $apiKey." }
            } else if (buffer.size < criticalSize) {
                buffer.add(TwitterStreamResult(json))
//                logger.info {
//                    "Queuing Twitter stream response (queue length: ${buffer.size}) for" +
//                        " API key $apiKey."
//                }
            } else {
                isRunning.set(false)
                logger.error { "Twitter stream for API key $apiKey exceeded critical size!" }
                logger.error { "Breaking Twitter stream for query with API key $apiKey." }
            }
        }
    }

    override fun run() {
        try {
            this.use {
                readStream()
            }
        } catch (e: Exception) {
            logger.error { "Twitter stream for API key $apiKey terminating with exception." }
            logger.error { e.printStackTrace() }
        }
    }

    override fun close() {
        if (!isClosed.get()) {
            isRunning.set(false)
            isClosed.set(true)
            reader.close()
            logger.info { "Closed Twitter stream for API key $apiKey." }
        }
    }

}
