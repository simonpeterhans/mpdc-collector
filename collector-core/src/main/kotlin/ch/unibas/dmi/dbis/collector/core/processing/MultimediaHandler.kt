package ch.unibas.dmi.dbis.collector.core.processing

import ch.unibas.dmi.dbis.collector.core.config.CineastConfig
import ch.unibas.dmi.dbis.collector.core.dal.repositories.MultimediaRepository
import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.platform.ApiPlatform
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit
import kotlin.math.max

class MultimediaHandler(
    private val platform: ApiPlatform,
    private val mediaTypes: List<MediaType>,
    private val maxActiveDownloads: Int = 100,
    private val fetchIntervalMillis: Long = 1_000L,
    private val cineastConfig: CineastConfig
) : Thread() {

    companion object {

        private val activeHandlers = ConcurrentLinkedQueue<MultimediaHandler>()

        fun deployPlatformHandlers(cineastConfig: CineastConfig) {
            // Start multimedia handlers.
            activeHandlers.add(
                MultimediaHandler(
                    ApiPlatform.CROWDTANGLE,
                    listOf(MediaType.IMAGE),
                    cineastConfig = cineastConfig
                )
            )
            activeHandlers.add(
                MultimediaHandler(
                    ApiPlatform.CROWDTANGLE,
                    listOf(MediaType.VIDEO),
                    cineastConfig = cineastConfig
                )
            )
            activeHandlers.add(
                MultimediaHandler(
                    ApiPlatform.TWITTER,
                    listOf(MediaType.IMAGE),
                    cineastConfig = cineastConfig
                )
            )
            activeHandlers.add(
                MultimediaHandler(
                    ApiPlatform.TWITTER,
                    listOf(MediaType.ANIMATED_IMAGE, MediaType.VIDEO),
                    cineastConfig = cineastConfig
                )
            )
        }

    }

    private val dispatcher = Dispatcher()

    val client = OkHttpClient.Builder()
        .dispatcher(dispatcher)
        .connectionPool(ConnectionPool(maxActiveDownloads, 10, TimeUnit.MINUTES))
        .build()

    private val processor = MultimediaProcessor(client, cineastConfig.url)

    init {
        dispatcher.maxRequestsPerHost = maxActiveDownloads
        start()
    }

    private fun getNumNewJobs(): Int {
        return max(
            0,
            maxActiveDownloads - (dispatcher.queuedCallsCount() + dispatcher.runningCallsCount())
        )
    }

    override fun run() {
        while (!isInterrupted) {
            MultimediaRepository.getMultimediaObjectsForProcessing(
                platform,
                mediaTypes,
                getNumNewJobs()
            ).map {
                processor.processMultimedia(it)
            }

            try {
                sleep(fetchIntervalMillis)
            } catch (e: InterruptedException) {
                break
            }
        }
    }

}
