@file:Suppress("RemoveRedundantQualifierName")

package ch.unibas.dmi.dbis.collector.core.dal.storage.query

import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDaoRefTable
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDtoWithRef
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongTableDao
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongTableDaoWithRef
import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder

data class SuperQueryDto(
    override val ref: Long,
    val indexInCineast: Boolean,
    override val key: Long? = null
) : LongDtoWithRef()

object SuperQueryTable : LongDaoRefTable("super_query") {

    override val refColumn: Column<Long> = long("query_id").index().references(
        QueryTable.keyColumn,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val indexInCineast = bool("index_in_cineast")

}

object SuperQueryDao : LongTableDao<SuperQueryDto>() {

    override val table = SuperQueryTable

    override fun rowToDto(row: ResultRow) = SuperQueryDto(
        ref = row[table.refColumn],
        indexInCineast = row[table.indexInCineast],
        key = row[table.keyColumn]
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: SuperQueryDto) {
        it[table.refColumn] = dto.ref
        it[table.indexInCineast] = dto.indexInCineast
    }

    fun getQueryDtoBySuperQueryId(superQueryId: Long): QueryDto? = table.innerJoin(
        QueryTable,
        { table.refColumn },
        { QueryTable.keyColumn },
        { SuperQueryTable.keyColumn eq superQueryId }
    ).selectAll().map {
        QueryDao.rowToDto(it)
    }.firstOrNull()

}

data class SuperQueryMultimediaDto(
    override val ref: Long,
    val multimediaType: MediaType,
    override val key: Long? = null
) : LongDtoWithRef()

object SuperQueryMultimediaTable : LongDaoRefTable("super_query_multimedia") {

    override val refColumn = long("super_query_id").index().references(
        SuperQueryTable.keyColumn,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val multimediaType = enumerationByName<MediaType>("multimedia_type", MAX_ENUM_LENGTH)

}

object SuperQueryMultimediaDao : LongTableDaoWithRef<SuperQueryMultimediaDto>() {

    override val table = SuperQueryMultimediaTable

    override fun rowToDto(row: ResultRow): SuperQueryMultimediaDto = SuperQueryMultimediaDto(
        ref = row[table.refColumn],
        multimediaType = row[table.multimediaType],
        key = row[table.keyColumn]
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: SuperQueryMultimediaDto) {
        it[table.refColumn] = dto.ref
        it[table.multimediaType] = dto.multimediaType
    }

}

