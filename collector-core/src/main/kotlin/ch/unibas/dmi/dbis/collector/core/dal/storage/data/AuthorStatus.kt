package ch.unibas.dmi.dbis.collector.core.dal.storage.data

import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDaoRefTable
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDtoWithRef
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongTableDao
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder

data class AuthorStatusDto(
    override val ref: Long,
    val platformId: String?,
    val name: String,
    override val key: Long? = null
) : LongDtoWithRef()

object AuthorStatusTable : LongDaoRefTable("author_status") {

    override val refColumn = long("entity_id").index().references(
        ModelEntityTable.keyColumn,
        onDelete = ReferenceOption.NO_ACTION,
        onUpdate = ReferenceOption.CASCADE
    )

    val platformId = text("platform_id").nullable()
    val name = text("name")

}

object AuthorStatusDao : LongTableDao<AuthorStatusDto>() {

    override val table = AuthorStatusTable

    override fun rowToDto(row: ResultRow) = AuthorStatusDto(
        ref = row[table.refColumn],
        platformId = row[table.platformId],
        name = row[table.name],
        key = row[table.keyColumn]
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: AuthorStatusDto) {
        it[table.refColumn] = dto.ref
        it[table.platformId] = dto.platformId
        it[table.name] = dto.name
    }

    fun authorCountBySubQuery(subQueryId: Long): Long =
        AuthorStatusTable.innerJoin(
            ModelEntityTable,
            { refColumn },
            { ModelEntityTable.keyColumn },
            { ModelEntityTable.refColumn eq subQueryId }
        ).selectAll().count()

}
