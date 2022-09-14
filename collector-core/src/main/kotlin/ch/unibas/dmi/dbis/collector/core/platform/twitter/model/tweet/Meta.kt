package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty

class Meta(
    @JsonProperty("newest_id")
    val newestId: String,

    @JsonProperty("oldest_id")
    val oldestId: String,

    @JsonProperty("result_count")
    val resultCount: Int,

    // Not present on last result page - can be used to check for query termination.
    @JsonProperty("next_token")
    val nextToken: String?
)
