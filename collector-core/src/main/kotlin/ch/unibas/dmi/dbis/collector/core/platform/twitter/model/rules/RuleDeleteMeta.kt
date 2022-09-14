package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.rules

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class RuleDeleteMeta(
    @JsonProperty("sent")
    val sent: Instant,

    @JsonProperty("summary")
    val summary: RuleDeleteSummary
)
