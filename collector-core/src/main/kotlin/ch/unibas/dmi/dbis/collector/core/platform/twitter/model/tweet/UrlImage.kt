package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty

data class UrlImage(
    @JsonProperty("url")
    val url: String? = null,

    @JsonProperty("height")
    val height: Int? = null,

    @JsonProperty("width")
    val width: Int? = null
)
