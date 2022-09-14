package ch.unibas.dmi.dbis.collector.core.platform.twitter.query.sample

import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.model.query.TemporalType
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterQueryType
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterSubQuery
import org.threeten.extra.Interval

class TwitterSampleSubQuery(
    override var superId: Long?,
    override val apiKey: String,
    override val temporalType: TemporalType,
    queryData: QueryData,
    val sampleQueryData: TwitterSampleQueryOptionsData,
    override var id: Long? = null
) : TwitterSubQuery(queryData), TwitterSampleQueryOptions by sampleQueryData {

    override val platformQueryType: TwitterQueryType = TwitterQueryType.TWITTER_SAMPLE_TWEET_QUERY

    override fun createRecoveryCopy(
        label: String,
        temporalType: TemporalType,
        interval: Interval,
        id: Long?
    ): TwitterSampleSubQuery = TwitterSampleSubQuery(
        this.superId,
        this.apiKey,
        temporalType,
        this.queryData.copy(label = label, interval = interval),
        this.sampleQueryData,
        id
    )

}
