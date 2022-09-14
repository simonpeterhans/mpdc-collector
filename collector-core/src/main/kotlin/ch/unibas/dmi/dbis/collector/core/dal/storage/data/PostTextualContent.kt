package ch.unibas.dmi.dbis.collector.core.dal.storage.data

import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDtoWithRef
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongTableDaoWithRef
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

data class PostTextualContentDto(
    override val ref: Long,
    val title: String?,
    val text: String?,
    val language: String?,
    override val key: Long? = null
) : LongDtoWithRef()

object PostTextualContentTable : PostExtensionTable("post_textual_content") {

    val title = text("title").nullable()
    val text = text("text").nullable()
    val language = text("language").nullable()

}

object PostTextualContentDao : LongTableDaoWithRef<PostTextualContentDto>() {

    override val table = PostTextualContentTable

    override fun rowToDto(row: ResultRow) = PostTextualContentDto(
        ref = row[table.refColumn],
        title = row[PostTextualContentTable.title],
        text = row[PostTextualContentTable.text],
        language = row[PostTextualContentTable.language],
        key = row[table.keyColumn]
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: PostTextualContentDto) {
        it[table.refColumn] = dto.ref
        it[PostTextualContentTable.title] = dto.title
        it[PostTextualContentTable.text] = dto.text
        it[PostTextualContentTable.language] = dto.language
    }

}

