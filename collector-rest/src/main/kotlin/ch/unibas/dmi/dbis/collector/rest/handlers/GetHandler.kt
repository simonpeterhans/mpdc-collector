package ch.unibas.dmi.dbis.collector.rest.handlers

import ch.unibas.dmi.dbis.collector.rest.ext.errorResponse
import ch.unibas.dmi.dbis.collector.rest.status.ErrorStatusException
import ch.unibas.dmi.dbis.collector.rest.status.StatusCode
import io.javalin.http.Context

interface GetHandler<T : Any> : RestHandler {

    // TODO Logging here.
    fun get(ctx: Context) {
        try {
            ctx.json(executeGet(ctx))
        } catch (e: ErrorStatusException) {
            ctx.errorResponse(e)
        } catch (e: Exception) {
            ctx.errorResponse(e.message ?: "", StatusCode.INTERNAL_SERVER_ERROR)
        }
    }

    fun executeGet(ctx: Context): T

}
