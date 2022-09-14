package ch.unibas.dmi.dbis.collector.core.dal.repositories

abstract class Repository<T : Any> {

    protected lateinit var impl: T

    fun setRepository(impl: T) {
        this.impl = impl
    }

}
