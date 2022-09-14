package ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet

import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.parameter.TwitterAccountQueryParameter
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.parameter.TwitterKeywordQueryParameter
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.parameter.TwitterQueryParameterType
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.parameter.TwitterQueryParametrizable

// TODO Add operators to filter for media etc.: https://developer.twitter.com/en/docs/twitter-api/tweets/counts/integrate/build-a-query#list

interface TwitterTweetQueryOptions {

    val keywords: List<TwitterKeywordQueryParameter>
    val accounts: List<TwitterAccountQueryParameter>
    val queryDelayMinutes: Int
    val referencedTweetsDepth: Int
    val useStreamingApiIfPossible: Boolean

}

data class TwitterTweetQueryOptionsData(
    private val rawKeywords: List<TwitterKeywordQueryParameter>,
    private val rawAccounts: List<TwitterAccountQueryParameter>,
    override val queryDelayMinutes: Int,
    override val referencedTweetsDepth: Int,
    override val useStreamingApiIfPossible: Boolean
) : TwitterTweetQueryOptions {

    override val keywords = cleanQueryParameterList(rawKeywords)
    override val accounts = cleanQueryParameterList(rawAccounts)

    val queryString = (keywords + accounts).joinToString(" OR ") { it.toQueryParameter() }

    companion object {

        const val MAX_QUERY_LENGTH = 1000

        private fun <T : TwitterQueryParametrizable> cleanQueryParameterList(
            list: List<T>
        ): List<T> {
            return list.filter { it.getRaw().isNotBlank() }.distinct()
        }

    }

    fun deriveQueryOptions(): List<TwitterTweetQueryOptionsData> {
        val tweetQueryOptionsList = mutableListOf<TwitterTweetQueryOptionsData>()

        val currKeywords = mutableListOf<TwitterKeywordQueryParameter>()
        val currAccounts = mutableListOf<TwitterAccountQueryParameter>()
        // The query string is only used to check the length.
        var currQueryString = ""

        // Add parameters until the maximum length has been reached.
        (keywords + accounts).forEach { queryParam ->
            val param = queryParam.toQueryParameter()

            if (currQueryString.length + param.length > MAX_QUERY_LENGTH) {
                tweetQueryOptionsList.add(
                    TwitterTweetQueryOptionsData(
                        currKeywords,
                        currAccounts,
                        queryDelayMinutes,
                        referencedTweetsDepth,
                        useStreamingApiIfPossible
                    )
                )

                // Reset current data.
                currKeywords.clear()
                currAccounts.clear()
                currQueryString = ""
            }

            when (queryParam.type) {
                TwitterQueryParameterType.KEYWORD -> currKeywords.add(
                    queryParam as TwitterKeywordQueryParameter
                )

                TwitterQueryParameterType.ACCOUNT -> currAccounts.add(
                    queryParam as TwitterAccountQueryParameter
                )
            }

            // Prepare query string for next iteration.
            currQueryString += queryParam.toQueryParameter() + " OR "
        }

        /*
         * If there are any leftovers, add them to a new query (guaranteed to be shorter than
         * MAX_QUERY_LENGTH), regardless of the current query string.
         */
        if (currKeywords.size + currAccounts.size != 0) {
            tweetQueryOptionsList.add(
                TwitterTweetQueryOptionsData(
                    currKeywords,
                    currAccounts,
                    queryDelayMinutes,
                    referencedTweetsDepth,
                    useStreamingApiIfPossible
                )
            )
        }

        return tweetQueryOptionsList
    }

}
