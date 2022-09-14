package ch.unibas.dmi.dbis.collector.core.dal.storage.data

import ch.unibas.dmi.dbis.collector.core.dal.columns.pgTimestampWithTimeZone
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDaoRefTable
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDtoWithRef
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongTableDao
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.QueryTable
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.SubQueryTable
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.SuperQueryMultimediaTable
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.SuperQueryTable
import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.data.ResourceStatus
import ch.unibas.dmi.dbis.collector.core.model.platform.ApiPlatform
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.time.Instant

data class MultimediaDto(
    override val ref: Long,
    val url: String?,
    val status: ResourceStatus,
    val statusTimestamp: Instant,
    val mediaType: MediaType,
    val contentType: String?,
    val hash: String?,
    override val key: Long? = null
) : LongDtoWithRef()

object MultimediaTable : LongDaoRefTable("multimedia") {

    override val refColumn = long("entity_id").index().references(
        ModelEntityTable.keyColumn,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val url = text("url").nullable()
    val status = enumerationByName<ResourceStatus>("status", MAX_ENUM_LENGTH)
    val statusTimestamp = pgTimestampWithTimeZone("status_ts")
    val mediaType = enumerationByName<MediaType>("media_type", MAX_ENUM_LENGTH)
    val contentType = text("content_type").nullable()
    val hash = text("hash").index().nullable()

}

data class MultimediaProcessingDtoOut(
    val multimediaDto: MultimediaDto,
    val collection: String,
    val indexInCineast: Boolean,
    val timestamp: Instant
)

object MultimediaDao : LongTableDao<MultimediaDto>() {

    override val table = MultimediaTable

    override fun rowToDto(row: ResultRow) = MultimediaDto(
        ref = row[table.refColumn],
        url = row[table.url],
        status = row[table.status],
        statusTimestamp = row[table.statusTimestamp],
        mediaType = row[table.mediaType],
        contentType = row[table.contentType],
        hash = row[table.hash],
        key = row[table.keyColumn]
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: MultimediaDto) {
        it[table.refColumn] = dto.ref
        it[table.url] = dto.url
        it[table.status] = dto.status
        it[table.statusTimestamp] = dto.statusTimestamp
        it[table.mediaType] = dto.mediaType
        it[table.contentType] = dto.contentType
        it[table.hash] = dto.hash
    }

    fun totalMediaObjectsBySubQuery(subQueryId: Long): Long =
        MultimediaTable.innerJoin(
            ModelEntityTable,
            { refColumn },
            { ModelEntityTable.keyColumn },
            { ModelEntityTable.refColumn eq subQueryId }
        ).selectAll().count()

    fun totalProcessedMediaObjectsBySubQuery(subQueryId: Long): Long =
        MultimediaTable.innerJoin(
            ModelEntityTable,
            { refColumn },
            { ModelEntityTable.keyColumn },
            {
                (ModelEntityTable.refColumn eq subQueryId) and
                    (MultimediaTable.status neq ResourceStatus.UNPROCESSED) and
                    (MultimediaTable.status neq ResourceStatus.PROCESSING)
            }
        ).selectAll().count()

    fun remainingMediaObjectsBySubQuery(subQueryId: Long, mediaTypes: List<MediaType>) =
        MultimediaTable.innerJoin(
            ModelEntityTable,
            { refColumn },
            { ModelEntityTable.keyColumn },
            {
                ((MultimediaTable.status eq ResourceStatus.UNPROCESSED) or
                    (MultimediaTable.status eq ResourceStatus.PROCESSING)) and
                    (ModelEntityTable.refColumn eq subQueryId) and
                    (MultimediaTable.mediaType inList mediaTypes)
            }
        ).selectAll().count()

    fun uniqueProcessedMediaObjectsBySubQuery(subQueryId: Long): Long =
        MultimediaTable.innerJoin(
            ModelEntityTable,
            { refColumn },
            { ModelEntityTable.keyColumn },
            { (ModelEntityTable.refColumn eq subQueryId) and (MultimediaTable.hash neq null) }
        ).slice(MultimediaTable.hash)
            .selectAll()
            .withDistinct(true)
            .count()

    fun getObjectsForProcessing(
        platform: ApiPlatform,
        mediaTypes: List<MediaType>,
        timestampOrder: SortOrder,
        numResults: Int,
    ): List<MultimediaProcessingDtoOut> {
        val media = MultimediaTable.innerJoin(
            ModelEntityTable,
            { refColumn },
            { keyColumn },
            { (MultimediaTable.url neq null) and (MultimediaTable.status eq ResourceStatus.UNPROCESSED) }
        ).innerJoin(
            SubQueryTable,
            { ModelEntityTable.refColumn },
            { keyColumn }
        ).innerJoin(
            QueryTable,
            { SubQueryTable.refColumn },
            { keyColumn },
            { QueryTable.platform eq platform }
        ).innerJoin(
            SuperQueryTable,
            { SubQueryTable.superId },
            { SuperQueryTable.keyColumn }
        ).innerJoin(
            SuperQueryMultimediaTable,
            { SubQueryTable.superId },
            { refColumn },
            { (SuperQueryMultimediaTable.multimediaType eq MultimediaTable.mediaType) and (MultimediaTable.mediaType inList mediaTypes) }
        ).slice(
            QueryTable.collection,
            ModelEntityTable.platformTimestamp,
            *MultimediaTable.columns.toTypedArray(), // All multimedia table columns.
            SuperQueryTable.indexInCineast
        ).selectAll(
        ).orderBy(
            Pair(ModelEntityTable.platformTimestamp, timestampOrder),
        ).limit(numResults).map {
            MultimediaProcessingDtoOut(
                it.toDto(),
                it[QueryTable.collection],
                it[SuperQueryTable.indexInCineast],
                it[ModelEntityTable.platformTimestamp]
            )
        }

        return media.sortedBy { it.timestamp }.map { it }
    }

    fun updateMultimediaStatus(id: Long, status: ResourceStatus, statusTimestamp: Instant) {
        MultimediaTable.update({ MultimediaTable.keyColumn eq id }) {
            it[MultimediaTable.status] = status
            it[MultimediaTable.statusTimestamp] = statusTimestamp
        }
    }

    fun updateMultimediaResource(
        id: Long,
        status: ResourceStatus,
        statusTimestamp: Instant,
        contentType: String?,
        hash: String?
    ) = MultimediaTable.update({ MultimediaTable.keyColumn eq id }) {
        it[MultimediaTable.status] = status
        it[MultimediaTable.statusTimestamp] = statusTimestamp
        it[MultimediaTable.contentType] = contentType
        it[MultimediaTable.hash] = hash
    }

    fun findByUrl(url: String): MultimediaDto? = MultimediaTable.select {
        (MultimediaTable.url eq url) and (MultimediaTable.hash neq null)
    }.limit(1).singleOrNull()?.toDto()

}
