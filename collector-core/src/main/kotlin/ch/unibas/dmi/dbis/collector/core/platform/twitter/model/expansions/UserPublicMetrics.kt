package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions

import com.fasterxml.jackson.annotation.JsonProperty

data class UserPublicMetrics(
    @JsonProperty("followers_count")
    val followersCount: Int,

    @JsonProperty("following_count")
    val followingCount: Int,

    @JsonProperty("tweet_count")
    val tweetCount: Int,

    @JsonProperty("listed_count")
    val listedCount: Int
)
