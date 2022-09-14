package ch.unibas.dmi.dbis.collector.core.dal.repositories.exposed

import ch.unibas.dmi.dbis.collector.core.dal.repositories.IQueryRepository
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.*
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.ct.*
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.twitter.*
import ch.unibas.dmi.dbis.collector.core.model.flow.QueryState
import ch.unibas.dmi.dbis.collector.core.model.flow.StreamStatus
import ch.unibas.dmi.dbis.collector.core.model.platform.ApiPlatform
import ch.unibas.dmi.dbis.collector.core.model.query.Query
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.model.query.SubQuery
import ch.unibas.dmi.dbis.collector.core.model.query.SuperQuery
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.CtQueryType
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.list.CtListQueryOptions
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.list.CtListQueryOptionsData
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.list.CtListSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.list.CtListSuperQuery
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.post.CtPostQueryOptions
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.post.CtPostQueryOptionsData
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.post.CtPostSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.post.CtPostSuperQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterQueryType
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.parameter.TwitterAccountQueryParameter
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.parameter.TwitterKeywordQueryParameter
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.sample.TwitterSampleQueryOptions
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.sample.TwitterSampleQueryOptionsData
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.sample.TwitterSampleSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.sample.TwitterSampleSuperQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet.TwitterTweetQueryOptions
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet.TwitterTweetQueryOptionsData
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet.TwitterTweetSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet.TwitterTweetSuperQuery
import ch.unibas.dmi.dbis.collector.core.query.NewQueryHasIdException
import ch.unibas.dmi.dbis.collector.core.query.SuperQueryLabelExistsException
import org.jetbrains.exposed.sql.transactions.transaction
import org.threeten.extra.Interval
import java.time.Instant

class QueryRepositoryExposed : IQueryRepository {

    private fun insertGenericQueryDetails(
        q: Query
    ): Long = QueryDao.insertDto(
        QueryDto(
            q.queryPlatform,
            q.platformQueryType,
            q.label,
            q.collectionName,
            q.interval.start,
            q.interval.end
        )
    )

    private fun insertCtListQueryDetails(
        q: CtListQueryOptions,
        queryId: Long
    ): Long {
        val detailsId = CtListQueryDao.insertDto(
            CtListQueryDto(queryId, q.queryDelayMinutes, q.includeHistory)
        )

        q.listIds.forEach {
            CtListQueryListDao.insertDto(CtListQueryListDto(detailsId, it))
        }

        return detailsId
    }

    private fun insertCtPostQueryDetails(
        q: CtPostQueryOptions,
        queryId: Long
    ): Long {
        val detailsId = CtPostQueryDao.insertDto(
            CtPostQueryDto(queryId, q.queryDelayMinutes, q.includeHistory)
        )

        q.keywords.forEach {
            CtPostQueryKeywordDao.insertDto(CtPostQueryKeywordDto(detailsId, it))
        }

        return detailsId
    }

    private fun insertTwitterTweetQueryDetails(
        q: TwitterTweetQueryOptions,
        queryId: Long
    ) {
        // Non-list details.
        val tweetQueryId = TwitterTweetQueryDao.insertDto(
            TwitterTweetQueryDto(
                queryId,
                q.queryDelayMinutes,
                q.referencedTweetsDepth,
                q.useStreamingApiIfPossible
            )
        )

        // Keywords lists.
        q.keywords.forEach {
            TwitterTweetQueryKeywordDao.insertDto(
                TwitterTweetQueryKeywordDto(tweetQueryId, it.keyword)
            )
        }

        q.accounts.forEach {
            TwitterTweetQueryAccountDao.insertDto(
                TwitterTweetQueryAccountDto(tweetQueryId, it.account)
            )
        }
    }

    private fun insertTwitterSampleQueryDetails(
        q: TwitterSampleQueryOptions,
        queryId: Long
    ) {
        TwitterSampleQueryDao.insertDto(TwitterSampleQueryDto(queryId, q.referencedTweetsDepth))
    }

