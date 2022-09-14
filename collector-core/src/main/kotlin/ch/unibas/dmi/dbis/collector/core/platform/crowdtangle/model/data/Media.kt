package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * CrowdTangle media data model.
 *
 * @param full The source of the full-sized version of the media.
 * @param type The type of the media (photo or video).
 * @param url The source URL of the media.
 * @param height The height of the media.
 * @param width The width of the media.
 */
data class Media(
    @JsonProperty("full")
    val full: String? = null,

    @JsonProperty("type")
    val type: Type? = null,

    @JsonProperty("url")
    val url: String? = null,

    @JsonProperty("height")
    val height: Int? = null,

    @JsonProperty("width")
    val width: Int? = null
) {

    /**
     * The type of the media (photo or video).
     */
    enum class Type(val value: String) {

        @JsonProperty("photo")
        PHOTO("photo"),

        @JsonProperty("video")
        VIDEO("video");

        override fun toString(): String = value

    }

}
