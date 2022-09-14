package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.api.endpoints

import com.fasterxml.jackson.annotation.JsonProperty

enum class Verified(val value: String) {

    @JsonProperty("exclude")
    EXCLUDE("exclude"),

    @JsonProperty("no_filter")
    NO_FILTER("no_filter"),

    @JsonProperty("only")
    ONLY("only");

    override fun toString(): String = value

}
