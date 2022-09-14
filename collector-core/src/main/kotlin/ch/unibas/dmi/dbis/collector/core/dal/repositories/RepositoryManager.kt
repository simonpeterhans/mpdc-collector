package ch.unibas.dmi.dbis.collector.core.dal.repositories

object RepositoryManager : Repository<IRepositoryManager>(), IRepositoryManager {

    override fun <T> tx(statement: () -> T): T {
        return impl.tx(statement)
    }

    override fun setup(dropIfExists: Boolean) {
        return impl.setup(dropIfExists)
    }

    override fun fixBrokenSubQueries() {
        return impl.fixBrokenSubQueries()
    }

}
