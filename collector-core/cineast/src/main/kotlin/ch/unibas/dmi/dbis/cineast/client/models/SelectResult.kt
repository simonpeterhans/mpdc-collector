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
 * @param columns 
 */

data class SelectResult (

    @field:JsonProperty("columns")
    val columns: kotlin.collections.List<kotlin.collections.Map<kotlin.String, kotlin.String>>? = null

)

