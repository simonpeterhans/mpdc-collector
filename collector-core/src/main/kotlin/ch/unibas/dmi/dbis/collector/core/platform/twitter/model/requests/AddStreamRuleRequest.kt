package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.requests

import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.rules.Rule
import com.fasterxml.jackson.annotation.JsonProperty

data class AddStreamRuleRequest(
    @JsonProperty("add")
    val add: List<Rule>?
)
