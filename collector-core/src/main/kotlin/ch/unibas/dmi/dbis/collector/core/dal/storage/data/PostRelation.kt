package ch.unibas.dmi.dbis.collector.core.dal.storage.data

import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDtoWithRef
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongTableDaoWithRef
import ch.unibas.dmi.dbis.collector.core.model.data.RelationType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

data class PostRelationDto(
    override val ref: Long,
    val idFrom: String,
    val idTo: String,
    val relationType: RelationType,
    override val key: Long? = null
) : LongDtoWithRef()

object PostRelationTable : PostExtensionTable("post_relation") {

    val idFrom = text("from_post_platform_id")
    val idTo = text("to_post_platform_id")
    val relationType = enumerationByName<RelationType>("relation_type", MAX_ENUM_LENGTH)

}

object PostRelationDao : LongTableDaoWithRef<PostRelationDto>() {

    override val table = PostRelationTable

    override fun rowToDto(row: ResultRow) = PostRelationDto(
        ref = row[table.refColumn],
        idFrom = row[PostRelationTable.idFrom],
        idTo = row[PostRelationTable.idTo],
        relationType = row[PostRelationTable.relationType],
        key = row[table.keyColumn],
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: PostRelationDto) {
        it[table.refColumn] = dto.ref
        it[PostRelationTable.idFrom] = dto.idFrom
        it[PostRelationTable.idTo] = dto.idTo
        it[PostRelationTable.relationType] = dto.relationType
    }

}
