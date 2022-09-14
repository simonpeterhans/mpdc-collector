package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class User(
    @JsonProperty("id")
    val id: String,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("username")
    val username: String,

    @JsonProperty("created_at")
    val createdAt: Instant? = null,

    @JsonProperty("protected")
    val `protected`: Boolean? = null,

    @JsonProperty("verified")
    val verified: Boolean? = null,

    @JsonProperty("withheld")
    val withheld: UserWithheld? = null,

    @JsonProperty("profile_image_url")
    val profileImageUrl: String? = null,

    @JsonProperty("location")
    val location: String? = null,

    @JsonProperty("url")
    val url: String? = null,

    @JsonProperty("description")
    val description: String? = null,

    @JsonProperty("entities")
    val entities: UserEntities? = null,

    @JsonProperty("pinned_tweet_id")
    val pinnedTweetId: String? = null,

    @JsonProperty("public_metrics")
    val publicMetrics: UserPublicMetrics? = null
)
