package ch.unibas.dmi.dbis.collector.core.dal.storage.query

import ch.unibas.dmi.dbis.collector.core.dal.columns.pgTimestampWithTimeZone
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDaoTable
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDto
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongTableDao
import ch.unibas.dmi.dbis.collector.core.model.platform.ApiPlatform
import ch.unibas.dmi.dbis.collector.core.model.query.PlatformQueryType
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.CtQueryType
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterQueryType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.time.Instant

data class QueryDto(
    val platform: ApiPlatform,
    val platformQueryType: PlatformQueryType,
    val label: String,
    val collectionName: String,
    val from: Instant,
    val to: Instant,
    override val key: Long? = null
) : LongDto()

object QueryTable : LongDaoTable("query") {

    val platform = enumerationByName<ApiPlatform>("platform", MAX_ENUM_LENGTH)
    val platformQueryType = text("query_type")
    val label = text("label")
    val collection = text("collection")
    val from = pgTimestampWithTimeZone("from_ts")
    val to = pgTimestampWithTimeZone("to_ts")

}

object QueryDao : LongTableDao<QueryDto>() {

    override val table = QueryTable

    override fun rowToDto(row: ResultRow): QueryDto {
        val platform = row[table.platform]
        val queryTypeString = row[table.platformQueryType]

        val queryType = when (platform) {
            ApiPlatform.TWITTER -> TwitterQueryType.valueOf(queryTypeString)
            ApiPlatform.REDDIT -> TODO()
            ApiPlatform.CROWDTANGLE -> CtQueryType.valueOf(queryTypeString)
        }

        return QueryDto(
            platform = platform,
            platformQueryType = queryType,
            label = row[table.label],
            collectionName = row[table.collection],
            from = row[table.from],
            to = row[table.to],
            key = row[table.keyColumn]
        )
    }

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: QueryDto) {
        it[table.platform] = dto.platform
        it[table.platformQueryType] = dto.platformQueryType.toString()
        it[table.label] = dto.label
        it[table.collection] = dto.collectionName
        it[table.from] = dto.from
        it[table.to] = dto.to
    }

    fun queryLabelExists(label: String): Boolean {
        val isEmpty = table
            .slice(table.label)
            .select {
                table.label eq label
            }.empty()

        return !isEmpty
    }

    fun getAllCollectionLabels(): List<String> = table
        .slice(table.collection)
        .selectAll()
        .map {
            it[table.collection]
        }
        .distinct()

}
