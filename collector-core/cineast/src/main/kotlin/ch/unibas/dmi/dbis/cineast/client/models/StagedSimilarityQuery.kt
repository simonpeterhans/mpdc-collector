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

import ch.unibas.dmi.dbis.cineast.client.models.QueryConfig
import ch.unibas.dmi.dbis.cineast.client.models.QueryStage

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 
 *
 * @param stages 
 * @param config 
 */

data class StagedSimilarityQuery (

    @field:JsonProperty("stages")
    val stages: kotlin.collections.List<QueryStage>? = null,

    @field:JsonProperty("config")
    val config: QueryConfig? = null

)

