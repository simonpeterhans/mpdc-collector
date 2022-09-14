package ch.unibas.dmi.dbis.collector.rest.requests.ct

import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.list.CtListQueryOptionsData
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.list.CtListSuperQuery
import org.threeten.extra.Interval
import java.time.Instant

data class CtListQueryRequest(
    val apiKey: String,
    val fetchMultimedia: List<MediaType>,
    val indexInCineast: Boolean,
    val label: String,
    val collectionName: String,
    val from: Instant,
    val to: Instant,
    val listIds: List<Long>? = null,
    val queryDelayMinutes: Int? = null,
    val includeHistory: Boolean? = null
) {

    fun toCtListQuery(): CtListSuperQuery = CtListSuperQuery(
        QueryData(label, collectionName, Interval.of(from, to)),
        CtListQueryOptionsData(
            listIds ?: emptyList(),
            queryDelayMinutes ?: 0,
            includeHistory ?: false
        ),
        fetchMultimedia,
        indexInCineast
    ).deriveInitialSubQueries(apiKey)

}
