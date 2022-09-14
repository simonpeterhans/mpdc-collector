package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Describes the parts of a CrowdTangle post to be searched by the query.
 */
enum class SearchField(val value: String) {

    @JsonProperty("text_fields_and_images")
    TEXT_FIELDS_AND_IMAGES("text_fields_and_images"),

    @JsonProperty("include_query_strings")
    INCLUDE_QUERY_STRINGS("include_query_strings"),

    @JsonProperty("text_fields_only")
    TEXT_FIELDS_ONLY("text_fields_only"),

    @JsonProperty("account_name_only")
    ACCOUNT_NAME_ONLY("account_name_only"),

    @JsonProperty("image_text_only")
    IMAGE_TEXT_ONLY("image_text_only");

    override fun toString(): String = value

}

