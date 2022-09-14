package ch.unibas.dmi.dbis.collector.core.platform.twitter.api.endpoints

import ch.unibas.dmi.dbis.collector.core.platform.PlatformApiEndpoint
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.requests.AddStreamRuleRequest
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.requests.DeleteStreamRuleRequest
import ch.unibas.dmi.dbis.collector.core.pooling.SimpleIntervalPoolSubscriptionManager
import com.fasterxml.jackson.databind.JsonNode
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TwitterFilteredStreamRulesApi : PlatformApiEndpoint {

    companion object {

        const val DEFAULT_BACKFILL = 5 // [0, 5] stream back-fill for recovery purposes.

        // 450 requests per 15 minutes -> 900s / 450 = 2s to refill.
        val poolManager = SimpleIntervalPoolSubscriptionManager<Any>(
            1,
            2_000
        )

    }

    @POST("/2/tweets/search/stream/rules")
    fun addStreamRules(
        @Body ruleModificationRequest: AddStreamRuleRequest,
        @Query("dry_run") dryRun: Boolean = false
    ): Call<JsonNode>

    @POST("/2/tweets/search/stream/rules")
    fun deleteStreamRules(
        @Body ruleModificationRequest: DeleteStreamRuleRequest,
        @Query("dry_run") dryRun: Boolean = false
    ): Call<JsonNode>

    @GET("/2/tweets/search/stream/rules")
    fun showStreamRules(
        @Query("ids") ids: String? = null
    ): Call<JsonNode>

}
