package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.api.endpoints

import com.fasterxml.jackson.annotation.JsonProperty

enum class SortBy(val value: String) {

    @JsonProperty("date")
    DATE("date"),

    @JsonProperty("interaction_rate")
    INTERACTION_RATE("interaction_rate"),

    @JsonProperty("overperforming")
    OVERPERFORMING("overperforming"),

    @JsonProperty("total_interactions")
    TOTAL_INTERACTIONS("total_interactions"),

    @JsonProperty("underperforming")
    UNDERPERFORMING("underperforming");

    override fun toString(): String = value

}
