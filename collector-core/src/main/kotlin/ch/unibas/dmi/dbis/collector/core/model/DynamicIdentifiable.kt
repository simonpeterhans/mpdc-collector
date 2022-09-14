package ch.unibas.dmi.dbis.collector.core.model

interface DynamicIdentifiable<T : Comparable<T>> {

    var id: T?

}
