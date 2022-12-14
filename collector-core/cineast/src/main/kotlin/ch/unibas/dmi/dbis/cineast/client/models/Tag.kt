/**
 * Cineast RESTful API
 *
 * Cineast is vitrivr's content-based multimedia retrieval engine. This is it's RESTful API.
 *
 * The version of the OpenAPI document: v1
 * Contact: contact@vitrivr.org
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package ch.unibas.dmi.dbis.cineast.client.models


import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 
 *
 * @param name 
 * @param priority 
 * @param id 
 * @param description 
 */

data class Tag (

    @field:JsonProperty("name")
    val name: kotlin.String? = null,

    @field:JsonProperty("priority")
    val priority: Tag.Priority? = null,

    @field:JsonProperty("id")
    val id: kotlin.String? = null,

    @field:JsonProperty("description")
    val description: kotlin.String? = null

) {

    /**
     * 
     *
     * Values: REQUEST,REQUIRE,EXCLUDE
     */
    enum class Priority(val value: kotlin.String) {
        @JsonProperty(value = "REQUEST") REQUEST("REQUEST"),
        @JsonProperty(value = "REQUIRE") REQUIRE("REQUIRE"),
        @JsonProperty(value = "EXCLUDE") EXCLUDE("EXCLUDE");
    }
}

