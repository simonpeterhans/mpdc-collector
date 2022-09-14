package ch.unibas.dmi.dbis.collector.core.config

import com.fasterxml.jackson.annotation.JsonProperty

data class CineastConfig(
    @JsonProperty("url")
    val url: String
)
