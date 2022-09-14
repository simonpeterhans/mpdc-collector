package ch.unibas.dmi.dbis.collector.rest.handlers.ct.list

import ch.unibas.dmi.dbis.collector.core.dal.repositories.QueryRepository
import ch.unibas.dmi.dbis.collector.rest.handlers.GetHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.common.QueryInfoBuilding
import ch.unibas.dmi.dbis.collector.rest.responses.ResponseMessage
import ch.unibas.dmi.dbis.collector.rest.responses.ct.CtListSuperQueryInfo
import ch.unibas.dmi.dbis.collector.rest.responses.ct.ListCtListQueryResponse
import ch.unibas.dmi.dbis.collector.rest.status.StatusCode
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiResponse

class ListCtListQueriesGetHandler : GetHandler<ListCtListQueryResponse> {

    override val route: String = "/ct/query/list/list"

    companion object {

        fun getListQueryInfo(): List<CtListSuperQueryInfo> {
            val queries = QueryRepository.getAllCtListQueries()
            val stateMap = QueryInfoBuilding.buildStateMap(queries)
            val statsMap = QueryInfoBuilding.buildStatsMap(queries)

            return queries.map {
                CtListSuperQueryInfo.fromCtListSuperQuery(it, stateMap, statsMap)
            }
        }

    }

    @OpenApi(
        method = HttpMethod.GET,
        summary = "Gets a list of all CrowdTangle post list super queries.",
        path = "/api/ct/query/list/list",
        tags = ["crowdtangle"],
        responses = [
            OpenApiResponse(
                StatusCode.OK.toString(),
                [OpenApiContent(ListCtListQueryResponse::class)]
            ),
            OpenApiResponse(
                StatusCode.INTERNAL_SERVER_ERROR.toString(),
                [OpenApiContent(ResponseMessage::class)]
            ),
        ]
    )
    override fun executeGet(ctx: Context): ListCtListQueryResponse {
        val queries = getListQueryInfo()
        return ListCtListQueryResponse(queries)
    }

}
