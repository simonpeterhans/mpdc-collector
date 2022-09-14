package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions

import com.fasterxml.jackson.annotation.JsonProperty

data class UserWithheld(
    @JsonProperty("country_codes")
    val countryCodes: List<String>,

    @JsonProperty("scope")
    val scope: Scope? = null
) {

    enum class Scope(val value: String) {

        @JsonProperty("user")
        USER("user");

        override fun toString(): String = value

    }

}
