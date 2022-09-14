package ch.unibas.dmi.dbis.collector.rest.responses

import ch.unibas.dmi.dbis.collector.core.model.flow.QueryState

data class QueryStateResponse(
    val states: Map<Long, QueryState?>
)
