package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty

data class UrlEntity(
    @JsonProperty("start")
    val start: Int,

    @JsonProperty("end")
    val end: Int,

    @JsonProperty("url")
    val url: String,

    @JsonProperty("media_key")
    val mediaKey: String? = null,

    @JsonProperty("expanded_url")
    val expandedUrl: String? = null,

    // Not an actual URI, but a (shortened) string.
    @JsonProperty("display_url")
    val displayUrl: String? = null,

    @JsonProperty("unwound_url")
    val unwoundUrl: String? = null,

    @JsonProperty("status")
    val status: Int? = null,

    @JsonProperty("title")
    val title: String? = null,

    @JsonProperty("description")
    val description: String? = null,

    @JsonProperty("images")
    val images: List<UrlImage>? = null
)
