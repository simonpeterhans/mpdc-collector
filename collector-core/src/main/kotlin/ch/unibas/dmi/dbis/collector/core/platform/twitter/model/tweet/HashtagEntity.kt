package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty

data class HashtagEntity(
    @JsonProperty("start")
    val start: Int,

    @JsonProperty("end")
    val end: Int,

    @JsonProperty("tag")
    val tag: String
)
