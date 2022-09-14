package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.runners

import ch.unibas.dmi.dbis.collector.core.model.data.ModelEntity
import ch.unibas.dmi.dbis.collector.core.model.flow.ModelStream
import ch.unibas.dmi.dbis.collector.core.model.flow.QueryState
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.api.CtApiClient
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.api.endpoints.CtPostApi
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.responses.PostSearchResponse
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.list.CtListSubQuery
import ch.unibas.dmi.dbis.collector.core.query.RequestResultWithStatus
import java.time.Instant

class CtListRunner(
    override val query: CtListSubQuery
) : CtArchivePostRunner(
    query.queryDelayMinutes
) {

    override val stream = ModelStream<ModelEntity>(query)
    override val client: CtApiClient = CtApiClient(query.apiKey)

    override var state = QueryState()

    private val postListSearchApi: CtPostApi = client.listSearchApi()

    override fun performSearch(
        from: Instant,
        to: Instant,
        offset: Int
    ): RequestResultWithStatus<PostSearchResponse> {
        // Build and execute request.
        val req = postListSearchApi.searchPostsByLists(
            startDate = from,
            endDate = to,
            listIds = query.listIds,
            includeHistory = query.includeHistory,
            offset = offset
        )

        return executeApiRequest(req, PostSearchResponse::class.java)
    }

}
