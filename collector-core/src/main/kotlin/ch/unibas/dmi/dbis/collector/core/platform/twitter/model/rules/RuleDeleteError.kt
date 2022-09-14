package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.rules

import com.fasterxml.jackson.annotation.JsonProperty

data class RuleDeleteError(
    @JsonProperty("errors")
    val errors: List<RuleDeleteErrorComponent>,

    @JsonProperty("title")
    val title: String,

    @JsonProperty("detail")
    val detail: String,

    @JsonProperty("type")
    val type: String
)
