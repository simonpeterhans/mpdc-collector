package ch.unibas.dmi.dbis.collector.rest.handlers.twitter.sample

import ch.unibas.dmi.dbis.collector.core.query.QueryHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.PostHandler
import ch.unibas.dmi.dbis.collector.rest.requests.twitter.TwitterSampleQueryRequest
import ch.unibas.dmi.dbis.collector.rest.responses.ResponseMessage
import ch.unibas.dmi.dbis.collector.rest.status.StatusCode
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

class NewTwitterSampleQueryPostHandler : PostHandler<ResponseMessage> {

    override val route: String = "/twitter/query/sample/new"

    @OpenApi(
        method = HttpMethod.POST,
        summary = "Deploys a new Twitter query for a sample stream.",
        path = "/api/twitter/query/sample/new",
        tags = ["twitter"],
        requestBody = OpenApiRequestBody(
            content = [OpenApiContent(TwitterSampleQueryRequest::class)],
            required = true,
            description = "An object containing the query details for the sample stream."
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
        val request = ctx.bodyAsClass<TwitterSampleQueryRequest>()

        QueryHandler.processNewSuperQuery(request.toTwitterSampleQuery())

        return ResponseMessage("Successfully issued new Twitter sample query.")
    }

}
