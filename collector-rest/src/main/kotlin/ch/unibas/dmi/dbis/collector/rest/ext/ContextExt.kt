package ch.unibas.dmi.dbis.collector.rest.ext

import ch.unibas.dmi.dbis.collector.rest.responses.ResponseMessage
import ch.unibas.dmi.dbis.collector.rest.status.ErrorStatusException
import io.javalin.http.Context

fun Context.errorResponse(errorMessage: String, statusCode: Int) {
    this.status(statusCode)
    this.json(ResponseMessage(errorMessage))
}

fun Context.errorResponse(error: ErrorStatusException) {
    this.status(error.statusCode)
    this.json(ResponseMessage(error.statusMessage))
}
