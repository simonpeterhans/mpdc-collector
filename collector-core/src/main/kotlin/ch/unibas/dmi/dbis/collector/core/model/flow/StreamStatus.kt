package ch.unibas.dmi.dbis.collector.core.model.flow

enum class StreamStatus {

    /*
     * Healthy query lifecycle: NEW -> WAITING -> STARTING -> RUNNING -> DONE.
     */
    NEW,
    WAITING,
    STARTING,
    RUNNING,
    DONE,

    /*
     * Interrupts and errors:
     * Manual interrupt: INTERRUPTED / ABORTED (default for unrecoverable queries).
     * Interrupted by an (unknown/unhandled) error: ERROR_INTERRUPT / ERROR_ABORT (default for unrecoverable queries).
     */
    INTERRUPTED,
    ABORTED,
    ERROR_INTERRUPT,
    ERROR_ABORT,

    /*
     * Recovery after crash: RECOVERABLE / UNRECOVERABLE (depending on whether the query is (un)recoverable).
     * For any recovered query (also INTERRUPTED/ERROR_INTERRUPT): RECOVERED
     * TODO Consider replacing RECOVERABLE/UNRECOVERABLE with ERROR_INTERRUPT / ERROR_ABORT after a crash.
     */
    RECOVERABLE,
    UNRECOVERABLE,
    RECOVERED;

}
