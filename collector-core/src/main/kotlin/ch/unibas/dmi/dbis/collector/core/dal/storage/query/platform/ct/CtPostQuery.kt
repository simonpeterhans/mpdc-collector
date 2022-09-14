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

data class CtPostSuperQueryDto(
    override val queryDto: QueryDto,
    override val superQueryDto: SuperQueryDto,
    val superQueryMultimediaDtos: List<SuperQueryMultimediaDto>,
    val postSearchQueryDto: CtPostQueryDto,
    val postSearchQueryKeywordDtos: List<CtPostQueryKeywordDto>,
    override val subQueryDtos: List<CtPostSubQueryDto>
) : PlatformSuperQueryDto()

data class CtPostSubQueryDto(
    override val queryDto: QueryDto,
    override val subQueryDto: SubQueryDto,
    val postSearchQueryDto: CtPostQueryDto,
    val postSearchQueryListDtos: List<CtPostQueryKeywordDto>,
) : PlatformSubQueryDto()

data class CtPostQueryDto(
    override val ref: Long,
    val queryDelayMinutes: Int,
    val includeHistory: Boolean,
    override val key: Long? = null
) : LongDtoWithRef()

object CtPostQueryTable : LongDaoRefTable("ct_post_query") {

    override val refColumn = long("query_id").index().references(
        QueryTable.keyColumn,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val queryDelayMinutes = integer("query_delay_minutes")
    val includeHistory = bool("include_history")

}

object CtPostQueryDao : PlatformQueryDao<CtPostQueryDto, CtPostSubQueryDto, CtPostSuperQueryDto>() {

    override val table = CtPostQueryTable

    override fun rowToDto(row: ResultRow) = CtPostQueryDto(
        row[table.refColumn],
        row[table.queryDelayMinutes],
        row[table.includeHistory],
        row[table.keyColumn]
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: CtPostQueryDto) {
        it[table.refColumn] = dto.ref
        it[table.queryDelayMinutes] = dto.queryDelayMinutes
        it[table.includeHistory] = dto.includeHistory
    }

    override fun rowToSubQueryDto(row: ResultRow): CtPostSubQueryDto {
        val keywordDtos = CtPostQueryKeywordDao.getByRef(row[table.keyColumn])

        return CtPostSubQueryDto(
            QueryDao.rowToDto(row),
            SubQueryDao.rowToDto(row),
            rowToDto(row),
            keywordDtos,
        )
    }

    override fun rowToSuperQueryDto(
        subQueries: List<CtPostSubQueryDto>,
        row: ResultRow
    ): CtPostSuperQueryDto {
        val keywordDtos = CtPostQueryKeywordDao.getByRef(row[table.keyColumn])
        val multimediaDtos = SuperQueryMultimediaDao.getByRef(row[SuperQueryTable.keyColumn])

        return CtPostSuperQueryDto(
            QueryDao.rowToDto(row),
            SuperQueryDao.rowToDto(row),
            multimediaDtos,
            rowToDto(row),
            keywordDtos,
            subQueries.filter { sub ->
                sub.subQueryDto.superId == row[SuperQueryTable.keyColumn]
            }
        )
    }

}

data class CtPostQueryKeywordDto(
    override val ref: Long,
    val keyword: String,
    override val key: Long? = null,
) : LongDtoWithRef()

object CtPostQueryKeywordTable : LongDaoRefTable("ct_post_query_keyword") {

    override val refColumn: Column<Long> = long("post_query_id").index().references(
        CtPostQueryTable.keyColumn,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val keyword = text("keyword")

}

object CtPostQueryKeywordDao : LongTableDaoWithRef<CtPostQueryKeywordDto>() {

    override val table = CtPostQueryKeywordTable

    override fun rowToDto(row: ResultRow) = CtPostQueryKeywordDto(
        row[table.refColumn],
        row[table.keyword],
        row[table.keyColumn],
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: CtPostQueryKeywordDto) {
        it[table.refColumn] = dto.ref
        it[table.keyword] = dto.keyword
    }

}
