package ch.unibas.dmi.dbis.collector.rest.handlers.ct.post

import ch.unibas.dmi.dbis.collector.core.query.QueryHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.PostHandler
import ch.unibas.dmi.dbis.collector.rest.requests.ct.CtPostQueryRequest
import ch.unibas.dmi.dbis.collector.rest.responses.ResponseMessage
import ch.unibas.dmi.dbis.collector.rest.status.StatusCode
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

class NewCtPostQueryPostHandler : PostHandler<ResponseMessage> {

    override val route: String = "/ct/query/post/new"

    @OpenApi(
        method = HttpMethod.POST,
        summary = "Deploys a new CrowdTangle post query.",
        path = "/api/ct/query/post/new",
        tags = ["crowdtangle"],
        requestBody = OpenApiRequestBody(
            content = [OpenApiContent(CtPostQueryRequest::class)],
            required = true,
            description = "An object containing the query details for a search or stream."
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
        val request = ctx.bodyAsClass<CtPostQueryRequest>()

        QueryHandler.processNewSuperQuery(request.toCtPostQuery())

        return ResponseMessage("Successfully issued new CrowdTangle post search query.")
    }

}
