package ch.unibas.dmi.dbis.collector.core.model.query

import ch.unibas.dmi.dbis.collector.core.model.DynamicIdentifiable
import ch.unibas.dmi.dbis.collector.core.model.platform.ApiPlatform
import org.threeten.extra.Interval

// Poor naming, but this only exists so we can use it for delegation in Query.
interface QueryDataInterface {

    val label: String
    val collectionName: String
    val interval: Interval

}

data class QueryData(
    override val label: String,
    override val collectionName: String,
    override val interval: Interval
) : QueryDataInterface

abstract class Query(
    val queryData: QueryData
) : DynamicIdentifiable<Long>, QueryDataInterface by queryData {

    abstract val queryPlatform: ApiPlatform
    abstract val platformQueryType: PlatformQueryType

}
