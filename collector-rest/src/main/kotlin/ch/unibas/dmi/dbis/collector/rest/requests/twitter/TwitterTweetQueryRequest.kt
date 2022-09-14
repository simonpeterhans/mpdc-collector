package ch.unibas.dmi.dbis.collector.rest.requests.twitter

import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.parameter.TwitterAccountQueryParameter
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.parameter.TwitterKeywordQueryParameter
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet.TwitterTweetQueryOptionsData
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet.TwitterTweetSuperQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.RecursiveTweetFetcher
import org.threeten.extra.Interval
import java.time.Instant

data class TwitterTweetQueryRequest(
    val apiKey: String,
    val fetchMultimedia: List<MediaType>,
    val indexInCineast: Boolean,
    val label: String,
    val collectionName: String,
    val from: Instant,
    val to: Instant,
    val keywords: List<String>? = null,
    val accounts: List<String>? = null,
    val queryDelayMinutes: Int? = null,
    val referencedTweetsDepth: Int? = null,
    val useStreamingApiIfPossible: Boolean? = null
) {

    fun toTwitterTweetQuery(): TwitterTweetSuperQuery = TwitterTweetSuperQuery(
        QueryData(label, collectionName, Interval.of(from, to)),
        TwitterTweetQueryOptionsData(
            keywords?.map { TwitterKeywordQueryParameter(it) } ?: emptyList(),
            accounts?.map { TwitterAccountQueryParameter(it) } ?: emptyList(),
            queryDelayMinutes ?: 0,
            referencedTweetsDepth ?: RecursiveTweetFetcher.DEFAULT_MAX_DEPTH,
            useStreamingApiIfPossible ?: false
        ),
        fetchMultimedia,
        indexInCineast
    ).deriveInitialSubQueries(apiKey)

}
