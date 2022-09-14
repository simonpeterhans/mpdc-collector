package ch.unibas.dmi.dbis.collector.core.model.flow

import ch.unibas.dmi.dbis.collector.core.model.data.ModelEntity

class StreamElement<T : ModelEntity>(
    val items: List<T>,
    val state: QueryState
)
