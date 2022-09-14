package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.rules

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class RuleShowMeta(
    @JsonProperty("sent")
    val sent: Instant,

    @JsonProperty("result_count")
    val resultCount: Int
)
