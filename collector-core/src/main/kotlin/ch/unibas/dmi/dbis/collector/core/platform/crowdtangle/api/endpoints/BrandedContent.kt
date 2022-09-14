package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.api.endpoints

import com.fasterxml.jackson.annotation.JsonProperty

enum class BrandedContent(val value: String) {

    @JsonProperty("as_marketer")
    AS_MARKETER("as_marketer"),

    @JsonProperty("as_publisher")
    AS_PUBLISHER("as_publisher"),

    @JsonProperty("exclude")
    EXCLUDE("exclude"),

    @JsonProperty("no_filter")
    NO_FILTER("no_filter");

    override fun toString(): String = value

}
