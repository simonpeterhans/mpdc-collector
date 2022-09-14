package ch.unibas.dmi.dbis.collector.core.dal.repositories.exposed

import ch.unibas.dmi.dbis.collector.core.dal.repositories.IRepositoryManager
import ch.unibas.dmi.dbis.collector.core.dal.storage.data.*
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.*
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.ct.CtListQueryDao
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.ct.CtListQueryListDao
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.ct.CtPostQueryDao
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.ct.CtPostQueryKeywordDao
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.twitter.TwitterSampleQueryDao
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.twitter.TwitterTweetQueryAccountDao
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.twitter.TwitterTweetQueryDao
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform.twitter.TwitterTweetQueryKeywordDao
import ch.unibas.dmi.dbis.collector.core.model.flow.StreamStatus
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

// TODO Further abstraction here, only implementation should use stuff from Exposed.

class RepositoryManagerExposed(private val db: Database) : IRepositoryManager {

    private val daos = setOf(
        // The order here matters!
        // Objects.
        // Multimedia.
        MultimediaDao,

        // Author.
        AuthorStatusDao,

        // Post.
        PostTextualContentDao,
        PostRelationDao,
        PostUrlDao,
        PostDao,

        // Model entity.
        ModelEntityDao,

        // Queries.
        // CT post list query.
        CtListQueryListDao,
        CtListQueryDao,

        // CT post query.
        CtPostQueryKeywordDao,
        CtPostQueryDao,

        // Twitter tweet query.
        TwitterTweetQueryKeywordDao,
        TwitterTweetQueryAccountDao,
        TwitterTweetQueryDao,

        // Twitter sample query.
        TwitterSampleQueryDao,

        // Generic query details.
        QueryStateDao,
        SubQueryDao,
        SuperQueryMultimediaDao,
        SuperQueryDao,
        QueryDao
    )

    // Abstract transaction. Might have to expand this depending on the actual needs.
    override fun <T> tx(statement: () -> T): T {
        // Currently implemented by a transaction in Exposed.
        return transaction {
            statement.invoke()
        }
    }

    override fun setup(dropIfExists: Boolean) {
        transaction(db) {
            if (dropIfExists) {
                daos.forEach { it.dropTable() }
            }

            daos.forEach { it.createTable(false) }
            fixBrokenSubQueries()
        }
    }

    override fun fixBrokenSubQueries() {
        // To be run once when we're restarting (potentially block requests until this is done).
        // Get all relevant query states to mark as recoverable.
        QueryStateDao.getAll().forEach {
            if (
                it.streamStatus == StreamStatus.NEW
                || it.streamStatus == StreamStatus.WAITING
                || it.streamStatus == StreamStatus.STARTING
                || it.streamStatus == StreamStatus.RUNNING
            ) {
                val queryDto = SubQueryDao.getQueryDtoBySubQueryId(it.ref!!)!!
                if (queryDto.platformQueryType.isRecoverable()) {
                    QueryStateDao.updateDto(it.copy(streamStatus = StreamStatus.RECOVERABLE))
                } else {
                    QueryStateDao.updateDto(it.copy(streamStatus = StreamStatus.UNRECOVERABLE))
                }
            }
        }
    }

}
