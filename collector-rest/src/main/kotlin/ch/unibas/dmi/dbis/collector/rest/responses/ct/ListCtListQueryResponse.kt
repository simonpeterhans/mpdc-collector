package ch.unibas.dmi.dbis.collector.rest.responses.ct

import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.flow.QueryState
import ch.unibas.dmi.dbis.collector.core.model.misc.SubQueryStats
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.model.query.TemporalType
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.list.CtListQueryOptions
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.list.CtListSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.list.CtListSuperQuery
import ch.unibas.dmi.dbis.collector.rest.responses.model.SubQueryResponse
import ch.unibas.dmi.dbis.collector.rest.responses.model.SuperQueryResponse

class CtListSubQueryInfo(
    apiKey: String,
    temporalType: TemporalType,
    queryData: QueryData,
    val listData: CtListQueryOptions,
    state: QueryState,
    stats: SubQueryStats,
    id: Long
) : SubQueryResponse(apiKey, temporalType, queryData, state, stats, id) {

    companion object {

        fun fromCtListSubQuery(
            q: CtListSubQuery,
            state: QueryState,
            stats: SubQueryStats
        ) = CtListSubQueryInfo(
            q.apiKey,
            q.temporalType,
            q.queryData,
            q.postListQueryData,
            state,
            stats,
            q.id!!
        )

    }

}

class CtListSuperQueryInfo(
    override val queryData: QueryData,
    val listData: CtListQueryOptions,
    override val fetchMultimedia: List<MediaType>,
    override val indexInCineast: Boolean,
    override val subQueries: List<CtListSubQueryInfo>,
    override val id: Long
) : SuperQueryResponse() {

    companion object {

        fun fromCtListSuperQuery(
            q: CtListSuperQuery,
            stateMap: Map<Long, QueryState?>,
            statsMap: Map<Long, SubQueryStats>,
        ) = CtListSuperQueryInfo(
            q.queryData,
            q.postListQueryData,
            q.fetchMultimedia,
            q.indexInCineast,
            q.subQueries.map {
                CtListSubQueryInfo.fromCtListSubQuery(
                    it,
                    stateMap[it.id]!!,
                    statsMap[it.id]!!
                )
            },
            q.id!!
        )

    }

}

data class ListCtListQueryResponse(
    val superQueries: List<CtListSuperQueryInfo>
)
