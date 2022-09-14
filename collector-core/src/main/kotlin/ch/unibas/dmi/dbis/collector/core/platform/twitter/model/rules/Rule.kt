package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.rules

import com.fasterxml.jackson.annotation.JsonProperty

data class Rule(
    @JsonProperty("value")
    val value: String?,

    @JsonProperty("id")
    val id: String?,

    @JsonProperty("tag")
    val label: String?
)
