package ch.unibas.dmi.dbis.collector.core.dal.repositories.exposed

import ch.unibas.dmi.dbis.collector.core.dal.repositories.IMultimediaRepository
import ch.unibas.dmi.dbis.collector.core.dal.repositories.MultimediaDetails
import ch.unibas.dmi.dbis.collector.core.dal.repositories.MultimediaProcessingRequest
import ch.unibas.dmi.dbis.collector.core.dal.repositories.MultimediaResourceUpdate
import ch.unibas.dmi.dbis.collector.core.dal.storage.data.MultimediaDao
import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.data.ResourceStatus
import ch.unibas.dmi.dbis.collector.core.model.platform.ApiPlatform
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

data class MultimediaStatusUpdate(
    val id: Long,
    val status: ResourceStatus,
    val statusTimestamp: Instant
)

class MultimediaRepositoryExposed : IMultimediaRepository {

    override fun updateMultimediaResource(u: MultimediaResourceUpdate) = transaction {
        MultimediaDao.updateMultimediaResource(
            u.id,
            u.status,
            u.statusTimestamp,
            u.contentType,
            u.hash
        )

        true
    }

    private fun updateMultimediaStatus(u: MultimediaStatusUpdate) = transaction {
        MultimediaDao.updateMultimediaStatus(u.id, u.status, u.statusTimestamp)
    }

    override fun getDetailsByUrl(url: String) = transaction {
        val dto = MultimediaDao.findByUrl(url) ?: return@transaction null
        MultimediaDetails(dto.key!!, dto.status, dto.hash, dto.contentType)
    }

    override fun getMultimediaObjectsForProcessing(
        platform: ApiPlatform,
        mediaTypes: List<MediaType>,
        numResults: Int
    ): List<MultimediaProcessingRequest> = transaction {
        val dtos =
            MultimediaDao.getObjectsForProcessing(platform, mediaTypes, SortOrder.ASC, numResults)

        dtos.forEach {
            updateMultimediaStatus(
                MultimediaStatusUpdate(
                    it.multimediaDto.key!!,
                    ResourceStatus.PROCESSING,
                    Instant.now()
                )
            )
        }

        dtos.map {
            MultimediaProcessingRequest(
                it.multimediaDto.key!!,
                it.multimediaDto.mediaType,
                it.multimediaDto.url,
                it.collection,
                it.indexInCineast
            )
        }
    }

}
