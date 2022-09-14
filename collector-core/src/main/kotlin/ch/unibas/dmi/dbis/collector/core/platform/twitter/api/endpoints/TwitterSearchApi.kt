package ch.unibas.dmi.dbis.collector.core.platform.twitter.api.endpoints

import ch.unibas.dmi.dbis.collector.core.platform.PlatformApiEndpoint
import ch.unibas.dmi.dbis.collector.core.pooling.SimpleIntervalPoolSubscriptionManager
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.Instant

interface TwitterSearchApi : PlatformApiEndpoint {

    enum class SortOrder(val value: String) {

        @JsonProperty("recency")
        RECENT("recency"),

        @JsonProperty("relevancy")
        RELEVANT("relevancy");

        override fun toString(): String = value

    }

    companion object {

        const val DEFAULT_NUM_POSTS_PER_REQUEST = 100
        val DEFAULT_SORT_ORDER = SortOrder.RECENT

        // 300 requests per 15 minutes -> 900s / 300 = 3s to refill.
        val poolManager = SimpleIntervalPoolSubscriptionManager<Any>(
            1,
            3_000
        )

    }

    @GET("/2/tweets/search/all")
    @JvmSuppressWildcards
    fun tweetSearch(
        @Query("query", encoded = true) query: String,
        @Query("start_time") startTime: Instant? = null,
        @Query("end_time") endTime: Instant? = null,
        @Query("sort_order") sortOrder: SortOrder = DEFAULT_SORT_ORDER,
        @Query("since_id") sinceId: String? = null,
        @Query("until_id") untilId: String? = null,
        @Query("next_token") nextToken: String? = null,
        @Query("expansions", encoded = true) expansions: String? = null,
        @Query("tweet.fields", encoded = true) tweetFields: String? = null,
        @Query("user.fields", encoded = true) userFields: String? = null,
        @Query("media.fields", encoded = true) mediaFields: String? = null,
        @Query("poll.fields", encoded = true) pollFields: String? = null,
        @Query("place.fields", encoded = true) placeFields: String? = null,
        @Query("max_results", encoded = true) maxResults: Int = DEFAULT_NUM_POSTS_PER_REQUEST
    ): Call<JsonNode>

}
