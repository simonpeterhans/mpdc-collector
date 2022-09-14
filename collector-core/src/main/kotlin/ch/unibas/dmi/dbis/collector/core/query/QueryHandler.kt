package ch.unibas.dmi.dbis.collector.core.query

import ch.unibas.dmi.dbis.collector.core.dal.repositories.QueryRepository
import ch.unibas.dmi.dbis.collector.core.dal.repositories.RepositoryManager
import ch.unibas.dmi.dbis.collector.core.model.flow.QueryState
import ch.unibas.dmi.dbis.collector.core.model.flow.StreamStatus
import ch.unibas.dmi.dbis.collector.core.model.platform.ApiPlatform
import ch.unibas.dmi.dbis.collector.core.model.query.SubQuery
import ch.unibas.dmi.dbis.collector.core.model.query.SuperQuery
import ch.unibas.dmi.dbis.collector.core.model.query.TemporalType
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.api.CtApi
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.CtSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.twitter.api.TwitterApi
import ch.unibas.dmi.dbis.collector.core.platform.twitter.query.TwitterSubQuery
import mu.KotlinLogging
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

private val logger = KotlinLogging.logger {}

object QueryHandler {

    private val activeQueries = ConcurrentHashMap<Long, QueryProcessor>()

    @Synchronized
    fun deregisterSubQuery(id: Long) {
        logger.info { "Removing query with ID $id from active queries." }

        val res = activeQueries.remove(id)

        if (res == null) {
            logger.error {
                "Failed to remove query with ID $id from active queries, query is not active."
            }
        } else {
            logger.info { "Removed query with ID $id from active queries." }
        }
    }

    @Synchronized
    fun interruptSubQuery(id: Long) {
        val q = activeQueries[id] ?: throw NoRunningSubQueryWithIdException()
        logger.info { "Attempting to interrupt query with ID $id." }

        // This is already called from the query runners (upon success and failure).
        // activeQueries.remove(id)

        // Stop query processor (runner & handler).
        q.stop()

        logger.info { "Interrupted query with ID $id." }
    }

    @Synchronized
    fun processNewSuperQuery(q: SuperQuery) {
        // Requirements: 1. Query cannot have an ID, 2. Query must have at least 1 sub query.
        if (q.id != null) throw NewQueryHasIdException()
        if (q.subQueries.isEmpty()) throw SuperQueryWithoutSubQueryException()

        // New/unknown super query, insert super query and its sub queries.
        QueryRepository.insertSuperQuery(q)

        // If we crash here, we can still recover the newly inserted super query.

        // Process all sub queries.
        deploySubQueries(q.subQueries)
    }

    @Synchronized
    fun recoverAndDeploySubQuery(id: Long) {
        val subQuery = QueryRepository.readSubQuery(id) ?: throw UnknownSubQueryIdException()
        recoverAndDeploySubQuery(subQuery)
    }

    @Synchronized
    fun recoverAndDeploySubQuery(subQuery: SubQuery) {
        val recoveredQueries = recoverSubQuery(subQuery)
        deploySubQueries(recoveredQueries)
    }

    private fun deploySubQueries(subQueries: List<SubQuery>) {
        subQueries.forEach {
            deploySubQuery(it)
        }
    }

    private fun deploySubQuery(sq: SubQuery) {
        val id = sq.id ?: throw QueryNotPersistedException()

        if (activeQueries.contains(id)) {
            throw SubQueryAlreadyRunningException()
        }

        val qr = buildQueryRunner(sq)
        val qp = QueryProcessor(qr).start()

        activeQueries[id] = qp
    }

    private fun buildQueryRunner(sq: SubQuery) = when (sq.queryPlatform) {
        ApiPlatform.TWITTER -> TwitterApi.processSubQuery(sq as TwitterSubQuery)
        ApiPlatform.REDDIT -> TODO()
        ApiPlatform.CROWDTANGLE -> CtApi.processSubQuery(sq as CtSubQuery)
    }

    private fun recoverSubQuery(subQuery: SubQuery): List<SubQuery> {
        // This is the actual recovery function with side effects.
        return RepositoryManager.tx {
            /*
             * 1. Load the query state and check for eligibility.
             * If we get a query state, the query was persisted and has an ID (and a super ID).
             */
            val state = QueryRepository.readQueryState(subQuery)
                ?: throw QueryNotPersistedException()

            if (!QueryRecovery.isStateEligibleForRecovery(state)) {
                return@tx emptyList()
            }

            // 2. Do query recovery and obtain new sub query/sub queries.
            val recovered = QueryRecovery.recoverSubQueryByState(subQuery, state)

            // 3. Persist new sub queries; superId was copied in the process.
            recovered.forEach { rq ->
                // TODO Also insert new state here instead in repository.
                QueryRepository.insertSubQuery(rq)
            }

            /*
             * 4. Delete sub query if no checkpoint exists (no data tracked),
             *    or update the query state and sub query interval if we already have a checkpoint.
             */
            if (state.lastCheckpointTimestamp == null) {
                QueryRepository.deleteSubQuery(subQuery.id!!)
            } else {
                // We already obtained data, update state and query interval.
                QueryRepository.updateQueryState(
                    QueryState(
                        StreamStatus.RECOVERED, // Not to be touched/modified again.
                        state.statusText, // TODO Consider clearing the message here.
                        state.lastCheckpointTimestamp,
                        Instant.now(),
                        state.id
                    )
                )

                // Update query timestamp based on recovery timestamp.
                when (subQuery.temporalType) {
                    TemporalType.SEARCH -> QueryRepository.updateQueryInterval(
                        subQuery.id!!,
                        state.lastCheckpointTimestamp,
                        subQuery.interval.end
                    )

                    TemporalType.STREAM -> QueryRepository.updateQueryInterval(
                        subQuery.id!!,
                        subQuery.interval.start,
                        state.lastCheckpointTimestamp
                    )
                }
            }

            recovered
        }
    }

}
