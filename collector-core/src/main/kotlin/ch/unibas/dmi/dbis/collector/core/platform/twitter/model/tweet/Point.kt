package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class Point(
    @JsonProperty("type")
    val type: Type,

    @JsonProperty("coordinates")
    val coordinates: List<BigDecimal>
) {

    enum class Type(val value: String) {

        @JsonProperty("Point")
        POINT("Point");

        override fun toString(): String = value

    }

}
