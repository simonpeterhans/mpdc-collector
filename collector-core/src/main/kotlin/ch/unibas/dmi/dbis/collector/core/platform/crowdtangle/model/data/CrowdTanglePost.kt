package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * CrowdTangle post data model.
 *
 * @param account
 * @param brandedContentSponsor
 * @param caption The caption to a photo, if available.
 * @param date Represents a date string of CrowdTangle. Note that these are just strings, because CrowdTangle does not adhere to any ISO standard.
 * @param description Further details, if available. Associated with links or images across different platforms.
 * @param expandedLinks A map where the keys are the original links that came in the post (which are often shortened), and the values are the expanded links.
 * @param history A list of statistics at timestamps the post was indexed by CrowdTangle (only available for includeHistory=true).
 * @param id The unique identifier of the post in the CrowdTangle system. This ID is specific to CrowdTangle, not the platform from which the post originated.
 * @param imageText The text, if it exists, within an image.
 * @param languageCode The language code (2 digits) of the post.
 * @param legacyId The legacy version of the unique identifier of the post in the CrowdTangle system. This ID is specific to CrowdTangle, not the platform from which the post originated.
 * @param likeAndViewCountsDisabled Whether like and view count has been disabled for this post.
 * @param link An external URL that the post links to, if available (Facebook only).
 * @param liveVideoStatus The status of the live video.
 * @param media An array of available media for the post.
 * @param message The user-submitted text on a post.
 * @param platform
 * @param platformId The platform's ID for the post.
 * @param postUrl The URL to access the post on its platform.
 * @param score The score of a post as measured by the request.
 * @param statistics
 * @param subscriberCount The number of subscriber the account had when the post was published. This is in contrast to the subscriberCount found on the account, which represents the current number of subscribers an account has.
 * @param title The title of the post.
 * @param type
 * @param updated Represents a date string of CrowdTangle. Note that these are just strings, because CrowdTangle does not adhere to any ISO standard.
 * @param videoLengthMS The length of the video in milliseconds.
 */
data class CrowdTanglePost(
    @JsonProperty("account")
    val account: Account? = null,

    @JsonProperty("brandedContentSponsor")
    val brandedContentSponsor: Account? = null,

    @JsonProperty("caption")
    val caption: String? = null,

    @JsonProperty("date")
    val date: String? = null,

    @JsonProperty("description")
    val description: String? = null,

    @JsonProperty("expandedLinks")
    val expandedLinks: List<ExpandedLink>? = null,

    @JsonProperty("history")
    val history: List<Statistics>? = null,

    @JsonProperty("id")
    val id: String? = null,

    @JsonProperty("imageText")
    val imageText: String? = null,

    @JsonProperty("languageCode")
    val languageCode: String? = null,

    @JsonProperty("legacyId")
    val legacyId: Int? = null,

    @JsonProperty("likeAndViewCountsDisabled")
    val likeAndViewCountsDisabled: Boolean? = null,

    @JsonProperty("link")
    val link: String? = null,

    @JsonProperty("liveVideoStatus")
    val liveVideoStatus: LiveVideoStatus? = null,

    @JsonProperty("media")
    val media: List<Media>? = null,

    @JsonProperty("message")
    val message: String? = null,

    @JsonProperty("platform")
    val platform: Platform? = null,

    @JsonProperty("platformId")
    val platformId: String? = null,

    @JsonProperty("postUrl")
    val postUrl: String? = null,

    @JsonProperty("score")
    val score: Double? = null,

    @JsonProperty("statistics")
    val statistics: Statistics? = null,

    @JsonProperty("subscriberCount")
    val subscriberCount: Int? = null,

    @JsonProperty("title")
    val title: String? = null,

    @JsonProperty("type")
    val type: Types? = null,

    @JsonProperty("updated")
    val updated: String? = null,

    @JsonProperty("videoLengthMS")
    val videoLengthMS: Int? = null
) {

    /**
     * The status of the live video.
     */
    enum class LiveVideoStatus(val value: String) {

        @JsonProperty("completed")
        COMPLETED("completed"),

        @JsonProperty("live")
        LIVE("live"),

        @JsonProperty("upcoming")
        UPCOMING("upcoming");

        override fun toString(): String = value

    }

}
