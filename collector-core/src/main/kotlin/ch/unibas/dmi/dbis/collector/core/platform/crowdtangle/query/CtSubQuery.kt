package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query

import ch.unibas.dmi.dbis.collector.core.model.platform.ApiPlatform
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.model.query.SubQuery

abstract class CtSubQuery(
    queryData: QueryData
) : SubQuery(queryData) {

    companion object {

        // TODO Re-adjust for final version (consider adding this to the config).
        const val DEFAULT_DELAY_MS: Long = 1 * 60 * 1_000
        const val DEFAULT_STREAM_INTERVAL_MS: Long = 1 * 60 * 1_000

    }

    abstract override val platformQueryType: CtQueryType

    override val queryPlatform: ApiPlatform = ApiPlatform.CROWDTANGLE

}
