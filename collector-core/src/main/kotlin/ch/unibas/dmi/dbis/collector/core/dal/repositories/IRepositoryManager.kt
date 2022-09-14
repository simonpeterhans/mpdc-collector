package ch.unibas.dmi.dbis.collector.core.dal.repositories

interface IRepositoryManager {

    fun <T> tx(statement: () -> T): T

    fun setup(dropIfExists: Boolean)

    fun fixBrokenSubQueries()

}
