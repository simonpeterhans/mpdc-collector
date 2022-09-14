package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.rules

import com.fasterxml.jackson.annotation.JsonProperty

data class RuleAddSummary(
    @JsonProperty("created")
    val created: Int,

    @JsonProperty("not_created")
    val notCreated: Int,

    @JsonProperty("valid")
    val valid: Int,

    @JsonProperty("invalid")
    val invalid: Int
)
