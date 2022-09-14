package ch.unibas.dmi.dbis.collector.core.util

import java.io.Closeable

inline fun <T : Closeable?, R> Array<T>.use(block: (Array<T>) -> R): R {
    var exception: Throwable? = null

    try {
        return block(this)
    } catch (e: Throwable) {
        exception = e
        throw e
    } finally {
        when (exception) {
            null -> forEach { it?.close() }
            else -> forEach {
                try {
                    it?.close()
                } catch (closeException: Throwable) {
                    exception.addSuppressed(closeException)
                }
            }
        }
    }
}
