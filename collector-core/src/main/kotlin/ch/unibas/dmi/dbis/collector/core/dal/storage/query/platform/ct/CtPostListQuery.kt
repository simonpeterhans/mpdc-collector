package ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.ct

import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDaoRefTable
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDtoWithRef
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongTableDaoWithRef
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.*
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.PlatformQueryDao
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.PlatformSubQueryDto
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.PlatformSuperQueryDto
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

data class CtListSuperQueryDto(
    override val queryDto: QueryDto,
    override val superQueryDto: SuperQueryDto,
    val superQueryMultimediaDtos: List<SuperQueryMultimediaDto>,
    val postListQueryDto: CtListQueryDto,
    val postListQueryListDtos: List<CtListQueryListDto>,
    override val subQueryDtos: List<CtListSubQueryDto>
) : PlatformSuperQueryDto()

data class CtListSubQueryDto(
    override val queryDto: QueryDto,
    override val subQueryDto: SubQueryDto,
    val postListQueryDto: CtListQueryDto,
    val postListQueryListDtos: List<CtListQueryListDto>,
) : PlatformSubQueryDto()

data class CtListQueryDto(
    override val ref: Long,
    val queryDelayMinutes: Int,
    val includeHistory: Boolean,
    override val key: Long? = null
) : LongDtoWithRef()

object CtListQueryTable : LongDaoRefTable("ct_list_query") {

    override val refColumn = long("query_id").index().references(
        QueryTable.keyColumn,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val queryDelayMinutes = integer("query_delay_minutes")
    val includeHistory = bool("include_history")

}

object CtListQueryDao :
    PlatformQueryDao<CtListQueryDto, CtListSubQueryDto, CtListSuperQueryDto>() {

    override val table = CtListQueryTable

    override fun rowToDto(row: ResultRow) = CtListQueryDto(
        row[table.refColumn],
        row[table.queryDelayMinutes],
        row[table.includeHistory],
        row[table.keyColumn]
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: CtListQueryDto) {
        it[table.refColumn] = dto.ref
        it[table.queryDelayMinutes] = dto.queryDelayMinutes
        it[table.includeHistory] = dto.includeHistory
    }

    override fun rowToSubQueryDto(row: ResultRow): CtListSubQueryDto {
        val listDtos = CtListQueryListDao.getByRef(row[table.keyColumn])

        return CtListSubQueryDto(
            QueryDao.rowToDto(row),
            SubQueryDao.rowToDto(row),
            rowToDto(row),
            listDtos,
        )
    }

    override fun rowToSuperQueryDto(
        subQueries: List<CtListSubQueryDto>,
        row: ResultRow
    ): CtListSuperQueryDto {
        val listDtos = CtListQueryListDao.getByRef(row[table.keyColumn])
        val multimediaDtos = SuperQueryMultimediaDao.getByRef(row[SuperQueryTable.keyColumn])

        return CtListSuperQueryDto(
            QueryDao.rowToDto(row),
            SuperQueryDao.rowToDto(row),
            multimediaDtos,
            rowToDto(row),
            listDtos,
            subQueries.filter { sub ->
                sub.subQueryDto.superId == row[SuperQueryTable.keyColumn]
            }
        )
    }

}

data class CtListQueryListDto(
    override val ref: Long,
    val listId: Long,
    override val key: Long? = null,
) : LongDtoWithRef()

object CtListQueryListTable : LongDaoRefTable("ct_query_list") {

    override val refColumn: Column<Long> = long("list_query_id").index().references(
        CtListQueryTable.keyColumn,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val listId = long("list_id")

}

object CtListQueryListDao : LongTableDaoWithRef<CtListQueryListDto>() {

    override val table = CtListQueryListTable

    override fun rowToDto(row: ResultRow) = CtListQueryListDto(
        row[table.refColumn],
        row[table.listId],
        row[table.keyColumn],
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: CtListQueryListDto) {
        it[table.refColumn] = dto.ref
        it[table.listId] = dto.listId
    }

}
