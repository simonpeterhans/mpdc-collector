package ch.unibas.dmi.dbis.collector.rest.handlers.twitter.tweet

import ch.unibas.dmi.dbis.collector.core.query.QueryHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.PostHandler
import ch.unibas.dmi.dbis.collector.rest.requests.twitter.TwitterTweetQueryRequest
import ch.unibas.dmi.dbis.collector.rest.responses.ResponseMessage
import ch.unibas.dmi.dbis.collector.rest.status.StatusCode
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

class NewTwitterTweetQueryPostHandler : PostHandler<ResponseMessage> {

    override val route: String = "/twitter/query/tweet/new"

    @OpenApi(
        method = HttpMethod.POST,
        summary = "Deploys a new Twitter query for a tweet search or stream.",
        path = "/api/twitter/query/tweet/new",
        tags = ["twitter"],
        requestBody = OpenApiRequestBody(
            content = [OpenApiContent(TwitterTweetQueryRequest::class)],
            required = true,
            description = "An object containing the query details for the tweet search or stream."
        ),
        responses = [
            OpenApiResponse(StatusCode.OK.toString(), [OpenApiContent(ResponseMessage::class)]),
            OpenApiResponse(
                StatusCode.INTERNAL_SERVER_ERROR.toString(),
                [OpenApiContent(ResponseMessage::class)]
            )
        ]
    )
    override fun executePost(ctx: Context): ResponseMessage {
        val request = ctx.bodyAsClass<TwitterTweetQueryRequest>()

        QueryHandler.processNewSuperQuery(request.toTwitterTweetQuery())

        return ResponseMessage("Successfully issued new Twitter tweet query.")
    }

}
