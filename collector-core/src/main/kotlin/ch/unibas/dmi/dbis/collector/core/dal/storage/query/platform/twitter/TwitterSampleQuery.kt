package ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.twitter

import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDaoRefTable
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDtoWithRef
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.*
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.PlatformQueryDao
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.PlatformSubQueryDto
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.PlatformSuperQueryDto
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

data class TwitterSampleSuperQueryDto(
    override val queryDto: QueryDto,
    override val superQueryDto: SuperQueryDto,
    val superQueryMultimediaDtos: List<SuperQueryMultimediaDto>,
    val sampleQueryDto: TwitterSampleQueryDto,
    override val subQueryDtos: List<TwitterSampleSubQueryDto>
) : PlatformSuperQueryDto()

data class TwitterSampleSubQueryDto(
    override val queryDto: QueryDto,
    override val subQueryDto: SubQueryDto,
    val sampleQueryDto: TwitterSampleQueryDto
) : PlatformSubQueryDto()

data class TwitterSampleQueryDto(
    override val ref: Long,
    val referencedTweetsDepth: Int,
    override val key: Long? = null
) : LongDtoWithRef()

object TwitterSampleQueryTable : LongDaoRefTable("twitter_sample_query") {

    override val refColumn = long("query_id").index().references(
        QueryTable.keyColumn,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val referencedTweetsDepth = integer("referenced_tweets_depth")

}

object TwitterSampleQueryDao :
    PlatformQueryDao<TwitterSampleQueryDto, TwitterSampleSubQueryDto, TwitterSampleSuperQueryDto>() {

    override val table = TwitterSampleQueryTable

    override fun rowToDto(row: ResultRow) = TwitterSampleQueryDto(
        row[table.refColumn],
        row[table.referencedTweetsDepth],
        row[table.keyColumn]
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: TwitterSampleQueryDto) {
        it[table.refColumn] = dto.ref
        it[table.referencedTweetsDepth] = dto.referencedTweetsDepth
    }

    override fun rowToSubQueryDto(row: ResultRow): TwitterSampleSubQueryDto =
        TwitterSampleSubQueryDto(QueryDao.rowToDto(row), SubQueryDao.rowToDto(row), rowToDto(row))

    override fun rowToSuperQueryDto(
        subQueries: List<TwitterSampleSubQueryDto>,
        row: ResultRow
    ): TwitterSampleSuperQueryDto {
        val twitterQueryId = row[table.keyColumn]
        val multimediaDtos = SuperQueryMultimediaDao.getByRef(twitterQueryId)

        return TwitterSampleSuperQueryDto(
            QueryDao.rowToDto(row),
            SuperQueryDao.rowToDto(row),
            multimediaDtos,
            rowToDto(row),
            subQueries.filter { sub ->
                sub.subQueryDto.superId == row[SuperQueryTable.keyColumn]
            }
        )
    }

}
