package ch.unibas.dmi.dbis.collector.rest.responses.model

import ch.unibas.dmi.dbis.collector.core.model.flow.QueryState
import ch.unibas.dmi.dbis.collector.core.model.flow.StreamStatus
import ch.unibas.dmi.dbis.collector.core.model.misc.SubQueryStats
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.model.query.TemporalType
import org.threeten.extra.Interval

abstract class SubQueryResponse(
    val apiKey: String,
    val temporalType: TemporalType,
    val queryData: QueryData,
    val state: QueryState,
    val stats: SubQueryStats,
    val id: Long
) {

    // Caught by Jackson, don't rename or delete this.
    fun getPercentComplete(): Double {
        if (state.streamStatus == StreamStatus.DONE) {
            return 1.0
        }

        if (state.lastCheckpointTimestamp == null) {
            return 0.0
        }

        val queryInterval = queryData.interval
        val totalIntervalSeconds = queryInterval.toDuration().seconds

        if (totalIntervalSeconds == (0).toLong()) {
            return 1.0
        }

        val completedInterval = when (temporalType) {
            TemporalType.SEARCH -> {
                Interval.of(state.lastCheckpointTimestamp, queryInterval.end)
            }

            TemporalType.STREAM -> {
                Interval.of(queryInterval.start, state.lastCheckpointTimestamp)
            }
        }
        val completedIntervalSeconds = completedInterval.toDuration().seconds

        return completedIntervalSeconds.toDouble() / totalIntervalSeconds
    }

}
