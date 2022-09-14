package ch.unibas.dmi.dbis.collector.core.query

import ch.unibas.dmi.dbis.collector.core.processing.StreamHandler
import java.util.concurrent.atomic.AtomicBoolean

class QueryProcessor(
    val qr: QueryRunner
) {

    val query = qr.query

    private val sh = StreamHandler(qr.stream)

    private val started = AtomicBoolean(false)
    private val terminated = AtomicBoolean(false)

    fun id() = query.id!!

    @Synchronized
    fun start(): QueryProcessor {
        if (!started.get()) {
            qr.start()
            sh.start()
            started.set(true)
        }
        return this
    }

    @Synchronized
    fun stop() {
        if (!terminated.get()) {
            // Stream handler doesn't need an interrupt, will shut down when told to do so by the QH.
            qr.stopQuery()
            terminated.set(true)
        }
    }

}
