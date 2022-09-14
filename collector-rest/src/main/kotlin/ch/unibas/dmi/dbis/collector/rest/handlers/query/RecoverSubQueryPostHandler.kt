package ch.unibas.dmi.dbis.collector.rest.handlers.query

import ch.unibas.dmi.dbis.collector.core.query.QueryHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.PostHandler
import ch.unibas.dmi.dbis.collector.rest.responses.ResponseMessage
import ch.unibas.dmi.dbis.collector.rest.responses.recovery.QueryProcessingResponse
import ch.unibas.dmi.dbis.collector.rest.status.StatusCode
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

class RecoverSubQueryPostHandler : PostHandler<QueryProcessingResponse> {

    override val route: String = "/query/recover/sub"

    @OpenApi(
        method = HttpMethod.POST,
        summary = "Recovers sub queries by ID.",
        path = "/api/query/recover/sub",
        tags = ["query"],
        queryParams = [
            OpenApiParam(
                "id",
                Long::class,
                description = "The IDs of the sub queries to recover.",
                required = true,
                isRepeatable = true
            )
        ],
        responses = [
            OpenApiResponse(StatusCode.OK.toString(), [OpenApiContent(ResponseMessage::class)]),
            OpenApiResponse(
                StatusCode.INTERNAL_SERVER_ERROR.toString(),
                [OpenApiContent(ResponseMessage::class)]
            )
        ]
    )
    override fun executePost(ctx: Context): QueryProcessingResponse {
        val ids = ctx.queryParams("id").map { it.toLong() }
        val responseMap = recoverSubQueries(ids)
        return QueryProcessingResponse(responseMap)
    }

    private fun recoverSubQueries(ids: List<Long>): MutableMap<Long, String> {
        val responseMap = mutableMapOf<Long, String>()

        ids.forEach {
            try {
                QueryHandler.recoverAndDeploySubQuery(it)
                responseMap[it] = "Successfully recovered sub query."
            } catch (e: Exception) {
                responseMap[it] = "Failed to recover sub query: ${e.message}"
            }
        }

        return responseMap
    }

}
