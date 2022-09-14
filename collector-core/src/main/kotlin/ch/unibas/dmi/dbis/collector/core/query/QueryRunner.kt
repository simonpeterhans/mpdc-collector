package ch.unibas.dmi.dbis.collector.core.query

import ch.unibas.dmi.dbis.collector.core.client.ApiClient
import ch.unibas.dmi.dbis.collector.core.model.data.ModelEntity
import ch.unibas.dmi.dbis.collector.core.model.flow.ModelStream
import ch.unibas.dmi.dbis.collector.core.model.flow.QueryState
import ch.unibas.dmi.dbis.collector.core.model.flow.StreamElement
import ch.unibas.dmi.dbis.collector.core.model.flow.StreamStatus
import ch.unibas.dmi.dbis.collector.core.model.query.SubQuery
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import retrofit2.Call
import java.util.concurrent.atomic.AtomicBoolean

private val logger = KotlinLogging.logger {}

abstract class QueryRunner : Thread() {

    abstract val query: SubQuery
    abstract val stream: ModelStream<ModelEntity>
    abstract val client: ApiClient

    protected abstract var state: QueryState

    protected val isQueryRunning: AtomicBoolean = AtomicBoolean(true)

    protected open val mapper: ObjectMapper = jacksonObjectMapper().findAndRegisterModules()

    /**
     * @exception InterruptedException Thrown when the query has been interrupted via stopQuery().
     * @exception Exception Thrown when an unknown, unhandled exception occurs.
     */
    /*
     * Make sure to adhere to the semantics described above when overriding this.
     * If it turns out to be impractical, simplify the run method in this class.
     */
    @Throws(InterruptedException::class, Exception::class)
    protected abstract fun executeQuery()

    protected abstract fun waitForQueryStart()

    protected fun updateStreamState(status: StreamStatus, statusText: String = "") {
        state = QueryState(status, statusText, state.lastCheckpointTimestamp)
        stream.put(StreamElement(emptyList(), state))
    }

    protected fun <T> executeApiRequest(
        req: Call<JsonNode>,
        resultClass: Class<T>
    ): RequestResultWithStatus<T> {
        return executeRequest(req, resultClass, mapper, query)
    }

    // Default implementation that interrupts the runner.
    open fun stopQuery() {
        if (isQueryRunning.get()) {
            logger.info { "Interrupting query with ID $query.id}." }
            isQueryRunning.set(false)
            interrupt()
        } else {
            logger.info { "Cannot stop query with ID ${query.id} (already stopped)." }
        }
    }

    override fun run() {
        name = "QR-${query.id}-${query.label}-${query.temporalType}"

        try {
            // Closed upon interrupt.
            client.use {
                logger.info { "Query ${query.id} starting." }

                updateStreamState(StreamStatus.WAITING)

                waitForQueryStart()

                updateStreamState(StreamStatus.STARTING)

                executeQuery()

                // If we made it until here, we're done.
                logger.info { "Successfully executed query with ID ${query.id}." }
            }
        } catch (e: InterruptedException) {
            logger.info { "Query ${query.id} interrupted." }

            /*
             * I'm not a big fan of the interrupt thing, but going without it would require a
             * refactor that changes how queries are deployed (either by a deployer that starts the
             * query when the time is right or by sleeping for short intervals and checking for some
             * Boolean whether the query has been interrupted).
             * Furthermore, it would also require a refactoring of the API client.
             */

            // Clear interrupted flag so we can properly terminate.
            interrupted()

            if (query.platformQueryType.isRecoverable()) {
                updateStreamState(StreamStatus.INTERRUPTED) // No status message here.
            } else {
                updateStreamState(StreamStatus.ABORTED) // No status message here.
            }
        } catch (e: Exception) {
            // These should have been handled by the query runners.
            logger.error {
                "Unhandled exception propagated from QueryRunner implementation" +
                    " for query with ID ${query.id}!"
            }

            e.printStackTrace()

            if (query.platformQueryType.isRecoverable()) {
                updateStreamState(StreamStatus.ERROR_INTERRUPT, "Unknown error.")
            } else {
                updateStreamState(StreamStatus.ERROR_ABORT, "Unknown error.")

            }
        } finally {
            logger.info { "QueryRunner for query with ID ${query.id} terminating." }

            // De-register the query in any case.
            QueryHandler.deregisterSubQuery(query.id!!)
        }

        // We're done.
        isQueryRunning.set(false)
    }

}
