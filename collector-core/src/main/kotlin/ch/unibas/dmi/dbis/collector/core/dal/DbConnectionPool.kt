package ch.unibas.dmi.dbis.collector.core.dal

import ch.unibas.dmi.dbis.collector.core.config.DatabaseConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

class DbConnectionPool private constructor(val pool: HikariDataSource) {

    lateinit var database: Database

    companion object {

        fun fromConfig(conf: DatabaseConfig): DbConnectionPool {
            // Creates a pool of reusable connections rather than a single one-time connection.
            val hikariConf = HikariConfig()

            hikariConf.dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
            hikariConf.maximumPoolSize = 64 // Make this a config parameter if we need more.
            hikariConf.addDataSourceProperty("serverName", conf.url)
            hikariConf.addDataSourceProperty("portNumber", conf.port)
            hikariConf.addDataSourceProperty("user", conf.user)
            hikariConf.addDataSourceProperty("password", conf.pass)
            hikariConf.addDataSourceProperty("databaseName", conf.database)
            hikariConf.addDataSourceProperty("reWriteBatchedInserts", "true")

            // Actually connect.
            val pool = HikariDataSource(hikariConf)

            return DbConnectionPool(pool)
        }

    }

    fun connectToDatabase(): DbConnectionPool {
        database = Database.connect(pool)
        return this
    }

}
