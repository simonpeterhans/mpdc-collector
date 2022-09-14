package ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.twitter

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

data class TwitterTweetSuperQueryDto(
    override val queryDto: QueryDto,
    override val superQueryDto: SuperQueryDto,
    val superQueryMultimediaDtos: List<SuperQueryMultimediaDto>,
    val tweetQueryDto: TwitterTweetQueryDto,
    val keywordDtos: List<TwitterTweetQueryKeywordDto>,
    val accountDtos: List<TwitterTweetQueryAccountDto>,
    override val subQueryDtos: List<TwitterTweetSubQueryDto>
) : PlatformSuperQueryDto()

data class TwitterTweetSubQueryDto(
    override val queryDto: QueryDto,
    override val subQueryDto: SubQueryDto,
    val tweetQueryDto: TwitterTweetQueryDto,
    val keywordDtos: List<TwitterTweetQueryKeywordDto>,
    val accountDtos: List<TwitterTweetQueryAccountDto>
) : PlatformSubQueryDto()

data class TwitterTweetQueryDto(
    override val ref: Long,
    val queryDelayMinutes: Int,
    val referencedTweetsDepth: Int,
    val useStreamingApiIfPossible: Boolean,
    override val key: Long? = null
) : LongDtoWithRef()

object TwitterTweetQueryTable : LongDaoRefTable("twitter_tweet_query") {

    override val refColumn = long("query_id").index().references(
        QueryTable.keyColumn,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val queryDelayMinutes = integer("query_delay_minutes")
    val referencedTweetsDepth = integer("referenced_tweets_depth")
    val useStreamingApiIfPossible = bool("use_streaming_api_if_possible")

}

object TwitterTweetQueryDao :
    PlatformQueryDao<TwitterTweetQueryDto, TwitterTweetSubQueryDto, TwitterTweetSuperQueryDto>() {

    override val table = TwitterTweetQueryTable

    override fun rowToDto(row: ResultRow) = TwitterTweetQueryDto(
        row[table.refColumn],
        row[table.queryDelayMinutes],
        row[table.referencedTweetsDepth],
        row[table.useStreamingApiIfPossible],
        row[table.keyColumn]
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: TwitterTweetQueryDto) {
        it[table.refColumn] = dto.ref
        it[table.queryDelayMinutes] = dto.queryDelayMinutes
        it[table.referencedTweetsDepth] = dto.referencedTweetsDepth
        it[table.useStreamingApiIfPossible] = dto.useStreamingApiIfPossible
    }

    override fun rowToSubQueryDto(row: ResultRow): TwitterTweetSubQueryDto {
        val twitterQueryId = row[table.keyColumn]
        val keywordDtos = TwitterTweetQueryKeywordDao.getByRef(twitterQueryId)
        val accountDtos = TwitterTweetQueryAccountDao.getByRef(twitterQueryId)

        return TwitterTweetSubQueryDto(
            QueryDao.rowToDto(row),
            SubQueryDao.rowToDto(row),
            rowToDto(row),
            keywordDtos,
            accountDtos
        )
    }

    override fun rowToSuperQueryDto(
        subQueries: List<TwitterTweetSubQueryDto>,
        row: ResultRow
    ): TwitterTweetSuperQueryDto {
        val twitterQueryId = row[table.keyColumn]
        val keywordDtos = TwitterTweetQueryKeywordDao.getByRef(twitterQueryId)
        val accountDtos = TwitterTweetQueryAccountDao.getByRef(twitterQueryId)
        val multimediaDtos = SuperQueryMultimediaDao.getByRef(twitterQueryId)

        return TwitterTweetSuperQueryDto(
            QueryDao.rowToDto(row),
            SuperQueryDao.rowToDto(row),
            multimediaDtos,
            rowToDto(row),
            keywordDtos,
            accountDtos,
            subQueries.filter { sub ->
                sub.subQueryDto.superId == row[SuperQueryTable.keyColumn]
            }
        )
    }

}

data class TwitterTweetQueryKeywordDto(
    override val ref: Long,
    val keyword: String,
    override val key: Long? = null,
) : LongDtoWithRef()

object TwitterTweetQueryKeywordTable : LongDaoRefTable("twitter_tweet_query_keyword") {

    override val refColumn: Column<Long> = long("tweet_query_id").index().references(
        TwitterTweetQueryTable.keyColumn,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val keyword = text("keyword")

}

object TwitterTweetQueryKeywordDao :
    LongTableDaoWithRef<TwitterTweetQueryKeywordDto>() {

    override val table = TwitterTweetQueryKeywordTable

    override fun rowToDto(row: ResultRow) = TwitterTweetQueryKeywordDto(
        row[table.refColumn],
        row[table.keyword],
        row[table.keyColumn],
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: TwitterTweetQueryKeywordDto) {
        it[table.refColumn] = dto.ref
        it[table.keyword] = dto.keyword
    }

}

data class TwitterTweetQueryAccountDto(
    override val ref: Long,
    val account: String,
    override val key: Long? = null,
) : LongDtoWithRef()

object TwitterTweetQueryAccountTable : LongDaoRefTable("twitter_tweet_query_account") {

    override val refColumn: Column<Long> = long("tweet_query_id").index().references(
        TwitterTweetQueryTable.keyColumn,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val account = text("account")

}

object TwitterTweetQueryAccountDao : LongTableDaoWithRef<TwitterTweetQueryAccountDto>() {

    override val table = TwitterTweetQueryAccountTable

    override fun rowToDto(row: ResultRow) = TwitterTweetQueryAccountDto(
        row[table.refColumn],
        row[table.account],
        row[table.keyColumn],
    )

    override fun dtoToStatement(it: UpdateBuilder<Int>, dto: TwitterTweetQueryAccountDto) {
        it[table.refColumn] = dto.ref
        it[table.account] = dto.account
    }

}
