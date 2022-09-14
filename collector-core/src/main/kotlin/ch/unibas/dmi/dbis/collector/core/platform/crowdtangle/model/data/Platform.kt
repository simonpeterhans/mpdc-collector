package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Describes the platform of a CrowdTangle post.
 */
enum class Platform(val value: String) {

    @JsonProperty("Facebook")
    FACEBOOK("Facebook"),

    @JsonProperty("Instagram")
    INSTAGRAM("Instagram"),

    @JsonProperty("Reddit")
    REDDIT("Reddit");

    override fun toString(): String = value

}
