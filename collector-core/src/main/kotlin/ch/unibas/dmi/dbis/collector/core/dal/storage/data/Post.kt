package ch.unibas.dmi.dbis.collector.core.dal.storage.data

import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDaoRefTable
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDtoWithRef
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongTableDao
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.SubQueryTable
import ch.unibas.dmi.dbis.collector.core.model.platform.Platform
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder

data class PostDto(
    override val ref: Long,
    val platformId: String,
    override val key: Long? = null
) : LongDtoWithRef()

object PostTable : LongDaoRefTable("post") {

    override val refColumn = long("entity_id").index().references(
        ModelEntityTable.keyColumn,
        onDelete = ReferenceOption.NO_ACTION,
        onUpdate = ReferenceOption.CASCADE
    )

    val platformId = text("platform_id").index()

}

object PostDao : LongTableDao<PostDto>() {

    override val table = PostTable

    override fun rowToDto(row: ResultRow) = PostDto(
        ref = row[table.refColumn],
        platformId = row[table.platformId],
        key = row[table.keyColumn]
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: PostDto) {
        it[table.refColumn] = dto.ref
        it[table.platformId] = dto.platformId
    }

    fun postCountBySubQuery(subQueryId: Long): Long =
        PostTable.innerJoin(
            ModelEntityTable,
            { refColumn },
            { ModelEntityTable.keyColumn },
            { ModelEntityTable.refColumn eq subQueryId }
        ).selectAll().count()

    fun existsByPlatformIdAndSuperQuery(
        platform: Platform,
        platformId: String,
        superId: Long
    ): Boolean {
        val res = PostTable.innerJoin(
            ModelEntityTable,
            { refColumn },
            { ModelEntityTable.keyColumn },
            {
                (ModelEntityTable.platform eq platform) and
                    (PostTable.platformId eq platformId)
            },
        ).innerJoin(
            SubQueryTable,
            { ModelEntityTable.refColumn },
            { SubQueryTable.keyColumn },
            { (SubQueryTable.superId eq superId) }
        ).slice(PostTable.platformId).selectAll()

        return !res.empty()
    }

    fun existsByPlatformIdAndCollection(
        platform: Platform,
        platformId: String,
        collectionName: String
    ): Boolean {
        val res = PostTable.innerJoin(
            ModelEntityTable,
            { refColumn },
            { ModelEntityTable.keyColumn },
            {
                (ModelEntityTable.platform eq platform) and
                    (PostTable.platformId eq platformId) and
                    (ModelEntityTable.collection eq collectionName)
            },
        ).slice(PostTable.platformId).selectAll()

        return !res.empty()
    }

    fun existByPlatformIdsAndCollection(
        platform: Platform,
        collectionName: String,
        platformIds: List<String>
    ): List<String> = PostTable.innerJoin(
        ModelEntityTable,
        { refColumn },
        { ModelEntityTable.keyColumn },
        {
            (ModelEntityTable.platform eq platform) and
                (PostTable.platformId inList platformIds) and
                (ModelEntityTable.collection eq collectionName)
        },
    ).slice(PostTable.platformId).selectAll().map {
        it[table.platformId] // Return the ones that already exist.
    }

}
