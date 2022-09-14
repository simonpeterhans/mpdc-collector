package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.runners

import ch.unibas.dmi.dbis.collector.core.model.data.*
import ch.unibas.dmi.dbis.collector.core.model.platform.Platform
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.api.CtApi
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data.Account
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data.CrowdTanglePost
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data.ExpandedLink
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data.Media
import com.fasterxml.jackson.databind.ObjectMapper
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatterBuilder

object CtConverter {

    data class CtPostConversionResult(
        val posts: List<Post>,
        val oldestPost: Post?,
        val newestPost: Post?
    )

    private val dateFormatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd HH:mm:ss")
        .toFormatter()
        .withZone(ZoneOffset.UTC)

    fun getModelElementsFromPosts(
        ctPosts: List<CrowdTanglePost>,
        mapper: ObjectMapper
    ): CtPostConversionResult {
        val posts = ctPosts.map { extractPost(it, mapper) }

        val oldest = posts.minByOrNull { it.platformTimestamp }
        val newest = posts.maxByOrNull { it.platformTimestamp }

        return CtPostConversionResult(posts, oldest, newest)
    }

    private fun parseCrowdTangleDate(s: String): Instant {
        return dateFormatter.parse(s, Instant::from)
    }

    private fun getPlatformByAccount(acc: Account?): Platform {
        // https://github.com/CrowdTangle/API/wiki/Account
        return if (acc == null) {
            // No account: No way to determine platform (should never happen according to CT).
            Platform.UNKNOWN
        } else if (acc.accountType == null) {
            // No account type: Instagram.
            Platform.INSTAGRAM
        } else {
            // Account type given: Facebook.
            Platform.FACEBOOK
        }
    }

    private fun extractAuthorStatus(
        acc: Account,
        mapper: ObjectMapper,
        fetchTimestamp: Instant = Instant.now()
    ): AuthorStatus {
        // We currently don't store the (tiny) user profile image.
//         val mo = MultimediaObject(
//            url = acc.profileImage,
//            status = ResourceStatus.UNPROCESSED,
//            statusTimestamp = fetchTimestamp,
//            mediaType = MediaType.IMAGE
//        )

        return AuthorStatus(
            platformId = acc.platformId,
            name = acc.name!!,
//            mos = listOf(mo),
            mos = emptyList(),
            raw = mapper.valueToTree(acc),
            platform = getPlatformByAccount(acc), // We don't get a timestamp from CT, so this is the next best thing.
            platformTimestamp = fetchTimestamp,
            fetchTimestamp = fetchTimestamp,
            apiString = CtApi.label()
        )
    }

    private fun replaceCompressedUrls(s: String?, mapping: List<ExpandedLink>?): String? {
        if (s == null || mapping == null) {
            return s
        }

        var newString = s

        // Replace all occurrences in the text.
        for (m in mapping) {
            // newString cannot be null, otherwise we would have returned.
            newString = newString?.replace(m.original, m.expanded)
        }

        return newString
    }

    private fun getPostText(ctp: CrowdTanglePost): List<PostTextualContent> {
        val texts = mutableListOf<PostTextualContent>()

        val content = PostTextualContent(
            title = replaceCompressedUrls(ctp.title, ctp.expandedLinks),
            text = replaceCompressedUrls(ctp.message ?: ctp.description, ctp.expandedLinks),
            language = ctp.languageCode
        )

        texts.add(content)

        return texts
    }

    private fun getPostUrls(ctp: CrowdTanglePost): List<PostUrl> {
        return ctp.expandedLinks?.map { i -> PostUrl(url = i.expanded) }?.toList() ?: emptyList()
    }

    private fun getMediaType(t: Media.Type?): MediaType {
        if (t == null) {
            return MediaType.UNKNOWN
        }

        return when (t) {
            Media.Type.PHOTO -> MediaType.IMAGE
            Media.Type.VIDEO -> MediaType.VIDEO
        }
    }

    private fun getMultimediaEmbeds(ctp: CrowdTanglePost): List<MultimediaObject> {
        val mos = mutableListOf<MultimediaObject>()

        if (ctp.media == null || ctp.media.isEmpty()) {
            return mos
        }

        for (media in ctp.media) {
            val mo = MultimediaObject(
                url = media.url,
                status = ResourceStatus.UNPROCESSED,
                statusTimestamp = Instant.now(),
                mediaType = getMediaType(media.type)
            )

            mos.add(mo)
        }

        return mos
    }

    private fun extractPost(ctp: CrowdTanglePost, mapper: ObjectMapper): Post {
        val now = Instant.now()

        val authorStatus = if (ctp.account != null) {
            extractAuthorStatus(ctp.account, mapper, now)
        } else {
            null
        }

        return Post(
            platformId = ctp.platformId!!,
            authorPlatformId = ctp.account?.platformId,
            authorStatus = authorStatus,
            texts = getPostText(ctp),
            urls = getPostUrls(ctp),
            rels = listOf(),
            mos = getMultimediaEmbeds(ctp),
            raw = mapper.valueToTree(ctp),
            platform = getPlatformByAccount(ctp.account),
            platformTimestamp = parseCrowdTangleDate(ctp.date!!),
            fetchTimestamp = now,
            apiString = CtApi.label()
        )
    }

}
