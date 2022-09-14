package ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream.sample

import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.sample.TwitterSampleSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream.TwitterStreamReader
import ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream.TwitterStreamReaderManager

object TwitterSampleStreamReaderManager : TwitterStreamReaderManager<TwitterSampleSubQuery>() {

    override fun createStreamReader(apiKey: String): TwitterStreamReader<TwitterSampleSubQuery> {
        return TwitterSampleStreamReader(apiKey)
    }

}
