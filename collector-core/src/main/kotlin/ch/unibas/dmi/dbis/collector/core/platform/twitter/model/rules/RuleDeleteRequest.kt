package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.rules

import com.fasterxml.jackson.annotation.JsonProperty

data class RuleDeleteRequest(
    @JsonProperty("ids")
    val ids: List<String>
)
