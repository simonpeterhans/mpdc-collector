package ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream.filtered

import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet.TwitterTweetSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream.TwitterStreamReader
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream.TwitterStreamReaderManager

object TwitterFilteredStreamReaderManager : TwitterStreamReaderManager<TwitterTweetSubQuery>() {

    override fun createStreamReader(apiKey: String): TwitterStreamReader<TwitterTweetSubQuery> {
        return TwitterFilteredStreamReader(apiKey)
    }

}
