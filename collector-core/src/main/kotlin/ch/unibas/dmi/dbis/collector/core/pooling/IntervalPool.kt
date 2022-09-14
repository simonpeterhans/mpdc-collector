package ch.unibas.dmi.dbis.collector.core.pooling

import mu.KotlinLogging
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

private val logger = KotlinLogging.logger {}

abstract class IntervalPool(
    protected val maxTickets: Int,
    protected val terminateOnDeactivate: Boolean
) : GenericPool, Thread() {

    protected val queue = ArrayBlockingQueue<Ticket>(maxTickets, true)

    protected val isTerminated = AtomicBoolean(false) // Initially not terminated.
    protected val isActive = AtomicBoolean(false) // Set to true when the thread starts.

    abstract fun getRefillDelayMs(): Long

    @Synchronized
    override fun isTerminated(): Boolean = isTerminated.get()

    @Synchronized
    override fun isActive(): Boolean = isActive.get()

    @Synchronized
    override fun activate(): Boolean {
        // Terminated, can't activate.
        if (isTerminated()) {
            return false
        }

        // Already active, can't activate.
        if (isActive()) {
            return false
        }

        // Activate.
        isActive.set(true)
        return true
    }

    @Synchronized
    override fun deactivate(): Boolean {
        // Can't deactivate if we have already terminated.
        if (!isTerminated()) {
            isActive.set(false)

            // Initiate quitting immediately if we terminate upon calling this method.
            if (terminateOnDeactivate) {
                setTerminated()
                interrupt()
            }

            return true
        }

        return false
    }

    override fun takeTicket(): Ticket {
        if (isTerminated()) {
            throw PoolTerminatedException()
        }

        return queue.take()
    }

    protected open fun refill() {
        val missingTickets = maxTickets - queue.size

        /*
         * If someone takes a ticket between the calculation of missingTickets and the refilling
         * it's fine since the new "period" basically already started and thus will still result in
         * the correct amount of tickets (i.e., not too many).
         */
        for (i in 0 until missingTickets) {
            // Don't use while(offer(0)) here, waiting threads may take out tickets while filling.
            queue.offer(Ticket.create())
        }
    }

    @Synchronized
    private fun setTerminated() {
        isActive.set(false)
        isTerminated.set(true)
    }

    override fun run() {
        isActive.set(true)

        try {
            while (isActive()) {
                // Refill tickets.
                refill()

                // Sleep until next interval is ready.
                sleep(getRefillDelayMs())
            }
        } catch (e: InterruptedException) {
            logger.debug { "Pool has been interrupted." }
        } finally {
            setTerminated()
        }

        logger.debug { "Terminating ticket pool." }
    }

}
