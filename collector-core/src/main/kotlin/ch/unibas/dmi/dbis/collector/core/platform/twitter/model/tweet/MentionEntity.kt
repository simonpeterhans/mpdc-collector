package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty

data class MentionEntity(
    @JsonProperty("start")
    val start: Int,

    @JsonProperty("end")
    val end: Int,

    @JsonProperty("username")
    val username: String,

    @JsonProperty("id")
    val id: String? // May not be present for mentions in the Users object.
)
