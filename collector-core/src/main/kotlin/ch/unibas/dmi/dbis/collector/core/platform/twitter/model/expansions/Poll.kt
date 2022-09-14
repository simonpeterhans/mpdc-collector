package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class Poll(
    @JsonProperty("id")
    val id: String,

    @JsonProperty("options")
    val options: List<PollOption>,

    @JsonProperty("voting_status")
    val votingStatus: VotingStatus? = null,

    @JsonProperty("end_datetime")
    val endDatetime: Instant? = null,

    @JsonProperty("duration_minutes")
    val durationMinutes: Int? = null
) {

    enum class VotingStatus(val value: String) {

        @JsonProperty("open")
        OPEN("open"),

        @JsonProperty("closed")
        CLOSED("closed");

        override fun toString(): String = value

    }

}
