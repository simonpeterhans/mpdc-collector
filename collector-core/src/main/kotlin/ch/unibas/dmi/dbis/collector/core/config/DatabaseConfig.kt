package ch.unibas.dmi.dbis.collector.core.config

import com.fasterxml.jackson.annotation.JsonProperty

data class DatabaseConfig(
    @JsonProperty("user")
    val user: String,

    @JsonProperty("pass")
    val pass: String,

    @JsonProperty("url")
    val url: String,

    @JsonProperty("port")
    val port: Short,

    @JsonProperty("database")
    val database: String
)
