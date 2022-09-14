package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions

import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet.Point
import com.fasterxml.jackson.annotation.JsonProperty

data class Geo(
    @JsonProperty("type")
    val type: Type,

    @JsonProperty("bbox")
    val bbox: List<Double>,

    @JsonProperty("properties")
    val properties: Any,

    @JsonProperty("geometry")
    val geometry: Point? = null
) {

    enum class Type(val value: String) {

        @JsonProperty("Feature")
        FEATURE("Feature");

        override fun toString(): String = value

    }

}
