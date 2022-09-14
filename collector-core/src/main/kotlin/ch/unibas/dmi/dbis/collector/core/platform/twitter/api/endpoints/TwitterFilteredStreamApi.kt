package ch.unibas.dmi.dbis.collector.core.platform.twitter.api.endpoints

import ch.unibas.dmi.dbis.collector.core.platform.PlatformApiEndpoint
import ch.unibas.dmi.dbis.collector.core.pooling.SimpleIntervalPoolSubscriptionManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming

interface TwitterFilteredStreamApi : PlatformApiEndpoint {

    companion object {

        /*
         * Note that this fills the streams with tweets matching the old rules if you change the
         * rules and then immediately connect to the stream using the backfill.
         */
        const val DEFAULT_BACKFILL = 0 // [0, 5] stream back-fill for recovery purposes.

        // 50 requests per 15 minutes -> 900s / 50 = 18s to refill.
        val poolManager = SimpleIntervalPoolSubscriptionManager<Any>(
            1,
            18_000
        )

    }

    @GET("/2/tweets/search/stream")
    @Streaming
    fun filteredStream(
        @Query("backfill_minutes") backfillMinutes: Int? = DEFAULT_BACKFILL,
        @Query("expansions") expansions: String? = null,
        @Query("media.fields") mediaFields: String? = null,
        @Query("place.fields") placeFields: String? = null,
        @Query("poll.fields") pollFields: String? = null,
        @Query("tweet.fields") tweetFields: String? = null,
        @Query("user.fields") userFields: String? = null
    ): Call<ResponseBody>

}
