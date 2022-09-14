package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data

import com.fasterxml.jackson.annotation.JsonProperty

data class ExpandedLink(
    @JsonProperty("expanded")
    val expanded: String,

    @JsonProperty("original")
    val original: String
)
