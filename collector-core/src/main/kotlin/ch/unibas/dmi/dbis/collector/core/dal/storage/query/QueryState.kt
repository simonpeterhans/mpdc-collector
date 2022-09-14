package ch.unibas.dmi.dbis.collector.core.dal.storage.query

import ch.unibas.dmi.dbis.collector.core.dal.columns.pgTimestampWithTimeZone
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDaoRefTable
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDtoWithRef
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongTableDaoWithRef
import ch.unibas.dmi.dbis.collector.core.model.flow.StreamStatus
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.time.Instant

data class QueryStateDto(
    override val ref: Long?,
    val streamStatus: StreamStatus,
    val statusText: String,
    val lastCheckpointTimestamp: Instant?,
    val lastProcessingTimestamp: Instant,
    override val key: Long? = null
) : LongDtoWithRef()

object QueryStateTable : LongDaoRefTable("query_state") {

    override val refColumn: Column<Long> = long("sub_query_id").uniqueIndex().references(
        SubQueryTable.keyColumn,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val streamStatus = enumerationByName<StreamStatus>("stream_status", MAX_ENUM_LENGTH)
    val statusText = text("status_text")
    val lastCheckpointTimestamp = pgTimestampWithTimeZone("last_checkpoint_ts").nullable()
    val lastProcessingTimestamp = pgTimestampWithTimeZone("last_processing_ts")

}

object QueryStateDao : LongTableDaoWithRef<QueryStateDto>() {

    override val table = QueryStateTable

    override fun rowToDto(row: ResultRow) = QueryStateDto(
        row[table.refColumn],
        row[table.streamStatus],
        row[table.statusText],
        row[table.lastCheckpointTimestamp],
        row[table.lastProcessingTimestamp],
        row[table.keyColumn],
    )

    // TODO Come up with a better solution for partial updates.
    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: QueryStateDto) {
        if (dto.ref != null) {
            // Only update the reference if it's not null, otherwise leave as is.
            it[table.refColumn] = dto.ref
        }
        it[table.streamStatus] = dto.streamStatus
        it[table.statusText] = dto.statusText
        it[table.lastCheckpointTimestamp] = dto.lastCheckpointTimestamp
        it[table.lastProcessingTimestamp] = dto.lastProcessingTimestamp
    }

}
