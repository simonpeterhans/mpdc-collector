package ch.unibas.dmi.dbis.collector.rest.requests.twitter

import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.sample.TwitterSampleQueryOptionsData
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.sample.TwitterSampleSuperQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.RecursiveTweetFetcher
import org.threeten.extra.Interval
import java.time.Instant

data class TwitterSampleQueryRequest(
    val apiKey: String,
    val fetchMultimedia: List<MediaType>,
    val indexInCineast: Boolean,
    val label: String,
    val collectionName: String,
    val from: Instant,
    val to: Instant,
    val referencedTweetsDepth: Int? = null
) {

    fun toTwitterSampleQuery(): TwitterSampleSuperQuery = TwitterSampleSuperQuery(
        QueryData(label, collectionName, Interval.of(from, to)),
        TwitterSampleQueryOptionsData(
            referencedTweetsDepth ?: RecursiveTweetFetcher.DEFAULT_MAX_DEPTH
        ),
        fetchMultimedia,
        indexInCineast
    ).deriveInitialSubQueries(apiKey)

}
