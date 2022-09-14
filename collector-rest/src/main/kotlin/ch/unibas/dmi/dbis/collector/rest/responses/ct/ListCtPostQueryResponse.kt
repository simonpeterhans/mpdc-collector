package ch.unibas.dmi.dbis.collector.rest.responses.ct

import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.flow.QueryState
import ch.unibas.dmi.dbis.collector.core.model.misc.SubQueryStats
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.model.query.TemporalType
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.post.CtPostQueryOptions
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.post.CtPostSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.post.CtPostSuperQuery
import ch.unibas.dmi.dbis.collector.rest.responses.model.SubQueryResponse
import ch.unibas.dmi.dbis.collector.rest.responses.model.SuperQueryResponse

class CtPostSubQueryInfo(
    apiKey: String,
    temporalType: TemporalType,
    queryData: QueryData,
    val postQueryData: CtPostQueryOptions,
    state: QueryState,
    stats: SubQueryStats,
    id: Long
) : SubQueryResponse(apiKey, temporalType, queryData, state, stats, id) {

    companion object {

        fun fromCtPostSubQuery(
            q: CtPostSubQuery,
            state: QueryState,
            stats: SubQueryStats
        ) = CtPostSubQueryInfo(
            q.apiKey,
            q.temporalType,
            q.queryData,
            q.postQueryData,
            state,
            stats,
            q.id!!
        )

    }

}

class CtPostSuperQueryInfo(
    override val queryData: QueryData,
    val postQueryData: CtPostQueryOptions,
    override val fetchMultimedia: List<MediaType>,
    override val indexInCineast: Boolean,
    override val subQueries: List<CtPostSubQueryInfo>,
    override val id: Long
) : SuperQueryResponse() {

    companion object {

        fun fromCtPostSuperQuery(
            q: CtPostSuperQuery,
            stateMap: Map<Long, QueryState?>,
            statsMap: Map<Long, SubQueryStats>,
        ) = CtPostSuperQueryInfo(
            q.queryData,
            q.postQueryData,
            q.fetchMultimedia,
            q.indexInCineast,
            q.subQueries.map {
                CtPostSubQueryInfo.fromCtPostSubQuery(
                    it,
                    stateMap[it.id]!!,
                    statsMap[it.id]!!
                )
            },
            q.id!!
        )

    }

}

data class ListCtPostQueryResponse(
    val superQueries: List<CtPostSuperQueryInfo>
)
