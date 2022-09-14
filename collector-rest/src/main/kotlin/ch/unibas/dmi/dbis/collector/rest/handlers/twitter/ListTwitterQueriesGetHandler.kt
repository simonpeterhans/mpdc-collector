package ch.unibas.dmi.dbis.collector.rest.handlers.twitter

import ch.unibas.dmi.dbis.collector.rest.handlers.GetHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.twitter.sample.ListTwitterSampleQueriesGetHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.twitter.tweet.ListTwitterTweetQueriesGetHandler
import ch.unibas.dmi.dbis.collector.rest.responses.ResponseMessage
import ch.unibas.dmi.dbis.collector.rest.responses.twitter.ListTwitterQueryResponse
import ch.unibas.dmi.dbis.collector.rest.status.StatusCode
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiResponse

class ListTwitterQueriesGetHandler : GetHandler<ListTwitterQueryResponse> {

    override val route: String = "/twitter/query/list"

    @OpenApi(
        method = HttpMethod.GET,
        summary = "Gets a list of all Twitter super queries.",
        path = "/api/twitter/query/list",
        tags = ["twitter"],
        responses = [
            OpenApiResponse(
                StatusCode.OK.toString(),
                [OpenApiContent(ListTwitterQueryResponse::class)]
            ),
            OpenApiResponse(
                StatusCode.INTERNAL_SERVER_ERROR.toString(),
                [OpenApiContent(ResponseMessage::class)]
            ),
        ]
    )
    override fun executeGet(ctx: Context): ListTwitterQueryResponse {
        val tweetQueries = ListTwitterTweetQueriesGetHandler.getTweetQueryInfo()
        val sampleQueries = ListTwitterSampleQueriesGetHandler.getSampleQueryInfo()

        return ListTwitterQueryResponse(tweetQueries, sampleQueries)
    }

}
