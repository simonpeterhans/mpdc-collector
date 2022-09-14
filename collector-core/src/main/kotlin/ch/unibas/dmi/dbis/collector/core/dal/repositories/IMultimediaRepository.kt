package ch.unibas.dmi.dbis.collector.core.dal.repositories

import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.data.ResourceStatus
import ch.unibas.dmi.dbis.collector.core.model.platform.ApiPlatform
import java.time.Instant

data class MultimediaDetails(
    val id: Long,
    val status: ResourceStatus,
    val hash: String?,
    val contentType: String?
)

data class MultimediaProcessingRequest(
    val id: Long,
    val mediaType: MediaType,
    val url: String?,
    val collection: String,
    val indexInCineast: Boolean
)

data class MultimediaResourceUpdate(
    val id: Long,
    val hash: String?,
    val contentType: String?,
    val status: ResourceStatus,
    val statusTimestamp: Instant
)

interface IMultimediaRepository {

    fun updateMultimediaResource(u: MultimediaResourceUpdate): Boolean

    fun getDetailsByUrl(url: String): MultimediaDetails?

    fun getMultimediaObjectsForProcessing(
        platform: ApiPlatform,
        mediaTypes: List<MediaType>,
        numResults: Int
    ): List<MultimediaProcessingRequest>

}
