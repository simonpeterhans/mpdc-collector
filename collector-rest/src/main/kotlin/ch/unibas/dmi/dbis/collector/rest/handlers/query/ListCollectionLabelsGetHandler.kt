package ch.unibas.dmi.dbis.collector.rest.handlers.query

import ch.unibas.dmi.dbis.collector.core.dal.repositories.QueryRepository
import ch.unibas.dmi.dbis.collector.rest.handlers.GetHandler
import ch.unibas.dmi.dbis.collector.rest.responses.CollectionLabelsResponse
import ch.unibas.dmi.dbis.collector.rest.responses.ResponseMessage
import ch.unibas.dmi.dbis.collector.rest.status.StatusCode
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiResponse

class ListCollectionLabelsGetHandler : GetHandler<CollectionLabelsResponse> {

    override val route: String = "/query/collections"

    @OpenApi(
        method = HttpMethod.GET,
        summary = "Fetches a list of all existing collections.",
        path = "/api/query/collections",
        tags = ["query"],
        responses = [
            OpenApiResponse(
                StatusCode.OK.toString(),
                [OpenApiContent(CollectionLabelsResponse::class)]
            ),
            OpenApiResponse(
                StatusCode.INTERNAL_SERVER_ERROR.toString(),
                [OpenApiContent(ResponseMessage::class)]
            ),
        ]
    )
    override fun executeGet(ctx: Context): CollectionLabelsResponse {
        return CollectionLabelsResponse(QueryRepository.getCollectionLabels())
    }

}
