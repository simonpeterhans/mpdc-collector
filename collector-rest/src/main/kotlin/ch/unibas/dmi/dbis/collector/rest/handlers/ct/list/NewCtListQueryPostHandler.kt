package ch.unibas.dmi.dbis.collector.rest.handlers.ct.list

import ch.unibas.dmi.dbis.collector.core.query.QueryHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.PostHandler
import ch.unibas.dmi.dbis.collector.rest.requests.ct.CtListQueryRequest
import ch.unibas.dmi.dbis.collector.rest.responses.ResponseMessage
import ch.unibas.dmi.dbis.collector.rest.status.StatusCode
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

class NewCtListQueryPostHandler : PostHandler<ResponseMessage> {

    override val route: String = "/ct/query/list/new"

    @OpenApi(
        method = HttpMethod.POST,
        summary = "Deploys a new CrowdTangle post query for lists.",
        path = "/api/ct/query/list/new",
        tags = ["crowdtangle"],
        requestBody = OpenApiRequestBody(
            content = [OpenApiContent(CtListQueryRequest::class)],
            required = true,
            description = "An object containing the query details for the list search or stream."
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
        val request = ctx.bodyAsClass<CtListQueryRequest>()

        QueryHandler.processNewSuperQuery(request.toCtListQuery())

        return ResponseMessage("Successfully issued new CrowdTangle post list query.")
    }

}
