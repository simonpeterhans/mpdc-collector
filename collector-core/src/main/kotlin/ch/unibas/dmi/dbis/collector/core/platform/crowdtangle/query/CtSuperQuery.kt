package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query

import ch.unibas.dmi.dbis.collector.core.model.platform.ApiPlatform
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.model.query.SuperQuery

abstract class CtSuperQuery(
    queryData: QueryData
) : SuperQuery(queryData) {

    abstract override val platformQueryType: CtQueryType
    abstract override val subQueries: List<CtSubQuery>

    override val queryPlatform: ApiPlatform = ApiPlatform.CROWDTANGLE

}
