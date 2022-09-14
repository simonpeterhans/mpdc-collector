package ch.unibas.dmi.dbis.collector.core.dal.storage.data

import ch.unibas.dmi.dbis.collector.core.dal.columns.pgJsonb
import ch.unibas.dmi.dbis.collector.core.dal.columns.pgTimestampWithTimeZone
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDaoRefTable
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDtoWithRef
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongTableDao
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.SubQueryTable
import ch.unibas.dmi.dbis.collector.core.model.platform.Platform
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.time.Instant

data class ModelEntityDto(
    override val ref: Long,
    val raw: JsonNode,
    val platform: Platform,
    val platformTimestamp: Instant,
    val collection: String,
    val timestamp: Instant,
    val apiString: String,
    override val key: Long? = null
) : LongDtoWithRef()

object ModelEntityTable : LongDaoRefTable("model_entity") {

    private val rawObjectMapper: ObjectMapper = jacksonObjectMapper().findAndRegisterModules()

    override val refColumn = long("sub_id").index().references(
        SubQueryTable.keyColumn,
        onDelete = ReferenceOption.NO_ACTION,
        onUpdate = ReferenceOption.CASCADE
    )

    val raw = pgJsonb("raw", JsonNode::class.java, rawObjectMapper)
    val platform = enumerationByName<Platform>("platform", MAX_ENUM_LENGTH)
    val platformTimestamp = pgTimestampWithTimeZone("platform_ts")
    val collection = text("collection")
    val timestamp = pgTimestampWithTimeZone("system_ts")
    val apiString = text("api")

}

object ModelEntityDao : LongTableDao<ModelEntityDto>() {

    override val table = ModelEntityTable

    override fun rowToDto(row: ResultRow) = ModelEntityDto(
        ref = row[table.refColumn],
        raw = row[table.raw],
        platform = row[table.platform],
        platformTimestamp = row[table.platformTimestamp],
        collection = row[table.collection],
        timestamp = row[table.timestamp],
        apiString = row[table.apiString],
        key = row[table.keyColumn]
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: ModelEntityDto) {
        it[table.refColumn] = dto.ref
        it[table.raw] = dto.raw
        it[table.platform] = dto.platform
        it[table.platformTimestamp] = dto.platformTimestamp
        it[table.collection] = dto.collection
        it[table.timestamp] = dto.timestamp
        it[table.apiString] = dto.apiString
    }

}
