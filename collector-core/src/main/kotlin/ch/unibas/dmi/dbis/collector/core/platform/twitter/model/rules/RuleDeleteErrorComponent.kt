package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.rules

import com.fasterxml.jackson.annotation.JsonProperty

data class RuleDeleteErrorComponent(
    @JsonProperty("parameters")
    val value: Map<String, String>,

    @JsonProperty("message")
    val message: String
)
