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

import ch.unibas.dmi.dbis.cineast.client.models.AbstractMetadataFilterDescriptor

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 
 *
 * @param type 
 * @param keywords 
 */

data class MetadataKeyFilter (

    @field:JsonProperty("type")
    override val type: kotlin.String,

    @field:JsonProperty("keywords")
    override val keywords: kotlin.collections.List<kotlin.String>? = null

) : AbstractMetadataFilterDescriptor

