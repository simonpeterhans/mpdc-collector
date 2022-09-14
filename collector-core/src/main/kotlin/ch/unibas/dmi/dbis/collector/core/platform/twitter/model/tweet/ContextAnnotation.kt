package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet

import com.fasterxml.jackson.annotation.JsonProperty

data class ContextAnnotation(
    @JsonProperty("domain")
    val domain: ContextAnnotationDomainFields,

    @JsonProperty("entity")
    val entity: ContextAnnotationEntityFields
)
