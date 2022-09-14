package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty

data class TweetAttachments(
    @JsonProperty("media_keys")
    val mediaKeys: List<String>? = null,

    @JsonProperty("poll_ids")
    val pollIds: List<String>? = null
)
