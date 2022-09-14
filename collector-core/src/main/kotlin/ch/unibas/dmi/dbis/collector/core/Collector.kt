package ch.unibas.dmi.dbis.collector.core

import ch.unibas.dmi.dbis.collector.core.config.CineastConfig
import ch.unibas.dmi.dbis.collector.core.config.DatabaseConfig
import ch.unibas.dmi.dbis.collector.core.dal.DbConnectionPool
import ch.unibas.dmi.dbis.collector.core.dal.repositories.RepositoryManager
import ch.unibas.dmi.dbis.collector.core.dal.repositories.exposed.Exposed
import ch.unibas.dmi.dbis.collector.core.processing.MultimediaHandler
import java.nio.file.Path

object Collector {

    var running = false

    val dataPathPrefix = Path.of("data")

    fun deploy(config: DatabaseConfig, cineastConfig: CineastConfig) {
        if (running) {
            return
        }

        // Connect to the Postgres database.
        val conn = DbConnectionPool.fromConfig(config).connectToDatabase()

        // Set repositories.
        Exposed.repositorySetup(conn.database)

        // Initialize tables if not existing.
        RepositoryManager.setup(false)

        // Deploy threads handling multimedia download.
        MultimediaHandler.deployPlatformHandlers(cineastConfig)

        running = true
    }

}
