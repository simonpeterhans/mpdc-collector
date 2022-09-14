package ch.unibas.dmi.dbis.collector.core.dal.repositories

import ch.unibas.dmi.dbis.collector.core.model.data.AuthorStatus
import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.data.Post
import ch.unibas.dmi.dbis.collector.core.model.misc.SubQueryStats
import ch.unibas.dmi.dbis.collector.core.model.platform.Platform
import ch.unibas.dmi.dbis.collector.core.model.query.SubQuery

interface IEntityRepository {

    fun getSubQueryStats(id: Long, types: List<MediaType>): SubQueryStats

    fun postsExistByPlatformIdAndCollection(
        platform: Platform,
        collection: String,
        platformIds: Set<String>
    ): MutableSet<String>

    fun insertAuthorStatuses(statuses: List<AuthorStatus>, subQuery: SubQuery)

    fun insertAuthorStatus(status: AuthorStatus, query: SubQuery)

    fun insertPosts(posts: List<Post>, subQuery: SubQuery)

    fun insertPost(post: Post, subQuery: SubQuery)

}
