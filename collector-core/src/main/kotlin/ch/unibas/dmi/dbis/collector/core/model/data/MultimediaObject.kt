package ch.unibas.dmi.dbis.collector.core.model.data

import ch.unibas.dmi.dbis.collector.core.model.Identifiable
import java.time.Instant

enum class ResourceStatus {

    UNPROCESSED,
    DUPLICATE,
    UNKNOWN_RESOURCE,
    PROCESSING,
    IO_ERROR,
    RESOURCE_NOT_FOUND,
    INVALID_RESOURCE,
    CONNECTION_CLOSED,
    EXPIRED,
    STORED

}

enum class MediaType {

    UNKNOWN,
    ANIMATED_IMAGE,
    IMAGE,
    VIDEO

}

data class MultimediaObject(
    val url: String?,
    val status: ResourceStatus,
    val statusTimestamp: Instant,
    val mediaType: MediaType,
    val contentType: String? = null,
    val hash: String? = null,
    override val id: Long? = null
) : Identifiable<Long>
