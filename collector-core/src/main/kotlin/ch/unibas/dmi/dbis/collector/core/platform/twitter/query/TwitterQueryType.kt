package ch.unibas.dmi.dbis.collector.core.platform.twitter.query

import ch.unibas.dmi.dbis.collector.core.model.query.PlatformQueryType

enum class TwitterQueryType(private val recoverable: Boolean) : PlatformQueryType {

    TWITTER_TWEET_QUERY(true),
    TWITTER_SAMPLE_TWEET_QUERY(false);

    override fun isRecoverable(): Boolean = recoverable

}
