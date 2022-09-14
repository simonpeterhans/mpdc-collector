package ch.unibas.dmi.dbis.collector.rest.responses.twitter

import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.flow.QueryState
import ch.unibas.dmi.dbis.collector.core.model.misc.SubQueryStats
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.model.query.TemporalType
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.sample.TwitterSampleQueryOptions
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.sample.TwitterSampleSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.sample.TwitterSampleSuperQuery
import ch.unibas.dmi.dbis.collector.rest.responses.model.SubQueryResponse
import ch.unibas.dmi.dbis.collector.rest.responses.model.SuperQueryResponse

class TwitterSampleSubQueryInfo(
    apiKey: String,
    temporalType: TemporalType,
    queryData: QueryData,
    val sampleQueryData: TwitterSampleQueryOptions,
    state: QueryState,
    stats: SubQueryStats,
    id: Long
) : SubQueryResponse(apiKey, temporalType, queryData, state, stats, id) {

    companion object {

        fun fromTwitterSampleSubQuery(
            q: TwitterSampleSubQuery,
            state: QueryState,
            stats: SubQueryStats
        ) = TwitterSampleSubQueryInfo(
            q.apiKey,
            q.temporalType,
            q.queryData,
            q.sampleQueryData,
            state,
            stats,
            q.id!!
        )

    }

}

data class TwitterSampleSuperQueryInfo(
    override val queryData: QueryData,
    val sampleQueryData: TwitterSampleQueryOptions,
    override val fetchMultimedia: List<MediaType>,
    override val indexInCineast: Boolean,
    override val subQueries: List<TwitterSampleSubQueryInfo>,
    override val id: Long
) : SuperQueryResponse() {

    companion object {

        fun fromTwitterSampleSuperQuery(
            q: TwitterSampleSuperQuery,
            stateMap: Map<Long, QueryState?>,
            statsMap: Map<Long, SubQueryStats>
        ) = TwitterSampleSuperQueryInfo(
            q.queryData,
            q.sampleQueryData,
            q.fetchMultimedia,
            q.indexInCineast,
            q.subQueries.map {
                TwitterSampleSubQueryInfo.fromTwitterSampleSubQuery(
                    it,
                    stateMap[it.id]!!,
                    statsMap[it.id]!!
                )
            },
            q.id!!
        )

    }

}

data class ListTwitterSampleQueryResponse(
    val superQueries: List<TwitterSampleSuperQueryInfo>
)