    override fun insertSubQuery(subQuery: SubQuery) {
        if (subQuery.id != null) {
            throw NewQueryHasIdException()
        }

        // TODO Check for subQuery.superId.

        val genericSubQueryId = insertGenericQueryDetails(subQuery)

        // Insert new sub query (ID used by sub queries).
        val subQueryId = SubQueryDao.insertDto(
            SubQueryDto(
                genericSubQueryId,
                subQuery.superId!!,
                subQuery.temporalType,
                subQuery.apiKey
            )
        )

        // Insert new default query state.
        QueryStateDao.insertDto(
            QueryStateDto(
                subQueryId,
                StreamStatus.NEW,
                "",
                null,
                Instant.now()
            )
        )

        // Insert sub query-specific details (reference not used).
        when (subQuery.queryPlatform) {
            ApiPlatform.TWITTER -> when (subQuery.platformQueryType as TwitterQueryType) {
                TwitterQueryType.TWITTER_TWEET_QUERY -> insertTwitterTweetQueryDetails(
                    subQuery as TwitterTweetSubQuery,
                    genericSubQueryId
                )

                TwitterQueryType.TWITTER_SAMPLE_TWEET_QUERY -> insertTwitterSampleQueryDetails(
                    subQuery as TwitterSampleSubQuery,
                    genericSubQueryId
                )
            }

            ApiPlatform.REDDIT -> {
                TODO()
            }

            ApiPlatform.CROWDTANGLE -> when (subQuery.platformQueryType as CtQueryType) {
                CtQueryType.CT_POST_LIST_QUERY -> insertCtListQueryDetails(
                    subQuery as CtListSubQuery,
                    genericSubQueryId
                )

                CtQueryType.CT_POST_SEARCH_QUERY -> insertCtPostQueryDetails(
                    subQuery as CtPostSubQuery,
                    genericSubQueryId
                )
            }
        }

        subQuery.id = subQueryId
    }

    private fun buildQueryData(
        queryDto: QueryDto,
    ) = QueryData(
        queryDto.label,
        queryDto.collectionName,
        Interval.of(queryDto.from, queryDto.to)
    )

    private fun buildCtListSubQueryData(
        postListQueryDto: CtListQueryDto,
        postListQueryListDtos: List<CtListQueryListDto>
    ) = CtListQueryOptionsData(
        postListQueryListDtos.map { it.listId },
        postListQueryDto.queryDelayMinutes,
        postListQueryDto.includeHistory
    )

    private fun buildCtListSubQuery(
        subDto: CtListSubQueryDto
    ) = CtListSubQuery(
        subDto.subQueryDto.superId,
        subDto.subQueryDto.apiKey,
        subDto.subQueryDto.temporalType,
        buildQueryData(subDto.queryDto),
        buildCtListSubQueryData(
            subDto.postListQueryDto,
            subDto.postListQueryListDtos
        ),
        subDto.subQueryDto.key
    )

    private fun buildCtListSuperQuery(
        subs: List<CtListSubQuery>,
        superDto: CtListSuperQueryDto
    ) = CtListSuperQuery(
        buildQueryData(superDto.queryDto),
        buildCtListSubQueryData(
            superDto.postListQueryDto,
            superDto.postListQueryListDtos
        ),
        superDto.superQueryMultimediaDtos.map { it.multimediaType },
        superDto.superQueryDto.indexInCineast,
        subs.toMutableList(),
        superDto.superQueryDto.key
    )

    private fun buildCtListSuperQuery(
        superDto: CtListSuperQueryDto
    ): CtListSuperQuery {
        val subs = superDto.subQueryDtos.map { subDto: CtListSubQueryDto ->
            buildCtListSubQuery(subDto)
        }
        return buildCtListSuperQuery(subs, superDto)
    }

    private fun buildCtListSuperQueries(
        superDtos: List<CtListSuperQueryDto>
    ): List<CtListSuperQuery> = superDtos.map { superDto ->
        buildCtListSuperQuery(superDto)
    }

