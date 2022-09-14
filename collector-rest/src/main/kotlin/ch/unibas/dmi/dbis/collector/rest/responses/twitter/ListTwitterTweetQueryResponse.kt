package ch.unibas.dmi.dbis.collector.rest.responses.twitter

import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.flow.QueryState
import ch.unibas.dmi.dbis.collector.core.model.misc.SubQueryStats
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.model.query.TemporalType
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet.TwitterTweetQueryOptions
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet.TwitterTweetSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet.TwitterTweetSuperQuery
import ch.unibas.dmi.dbis.collector.rest.responses.model.SubQueryResponse
import ch.unibas.dmi.dbis.collector.rest.responses.model.SuperQueryResponse

class TwitterTweetSubQueryInfo(
    apiKey: String,
    temporalType: TemporalType,
    queryData: QueryData,
    val tweetQueryData: TwitterTweetQueryOptions,
    state: QueryState,
    stats: SubQueryStats,
    id: Long
) : SubQueryResponse(apiKey, temporalType, queryData, state, stats, id) {

    companion object {

        fun fromTwitterTweetSubQuery(
            q: TwitterTweetSubQuery,
            state: QueryState,
            stats: SubQueryStats
        ) = TwitterTweetSubQueryInfo(
            q.apiKey,
            q.temporalType,
            q.queryData,
            q.tweetQueryData,
            state,
            stats,
            q.id!!
        )

    }

}

data class TwitterTweetSuperQueryInfo(
    override val queryData: QueryData,
    val tweetQueryData: TwitterTweetQueryOptions,
    override val fetchMultimedia: List<MediaType>,
    override val indexInCineast: Boolean,
    override val subQueries: List<TwitterTweetSubQueryInfo>,
    override val id: Long
) : SuperQueryResponse() {

    companion object {

        fun fromTwitterTweetSuperQuery(
            q: TwitterTweetSuperQuery,
            stateMap: Map<Long, QueryState?>,
            statsMap: Map<Long, SubQueryStats>
        ) = TwitterTweetSuperQueryInfo(
            q.queryData,
            q.tweetQueryData,
            q.fetchMultimedia,
            q.indexInCineast,
            q.subQueries.map {
                TwitterTweetSubQueryInfo.fromTwitterTweetSubQuery(
                    it,
                    stateMap[it.id]!!,
                    statsMap[it.id]!!
                )
            },
            q.id!!
        )

    }

}

data class ListTwitterTweetQueryResponse(
    val superQueries: List<TwitterTweetSuperQueryInfo>
)
