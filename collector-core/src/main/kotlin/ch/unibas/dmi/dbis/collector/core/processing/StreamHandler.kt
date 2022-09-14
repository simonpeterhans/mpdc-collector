package ch.unibas.dmi.dbis.collector.core.processing

import ch.unibas.dmi.dbis.collector.core.dal.repositories.EntityRepository
import ch.unibas.dmi.dbis.collector.core.dal.repositories.QueryRepository
import ch.unibas.dmi.dbis.collector.core.dal.repositories.RepositoryManager
import ch.unibas.dmi.dbis.collector.core.model.data.AuthorStatus
import ch.unibas.dmi.dbis.collector.core.model.data.ModelEntity
import ch.unibas.dmi.dbis.collector.core.model.data.Post
import ch.unibas.dmi.dbis.collector.core.model.flow.EntityType
import ch.unibas.dmi.dbis.collector.core.model.flow.ModelStream
import ch.unibas.dmi.dbis.collector.core.model.flow.StreamElement
import ch.unibas.dmi.dbis.collector.core.model.flow.StreamStatus
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class StreamHandler(
    private val stream: ModelStream<ModelEntity>
) : Thread() {

    val query = stream.query

    init {
        name = "SH-${query.id}-${query.label}-${query.temporalType}"
    }

    private fun processStreamElement(msg: StreamElement<ModelEntity>) {
        val posts = msg.items.filter { it.type == EntityType.POST }.map { it as Post }
        val authorStatuses =
            msg.items.filter { it.type == EntityType.AUTHOR_STATE }.map { it as AuthorStatus }

        // TODO Consider passing the items and the state to the repository instead of a tx here.
        RepositoryManager.tx {
            EntityRepository.insertPosts(posts, query)
            EntityRepository.insertAuthorStatuses(authorStatuses, query)

            // Write new sub query state - only do this if the tx hasn't been aborted yet.
            QueryRepository.writeQueryState(msg.state, query)
        }
    }

    private fun processStream() {
        var msg: StreamElement<ModelEntity>

        do {
            // Wait for new stream elements.
            msg = stream.take()

            logger.info { "Persisting state and ${msg.items.size} elements..." }

            try {
                processStreamElement(msg)
            } catch (e: Exception) {
                logger.warn { "Failed to write stream element!" }

                // TODO Proper dump.
                e.printStackTrace()
            }

            logger.info { "Persisted state and ${msg.items.size} elements." }

            // Check whether we're done.
        } while (
            msg.state.streamStatus != StreamStatus.DONE
            && msg.state.streamStatus != StreamStatus.INTERRUPTED
            && msg.state.streamStatus != StreamStatus.ABORTED
            && msg.state.streamStatus != StreamStatus.RECOVERED
            && !this.isInterrupted
        )

        logger.info {
            "Stream for sub query ${stream.query.id!!} ending (status: ${msg.state.streamStatus})."
        }
    }

    override fun run() {
        processStream()
    }

}