    override fun getAllCtListQueries() = transaction {
        val dtos = CtListQueryDao.getAllSuperQueries()
        buildCtListSuperQueries(dtos)
    }

    private fun buildCtPostSubQueryData(
        postSearchQueryDto: CtPostQueryDto,
        postSearchQueryListDtos: List<CtPostQueryKeywordDto>
    ) = CtPostQueryOptionsData(
        postSearchQueryListDtos.map { it.keyword },
        postSearchQueryDto.queryDelayMinutes,
        postSearchQueryDto.includeHistory
    )

    private fun buildCtPostSubQuery(
        subDto: CtPostSubQueryDto
    ) = CtPostSubQuery(
        subDto.subQueryDto.superId,
        subDto.subQueryDto.apiKey,
        subDto.subQueryDto.temporalType,
        buildQueryData(subDto.queryDto),
        buildCtPostSubQueryData(
            subDto.postSearchQueryDto,
            subDto.postSearchQueryListDtos
        ),
        subDto.subQueryDto.key
    )

    private fun buildCtPostSuperQuery(
        subs: List<CtPostSubQuery>,
        superDto: CtPostSuperQueryDto
    ) = CtPostSuperQuery(
        buildQueryData(superDto.queryDto),
        buildCtPostSubQueryData(
            superDto.postSearchQueryDto,
            superDto.postSearchQueryKeywordDtos
        ),
        superDto.superQueryMultimediaDtos.map { it.multimediaType },
        superDto.superQueryDto.indexInCineast,
        subs.toMutableList(),
        superDto.superQueryDto.key
    )

    private fun buildCtPostSuperQuery(
        superDto: CtPostSuperQueryDto
    ): CtPostSuperQuery {
        val subs = superDto.subQueryDtos.map { subDto: CtPostSubQueryDto ->
            buildCtPostSubQuery(subDto)
        }
        return buildCtPostSuperQuery(subs, superDto)
    }

    private fun buildCtPostSuperQueries(
        superDtos: List<CtPostSuperQueryDto>
    ): List<CtPostSuperQuery> = superDtos.map { superDto ->
        buildCtPostSuperQuery(superDto)
    }

    override fun getAllCtPostQueries() = transaction {
        val dtos = CtPostQueryDao.getAllSuperQueries()
        buildCtPostSuperQueries(dtos)
    }

    private fun buildTwitterSampleSubQueryData(
        sampleQueryDto: TwitterSampleQueryDto
    ) = TwitterSampleQueryOptionsData(sampleQueryDto.referencedTweetsDepth)

    private fun buildTwitterSampleSubQuery(
        subDto: TwitterSampleSubQueryDto
    ) = TwitterSampleSubQuery(
        subDto.subQueryDto.superId,
        subDto.subQueryDto.apiKey,
        subDto.subQueryDto.temporalType,
        buildQueryData(subDto.queryDto),
        buildTwitterSampleSubQueryData(subDto.sampleQueryDto),
        subDto.subQueryDto.key
    )

    private fun buildTwitterSampleSuperQuery(
        subs: List<TwitterSampleSubQuery>,
        superDto: TwitterSampleSuperQueryDto
    ) = TwitterSampleSuperQuery(
        buildQueryData(superDto.queryDto),
        buildTwitterSampleSubQueryData(superDto.sampleQueryDto),
        superDto.superQueryMultimediaDtos.map { it.multimediaType },
        superDto.superQueryDto.indexInCineast,
        subs.toMutableList(),
        superDto.superQueryDto.key
    )

    private fun buildTwitterSampleSuperQuery(
        superDto: TwitterSampleSuperQueryDto
    ): TwitterSampleSuperQuery {
        val subs = superDto.subQueryDtos.map { subDto: TwitterSampleSubQueryDto ->
            buildTwitterSampleSubQuery(subDto)
        }
        return buildTwitterSampleSuperQuery(subs, superDto)
    }

