package ch.unibas.dmi.dbis.collector.core.platform.twitter.api

import ch.unibas.dmi.dbis.collector.core.model.api.Api
import ch.unibas.dmi.dbis.collector.core.model.platform.ApiPlatform
import ch.unibas.dmi.dbis.collector.core.model.query.TemporalType
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterQueryType
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.sample.TwitterSampleSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet.TwitterTweetSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.search.TwitterArchiveRunner
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream.TwitterStreamRunner
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream.filtered.TwitterFilteredStreamReaderManager
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream.sample.TwitterSampleStreamReaderManager
import ch.unibas.dmi.dbis.collector.core.query.QueryRunner
import ch.unibas.dmi.dbis.collector.core.util.Version

object TwitterApi : Api<TwitterSubQuery> {

    override val platform: ApiPlatform
        get() = ApiPlatform.TWITTER

    override val version: Version
        get() = Version(0, 0, 0)

    override fun processSubQuery(
        subQuery: TwitterSubQuery
    ): QueryRunner {
        return when (subQuery.platformQueryType) {
            TwitterQueryType.TWITTER_TWEET_QUERY -> {
                val q = subQuery as TwitterTweetSubQuery

                if (q.temporalType == TemporalType.STREAM
                    && q.useStreamingApiIfPossible
                    && q.queryDelayMinutes == 0
                ) {
                    return TwitterStreamRunner(
                        q,
                        q.referencedTweetsDepth,
                        TwitterFilteredStreamReaderManager
                    )
                }

                TwitterArchiveRunner(q)
            }

            TwitterQueryType.TWITTER_SAMPLE_TWEET_QUERY -> {
                return TwitterStreamRunner(
                    subQuery as TwitterSampleSubQuery,
                    subQuery.referencedTweetsDepth,
                    TwitterSampleStreamReaderManager
                )
            }
        }
    }

}
