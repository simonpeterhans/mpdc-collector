package ch.unibas.dmi.dbis.collector.core.dal.storage.data

import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDtoWithRef
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongTableDaoWithRef
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

data class PostUrlDto(
    override val ref: Long,
    val url: String,
    override val key: Long? = null
) : LongDtoWithRef()

object PostUrlTable : PostExtensionTable("post_url") {

    val url = text("url")

}

object PostUrlDao : LongTableDaoWithRef<PostUrlDto>() {

    override val table = PostUrlTable

    override fun rowToDto(row: ResultRow) = PostUrlDto(
        ref = row[table.refColumn],
        url = row[PostUrlTable.url],
        key = row[table.keyColumn]
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: PostUrlDto) {
        it[table.refColumn] = dto.ref
        it[PostUrlTable.url] = dto.url
    }

}
