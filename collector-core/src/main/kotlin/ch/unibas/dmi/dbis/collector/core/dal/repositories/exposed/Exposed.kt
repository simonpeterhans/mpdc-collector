package ch.unibas.dmi.dbis.collector.core.dal.repositories.exposed

import ch.unibas.dmi.dbis.collector.core.dal.repositories.EntityRepository
import ch.unibas.dmi.dbis.collector.core.dal.repositories.MultimediaRepository
import ch.unibas.dmi.dbis.collector.core.dal.repositories.QueryRepository
import ch.unibas.dmi.dbis.collector.core.dal.repositories.RepositoryManager
import org.jetbrains.exposed.sql.Database

object Exposed {

    fun repositorySetup(db: Database) {
        val manager = RepositoryManagerExposed(db)

        RepositoryManager.setRepository(manager)
        QueryRepository.setRepository(QueryRepositoryExposed())
        EntityRepository.setRepository(EntityRepositoryExposed())
        MultimediaRepository.setRepository(MultimediaRepositoryExposed())
    }

}
