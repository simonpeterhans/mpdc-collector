package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions

import com.fasterxml.jackson.annotation.JsonProperty

data class Video(
    @JsonProperty("media_key")
    override val mediaKey: String,

    @JsonProperty("height")
    override val height: Int? = null,

    @JsonProperty("width")
    override val width: Int? = null,

    @JsonProperty("public_metrics")
    override val publicMetrics: VideoAllOfPublicMetrics? = null,

    @JsonProperty("non_public_metrics")
    override val nonPublicMetrics: VideoAllOfNonPublicMetrics? = null,

    @JsonProperty("organic_metrics")
    override val organicMetrics: VideoAllOfOrganicMetrics? = null,

    @JsonProperty("promoted_metrics")
    override val promotedMetrics: VideoAllOfPromotedMetrics? = null,

    @JsonProperty
    val variants: List<VideoVariant>? = null,

    @JsonProperty("preview_image_url")
    val previewImageUrl: String? = null,

    @JsonProperty("duration_ms")
    val durationMs: Int? = null,

    @JsonProperty("type")
    override val type: Type = Type.VIDEO
) : Media()
