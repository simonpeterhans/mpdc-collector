package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.rules

import com.fasterxml.jackson.annotation.JsonProperty

data class RuleDeleteSummary(
    @JsonProperty("deleted")
    val deleted: Int,

    @JsonProperty("not_deleted")
    val notDeleted: Int
)
