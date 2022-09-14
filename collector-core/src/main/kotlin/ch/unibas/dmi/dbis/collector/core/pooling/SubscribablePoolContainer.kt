package ch.unibas.dmi.dbis.collector.core.pooling

class SubscribablePoolContainer<T : Any>(
    private val pool: GenericPool,
    private val deactivateIfNoSubs: Boolean
) {

    private val subscribers = mutableListOf<T>()

    @Synchronized
    fun isTerminated(): Boolean = pool.isTerminated()

    @Synchronized
    fun subscribe(o: T): Boolean {
        if (isTerminated()) {
            // Deactivated, can't be re-activated.
            return false
        }

        if (!subscribers.contains(o)) {
            // Activate the pool if we have a subscriber.
            pool.activate()

            // Register the subscriber so it is eligible to take tickets.
            subscribers.add(o)
        }

        return true
    }

    @Synchronized
    fun unsubscribe(o: T): Boolean {
        // Always allow unsubscribing.
        val result = subscribers.remove(o)

        if (deactivateIfNoSubs && subscribers.isEmpty()) {
            pool.deactivate()
        }

        return result
    }

    // Override take ticket to make use of the subscriber.
    fun takeTicket(subscriber: T): Ticket {
        if (isTerminated()) {
            throw PoolTerminatedException()
        }

        if (!subscribers.contains(subscriber)) {
            throw UnsubscribedTicketRequestException()
        }

        // This is not synchronized, threads are waiting here until it's their turn for a ticket.
        return pool.takeTicket()
    }

}
