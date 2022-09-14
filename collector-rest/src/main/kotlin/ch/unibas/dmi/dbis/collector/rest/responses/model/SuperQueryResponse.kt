package ch.unibas.dmi.dbis.collector.rest.responses.model

import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData

abstract class SuperQueryResponse {

    abstract val queryData: QueryData
    abstract val fetchMultimedia: List<MediaType>
    abstract val indexInCineast: Boolean
    abstract val subQueries: List<SubQueryResponse>
    abstract val id: Long

}
