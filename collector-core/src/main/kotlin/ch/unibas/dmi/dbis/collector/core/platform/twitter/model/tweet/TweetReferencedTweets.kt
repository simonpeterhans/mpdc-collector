package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty

data class TweetReferencedTweets(
    @JsonProperty("type")
    val type: Type,

    @JsonProperty("id")
    val id: String
) {

    enum class Type(val value: String) {

        @JsonProperty("retweeted")
        RETWEETED("retweeted"),

        @JsonProperty("quoted")
        QUOTED("quoted"),

        @JsonProperty("replied_to")
        REPLIED_TO("replied_to");

        override fun toString(): String = value

    }

}
