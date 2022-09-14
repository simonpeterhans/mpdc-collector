package ch.unibas.dmi.dbis.collector.rest.handlers

import ch.unibas.dmi.dbis.collector.rest.responses.ResponseMessage
import ch.unibas.dmi.dbis.collector.rest.status.StatusCode
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiResponse

class PingHandler : GetHandler<ResponseMessage> {

    override val route: String = "/ping"

    @OpenApi(
        method = HttpMethod.GET,
        summary = "Sends a simple ping to determine whether the API is reachable.",
        path = "/api/ping",
        tags = ["general"],
        responses = [
            OpenApiResponse(StatusCode.OK.toString(), [OpenApiContent(ResponseMessage::class)]),
            OpenApiResponse(
                StatusCode.INTERNAL_SERVER_ERROR.toString(),
                [OpenApiContent(ResponseMessage::class)]
            ),
        ]
    )
    override fun executeGet(ctx: Context): ResponseMessage {
        return ResponseMessage("Ping request received.")
    }

}
