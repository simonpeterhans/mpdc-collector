package ch.unibas.dmi.dbis.collector.rest.config

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiConfig(
    @JsonProperty("port") val port: Int
)
