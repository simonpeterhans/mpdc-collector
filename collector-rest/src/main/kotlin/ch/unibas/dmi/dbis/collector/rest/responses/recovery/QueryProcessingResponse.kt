package ch.unibas.dmi.dbis.collector.rest.responses.recovery

data class QueryProcessingResponse(
    val result: Map<Long, String>
)
