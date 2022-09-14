package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.list

import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.model.query.SubQueryInterval
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.CtQueryType
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.CtSuperQuery

class CtListSuperQuery(
    queryData: QueryData,
    val postListQueryData: CtListQueryOptionsData,
    override val fetchMultimedia: List<MediaType>,
    override val indexInCineast: Boolean,
    override val subQueries: MutableList<CtListSubQuery> = mutableListOf(),
    override var id: Long? = null
) : CtSuperQuery(queryData), CtListQueryOptions by postListQueryData {

    override val platformQueryType: CtQueryType = CtQueryType.CT_POST_LIST_QUERY

    fun deriveInitialSubQueries(
        apiKey: String
    ): CtListSuperQuery {
        val intervals = SubQueryInterval.deriveFromInterval(interval)

        intervals.forEach { subInterval ->
            val sub = CtListSubQuery(
                id,
                apiKey,
                subInterval.temporalType,
                QueryData(this.label, this.collectionName, subInterval.interval),
                postListQueryData
            )

            subQueries.add(sub)
        }

        return this
    }

}
