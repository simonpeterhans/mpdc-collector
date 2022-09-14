package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.api.endpoints

import ch.unibas.dmi.dbis.collector.core.platform.PlatformApiEndpoint
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data.AccountType
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data.Platform
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data.SearchField
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data.Types
import ch.unibas.dmi.dbis.collector.core.pooling.TimedIntervalPoolSubscriptionManager
import com.fasterxml.jackson.databind.JsonNode
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.Instant
import java.time.temporal.ChronoUnit

interface CtPostApi : PlatformApiEndpoint {

    companion object {

        // TODO Add API constraints (earliest date etc.).

        const val DEFAULT_NUM_POSTS_PER_REQUEST = 100
        val DEFAULT_SORT_METHOD = SortBy.DATE

        // 6 requests per minute, resetting at :00.
        val poolManager = TimedIntervalPoolSubscriptionManager<Any>(
            6,
            ChronoUnit.MINUTES,
            1,
            1_000 // Add a 1-second delay before refilling at :00 (-> :01).
        )

    }

    /**
     *
     * Performs an archive search based on the specified lists (or all of them if none specified).
     * Responses:
     *  - 200: OK
     *  - 400: Bad Request (invalid parameter)
     *  - 401: Unauthorized (invalid API token)
     *  - 403: Forbidden (invalid list ID)
     *
     * @param accounts The account handles or platform ids to search. These can be separated by commas to include multiple accounts. (optional)
     * @param brandedContent Limits to or excludes posts that have been marked as Branded Content, either as Publisher or Marketer. (optional)
     * @param count The number of posts to return. (optional)
     * @param endDate The latest date at which a post could be posted. Time zone is UTC. Format is &#39;yyyy-mm-ddThh:mm:ss&#39; or &#39;yyyy-mm-dd&#39; (defaults to time 00:00:00). (optional)
     * @param includeHistory Includes timestep data for growth of each post returned. Note that we will not have time-series data for posts that were created after the account was added to CrowdTangle. (optional)
     * @param language Language to filter for (2-character locale code); Some languages require more than two characters (Chinese (Simplified) is zh-CN and Chinese (Traditional) is zh-TW). (optional)
     * @param listIds The IDs of lists or saved searches to retrieve. These can be separated by commas to include multiple lists. (optional)
     * @param minInteractions If set, will exclude posts with total interactions below this threshold. (optional)
     * @param offset The number of posts to offset (generally used for pagination). Pagination links will also be provided in the response. (optional)
     * @param pageAdminTopCountry Limits to posts for which the account has the pageAdminTopCountry matching the parameter setting (2-character country code). (optional)
     * @param searchTerm Returns only posts that match this search term. Terms AND automatically. Separate with commas for OR, use quotes for phrases. E.g. CrowdTangle API -&gt; AND. CrowdTangle, API -&gt; OR. &#39;CrowdTangle API&#39; -&gt; AND in that exact order. You can also use traditional Boolean search with this parameter. (optional)
     * @param sortBy The method by which to filter and order posts. (optional)
     * @param startDate The earliest date at which a post could be posted. Time zone is UTC. Format is &#39;yyyy-mm-ddThh:mm:ss&#39; or &#39;yyyy-mm-dd&#39; (defaults to time 00:00:00). This must be before endDate. Timeframe and startDate are mutually exclusive; if both are passed, startDate will be preferred. (optional)
     * @param types The types of post to include. These can be separated by commas to include multiple types. If you want all live videos (whether currently or formerly live), be sure to include both live_video and live_video_complete. The \&quot;video\&quot; type does not mean all videos, it refers to videos that aren&#39;t native_video or youtube (e.g. a video on Vimeo). (optional)
     * @param verified Limits to posts where the account has the verified setting matching the input. (optional)
     * @return [Call]<[JsonNode]>
     */
    @GET("/posts")
    @JvmSuppressWildcards
    fun searchPostsByLists(
        @Query("accounts") accounts: List<String>? = null,
        @Query("brandedContent") brandedContent: BrandedContent? = null,
        @Query("count") count: Int? = DEFAULT_NUM_POSTS_PER_REQUEST,
        @Query("endDate") endDate: Instant? = null,
        @Query("includeHistory") includeHistory: Boolean? = null,
        @Query("language") language: String? = null,
        @Query("listIds") listIds: List<Long>? = null,
        @Query("minInteractions") minInteractions: Int? = null,
        @Query("offset") offset: Int? = null,
        @Query("pageAdminTopCountry") pageAdminTopCountry: String? = null,
        @Query("searchTerm") searchTerm: String? = null,
        @Query("sortBy") sortBy: SortBy? = DEFAULT_SORT_METHOD,
        @Query("startDate") startDate: Instant? = null,
        @Query("types") types: List<Types>? = null,
        @Query("verified") verified: Verified? = null
    ): Call<JsonNode>

