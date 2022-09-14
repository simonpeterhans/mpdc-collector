package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty

data class TweetOrganicMetrics(
    @JsonProperty("impression_count")
    val impressionCount: Int,

    @JsonProperty("retweet_count")
    val retweetCount: Int,

    @JsonProperty("reply_count")
    val replyCount: Int,

    @JsonProperty("like_count")
    val likeCount: Int
)
