package ch.unibas.dmi.dbis.collector.core.dal.repositories

import ch.unibas.dmi.dbis.collector.core.model.flow.QueryState
import ch.unibas.dmi.dbis.collector.core.model.query.SubQuery
import ch.unibas.dmi.dbis.collector.core.model.query.SuperQuery
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.list.CtListSuperQuery
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.post.CtPostSuperQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.sample.TwitterSampleSuperQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet.TwitterTweetSuperQuery
import java.time.Instant

object QueryRepository : Repository<IQueryRepository>(), IQueryRepository {

    override fun insertSubQuery(subQuery: SubQuery) {
        return impl.insertSubQuery(subQuery)
    }

    override fun readSubQuery(subId: Long): SubQuery? {
        return impl.readSubQuery(subId)
    }

    override fun insertSuperQuery(superQuery: SuperQuery) {
        return impl.insertSuperQuery(superQuery)
    }

    override fun readSuperQuery(superId: Long): SuperQuery? {
        return impl.readSuperQuery(superId)
    }

    override fun updateQueryInterval(id: Long, newStart: Instant, newEnd: Instant): Boolean {
        return impl.updateQueryInterval(id, newStart, newEnd)
    }

    override fun readQueryState(subQuery: SubQuery): QueryState? {
        return impl.readQueryState(subQuery)
    }

    override fun writeQueryState(state: QueryState, sq: SubQuery): Long? {
        return impl.writeQueryState(state, sq)
    }

    override fun updateQueryState(state: QueryState): Boolean {
        return impl.updateQueryState(state)
    }

    override fun deleteSubQuery(id: Long): Boolean {
        return impl.deleteSubQuery(id)
    }

    override fun getStatesByQueryIds(ids: List<Long>): Map<Long, QueryState?> {
        return impl.getStatesByQueryIds(ids)
    }

    override fun getCollectionLabels(): List<String> {
        return impl.getCollectionLabels()
    }

    override fun getAllCtListQueries(): List<CtListSuperQuery> {
        return impl.getAllCtListQueries()
    }

    override fun getAllCtPostQueries(): List<CtPostSuperQuery> {
        return impl.getAllCtPostQueries()
    }

    override fun getAllTwitterTweetQueries(): List<TwitterTweetSuperQuery> {
        return impl.getAllTwitterTweetQueries()
    }

    override fun getAllTwitterSampleQueries(): List<TwitterSampleSuperQuery> {
        return impl.getAllTwitterSampleQueries()
    }

}
