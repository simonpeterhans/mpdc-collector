package ch.unibas.dmi.dbis.collector.rest.status

data class ErrorStatusException(
    val statusMessage: String,
    val statusCode: Int
) : Exception(statusMessage)
