package ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet

import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.model.query.TemporalType
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterQueryType
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterSubQuery
import org.threeten.extra.Interval

class TwitterTweetSubQuery(
    override var superId: Long?,
    override val apiKey: String,
    override val temporalType: TemporalType,
    queryData: QueryData,
    val tweetQueryData: TwitterTweetQueryOptionsData,
    override var id: Long? = null
) : TwitterSubQuery(queryData), TwitterTweetQueryOptions by tweetQueryData {

    override val platformQueryType: TwitterQueryType = TwitterQueryType.TWITTER_TWEET_QUERY

    companion object {

        fun buildSafeTwitterTweetSubQueries(
            apiKey: String,
            temporalType: TemporalType,
            queryData: QueryData,
            tweetQueryData: TwitterTweetQueryOptionsData
        ): List<TwitterTweetSubQuery> {
            val splits = tweetQueryData.deriveQueryOptions()

            return splits.map {
                TwitterTweetSubQuery(
                    null,
                    apiKey,
                    temporalType,
                    queryData,
                    it
                )
            }
        }

    }

    override fun createRecoveryCopy(
        label: String,
        temporalType: TemporalType,
        interval: Interval,
        id: Long?
    ): TwitterTweetSubQuery = TwitterTweetSubQuery(
        this.superId,
        this.apiKey,
        temporalType,
        this.queryData.copy(label = label, interval = interval),
        this.tweetQueryData,
        id
    )

}
