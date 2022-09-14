package ch.unibas.dmi.dbis.collector.core.platform.twitter.query

import ch.unibas.dmi.dbis.collector.core.model.platform.ApiPlatform
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.model.query.SuperQuery

abstract class TwitterSuperQuery(
    queryData: QueryData
) : SuperQuery(queryData) {

    abstract override val platformQueryType: TwitterQueryType
    abstract override val subQueries: List<TwitterSubQuery>

    override val queryPlatform: ApiPlatform = ApiPlatform.TWITTER

}
