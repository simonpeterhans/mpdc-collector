package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions

import com.fasterxml.jackson.annotation.JsonProperty

data class VideoVariant(
    @JsonProperty("bit_rate")
    val bitrate: Long,

    @JsonProperty("content_type")
    val content_type: String,

    @JsonProperty("url")
    val url: String
)
