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
 * @param queryId 
 * @param featureValues 
 * @param category 
 * @param elementID 
 */

data class FeaturesTextCategoryQueryResult (

    @field:JsonProperty("queryId")
    val queryId: kotlin.String? = null,

    @field:JsonProperty("featureValues")
    val featureValues: kotlin.collections.List<kotlin.String>? = null,

    @field:JsonProperty("category")
    val category: kotlin.String? = null,

    @field:JsonProperty("elementID")
    val elementID: kotlin.String? = null

)

