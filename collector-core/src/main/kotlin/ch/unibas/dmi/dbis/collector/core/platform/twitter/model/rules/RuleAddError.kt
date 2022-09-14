package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.rules

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

data class RuleAddError(
    @JsonProperty("id")
    val id: String?,

    @JsonProperty("value")
    val value: String,

    @JsonProperty("title")
    val title: String,

    @JsonProperty("type")
    val type: String,

    @JsonProperty("details")
    val details: JsonNode
)
