package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty

data class TweetNonPublicMetrics(
    @JsonProperty("impression_count")
    val impressionCount: Int? = null
)
