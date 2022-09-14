package ch.unibas.dmi.dbis.collector.core.dal.storage.query

import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDaoRefTable
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDtoWithRef
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongTableDaoWithRef
import ch.unibas.dmi.dbis.collector.core.model.query.TemporalType
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder

data class SubQueryDto(
    override val ref: Long,
    val superId: Long,
    val temporalType: TemporalType,
    val apiKey: String,
    override val key: Long? = null
) : LongDtoWithRef()

object SubQueryTable : LongDaoRefTable("sub_query") {

    override val refColumn = long("query_id").index().references(
        QueryTable.keyColumn,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val superId = long("super_id").index().references(
        SuperQueryTable.keyColumn,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val temporalType = enumerationByName<TemporalType>("temporal_type", MAX_ENUM_LENGTH)
    val apiKey = text("api_key")

}

object SubQueryDao : LongTableDaoWithRef<SubQueryDto>() {

    override val table = SubQueryTable

    override fun rowToDto(row: ResultRow): SubQueryDto = SubQueryDto(
        ref = row[table.refColumn],
        superId = row[table.superId],
        temporalType = row[table.temporalType],
        apiKey = row[table.apiKey],
        key = row[table.keyColumn]
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: SubQueryDto) {
        it[table.refColumn] = dto.ref
        it[table.superId] = dto.superId
        it[table.temporalType] = dto.temporalType
        it[table.apiKey] = dto.apiKey
    }

    fun getQueryDtoBySubQueryId(subQueryId: Long): QueryDto? = table.innerJoin(
        QueryTable,
        { table.refColumn },
        { QueryTable.keyColumn },
        { SubQueryTable.keyColumn eq subQueryId }
    ).selectAll().map {
        QueryDao.rowToDto(it)
    }.firstOrNull()

}
