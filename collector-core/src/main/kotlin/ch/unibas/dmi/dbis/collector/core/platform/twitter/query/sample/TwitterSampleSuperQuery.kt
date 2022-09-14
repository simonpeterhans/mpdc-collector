package ch.unibas.dmi.dbis.collector.core.platform.twitter.query.sample

import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.model.query.TemporalType
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterQueryType
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterSuperQuery
import org.threeten.extra.Interval
import java.time.Instant

class TwitterSampleSuperQuery(
    queryData: QueryData,
    val sampleQueryData: TwitterSampleQueryOptionsData,
    override val fetchMultimedia: List<MediaType>,
    override val indexInCineast: Boolean,
    override val subQueries: MutableList<TwitterSampleSubQuery> = mutableListOf(),
    override var id: Long? = null
) : TwitterSuperQuery(
    if (queryData.interval.start.isAfter(Instant.now())) {
        queryData
    } else {
        queryData.copy(interval = Interval.of(Instant.now(), queryData.interval.end))
    }
), TwitterSampleQueryOptions by sampleQueryData {

    override val platformQueryType: TwitterQueryType = TwitterQueryType.TWITTER_SAMPLE_TWEET_QUERY

    // TODO Error checks.
    fun deriveInitialSubQueries(
        apiKey: String
    ): TwitterSampleSuperQuery {
        // We only can derive 1 query here - it's an unrecoverable stream.
        subQueries.add(
            TwitterSampleSubQuery(
                this.id, // Null if called before stored.
                apiKey,
                TemporalType.STREAM,
                queryData,
                this.sampleQueryData
            )
        )

        return this
    }


}

