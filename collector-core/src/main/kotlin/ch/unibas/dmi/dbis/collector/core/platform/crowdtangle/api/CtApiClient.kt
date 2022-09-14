package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.api

import ch.unibas.dmi.dbis.collector.core.client.ApiClient
import ch.unibas.dmi.dbis.collector.core.client.interceptors.ApiKeyAuth
import ch.unibas.dmi.dbis.collector.core.client.interceptors.SubscriptionApiPooling
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.api.endpoints.CtPostApi
import ch.unibas.dmi.dbis.collector.core.pooling.SubscribablePoolContainer
import ch.unibas.dmi.dbis.collector.core.pooling.SubscribablePoolManager
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class CtApiClient(
    private val apiKey: String,
    retrofitInstanceBuilder: Retrofit.Builder? = null,
    jacksonObjectMapper: ObjectMapper? = null,
) : ApiClient(CROWDTANGLE_API_URL, retrofitInstanceBuilder, jacksonObjectMapper) {

    companion object {

        private const val CROWDTANGLE_API_URL = "https://api.crowdtangle.com/"
        private const val API_KEY_LABEL = "x-api-token"

    }

    private val subscriptions = mutableListOf<SubscribablePoolManager<Any>>()

    @Synchronized
    override fun close() {
        subscriptions.forEach {
            it.unsubscribe(apiKey, this)
        }
    }

    override fun addInterceptors(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.addInterceptor(ApiKeyAuth(ApiKeyAuth.Location.HEADER, API_KEY_LABEL, apiKey))
        return builder
    }

    private fun getDefaultHttpClient(container: SubscribablePoolContainer<Any>): OkHttpClient {
        return getDefaultHttpClient(SubscriptionApiPooling(container, this))
    }

    fun listSearchApi(): CtPostApi {
        subscriptions.add(CtPostApi.poolManager)

        val container = CtPostApi.poolManager.subscribe(apiKey, this)
        val client = getDefaultHttpClient(container)

        return createService(CtPostApi::class.java, client)
    }

}
