package ch.unibas.dmi.dbis.collector.core.dal.repositories

import ch.unibas.dmi.dbis.collector.core.model.data.AuthorStatus
import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.data.Post
import ch.unibas.dmi.dbis.collector.core.model.misc.SubQueryStats
import ch.unibas.dmi.dbis.collector.core.model.platform.Platform
import ch.unibas.dmi.dbis.collector.core.model.query.SubQuery

object EntityRepository : Repository<IEntityRepository>(), IEntityRepository {

    override fun getSubQueryStats(id: Long, types: List<MediaType>): SubQueryStats {
        return impl.getSubQueryStats(id, types)
    }

    override fun postsExistByPlatformIdAndCollection(
        platform: Platform,
        collection: String,
        platformIds: Set<String>
    ): MutableSet<String> {
        return impl.postsExistByPlatformIdAndCollection(platform, collection, platformIds)
    }

    override fun insertAuthorStatuses(statuses: List<AuthorStatus>, subQuery: SubQuery) {
        return impl.insertAuthorStatuses(statuses, subQuery)
    }

    override fun insertAuthorStatus(status: AuthorStatus, query: SubQuery) {
        return impl.insertAuthorStatus(status, query)
    }

    override fun insertPosts(posts: List<Post>, subQuery: SubQuery) {
        return impl.insertPosts(posts, subQuery)
    }

    override fun insertPost(post: Post, subQuery: SubQuery) {
        return impl.insertPost(post, subQuery)
    }

}
