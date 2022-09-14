package ch.unibas.dmi.dbis.collector.rest.handlers.ct.post

import ch.unibas.dmi.dbis.collector.core.dal.repositories.QueryRepository
import ch.unibas.dmi.dbis.collector.rest.handlers.GetHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.common.QueryInfoBuilding
import ch.unibas.dmi.dbis.collector.rest.responses.ResponseMessage
import ch.unibas.dmi.dbis.collector.rest.responses.ct.CtPostSuperQueryInfo
import ch.unibas.dmi.dbis.collector.rest.responses.ct.ListCtPostQueryResponse
import ch.unibas.dmi.dbis.collector.rest.status.StatusCode
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiResponse

class ListCtPostQueriesGetHandler : GetHandler<ListCtPostQueryResponse> {

    override val route: String = "/ct/query/post/list"

    companion object {

        fun getPostQueryInfo(): List<CtPostSuperQueryInfo> {
            val queries = QueryRepository.getAllCtPostQueries()
            val stateMap = QueryInfoBuilding.buildStateMap(queries)
            val statsMap = QueryInfoBuilding.buildStatsMap(queries)

            return queries.map {
                CtPostSuperQueryInfo.fromCtPostSuperQuery(it, stateMap, statsMap)
            }
        }

    }

    @OpenApi(
        method = HttpMethod.GET,
        summary = "Gets a list of all CrowdTangle post super queries.",
        path = "/api/ct/query/post/list",
        tags = ["crowdtangle"],
        responses = [
            OpenApiResponse(
                StatusCode.OK.toString(),
                [OpenApiContent(ListCtPostQueryResponse::class)]
            ),
            OpenApiResponse(
                StatusCode.INTERNAL_SERVER_ERROR.toString(),
                [OpenApiContent(ResponseMessage::class)]
            ),
        ]
    )
    override fun executeGet(ctx: Context): ListCtPostQueryResponse {
        val queries = getPostQueryInfo()
        return ListCtPostQueryResponse(queries)
    }

}
