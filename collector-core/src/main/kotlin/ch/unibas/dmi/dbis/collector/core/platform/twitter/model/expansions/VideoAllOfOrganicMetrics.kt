package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions

import com.fasterxml.jackson.annotation.JsonProperty

data class VideoAllOfOrganicMetrics(
    @JsonProperty("playback_0_count")
    val playback0Count: Int,

    @JsonProperty("playback_25_count")
    val playback25Count: Int,

    @JsonProperty("playback_50_count")
    val playback50Count: Int,

    @JsonProperty("playback_75_count")
    val playback75Count: Int,

    @JsonProperty("playback_100_count")
    val playback100Count: Int,

    @JsonProperty("view_count")
    val viewCount: Int
)
