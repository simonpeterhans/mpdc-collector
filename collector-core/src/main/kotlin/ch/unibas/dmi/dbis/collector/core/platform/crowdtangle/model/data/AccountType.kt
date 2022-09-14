package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * The account type, for Facebook only.
 */
enum class AccountType(val value: String) {

    @JsonProperty("facebook_group")
    GROUP("facebook_group"),

    @JsonProperty("facebook_page")
    PAGE("facebook_page"),

    @JsonProperty("facebook_profile")
    PROFILE("facebook_profile");

    override fun toString(): String = value

}
