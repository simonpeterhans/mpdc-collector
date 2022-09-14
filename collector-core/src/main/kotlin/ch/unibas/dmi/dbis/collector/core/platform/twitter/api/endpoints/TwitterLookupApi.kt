package ch.unibas.dmi.dbis.collector.core.platform.twitter.api.endpoints

import ch.unibas.dmi.dbis.collector.core.platform.PlatformApiEndpoint
import ch.unibas.dmi.dbis.collector.core.pooling.SimpleIntervalPoolSubscriptionManager
import com.fasterxml.jackson.databind.JsonNode
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitterLookupApi : PlatformApiEndpoint {

    companion object {

        const val MAX_LOOKUP_IDS = 100

        // 300 requests per 15 minutes -> 900s / 300 = 3s to refill.
        val poolManager = SimpleIntervalPoolSubscriptionManager<Any>(
            1,
            3_000
        )

    }

    @GET("/2/tweets")
    @JvmSuppressWildcards
    fun tweetLookup(
        @Query("ids", encoded = true) ids: String,
        @Query("expansions", encoded = true) expansions: String? = null,
        @Query("tweet.fields", encoded = true) tweetFields: String? = null,
        @Query("user.fields", encoded = true) userFields: String? = null,
        @Query("media.fields", encoded = true) mediaFields: String? = null,
        @Query("poll.fields", encoded = true) pollFields: String? = null,
        @Query("place.fields", encoded = true) placeFields: String? = null,
    ): Call<JsonNode>

}
