package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions

import com.fasterxml.jackson.annotation.JsonProperty

data class PollOption(
    @JsonProperty("position")
    val position: Int,

    @JsonProperty("label")
    val label: String,

    @JsonProperty("votes")
    val votes: Int
)
