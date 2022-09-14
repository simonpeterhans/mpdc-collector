package ch.unibas.dmi.dbis.collector.core.model.query

import ch.unibas.dmi.dbis.collector.core.model.data.MediaType

abstract class SuperQuery(
    queryData: QueryData
) : Query(queryData) {

    abstract val subQueries: List<SubQuery>
    abstract val fetchMultimedia: List<MediaType>
    abstract val indexInCineast: Boolean

}
