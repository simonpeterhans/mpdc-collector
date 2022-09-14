package ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet

import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.model.query.SubQueryInterval
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterQueryType
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterSuperQuery

class TwitterTweetSuperQuery(
    queryData: QueryData,
    val tweetQueryData: TwitterTweetQueryOptionsData,
    override val fetchMultimedia: List<MediaType>,
    override val indexInCineast: Boolean,
    override val subQueries: MutableList<TwitterTweetSubQuery> = mutableListOf(),
    override var id: Long? = null
) : TwitterSuperQuery(queryData), TwitterTweetQueryOptions by tweetQueryData {

    override val platformQueryType: TwitterQueryType = TwitterQueryType.TWITTER_TWEET_QUERY

    fun deriveInitialSubQueries(
        apiKey: String
    ): TwitterTweetSuperQuery {
        val intervals = SubQueryInterval.deriveFromInterval(interval)

        intervals.forEach { subInterval ->
            val sub = TwitterTweetSubQuery.buildSafeTwitterTweetSubQueries(
                apiKey,
                subInterval.temporalType,
                QueryData(this.label, this.collectionName, subInterval.interval),
                tweetQueryData
            )

            subQueries.addAll(sub)
        }

        return this
    }

}
