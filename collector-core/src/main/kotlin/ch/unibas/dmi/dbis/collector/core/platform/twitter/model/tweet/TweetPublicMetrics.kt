package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty

data class TweetPublicMetrics(
    @JsonProperty("retweet_count")
    val retweetCount: Int,

    @JsonProperty("reply_count")
    val replyCount: Int,

    @JsonProperty("like_count")
    val likeCount: Int,

    @JsonProperty("quote_count")
    val quoteCount: Int? = null
)
