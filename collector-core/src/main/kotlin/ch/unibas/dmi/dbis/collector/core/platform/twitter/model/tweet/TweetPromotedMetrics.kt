package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty

data class TweetPromotedMetrics(
    @JsonProperty("impression_count")
    val impressionCount: Int? = null,

    @JsonProperty("like_count")
    val likeCount: Int? = null,

    @JsonProperty("reply_count")
    val replyCount: Int? = null,

    @JsonProperty("retweet_count")
    val retweetCount: Int? = null
)
