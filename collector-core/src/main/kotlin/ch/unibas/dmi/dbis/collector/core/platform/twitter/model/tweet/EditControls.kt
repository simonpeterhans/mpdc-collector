package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class EditControls(
    @JsonProperty("is_edit_eligible")
    val isEditEligible: Boolean? = null,

    @JsonProperty("editable_until")
    val editableUntil: Instant? = null,

    @JsonProperty("edits_remaining")
    val editsRemaining: Int? = null
)