    private fun buildTwitterSampleSuperQueries(
        superDtos: List<TwitterSampleSuperQueryDto>
    ): List<TwitterSampleSuperQuery> = superDtos.map { superDto ->
        buildTwitterSampleSuperQuery(superDto)
    }

    override fun getAllTwitterSampleQueries() = transaction {
        val dtos = TwitterSampleQueryDao.getAllSuperQueries()
        buildTwitterSampleSuperQueries(dtos)
    }

    private fun buildTwitterTweetSubQueryData(
        tweetQueryDto: TwitterTweetQueryDto,
        tweetQueryKeywordDtos: List<TwitterTweetQueryKeywordDto>,
        tweetQueryAccountDtos: List<TwitterTweetQueryAccountDto>
    ) = TwitterTweetQueryOptionsData(
        tweetQueryKeywordDtos.map { TwitterKeywordQueryParameter(it.keyword) },
        tweetQueryAccountDtos.map { TwitterAccountQueryParameter(it.account) },
        tweetQueryDto.queryDelayMinutes,
        tweetQueryDto.referencedTweetsDepth,
        tweetQueryDto.useStreamingApiIfPossible
    )

    private fun buildTwitterTweetSubQuery(
        subDto: TwitterTweetSubQueryDto
    ) = TwitterTweetSubQuery(
        subDto.subQueryDto.superId,
        subDto.subQueryDto.apiKey,
        subDto.subQueryDto.temporalType,
        buildQueryData(subDto.queryDto),
        buildTwitterTweetSubQueryData(
            subDto.tweetQueryDto,
            subDto.keywordDtos,
            subDto.accountDtos
        ),
        subDto.subQueryDto.key
    )

    private fun buildTwitterTweetSuperQuery(
        subs: List<TwitterTweetSubQuery>,
        superDto: TwitterTweetSuperQueryDto
    ) = TwitterTweetSuperQuery(
        buildQueryData(superDto.queryDto),
        buildTwitterTweetSubQueryData(
            superDto.tweetQueryDto,
            superDto.keywordDtos,
            superDto.accountDtos
        ),
        superDto.superQueryMultimediaDtos.map { it.multimediaType },
        superDto.superQueryDto.indexInCineast,
        subs.toMutableList(),
        superDto.superQueryDto.key
    )

    private fun buildTwitterTweetSuperQuery(
        superDto: TwitterTweetSuperQueryDto
    ): TwitterTweetSuperQuery {
        val subs = superDto.subQueryDtos.map { subDto: TwitterTweetSubQueryDto ->
            buildTwitterTweetSubQuery(subDto)
        }
        return buildTwitterTweetSuperQuery(subs, superDto)
    }

    private fun buildTwitterTweetSuperQueries(
        superDtos: List<TwitterTweetSuperQueryDto>
    ): List<TwitterTweetSuperQuery> = superDtos.map { superDto ->
        buildTwitterTweetSuperQuery(superDto)
    }

    override fun getAllTwitterTweetQueries() = transaction {
        val dtos = TwitterTweetQueryDao.getAllSuperQueries()
        buildTwitterTweetSuperQueries(dtos)
    }

    private fun readCtListSubQuery(subId: Long): CtListSubQuery? {
        val subQuery = CtListQueryDao.getSubQueryById(subId) ?: return null
        return buildCtListSubQuery(subQuery)
    }

    private fun readCtPostSubQuery(subId: Long): CtPostSubQuery? {
        val subQuery = CtPostQueryDao.getSubQueryById(subId) ?: return null
        return buildCtPostSubQuery(subQuery)
    }

    private fun readTwitterTweetSubQuery(subId: Long): TwitterTweetSubQuery? {
        val subQuery = TwitterTweetQueryDao.getSubQueryById(subId) ?: return null
        return buildTwitterTweetSubQuery(subQuery)
    }

    private fun readTwitterSampleSubQuery(subId: Long): TwitterSampleSubQuery? {
        val subQuery = TwitterSampleQueryDao.getSubQueryById(subId) ?: return null
        return buildTwitterSampleSubQuery(subQuery)
    }

