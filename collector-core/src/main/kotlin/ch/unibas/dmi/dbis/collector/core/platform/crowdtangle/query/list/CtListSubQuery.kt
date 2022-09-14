package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.list

import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.model.query.TemporalType
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.CtQueryType
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.CtSubQuery
import org.threeten.extra.Interval

class CtListSubQuery(
    override var superId: Long?,
    override val apiKey: String,
    override val temporalType: TemporalType,
    queryData: QueryData,
    val postListQueryData: CtListQueryOptionsData,
    override var id: Long? = null
) : CtSubQuery(queryData), CtListQueryOptions by postListQueryData {

    override val platformQueryType: CtQueryType = CtQueryType.CT_POST_LIST_QUERY

    override fun createRecoveryCopy(
        label: String,
        temporalType: TemporalType,
        interval: Interval,
        id: Long?
    ): CtListSubQuery = CtListSubQuery(
        this.superId,
        this.apiKey,
        temporalType,
        this.queryData.copy(label = label, interval = interval),
        this.postListQueryData,
        id
    )

}
