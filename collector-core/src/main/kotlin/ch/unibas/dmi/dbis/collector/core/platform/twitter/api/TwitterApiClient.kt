package ch.unibas.dmi.dbis.collector.core.platform.twitter.api

import ch.unibas.dmi.dbis.collector.core.client.ApiClient
import ch.unibas.dmi.dbis.collector.core.client.interceptors.ApiKeyAuth
import ch.unibas.dmi.dbis.collector.core.client.interceptors.SubscriptionApiPooling
import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.endpoints.*
import ch.unibas.dmi.dbis.collector.core.pooling.SubscribablePoolContainer
import ch.unibas.dmi.dbis.collector.core.pooling.SubscribablePoolManager
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class TwitterApiClient(
    private val apiKey: String,
    baseUrl: String = TWITTER_API_URL,
    retrofitInstanceBuilder: Retrofit.Builder? = null,
    jacksonObjectMapper: ObjectMapper? = null
) : ApiClient(baseUrl, retrofitInstanceBuilder, jacksonObjectMapper) {

    companion object {

        private const val TWITTER_API_URL = "https://api.twitter.com/"
        private const val API_KEY_LABEL = "Authorization"
        private const val API_KEY_PREFIX = "Bearer"

    }

    private val subscriptions = mutableListOf<SubscribablePoolManager<Any>>()

    @Synchronized
    override fun close() {
        subscriptions.forEach {
            it.unsubscribe(apiKey, this)
        }
    }

    override fun addInterceptors(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.addInterceptor(
            ApiKeyAuth(
                ApiKeyAuth.Location.HEADER,
                API_KEY_LABEL,
                "$API_KEY_PREFIX $apiKey"
            )
        )
        return builder
    }

    private fun getDefaultHttpClient(container: SubscribablePoolContainer<Any>): OkHttpClient {
        return getDefaultHttpClient(SubscriptionApiPooling(container, this))
    }

    fun searchApi(): TwitterSearchApi {
        subscriptions.add(TwitterSearchApi.poolManager)

        val container = TwitterSearchApi.poolManager.subscribe(apiKey, this)
        val client = getDefaultHttpClient(container)

        return createService(TwitterSearchApi::class.java, client)
    }

    fun lookupApi(): TwitterLookupApi {
        subscriptions.add(TwitterLookupApi.poolManager)

        val container = TwitterLookupApi.poolManager.subscribe(apiKey, this)
        val client = getDefaultHttpClient(container)

        return createService(TwitterLookupApi::class.java, client)
    }

    fun filteredStreamApi(): TwitterFilteredStreamApi {
        subscriptions.add(TwitterFilteredStreamApi.poolManager)

        val container = TwitterFilteredStreamApi.poolManager.subscribe(apiKey, this)
        val client = getDefaultHttpClient(container)

        return createService(TwitterFilteredStreamApi::class.java, client)
    }

    fun filteredStreamRules(): TwitterFilteredStreamRulesApi {
        subscriptions.add(TwitterFilteredStreamRulesApi.poolManager)

        val container = TwitterFilteredStreamRulesApi.poolManager.subscribe(apiKey, this)
        val client = getDefaultHttpClient(container)

        return createService(TwitterFilteredStreamRulesApi::class.java, client)
    }

    fun sampleStreamApi(): TwitterSampleStreamApi {
        subscriptions.add(TwitterSampleStreamApi.poolManager)

        val container = TwitterSampleStreamApi.poolManager.subscribe(apiKey, this)
        val client = getDefaultHttpClient(container)

        return createService(TwitterSampleStreamApi::class.java, client)
    }

}
