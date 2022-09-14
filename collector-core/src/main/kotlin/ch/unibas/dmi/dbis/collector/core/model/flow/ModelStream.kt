package ch.unibas.dmi.dbis.collector.core.model.flow

import ch.unibas.dmi.dbis.collector.core.model.data.ModelEntity
import ch.unibas.dmi.dbis.collector.core.model.query.SubQuery
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

class ModelStream<T : ModelEntity>(
    val query: SubQuery,
    private val queue: BlockingQueue<StreamElement<T>> = ArrayBlockingQueue(DEFAULT_CAPACITY)
) : BlockingQueue<StreamElement<T>> by queue {

    companion object {

        // Don't allow more than 1'000 elements in the queue by default.
        const val DEFAULT_CAPACITY = 1_000

    }

}
