package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions

import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet.Tweet
import com.fasterxml.jackson.annotation.JsonProperty

data class Expansions(
    @JsonProperty("users")
    val users: List<User>? = null,

    @JsonProperty("tweets")
    val tweets: List<Tweet>? = null,

    @JsonProperty("places")
    val places: List<Place>? = null,

    @JsonProperty("media")
    val media: List<Media>? = null,

    @JsonProperty("polls")
    val polls: List<Poll>? = null
)
