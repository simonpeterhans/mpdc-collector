package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Describes the type of CrowdTangle post.
 */
enum class Types(val value: String) {

    @JsonProperty("album")
    ALBUM("album"),

    @JsonProperty("igtv")
    IGTV("igtv"),

    @JsonProperty("link")
    LINK("link"),

    @JsonProperty("live_video")
    LIVE_VIDEO("live_video"),

    @JsonProperty("live_video_complete")
    LIVE_VIDEO_COMPLETE("live_video_complete"),

    @JsonProperty("live_video_scheduled")
    LIVE_VIDEO_SCHEDULED("live_video_scheduled"),

    @JsonProperty("native_video")
    NATIVE_VIDEO("native_video"),

    @JsonProperty("photo")
    PHOTO("photo"),

    @JsonProperty("status")
    STATUS("status"),

    @JsonProperty("video")
    VIDEO("video"),

    @JsonProperty("vine")
    VINE("vine"),

    @JsonProperty("youtube")
    YOUTUBE("youtube");

    override fun toString(): String = value

}
