package ch.unibas.dmi.dbis.collector.core.pooling

class SimpleIntervalPoolSubscriptionManager<T : Any>(
    private val maxTickets: Int,
    private val refillIntervalMs: Long,
    private val terminateOnDeactivate: Boolean = false,
    private val terminateIfNoSubs: Boolean = true
) : SubscribablePoolManager<T>() {

    override fun createPool(): SubscribablePoolContainer<T> {
        val pool = SimpleIntervalPool.createAndStart(
            maxTickets,
            refillIntervalMs,
            terminateOnDeactivate
        )
        return SubscribablePoolContainer(pool, terminateIfNoSubs)
    }

}
