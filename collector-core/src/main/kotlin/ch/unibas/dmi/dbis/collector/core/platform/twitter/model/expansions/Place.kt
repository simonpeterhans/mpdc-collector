package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions

import com.fasterxml.jackson.annotation.JsonProperty

data class Place(
    @JsonProperty("id")
    val id: String,

    @JsonProperty("full_name")
    val fullName: String,

    @JsonProperty("name")
    val name: String? = null,

    @JsonProperty("country_code")
    val countryCode: String? = null,

    @JsonProperty("place_type")
    val placeType: PlaceType? = null,

    @JsonProperty("country")
    val country: String? = null,

    @JsonProperty("contained_within")
    val containedWithin: List<String>? = null,

    @JsonProperty("geo")
    val geo: Geo? = null
) {

    enum class PlaceType(val value: String) {

        @JsonProperty("poi")
        POI("poi"),

        @JsonProperty("neighborhood")
        NEIGHBORHOOD("neighborhood"),

        @JsonProperty("city")
        CITY("city"),

        @JsonProperty("admin")
        ADMIN("admin"),

        @JsonProperty("country")
        COUNTRY("country"),

        @JsonProperty("unknown")
        UNKNOWN("unknown");

        override fun toString(): String = value

    }

}
