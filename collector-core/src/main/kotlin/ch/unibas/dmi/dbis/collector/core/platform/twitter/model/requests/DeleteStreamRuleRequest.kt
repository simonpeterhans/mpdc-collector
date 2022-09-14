package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.requests

import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.rules.RuleDeleteRequest
import com.fasterxml.jackson.annotation.JsonProperty

data class DeleteStreamRuleRequest(
    @JsonProperty("delete")
    val delete: RuleDeleteRequest?
)
