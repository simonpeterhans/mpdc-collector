package ch.unibas.dmi.dbis.collector.core.client.interceptors

import ch.unibas.dmi.dbis.collector.core.pooling.SubscribablePoolContainer
import mu.KotlinLogging
import okhttp3.Interceptor
import okhttp3.Response

private val logger = KotlinLogging.logger {}

class SubscriptionApiPooling<T : Any>(
    private val ticketPool: SubscribablePoolContainer<T>,
    private val subscriber: T
) : PoolingInterceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        logger.info { "Waiting for ticket to perform request..." }

        // Throws interrupted exception if interrupted while waiting.
        ticketPool.takeTicket(subscriber)

        logger.info { "Received ticket to perform request." }

        return chain.proceed(chain.request())
    }

}
