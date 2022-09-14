package ch.unibas.dmi.dbis.collector.rest.requests.ct

import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.post.CtPostQueryOptionsData
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.post.CtPostSuperQuery
import org.threeten.extra.Interval
import java.time.Instant

data class CtPostQueryRequest(
    val apiKey: String,
    val fetchMultimedia: List<MediaType>,
    val indexInCineast: Boolean,
    val label: String,
    val collectionName: String,
    val from: Instant,
    val to: Instant,
    val keywords: List<String>? = null,
    val queryDelayMinutes: Int? = null,
    val includeHistory: Boolean? = null
) {

    fun toCtPostQuery(): CtPostSuperQuery = CtPostSuperQuery(
        QueryData(label, collectionName, Interval.of(from, to)),
        CtPostQueryOptionsData(
            keywords ?: emptyList(),
            queryDelayMinutes ?: 0,
            includeHistory ?: false
        ),
        fetchMultimedia,
        indexInCineast
    ).deriveInitialSubQueries(apiKey)

}
