package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(value = AnimatedGif::class, name = "animated_gif"),
    JsonSubTypes.Type(value = Photo::class, name = "photo"),
    JsonSubTypes.Type(value = Video::class, name = "video")
)
//@JsonIgnoreProperties(ignoreUnknown = true)
abstract class Media {

    @get:JsonProperty("type")
    abstract val type: Type

    @get:JsonProperty("media_key")
    abstract val mediaKey: String

    @get:JsonProperty("height")
    abstract val height: Int?

    @get:JsonProperty("width")
    abstract val width: Int?

    @get:JsonProperty("public_metrics")
    abstract val publicMetrics: VideoAllOfPublicMetrics?

    @get:JsonProperty("non_public_metrics")
    abstract val nonPublicMetrics: VideoAllOfNonPublicMetrics?

    @get:JsonProperty("organic_metrics")
    abstract val organicMetrics: VideoAllOfOrganicMetrics?

    @get:JsonProperty("promoted_metrics")
    abstract val promotedMetrics: VideoAllOfPromotedMetrics?

    enum class Type(val value: String) {

        @JsonProperty("animated_gif")
        ANIMATED_GIF("animated_gif"),

        @JsonProperty("photo")
        PHOTO("photo"),

        @JsonProperty("video")
        VIDEO("video"),

        @JsonProperty("unknown")
        UNKNOWN("unknown");

        override fun toString(): String = value

    }

}
