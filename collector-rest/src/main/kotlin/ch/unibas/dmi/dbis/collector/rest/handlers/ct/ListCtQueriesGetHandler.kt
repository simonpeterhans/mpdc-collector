package ch.unibas.dmi.dbis.collector.rest.handlers.ct

import ch.unibas.dmi.dbis.collector.rest.handlers.GetHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.ct.list.ListCtListQueriesGetHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.ct.post.ListCtPostQueriesGetHandler
import ch.unibas.dmi.dbis.collector.rest.responses.ResponseMessage
import ch.unibas.dmi.dbis.collector.rest.responses.ct.ListCtQueryResponse
import ch.unibas.dmi.dbis.collector.rest.status.StatusCode
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiResponse

class ListCtQueriesGetHandler : GetHandler<ListCtQueryResponse> {

    override val route: String = "/ct/query/list"

    @OpenApi(
        method = HttpMethod.GET,
        summary = "Gets a list of all CrowdTangle super queries.",
        path = "/api/ct/query/list",
        tags = ["crowdtangle"],
        responses = [
            OpenApiResponse(
                StatusCode.OK.toString(),
                [OpenApiContent(ListCtQueryResponse::class)]
            ),
            OpenApiResponse(
                StatusCode.INTERNAL_SERVER_ERROR.toString(),
                [OpenApiContent(ResponseMessage::class)]
            ),
        ]
    )
    override fun executeGet(ctx: Context): ListCtQueryResponse {
        val postQueries = ListCtPostQueriesGetHandler.getPostQueryInfo()
        val listQueries = ListCtListQueriesGetHandler.getListQueryInfo()

        return ListCtQueryResponse(postQueries, listQueries)
    }

}