    /**
     *
     * Performs an archive search based on the specified lists (or all of them if none specified).
     * Responses:
     *  - 200: OK
     *  - 400: Bad Request (invalid parameter)
     *  - 401: Unauthorized (invalid API token)
     *  - 403: Forbidden (invalid list ID)
     *
     * @param searchTerm By default, post search is split into OR, AND and NOT chunks. This is the OR section. Each is a phrase match, meaning that "CrowdTangle API, organized so cleanly" will search for "CrowdTangle API" or "organized so cleanly." If you want to find those phrases together, put one in here and put one in the AND section. This parameter does not support wildcard or partial-term searches, and is not case-sensitive. If you submit a query with a blank searchTerm (i.e., searchTerm=" "), the result set will be limited to the Dashboard associated with the supplied API token.
     * @param accounts The account handles or platform ids to search. These can be separated by commas to include multiple accounts. (optional)
     * @param accountTypes Limits search to a specific Facebook account type. You can use more than one type. Requires "platforms=facebook" to be set also. If "platforms=facebook" is not set, all post types including IG and Reddit will be returned. Only applies to Facebook. (optional)
     * @param andString Post search is split into OR, AND and NOT chunks. This is the AND section. Each is a phrase match, meaning that searchTerm is "CrowdTangle, API" and and is "so fast, great documentation," it will search for (("CrowdTangle" AND "so fast" AND "great documentation") OR ("API" AND "so fast" AND "great documentation")). (optional)
     * @param brandedContent Limits to or excludes posts that have been marked as Branded Content, either as Publisher or Marketer. (optional)
     * @param count The number of posts to return. (optional)
     * @param endDate The latest date at which a post could be posted. Time zone is UTC. Format is &#39;yyyy-mm-ddThh:mm:ss&#39; or &#39;yyyy-mm-dd&#39; (defaults to time 00:00:00). (optional)
     * @param includeHistory Includes timestep data for growth of each post returned. Note that we will not have time-series data for posts that were created after the account was added to CrowdTangle. (optional)
     * @param language Language to filter for (2-character locale code); Some languages require more than two characters (Chinese (Simplified) is zh-CN and Chinese (Traditional) is zh-TW). (optional)
     * @param inListIds The IDs of lists or saved searches to retrieve. These can be separated by commas to include multiple lists. (optional)
     * @param minInteractions If set, will exclude posts with total interactions below this threshold. (optional)
     * @param minSubscriberCount The minimum number of subscribers an account must have to be included in the search results. (optional)
     * @param notString A corollary to and, not will exclude all posts matching this word/phrase.
     * @param offset The number of posts to offset (generally used for pagination). Pagination links will also be provided in the response. (optional)
     * @param pageAdminTopCountry Limits to posts for which the account has the pageAdminTopCountry matching the parameter setting (2-character country code). (optional)
     * @param platforms The platforms from which to retrieve posts. This value can be comma-separated. (optional)
     * @param searchField This allows you to search image text, URLs with query strings, and account names, in addition to text fields only or both text fields and image text. (optional)
     * @param sortBy The method by which to filter and order posts. (optional)
     * @param startDate The earliest date at which a post could be posted. Time zone is UTC. Format is &#39;yyyy-mm-ddThh:mm:ss&#39; or &#39;yyyy-mm-dd&#39; (defaults to time 00:00:00). This must be before endDate. Timeframe and startDate are mutually exclusive; if both are passed, startDate will be preferred. (optional)
     * @param types The types of post to include. These can be separated by commas to include multiple types. If you want all live videos (whether currently or formerly live), be sure to include both live_video and live_video_complete. The \&quot;video\&quot; type does not mean all videos, it refers to videos that aren&#39;t native_video or youtube (e.g. a video on Vimeo). (optional)
     * @param verified Limits to posts where the account has the verified setting matching the input. (optional)
     * @param verifiedOnly - 	Limit results to verified accounts only. Note, this only applies to platforms that supply information about verified accounts. (optional)
     * @return [Call]<[JsonNode]>
     */
    @GET("/posts/search")
    @JvmSuppressWildcards
    fun searchPosts(
        @Query("searchTerm") searchTerm: String? = null,
        @Query("accounts") accounts: List<String>? = null,
        @Query("accountTypes") accountTypes: List<AccountType>? = null,
        @Query("and") andString: String? = null,
        @Query("brandedContent") brandedContent: BrandedContent? = null,
        @Query("count") count: Int? = DEFAULT_NUM_POSTS_PER_REQUEST,
        @Query("endDate") endDate: Instant? = null,
        @Query("includeHistory") includeHistory: Boolean? = null,
        @Query("language") language: String? = null,
        @Query("inListIds") inListIds: List<Long>? = null,
        @Query("minInteractions") minInteractions: Int? = null,
        @Query("minSubscriberCount") minSubscriberCount: Int? = null,
        @Query("not") notString: String? = null,
        @Query("offset") offset: Int? = null,
        @Query("pageAdminTopCountry") pageAdminTopCountry: String? = null,
        @Query("platforms") platforms: List<Platform>? = null,
        @Query("searchField") searchField: SearchField? = null,
        @Query("sortBy") sortBy: SortBy? = DEFAULT_SORT_METHOD,
        @Query("startDate") startDate: Instant? = null,
        @Query("types") types: List<Types>? = null,
        @Query("verified") verified: Verified? = null,
        @Query("verifiedOnly") verifiedOnly: Boolean? = null
    ): Call<JsonNode>

}
