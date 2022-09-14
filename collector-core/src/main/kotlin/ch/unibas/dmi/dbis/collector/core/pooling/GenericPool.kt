package ch.unibas.dmi.dbis.collector.core.pooling

interface GenericPool {

    fun isTerminated(): Boolean

    fun isActive(): Boolean

    fun activate(): Boolean

    fun deactivate(): Boolean

    fun takeTicket(): Ticket

}
