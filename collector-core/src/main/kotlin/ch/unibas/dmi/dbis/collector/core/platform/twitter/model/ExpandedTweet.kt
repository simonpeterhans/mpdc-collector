package ch.unibas.dmi.dbis.collector.core.platform.twitter.model

import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions.Media
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions.Place
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions.Poll
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions.User
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet.Tweet
import com.fasterxml.jackson.annotation.JsonProperty

/*
 * Not actually defined by the Twitter API - used internally to make sense of the Twitter data
 * model with expansions.
 * Instead of having all tweets in an attribute and all expansions, we map the expansions to the
 * tweet they belong to by using this class.
 */
data class ExpandedTweet(
    @JsonProperty("tweet")
    val tweet: Tweet,

    @JsonProperty("users")
    val users: List<User> = emptyList(),

    @JsonProperty("places")
    val places: List<Place> = emptyList(),

    @JsonProperty("media")
    val media: List<Media> = emptyList(),

    @JsonProperty("polls")
    val polls: List<Poll> = emptyList()
)
