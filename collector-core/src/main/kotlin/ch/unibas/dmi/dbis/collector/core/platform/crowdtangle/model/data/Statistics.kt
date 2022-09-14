package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * CrowdTangle statistics data model.
 *
 * @param expected A mapping of the expected properties and their values (integers).
 * @param `actual` A mapping of the actual properties and their values (integers).
 * @param timestep The timestep of the statistics data (only available for includeHistory=true), 1 = first.
 * @param date The date the statistics were recorded at (only available for includeHistory=true).
 * @param score The score of the statistics for the given timestep (only available for includeHistory=true).
 */
data class Statistics(
    @JsonProperty("expected")
    val expected: Map<String, Int>? = null,

    @JsonProperty("actual")
    val `actual`: Map<String, Int>? = null,

    @JsonProperty("timestep")
    val timestep: Int? = null,

    @JsonProperty("date")
    val date: String? = null,

    @JsonProperty("score")
    val score: Double? = null
)
