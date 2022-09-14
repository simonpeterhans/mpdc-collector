package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.post

import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.model.query.TemporalType
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.CtQueryType
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.CtSubQuery
import org.threeten.extra.Interval

class CtPostSubQuery(
    override var superId: Long?,
    override val apiKey: String,
    override val temporalType: TemporalType,
    queryData: QueryData,
    val postQueryData: CtPostQueryOptionsData,
    override var id: Long? = null
) : CtSubQuery(queryData), CtPostQueryOptions by postQueryData {

    override val platformQueryType: CtQueryType = CtQueryType.CT_POST_SEARCH_QUERY

    override fun createRecoveryCopy(
        label: String,
        temporalType: TemporalType,
        interval: Interval,
        id: Long?
    ): CtPostSubQuery = CtPostSubQuery(
        this.superId,
        this.apiKey,
        temporalType,
        this.queryData.copy(label = label, interval = interval),
        this.postQueryData,
        id
    )

}
