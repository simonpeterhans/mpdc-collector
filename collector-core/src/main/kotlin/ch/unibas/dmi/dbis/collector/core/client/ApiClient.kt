package ch.unibas.dmi.dbis.collector.core.client

import ch.unibas.dmi.dbis.collector.core.client.interceptors.InterruptedDetector
import ch.unibas.dmi.dbis.collector.core.client.interceptors.PoolingInterceptor
import ch.unibas.dmi.dbis.collector.core.client.interceptors.UrlLogger
import ch.unibas.dmi.dbis.collector.core.platform.PlatformApiEndpoint
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.Closeable
import java.util.concurrent.TimeUnit

abstract class ApiClient(
    url: String,
    private val retrofitInstanceBuilder: Retrofit.Builder? = null,
    private val jacksonObjectMapper: ObjectMapper? = null
) : Closeable {

    companion object {

        private const val CONNECT_TIMEOUT_SECONDS: Long = 10
        private const val WRITE_TIMEOUT_SECONDS: Long = 30
        private const val READ_TIMEOUT_SECONDS: Long = 30

    }

    protected val baseUrl = normalizeUrl(url)

    abstract fun addInterceptors(builder: OkHttpClient.Builder): OkHttpClient.Builder

    fun getDefaultHttpClient(pool: PoolingInterceptor? = null): OkHttpClient {
        // Default.
        val builder = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)

        // Detect interrupts first.
        builder.addInterceptor(InterruptedDetector())

        // Wait for the ticket to make a request.
        if (pool != null) {
            builder.addInterceptor(pool)
        }

        // Log the URL for the request for debugging.
        builder.addInterceptor(UrlLogger())

        // Add authorization.
        addInterceptors(builder)

        return builder.build()
    }

    protected fun <S : PlatformApiEndpoint> createService(
        serviceClass: Class<S>,
        httpClient: OkHttpClient
    ): S {
        return retrofitBuilder.callFactory(httpClient).build().create(serviceClass)
    }

    protected val retrofitBuilder: Retrofit.Builder by lazy {
        retrofitInstanceBuilder ?: Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
    }

    protected val objectMapper: ObjectMapper by lazy {
        jacksonObjectMapper ?: jacksonObjectMapper().findAndRegisterModules()
    }

    protected fun normalizeUrl(s: String): String {
        if (!s.endsWith("/")) {
            return "$s/"
        }

        return s
    }

}
