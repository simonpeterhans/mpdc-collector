package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty

data class TweetGeo(
    @JsonProperty("coordinates")
    val coordinates: Point? = null,

    @JsonProperty("place_id")
    val placeId: String? = null
)
