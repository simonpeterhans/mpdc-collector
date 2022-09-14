package ch.unibas.dmi.dbis.collector.core.model

interface Identifiable<T : Comparable<T>> {

    val id: T?

}
