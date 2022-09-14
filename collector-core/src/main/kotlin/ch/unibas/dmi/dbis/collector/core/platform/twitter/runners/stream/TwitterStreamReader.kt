package ch.unibas.dmi.dbis.collector.core.platform.twitter.runners.stream

import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions.Expansions
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet.Tweet
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterSubQuery
import java.time.Instant
import java.util.concurrent.LinkedBlockingQueue

data class TweetStreamElement(
    val timestamp: Instant,
    val tweet: Tweet? = null,
    val expansions: Expansions? = null
)

abstract class TwitterStreamReader<T : TwitterSubQuery>(
    val apiKey: String
) : Thread() {

    abstract fun isRunning(): Boolean

    abstract fun getStream(query: T): LinkedBlockingQueue<TweetStreamElement>?

    abstract fun subscribe(query: T): Boolean

    abstract fun unsubscribe(query: T): Boolean

}