    override fun readSubQuery(
        subId: Long
    ): SubQuery? = transaction {
        val queryDto = SubQueryDao.getQueryDtoBySubQueryId(subId)
            ?: return@transaction null

        when (queryDto.platform) {
            ApiPlatform.TWITTER -> when (queryDto.platformQueryType as TwitterQueryType) {
                TwitterQueryType.TWITTER_TWEET_QUERY -> readTwitterTweetSubQuery(subId)
                TwitterQueryType.TWITTER_SAMPLE_TWEET_QUERY -> readTwitterSampleSubQuery(subId)
            }

            ApiPlatform.REDDIT -> {
                TODO()
            }

            ApiPlatform.CROWDTANGLE -> when (queryDto.platformQueryType as CtQueryType) {
                CtQueryType.CT_POST_LIST_QUERY -> readCtListSubQuery(subId)
                CtQueryType.CT_POST_SEARCH_QUERY -> readCtPostSubQuery(subId)
            }
        }
    }

    private fun readCtListSuperQuery(superId: Long): CtListSuperQuery? {
        val superQuery = CtListQueryDao.getSuperQueryById(superId) ?: return null
        return buildCtListSuperQuery(superQuery)
    }

    private fun readCtPostSuperQuery(superId: Long): CtPostSuperQuery? {
        val superQuery = CtPostQueryDao.getSuperQueryById(superId) ?: return null
        return buildCtPostSuperQuery(superQuery)
    }

    private fun readTwitterTweetSuperQuery(superId: Long): TwitterTweetSuperQuery? {
        val superQuery = TwitterTweetQueryDao.getSuperQueryById(superId) ?: return null
        return buildTwitterTweetSuperQuery(superQuery)
    }

    private fun readTwitterSampleSuperQuery(superId: Long): TwitterSampleSuperQuery? {
        val superQuery = TwitterSampleQueryDao.getSuperQueryById(superId) ?: return null
        return buildTwitterSampleSuperQuery(superQuery)
    }

    override fun readSuperQuery(superId: Long): SuperQuery? = transaction {
        val queryDto = SuperQueryDao.getQueryDtoBySuperQueryId(superId)
            ?: return@transaction null

        when (queryDto.platform) {
            ApiPlatform.TWITTER -> when (queryDto.platformQueryType as TwitterQueryType) {
                TwitterQueryType.TWITTER_TWEET_QUERY -> readTwitterTweetSuperQuery(superId)
                TwitterQueryType.TWITTER_SAMPLE_TWEET_QUERY -> readTwitterSampleSuperQuery(superId)
            }

            ApiPlatform.REDDIT -> {
                TODO()
            }

            ApiPlatform.CROWDTANGLE -> when (queryDto.platformQueryType as CtQueryType) {
                CtQueryType.CT_POST_LIST_QUERY -> readCtListSuperQuery(superId)
                CtQueryType.CT_POST_SEARCH_QUERY -> readCtPostSuperQuery(superId)
            }
        }
    }

