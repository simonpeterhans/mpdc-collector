package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty

data class TweetAnnotation(
    @JsonProperty("start")
    val startIndex: Int,

    @JsonProperty("end")
    val endIndex: Int,

    @JsonProperty("probability")
    val probability: Float,

    // This could also be an enum, see https://developer.twitter.com/en/docs/twitter-api/annotations/overview.
    @JsonProperty("type")
    val type: String,

    @JsonProperty("normalized_text")
    val normalizedText: String
)
