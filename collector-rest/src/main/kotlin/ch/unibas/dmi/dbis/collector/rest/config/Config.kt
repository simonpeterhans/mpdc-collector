package ch.unibas.dmi.dbis.collector.rest.config

import ch.unibas.dmi.dbis.collector.core.config.CineastConfig
import ch.unibas.dmi.dbis.collector.core.config.DatabaseConfig
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File

data class Config(
    @JsonProperty("database") val database: DatabaseConfig,
    @JsonProperty("api") val api: ApiConfig,
    @JsonProperty("cineast") val cineast: CineastConfig
) {

    companion object {

        private const val DEFAULT_CONFIG_FILE = "config.json"

        private val mapper = jacksonObjectMapper().findAndRegisterModules()

        fun load(config: String = DEFAULT_CONFIG_FILE): Config {
            val jsonString = File(config).readText()
            return mapper.readValue(jsonString, Config::class.java)
        }

    }

}
