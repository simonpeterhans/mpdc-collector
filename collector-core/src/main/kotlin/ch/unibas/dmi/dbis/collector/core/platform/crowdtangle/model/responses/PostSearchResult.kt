package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.responses

import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data.CrowdTanglePost
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * The result of archive search, consisting of an array of posts and a pagination object.
 *
 * @param posts An array of post objects matching the query.
 * @param pagination The pagination object containing previous/next URLs.
 */
data class PostSearchResult(
    @JsonProperty("posts")
    val posts: List<CrowdTanglePost>? = null,

    @JsonProperty("pagination")
    val pagination: Pagination? = null,

    @JsonProperty("hitCount")
    val hitCount: Int? = null
)
