package ch.unibas.dmi.dbis.collector.rest.handlers.query

import ch.unibas.dmi.dbis.collector.core.dal.repositories.QueryRepository
import ch.unibas.dmi.dbis.collector.rest.handlers.GetHandler
import ch.unibas.dmi.dbis.collector.rest.responses.QueryStateResponse
import ch.unibas.dmi.dbis.collector.rest.responses.ResponseMessage
import ch.unibas.dmi.dbis.collector.rest.status.StatusCode
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

class QueryStateGetHandler : GetHandler<QueryStateResponse> {

    override val route: String = "/query/state"

    @OpenApi(
        method = HttpMethod.GET,
        summary = "Gets the query state for one or multiple IDs.",
        path = "/api/query/state",
        tags = ["query"],
        queryParams = [OpenApiParam(
            "id",
            Long::class,
            description = "The sub query IDs to get the query state for.",
            required = true,
            isRepeatable = true
        )],
        responses = [
            OpenApiResponse(
                StatusCode.OK.toString(),
                [OpenApiContent(QueryStateResponse::class)]
            ),
            OpenApiResponse(
                StatusCode.INTERNAL_SERVER_ERROR.toString(),
                [OpenApiContent(ResponseMessage::class)]
            ),
        ]
    )
    override fun executeGet(ctx: Context): QueryStateResponse {
        val ids = ctx.queryParams("id").map { it.toLong() }
        return QueryStateResponse(QueryRepository.getStatesByQueryIds(ids))
    }

}
