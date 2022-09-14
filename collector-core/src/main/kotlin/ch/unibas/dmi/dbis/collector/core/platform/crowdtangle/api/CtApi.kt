package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.api

import ch.unibas.dmi.dbis.collector.core.model.api.Api
import ch.unibas.dmi.dbis.collector.core.model.platform.ApiPlatform
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.CtQueryType
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.CtSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.list.CtListSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.post.CtPostSubQuery
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.runners.CtListRunner
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.runners.CtPostRunner
import ch.unibas.dmi.dbis.collector.core.query.QueryRunner
import ch.unibas.dmi.dbis.collector.core.util.Version

object CtApi : Api<CtSubQuery> {

    override val platform: ApiPlatform
        get() = ApiPlatform.CROWDTANGLE

    override val version: Version
        get() = Version(0, 0, 0)

    override fun processSubQuery(
        subQuery: CtSubQuery
    ): QueryRunner {
        return when (subQuery.platformQueryType) {
            CtQueryType.CT_POST_LIST_QUERY -> CtListRunner(subQuery as CtListSubQuery)
            CtQueryType.CT_POST_SEARCH_QUERY -> CtPostRunner(subQuery as CtPostSubQuery)
        }
    }

}
