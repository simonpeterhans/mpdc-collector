package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty

data class TweetWithheld(
    @JsonProperty("copyright")
    val copyright: Boolean,

    @JsonProperty("country_codes")
    val countryCodes: List<String>,

    @JsonProperty("scope")
    val scope: Scope? = null
) {

    enum class Scope(val value: String) {

        @JsonProperty("tweet")
        TWEET("tweet"),

        @JsonProperty("user")
        USER("user");

        override fun toString(): String = value

    }

}
