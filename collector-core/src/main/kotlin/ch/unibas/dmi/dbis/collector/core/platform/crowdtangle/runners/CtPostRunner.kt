package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.runners

import ch.unibas.dmi.dbis.collector.core.model.data.ModelEntity
import ch.unibas.dmi.dbis.collector.core.model.flow.ModelStream
import ch.unibas.dmi.dbis.collector.core.model.flow.QueryState
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.api.CtApiClient
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.api.endpoints.CtPostApi
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.responses.PostSearchResponse
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.post.CtPostSubQuery
import ch.unibas.dmi.dbis.collector.core.query.RequestResultWithStatus
import java.time.Instant

class CtPostRunner(
    override val query: CtPostSubQuery
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
        val req = postListSearchApi.searchPosts(
            startDate = from,
            endDate = to,
            searchTerm = query.postQueryData.keywordString,
            includeHistory = query.includeHistory,
            offset = offset
        )

        return executeApiRequest(req, PostSearchResponse::class.java)
    }

}
