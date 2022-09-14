package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class Tweet(
    @JsonProperty("id")
    val id: String,

    @JsonProperty("text")
    val text: String,

    @JsonProperty("created_at")
    val createdAt: Instant? = null,

    @JsonProperty("author_id")
    val authorId: String? = null,

    @JsonProperty("in_reply_to_user_id")
    val inReplyToUserId: String? = null,

    @JsonProperty("conversation_id")
    val conversationId: String? = null,

    @JsonProperty("reply_settings")
    val replySettings: ReplySettings? = null,

    @JsonProperty("referenced_tweets")
    val referencedTweets: List<TweetReferencedTweets>? = null,

    @JsonProperty("attachments")
    val attachments: TweetAttachments? = null,

    @JsonProperty("context_annotations")
    val contextAnnotations: List<ContextAnnotation>? = null,

    @JsonProperty("withheld")
    val withheld: TweetWithheld? = null,

    @JsonProperty("geo")
    val geo: TweetGeo? = null,

    @JsonProperty("entities")
    val entities: FullTextEntities? = null,

    @JsonProperty("public_metrics")
    val publicMetrics: TweetPublicMetrics? = null,

    @JsonProperty("possibly_sensitive")
    val possiblySensitive: Boolean? = null,

    @JsonProperty("lang")
    val lang: String? = null,

    @JsonProperty("source")
    val source: String? = null,

    @JsonProperty("non_public_metrics")
    val nonPublicMetrics: TweetNonPublicMetrics? = null,

    @JsonProperty("promoted_metrics")
    val promotedMetrics: TweetPromotedMetrics? = null,

    @JsonProperty("organic_metrics")
    val organicMetrics: TweetOrganicMetrics? = null
)
