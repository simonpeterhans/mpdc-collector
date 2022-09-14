package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty

data class FullTextEntities(
    @JsonProperty("annotations")
    val annotation: List<TweetAnnotation>? = null,

    @JsonProperty("urls")
    val urls: List<UrlEntity>? = null,

    @JsonProperty("hashtags")
    val hashtags: List<HashtagEntity>? = null,

    @JsonProperty("mentions")
    val mentions: List<MentionEntity>? = null,

    @JsonProperty("cashtags")
    val cashtags: List<CashtagEntity>? = null
)
