package ch.unibas.dmi.dbis.collector.core.platform.twitter.runners

import ch.unibas.dmi.dbis.collector.core.dal.repositories.EntityRepository
import ch.unibas.dmi.dbis.collector.core.model.data.Post
import ch.unibas.dmi.dbis.collector.core.model.platform.Platform
import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.endpoints.TwitterApiOptions
import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.endpoints.TwitterLookupApi
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.ExpandedTweet
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.responses.TweetLookupResponse
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterSubQuery
import ch.unibas.dmi.dbis.collector.core.query.FetchStatus
import ch.unibas.dmi.dbis.collector.core.query.RequestResultWithStatus
import ch.unibas.dmi.dbis.collector.core.query.executeRequest
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class RecursiveTweetFetcher(
    private val lookupApi: TwitterLookupApi,
    private val query: TwitterSubQuery,
    private val mapper: ObjectMapper,
    private val maxDepth: Int = DEFAULT_MAX_DEPTH,
) {

    companion object {

        const val DEFAULT_MAX_DEPTH = 1

    }

    private val uncheckedRefIds = mutableSetOf<String>()
    private val localPostIdStorage = mutableSetOf<String>()

    private fun updateUncheckedRefIds(): Int {
        if (uncheckedRefIds.isEmpty()) {
            return 0
        }

        val existingRefIds = EntityRepository.postsExistByPlatformIdAndCollection(
            Platform.TWITTER,
            query.collectionName,
            uncheckedRefIds
        )

        uncheckedRefIds.removeIf { it in existingRefIds }
        uncheckedRefIds.removeIf { it in localPostIdStorage }

        return uncheckedRefIds.size
    }

    private fun executeLookupRequest(
        idsToObtain: List<String>
    ): RequestResultWithStatus<TweetLookupResponse> {
        val req = lookupApi.tweetLookup(
            ids = idsToObtain.joinToString(",") { it },
            expansions = TwitterApiOptions.allExpansions,
            tweetFields = TwitterApiOptions.allTweetFields,
            userFields = TwitterApiOptions.allUserFields,
            mediaFields = TwitterApiOptions.allMediaFields,
            pollFields = TwitterApiOptions.allPollFields,
            placeFields = TwitterApiOptions.allPlaceFields
        )

        return executeRequest(req, TweetLookupResponse::class.java, mapper, query)
    }

    // Don't use this object across threads, or synchronize the methods if you do so.
    private fun lookup(minPosts: Int): List<Post> {
        val posts = mutableListOf<Post>()
        var currDepth = 0 // Maximum depth

        while (currDepth < maxDepth && updateUncheckedRefIds() >= minPosts) {
            // We just updated, now pick and remove 100 (MAX_ID) references and fetch them.
            val idsToObtain: List<String> = uncheckedRefIds.take(TwitterLookupApi.MAX_LOOKUP_IDS)
            uncheckedRefIds.removeAll(idsToObtain.toSet())

            val result = executeLookupRequest(idsToObtain)
            val response = result.response

            if (result.status != FetchStatus.SUCCESS || response?.data == null) {
                break // TODO Better handling here.
            }

            // Expand tweets and convert them to posts.
            val expandedTweets: List<ExpandedTweet> = response.data.map { tweet ->
                TwitterConverter.composeExpandedTweet(tweet, response.expansions)
            }

            val newPosts = expandedTweets.map {
                TwitterConverter.getPostFromTweet(it, mapper)
            }

            logger.info { "Obtained ${newPosts.size} tweets with lookup for query ${query.id}." }

            // Add obtained posts to buffer.
            posts.addAll(newPosts)

            // Update references we have to obtain.
            addPosts(newPosts)

            /*
             * Only update depth if we tried to obtain less than 100 IDs.
             * This means we're (recursively) following the references of these tweets, and we only
             * want to do so maxDepth times to avoid following chains of thousands of tweets
             * (which sadly exist).
             * Note that, if we have 100 or more of those very long tweet chains in the unchecked
             * ID list, this means we will follow the references of those tweets until we drop
             * below 100 IDs in the list - so make sure to not let the unchecked ID list too large
             * before trying to fetch the tweets (always checking for size > 100 after adding is
             * a good practice).
             */
            if (idsToObtain.size < TwitterLookupApi.MAX_LOOKUP_IDS) {
                currDepth++
            }
        }

        // Max depth has been reached, clear (and store?) tweets not yet obtained.
        if (currDepth >= maxDepth) {
            localPostIdStorage.addAll(uncheckedRefIds)
            uncheckedRefIds.clear()
        }

        // TODO Depth logging.

        return posts
    }

    fun efficientLookup(): List<Post> {
        return lookup(TwitterLookupApi.MAX_LOOKUP_IDS)
    }

    fun lookupAllRemaining(): List<Post> {
        return lookup(1) // Fetch all remaining post until the list remains empty.
    }

    fun addPost(p: Post) {
        val refs = p.rels.map { it.idTo }.toMutableSet()
        uncheckedRefIds.addAll(refs)
        localPostIdStorage.add(p.platformId)
    }

    fun addPosts(ps: List<Post>) {
        ps.forEach { addPost(it) }
    }

    fun clear() {
        this.uncheckedRefIds.clear()
        this.localPostIdStorage.clear()
    }

}
