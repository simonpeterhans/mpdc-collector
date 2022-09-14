package ch.unibas.dmi.dbis.collector.core.dal.repositories.exposed

import ch.unibas.dmi.dbis.collector.core.dal.repositories.IEntityRepository
import ch.unibas.dmi.dbis.collector.core.dal.storage.data.*
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.SubQueryDao
import ch.unibas.dmi.dbis.collector.core.model.data.AuthorStatus
import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.data.ModelEntity
import ch.unibas.dmi.dbis.collector.core.model.data.Post
import ch.unibas.dmi.dbis.collector.core.model.misc.SubQueryStats
import ch.unibas.dmi.dbis.collector.core.model.platform.Platform
import ch.unibas.dmi.dbis.collector.core.model.query.SubQuery
import ch.unibas.dmi.dbis.collector.core.query.UnknownSubQueryIdException
import mu.KotlinLogging
import org.jetbrains.exposed.sql.transactions.transaction

private val logger = KotlinLogging.logger {}

/*
 * TODO
 *  Multi-versioning caching to store duplicates that have been obtained within the last n
 *  minutes.
 */

class EntityRepositoryExposed : IEntityRepository {

    override fun getSubQueryStats(id: Long, types: List<MediaType>) = transaction {
        if (!SubQueryDao.existsByKey(id)) {
            throw UnknownSubQueryIdException()
        }

        val numPosts = PostDao.postCountBySubQuery(id)
        val numAuthorStatuses = AuthorStatusDao.authorCountBySubQuery(id)
        val numTotalMediaObjects = MultimediaDao.totalMediaObjectsBySubQuery(id)
        val numProcessedMediaObjects = MultimediaDao.totalProcessedMediaObjectsBySubQuery(id)
        val numUniqueMediaObjects = MultimediaDao.uniqueProcessedMediaObjectsBySubQuery(id)
        val numRemainingMediaObjects = MultimediaDao.remainingMediaObjectsBySubQuery(id, types)

        SubQueryStats(
            numPosts,
            numAuthorStatuses,
            numTotalMediaObjects,
            numProcessedMediaObjects,
            numUniqueMediaObjects,
            numRemainingMediaObjects,
        )
    }

    override fun postsExistByPlatformIdAndCollection(
        platform: Platform,
        collection: String,
        platformIds: Set<String>
    ): MutableSet<String> {
        // Return early if the list is empty.
        if (platformIds.isEmpty()) {
            return mutableSetOf()
        }

        return transaction {
            PostDao.existByPlatformIdsAndCollection(platform, collection, platformIds.toList())
                .toMutableSet()
        }
    }

    private fun insertEntity(entity: ModelEntity, query: SubQuery): Long = ModelEntityDao.insertDto(
        ModelEntityDto(
            query.id!!,
            entity.raw,
            entity.platform,
            entity.platformTimestamp,
            // This is only here for convenience and in case we want manipulable query names later on.
            query.collectionName,
            entity.fetchTimestamp,
            entity.apiString,
            entity.id
        )
    )

    override fun insertAuthorStatuses(statuses: List<AuthorStatus>, subQuery: SubQuery) {
        statuses.forEach {
            insertAuthorStatus(it, subQuery)
        }
    }

    override fun insertAuthorStatus(status: AuthorStatus, query: SubQuery) = transaction {
        val entityRef = insertEntity(status, query)
        val ref = AuthorStatusDao.insertDto(
            AuthorStatusDto(entityRef, status.platformId, status.name)
        )

        status.mos.forEach { mo ->
            MultimediaDao.insertDto(
                MultimediaDto(
                    ref,
                    mo.url,
                    mo.status,
                    mo.statusTimestamp,
                    mo.mediaType,
                    mo.contentType,
                    mo.hash,
                    mo.id
                )
            )
        }
    }

    override fun insertPosts(posts: List<Post>, subQuery: SubQuery) {
        posts.forEach {
            insertPost(it, subQuery)
        }
    }

    override fun insertPost(
        post: Post,
        subQuery: SubQuery
    ) = transaction {
        if (post.authorStatus != null) {
            insertAuthorStatus(post.authorStatus, subQuery)
        }

        val entityRef = insertEntity(post, subQuery)

        post.mos.forEach { mo ->
            MultimediaDao.insertDto(
                MultimediaDto(
                    entityRef,
                    mo.url,
                    mo.status,
                    mo.statusTimestamp,
                    mo.mediaType,
                    mo.contentType,
                    mo.hash,
                    mo.id
                )
            )
        }

        val postRef = PostDao.insertDto(PostDto(entityRef, post.platformId))

        post.rels.forEach { rel ->
            PostRelationDao.insertDto(PostRelationDto(postRef, rel.idFrom, rel.idTo, rel.relType))
        }

        post.urls.forEach { url ->
            PostUrlDao.insertDto(PostUrlDto(postRef, url.url))
        }

        post.texts.forEach { t ->
            PostTextualContentDao.insertDto(
                PostTextualContentDto(
                    postRef,
                    t.title,
                    t.text,
                    t.language
                )
            )
        }
    }

}
