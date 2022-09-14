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
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * 
 *
 * @param type 
 * @param keywords 
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes(
    JsonSubTypes.Type(value = MetadataDomainFilter::class, name = "MetadataDomainFilter"),
    JsonSubTypes.Type(value = MetadataKeyFilter::class, name = "MetadataKeyFilter")
)
interface AbstractMetadataFilterDescriptor {

    @get:JsonProperty("type")
    val type: kotlin.String
    @get:JsonProperty("keywords")
    val keywords: kotlin.collections.List<kotlin.String>?
}

