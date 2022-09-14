package ch.unibas.dmi.dbis.collector.core.platform.twitter.runners

import ch.unibas.dmi.dbis.collector.core.model.data.*
import ch.unibas.dmi.dbis.collector.core.model.platform.Platform
import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.TwitterApi
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.ExpandedTweet
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions.*
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet.Tweet
import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet.TweetReferencedTweets
import com.fasterxml.jackson.databind.ObjectMapper
import java.time.Instant

object TwitterConverter {

    data class TwitterPostConversionResult(
        val posts: List<Post>,
        val oldestPost: Post?,
        val newestPost: Post?
    )

    fun getPostsFromRaw(
        tweets: List<Tweet>,
        expansions: Expansions?,
        mapper: ObjectMapper
    ): TwitterPostConversionResult {
        val expandedTweets: List<ExpandedTweet> = tweets.map {
            composeExpandedTweet(it, expansions)
        }

        val posts = expandedTweets.map {
            getPostFromTweet(it, mapper)
        }

        // Ignore age of expanded posts.
        val oldest = posts.minByOrNull { it.platformTimestamp }
        val newest = posts.maxByOrNull { it.platformTimestamp }

        return TwitterPostConversionResult(
            posts,
            oldest,
            newest
        )
    }

    fun composeExpandedTweet(tweet: Tweet, expansions: Expansions? = null): ExpandedTweet {
        if (expansions == null) {
            return ExpandedTweet(tweet)
        }

        // Users.
        val users = expansions.users?.filter { it.id == tweet.authorId }

        // Multimedia.
        val media = tweet.attachments?.mediaKeys?.let { keys ->
            expansions.media?.filter { media ->
                keys.contains(media.mediaKey)
            }
        }

        // Places.
        val places = expansions.places?.filter { it.id == tweet.geo?.placeId }

        // Polls.
        val polls = tweet.attachments?.pollIds?.let { keys ->
            expansions.polls?.filter { poll ->
                keys.contains(poll.id)
            }
        }

        return ExpandedTweet(
            tweet,
            users ?: emptyList(),
            places ?: emptyList(),
            media ?: emptyList(),
            polls ?: emptyList()
        )
    }

    fun getPostFromTweet(
        et: ExpandedTweet,
        mapper: ObjectMapper,
    ): Post {
        val now = Instant.now()

        val tweet = et.tweet

        // Extract user from expansion.
        val user = et.users.firstOrNull { it.id == et.tweet.authorId!! }

        // Get author if we have a User object.
        val author = if (user == null) {
            null
        } else {
            getAuthorFromUser(user, mapper, now)
        }

        return Post(
            tweet.id,
            tweet.authorId,
            author,
            getTexts(et),
            getUrls(et),
            getRelations(et),
            getMultimediaObjects(et),
            mapper.valueToTree(et),
            Platform.TWITTER,
            tweet.createdAt!!,
            now,
            TwitterApi.label()
        )
    }

    // This function name is actually correct, we're getting the author from the tweet.
    private fun getAuthorFromUser(
        author: User,
        mapper: ObjectMapper,
        fetchTimestamp: Instant = Instant.now()
    ): AuthorStatus {
        // We currently don't store the user profile image.
//        val mo = MultimediaObject(
//            url = author.profileImageUrl,
//            status = ResourceStatus.UNPROCESSED,
//            statusTimestamp = fetchTimestamp,
//            mediaType = MediaType.IMAGE
//        )

        return AuthorStatus(
            author.id,
            author.name,
//            listOf(mo),
            emptyList(),
            mapper.valueToTree(author),
            Platform.TWITTER,
            fetchTimestamp,
            fetchTimestamp,
            TwitterApi.label()
        )
    }

    private fun getTexts(et: ExpandedTweet): List<PostTextualContent> {
        val tweet = et.tweet
        val texts = mutableListOf<PostTextualContent>()

        // Regular tweet text.
        texts.add(PostTextualContent(null, tweet.text, tweet.lang))

        return texts
    }

    private fun getUrls(et: ExpandedTweet): List<PostUrl> {
        val tweet = et.tweet
        val urls = mutableListOf<PostUrl>()

        tweet.entities?.urls?.forEach {
            if (it.expandedUrl != null) {
                PostUrl(it.expandedUrl)
            }
        }

        return urls
    }

    private fun getRelations(et: ExpandedTweet): List<PostRelation> {
        val tweet = et.tweet

        return tweet.referencedTweets?.map {
            val type = when (it.type) {
                TweetReferencedTweets.Type.RETWEETED -> RelationType.QUOTE // Retweet without own text.
                TweetReferencedTweets.Type.QUOTED -> RelationType.QUOTE_COMMENT // Quoted retweet with own comment.
                TweetReferencedTweets.Type.REPLIED_TO -> RelationType.COMMENT // Reply in discussion.
            }
            PostRelation(tweet.id, it.id, type)
        } ?: emptyList()
    }

    private fun getMultimediaFromGif(g: AnimatedGif): MultimediaObject {
        val bestVariant = g.variants?.maxBy { it.bitrate }

        return MultimediaObject(
            bestVariant?.url.toString(),
            ResourceStatus.UNPROCESSED,
            Instant.now(),
            MediaType.ANIMATED_IMAGE
        )
    }

    private fun getMultimediaFromPhoto(p: Photo): MultimediaObject {
        return MultimediaObject(
            p.url.toString(),
            ResourceStatus.UNPROCESSED,
            Instant.now(),
            MediaType.IMAGE
        )
    }

    private fun getMultimediaFromVideo(v: Video): MultimediaObject {
        val bestVariant = v.variants?.maxBy { it.bitrate }

        return MultimediaObject(
            bestVariant?.url.toString(),
            ResourceStatus.UNPROCESSED,
            Instant.now(),
            MediaType.VIDEO
        )
    }

    private fun getMultimediaObjects(et: ExpandedTweet): List<MultimediaObject> {
        val mos = mutableListOf<MultimediaObject>()

        // TODO Might have to extend multimedia data object with a label so we can associate properly.
        et.media.forEach {
            val mo = when (it.type) {
                Media.Type.ANIMATED_GIF -> getMultimediaFromGif(it as AnimatedGif)
                Media.Type.PHOTO -> getMultimediaFromPhoto(it as Photo)
                Media.Type.VIDEO -> getMultimediaFromVideo(it as Video)
                Media.Type.UNKNOWN -> null
            }
            if (mo != null) {
                mos.add(mo)
            }
        }

        return mos
    }

}
