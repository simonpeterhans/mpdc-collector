package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.responses

import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.rules.Rule
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.rules.RuleAddError
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.rules.RuleAddMeta
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/*
 * We ignore unknown properties (mostly error messages) for now.
 * If this should ever become a problem and we are required to check for more than the response
 * code, consider extending/adjusting this object accordingly.
 * Due to the many error message formats, we would probably have to work with subtypes.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AddStreamRuleResponse(
    @JsonProperty("data")
    val data: List<Rule>?,

    @JsonProperty("meta")
    val meta: RuleAddMeta?,

    @JsonProperty("errors")
    val errors: List<RuleAddError>?
)
