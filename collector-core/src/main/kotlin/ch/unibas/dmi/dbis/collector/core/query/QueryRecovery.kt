package ch.unibas.dmi.dbis.collector.core.query

import ch.unibas.dmi.dbis.collector.core.model.flow.QueryState
import ch.unibas.dmi.dbis.collector.core.model.flow.StreamStatus
import ch.unibas.dmi.dbis.collector.core.model.query.SubQuery
import ch.unibas.dmi.dbis.collector.core.model.query.TemporalType
import org.threeten.extra.Interval
import java.time.Instant

object QueryRecovery {

    fun isStateEligibleForRecovery(state: QueryState): Boolean {
        return state.streamStatus == StreamStatus.INTERRUPTED // Interrupted manually.
            || state.streamStatus == StreamStatus.ERROR_INTERRUPT // Interrupted by some error.
            || state.streamStatus == StreamStatus.RECOVERABLE // Running query crashed, but is recoverable.
    }

    fun recoverSubQueryByState(sq: SubQuery, state: QueryState) = when (sq.temporalType) {
        TemporalType.SEARCH -> {
            listOf(recoverSearchSubQuery(sq, state))
        }

        TemporalType.STREAM -> {
            recoverStreamSubQuery(sq, state)
        }
    }

    fun recoverSearchSubQuery(sq: SubQuery, state: QueryState) = sq.createRecoveryCopy(
        // Search full range if no checkpoint timestamp is available.
        temporalType = sq.temporalType,
        interval = Interval.of(sq.interval.start, state.lastCheckpointTimestamp ?: sq.interval.end),
        id = null // Reset ID since we're creating a new query.
    )

    fun recoverStreamSubQuery(sq: SubQuery, state: QueryState): List<SubQuery> {
        /*
         * Stream query conditions:
         * 1. Query has not yet begun: Return copy of original query.
         * 2. Query has begun:
         *      a) Query has ended while it was interrupted (to < now): Create search from
         *         checkpoint until end (use from (start) if state == null).
         *      b) Query ends in the future: Create search from query start to now and stream from
         *         now to end (use from (start) if state == null).
         */
        val now = Instant.now()

        // Query has not yet begun: Return copy of original query.
        if (sq.interval.isAfter(now)) {
            // Reset ID since we're creating a new query.
            return listOf(sq.createRecoveryCopy(id = null))
        }

        // Query has begun.
        if (sq.interval.isBefore(now)) {
            /*
             * Query has ended while it was interrupted (to < now): Create search from checkpoint
             * until end (use from if state == null).
             */
            return listOf(
                sq.createRecoveryCopy(
                    temporalType = TemporalType.SEARCH,
                    interval = Interval.of(
                        state.lastCheckpointTimestamp ?: sq.interval.start,
                        sq.interval.end
                    ),
                    id = null
                )
            )
        }

        /*
         * Query ends in the future: Create search from query start to now and stream from now to
         * end (use from if state == null).
         */
        val search = sq.createRecoveryCopy(
            temporalType = TemporalType.SEARCH,
            interval = Interval.of(state.lastCheckpointTimestamp ?: sq.interval.start, now),
            id = null
        )

        val stream = sq.createRecoveryCopy(
            temporalType = TemporalType.STREAM,
            interval = Interval.of(now, sq.interval.end),
            id = null
        )

        return listOf(search, stream)
    }

}
