package ch.unibas.dmi.dbis.collector.rest.handlers.twitter.tweet

import ch.unibas.dmi.dbis.collector.core.dal.repositories.QueryRepository
import ch.unibas.dmi.dbis.collector.rest.handlers.GetHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.common.QueryInfoBuilding
import ch.unibas.dmi.dbis.collector.rest.responses.ResponseMessage
import ch.unibas.dmi.dbis.collector.rest.responses.twitter.ListTwitterTweetQueryResponse
import ch.unibas.dmi.dbis.collector.rest.responses.twitter.TwitterTweetSuperQueryInfo
import ch.unibas.dmi.dbis.collector.rest.status.StatusCode
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiResponse

class ListTwitterTweetQueriesGetHandler : GetHandler<ListTwitterTweetQueryResponse> {

    override val route: String = "/twitter/query/tweet/list"

    companion object {

        fun getTweetQueryInfo(): List<TwitterTweetSuperQueryInfo> {
            val queries = QueryRepository.getAllTwitterTweetQueries()
            val stateMap = QueryInfoBuilding.buildStateMap(queries)
            val statsMap = QueryInfoBuilding.buildStatsMap(queries)

            return queries.map {
                TwitterTweetSuperQueryInfo.fromTwitterTweetSuperQuery(it, stateMap, statsMap)
            }
        }

    }

    @OpenApi(
        method = HttpMethod.GET,
        summary = "Gets a list of all Twitter tweet super queries.",
        path = "/api/twitter/query/tweet/list",
        tags = ["twitter"],
        responses = [
            OpenApiResponse(
                StatusCode.OK.toString(),
                [OpenApiContent(ListTwitterTweetQueryResponse::class)]
            ),
            OpenApiResponse(
                StatusCode.INTERNAL_SERVER_ERROR.toString(),
                [OpenApiContent(ResponseMessage::class)]
            ),
        ]
    )
    override fun executeGet(ctx: Context): ListTwitterTweetQueryResponse {
        val queries = getTweetQueryInfo()
        return ListTwitterTweetQueryResponse(queries)
    }

}
