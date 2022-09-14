package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty

enum class ReplySettings(val value: String) {

    @JsonProperty("everyone")
    EVERYONE("everyone"),

    @JsonProperty("mentionedUsers")
    MENTIONED_USERS("mentionedUsers"),

    @JsonProperty("following")
    FOLLOWING("following"),

    @JsonProperty("other")
    OTHER("other");

    override fun toString(): String = value

}
