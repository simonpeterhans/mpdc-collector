package ch.unibas.dmi.dbis.collector.core.dal.storage

abstract class Dto<PK : Comparable<PK>> {

    abstract val key: PK?

}
