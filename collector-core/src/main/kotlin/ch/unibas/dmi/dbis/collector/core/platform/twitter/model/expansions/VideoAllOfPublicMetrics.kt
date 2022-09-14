package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions

import com.fasterxml.jackson.annotation.JsonProperty

data class VideoAllOfPublicMetrics(
    @JsonProperty("view_count")
    val viewCount: Int
)
