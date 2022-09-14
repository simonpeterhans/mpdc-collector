package ch.unibas.dmi.dbis.collector.core.pooling

import java.time.Instant
import java.time.temporal.ChronoUnit

open class SimpleIntervalPool(
    maxTickets: Int,
    private val refillIntervalMs: Long,
    terminateOnDeactivate: Boolean,
) : IntervalPool(maxTickets, terminateOnDeactivate) {

    companion object {

        fun createAndStart(
            maxTickets: Int,
            refillIntervalMs: Long,
            terminateOnDeactivate: Boolean
        ): SimpleIntervalPool {
            val pool = SimpleIntervalPool(
                maxTickets,
                refillIntervalMs,
                terminateOnDeactivate
            )
            pool.start()
            return pool
        }

    }

    override fun getRefillDelayMs(): Long {
        val nextRefill = Instant.now().plusMillis(refillIntervalMs)
        return ChronoUnit.MILLIS.between(Instant.now(), nextRefill)
    }

}
