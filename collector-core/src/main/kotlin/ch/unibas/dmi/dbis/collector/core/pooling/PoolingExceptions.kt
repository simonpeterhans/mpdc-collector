package ch.unibas.dmi.dbis.collector.core.pooling

class PoolTerminatedException : Exception("Pool has been terminated!")

class UnsubscribedTicketRequestException :
    Exception("Only subscribers can take tickets from a pool!")
