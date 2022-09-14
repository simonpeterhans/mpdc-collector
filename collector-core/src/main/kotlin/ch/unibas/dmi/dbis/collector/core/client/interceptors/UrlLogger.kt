package ch.unibas.dmi.dbis.collector.core.client.interceptors

import mu.KotlinLogging
import okhttp3.Interceptor
import okhttp3.Response

private val logger = KotlinLogging.logger {}

class UrlLogger : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        logger.info { "Request URL: ${chain.request().url}" }

        return chain.proceed(chain.request())
    }

}