    override fun insertSuperQuery(superQuery: SuperQuery) {
        return transaction {
            if (QueryDao.queryLabelExists(superQuery.label)) {
                throw SuperQueryLabelExistsException()
            }

            // Insert generic query details of the super query.
            val queryId = insertGenericQueryDetails(superQuery)

            // Insert new super query (ID used as FK by sub queries).
            val superQueryId =
                SuperQueryDao.insertDto(SuperQueryDto(queryId, superQuery.indexInCineast))

            // Insert query-specific details of the super query (reference not used).
            when (superQuery.queryPlatform) {
                ApiPlatform.TWITTER -> when (superQuery.platformQueryType as TwitterQueryType) {
                    TwitterQueryType.TWITTER_TWEET_QUERY -> insertTwitterTweetQueryDetails(
                        superQuery as TwitterTweetSuperQuery,
                        queryId
                    )

                    TwitterQueryType.TWITTER_SAMPLE_TWEET_QUERY -> insertTwitterSampleQueryDetails(
                        superQuery as TwitterSampleSuperQuery,
                        queryId
                    )
                }

                ApiPlatform.REDDIT -> {
                    TODO()
                }

                ApiPlatform.CROWDTANGLE -> when (superQuery.platformQueryType as CtQueryType) {
                    CtQueryType.CT_POST_LIST_QUERY -> insertCtListQueryDetails(
                        superQuery as CtListSuperQuery,
                        queryId
                    )

                    CtQueryType.CT_POST_SEARCH_QUERY -> insertCtPostQueryDetails(
                        superQuery as CtPostSuperQuery,
                        queryId
                    )
                }
            }

            // Insert multimedia details.
            superQuery.fetchMultimedia.forEach {
                SuperQueryMultimediaDao.insertDto(SuperQueryMultimediaDto(superQueryId, it))
            }

            // Insert sub queries, referencing the super query.
            superQuery.subQueries.map { subQuery ->
                // Update super ID.
                subQuery.superId = superQueryId
                insertSubQuery(subQuery)
            }

            superQuery.id = superQueryId
        }
    }

    override fun updateQueryInterval(id: Long, newStart: Instant, newEnd: Instant) = transaction {
        val subQueryDto = SubQueryDao.findByKey(id) ?: return@transaction false
        val queryDto = QueryDao.getByKey(subQueryDto.ref)
        val updatedDto = queryDto.copy(from = newStart, to = newEnd)
        QueryDao.updateDto(updatedDto)
    }

    override fun deleteSubQuery(id: Long): Boolean = transaction {
        // Remove the object from QueryTable and let cascade remove everything else.
        val subQueryDto = SubQueryDao.findByKey(id) ?: return@transaction false
        QueryDao.deleteByKey(subQueryDto.ref)
    }

    override fun readQueryState(subQuery: SubQuery): QueryState? {
        val id = subQuery.id ?: return null

        return transaction {
            if (!QueryStateDao.existsByKey(id)) {
                return@transaction null
            }

            /*
             * Ids of state and sub query should be identical since we only insert sub queries
             * with states, but we go via the FK here anyway to be sure.
            */
            val stateDto = QueryStateDao.getByRef(id).first()

            QueryState(
                stateDto.streamStatus,
                stateDto.statusText,
                stateDto.lastCheckpointTimestamp,
                stateDto.lastProcessingTimestamp,
                stateDto.key
            )
        }
    }

    override fun writeQueryState(state: QueryState, sq: SubQuery): Long? = transaction {
        // TODO Only allow updates if the processing timestamp is newer (trigger?).
        QueryStateDao.upsertDtoByRef(
            QueryStateDto(
                sq.id!!,
                state.streamStatus,
                state.statusText,
                state.lastCheckpointTimestamp,
                state.lastProcessingTimestamp,
                state.id // Null if state has not been persisted.
            )
        )
    }

    override fun updateQueryState(state: QueryState) = transaction {
        QueryStateDao.updateDto(
            QueryStateDto(
                null, // No sub query provided, keep old reference.
                state.streamStatus,
                state.statusText,
                state.lastCheckpointTimestamp,
                state.lastProcessingTimestamp,
                state.id
            )
        )
    }

    override fun getStatesByQueryIds(ids: List<Long>): Map<Long, QueryState?> = transaction {
        val stateMap = mutableMapOf<Long, QueryState?>()

        ids.forEach {
            val stateDto = QueryStateDao.findByKey(it)

            if (stateDto == null) {
                stateMap[it] = null
            } else {
                stateMap[it] = QueryState(
                    stateDto.streamStatus,
                    stateDto.statusText,
                    stateDto.lastCheckpointTimestamp,
                    stateDto.lastProcessingTimestamp,
                    stateDto.ref
                )
            }
        }

        stateMap
    }

    override fun getCollectionLabels(): List<String> = transaction {
        QueryDao.getAllCollectionLabels()
    }

}
