package ch.unibas.dmi.dbis.collector.rest.handlers.twitter.sample

import ch.unibas.dmi.dbis.collector.core.dal.repositories.QueryRepository
import ch.unibas.dmi.dbis.collector.rest.handlers.GetHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.common.QueryInfoBuilding
import ch.unibas.dmi.dbis.collector.rest.responses.ResponseMessage
import ch.unibas.dmi.dbis.collector.rest.responses.twitter.ListTwitterSampleQueryResponse
import ch.unibas.dmi.dbis.collector.rest.responses.twitter.TwitterSampleSuperQueryInfo
import ch.unibas.dmi.dbis.collector.rest.status.StatusCode
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiResponse

class ListTwitterSampleQueriesGetHandler : GetHandler<ListTwitterSampleQueryResponse> {

    override val route: String = "/twitter/query/sample/list"

    companion object {

        fun getSampleQueryInfo(): List<TwitterSampleSuperQueryInfo> {
            val queries = QueryRepository.getAllTwitterSampleQueries()
            val stateMap = QueryInfoBuilding.buildStateMap(queries)
            val statsMap = QueryInfoBuilding.buildStatsMap(queries)

            return queries.map {
                TwitterSampleSuperQueryInfo.fromTwitterSampleSuperQuery(it, stateMap, statsMap)
            }
        }

    }

    @OpenApi(
        method = HttpMethod.GET,
        summary = "Gets a list of all Twitter sample super queries.",
        path = "/api/twitter/query/sample/list",
        tags = ["twitter"],
        responses = [
            OpenApiResponse(
                StatusCode.OK.toString(),
                [OpenApiContent(ListTwitterSampleQueryResponse::class)]
            ),
            OpenApiResponse(
                StatusCode.INTERNAL_SERVER_ERROR.toString(),
                [OpenApiContent(ResponseMessage::class)]
            ),
        ]
    )
    override fun executeGet(ctx: Context): ListTwitterSampleQueryResponse {
        val superQueries = getSampleQueryInfo()
        return ListTwitterSampleQueryResponse(superQueries)
    }

}
