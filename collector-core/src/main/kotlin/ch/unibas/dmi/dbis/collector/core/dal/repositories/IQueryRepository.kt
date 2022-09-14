package ch.unibas.dmi.dbis.collector.core.dal.repositories

import ch.unibas.dmi.dbis.collector.core.model.flow.QueryState
import ch.unibas.dmi.dbis.collector.core.model.query.SubQuery
import ch.unibas.dmi.dbis.collector.core.model.query.SuperQuery
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.list.CtListSuperQuery
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.post.CtPostSuperQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.sample.TwitterSampleSuperQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.tweet.TwitterTweetSuperQuery
import java.time.Instant

interface IQueryRepository {

    fun insertSubQuery(subQuery: SubQuery)

    fun readSubQuery(subId: Long): SubQuery?

    fun insertSuperQuery(superQuery: SuperQuery)

    fun readSuperQuery(superId: Long): SuperQuery?

    fun updateQueryInterval(id: Long, newStart: Instant, newEnd: Instant): Boolean

    fun readQueryState(subQuery: SubQuery): QueryState?

    fun writeQueryState(state: QueryState, sq: SubQuery): Long?

    fun updateQueryState(state: QueryState): Boolean

    fun deleteSubQuery(id: Long): Boolean

    fun getStatesByQueryIds(ids: List<Long>): Map<Long, QueryState?>

    fun getCollectionLabels(): List<String>

    fun getAllCtListQueries(): List<CtListSuperQuery>

    fun getAllCtPostQueries(): List<CtPostSuperQuery>

    fun getAllTwitterTweetQueries(): List<TwitterTweetSuperQuery>

    fun getAllTwitterSampleQueries(): List<TwitterSampleSuperQuery>

}
