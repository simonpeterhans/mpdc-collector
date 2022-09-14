package ch.unibas.dmi.dbis.collector.core.pooling

import java.time.temporal.TemporalUnit

class TimedIntervalPoolSubscriptionManager<T : Any>(
    private val maxTickets: Int,
    private val refillUnit: TemporalUnit,
    private val refillsPerUnit: Int,
    private val refillDelayMs: Long,
    private val terminateOnDeactivate: Boolean = false,
    private val terminateIfNoSubs: Boolean = true
) : SubscribablePoolManager<T>() {

    override fun createPool(): SubscribablePoolContainer<T> {
        val pool = TimedIntervalPool.createAndStart(
            maxTickets,
            refillUnit,
            refillsPerUnit,
            refillDelayMs,
            terminateOnDeactivate
        )
        return SubscribablePoolContainer(pool, terminateIfNoSubs)
    }

}
