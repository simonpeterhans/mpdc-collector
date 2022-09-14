package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.responses

import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions.Expansions
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet.Meta
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet.Tweet
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/*
 * We ignore unknown properties (mostly error messages) for now.
 * If this should ever become a problem and we are required to check for more than the response
 * code, consider extending/adjusting this object accordingly.
 * Due to the many error message formats, we would probably have to work with subtypes.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class TweetSearchResponse(
    @JsonProperty("data")
    val data: List<Tweet>?,

    @JsonProperty("includes")
    val expansions: Expansions?,

    @JsonProperty("meta")
    val meta: Meta?
)
