package ch.unibas.dmi.dbis.collector.core.model.flow

import ch.unibas.dmi.dbis.collector.core.model.Identifiable
import java.time.Instant

data class QueryState(
    val streamStatus: StreamStatus = StreamStatus.NEW,
    val statusText: String = "",
    val lastCheckpointTimestamp: Instant? = null,
    val lastProcessingTimestamp: Instant = Instant.now(),
    override val id: Long? = null
) : Identifiable<Long>
