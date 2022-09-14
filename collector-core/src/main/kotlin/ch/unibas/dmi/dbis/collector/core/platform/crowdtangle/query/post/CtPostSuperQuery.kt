package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.post

import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.query.QueryData
import ch.unibas.dmi.dbis.collector.core.model.query.SubQueryInterval
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.CtQueryType
import ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.CtSuperQuery

class CtPostSuperQuery(
    queryData: QueryData,
    val postQueryData: CtPostQueryOptionsData,
    override val fetchMultimedia: List<MediaType>,
    override val indexInCineast: Boolean,
    override val subQueries: MutableList<CtPostSubQuery> = mutableListOf(),
    override var id: Long? = null
) : CtSuperQuery(queryData), CtPostQueryOptions by postQueryData {

    override val platformQueryType: CtQueryType = CtQueryType.CT_POST_SEARCH_QUERY

    fun deriveInitialSubQueries(
        apiKey: String
    ): CtPostSuperQuery {
        val intervals = SubQueryInterval.deriveFromInterval(interval)

        intervals.forEach { subInterval ->
            val sub = CtPostSubQuery(
                id,
                apiKey,
                subInterval.temporalType,
                QueryData(this.label, this.collectionName, subInterval.interval),
                postQueryData
            )

            subQueries.add(sub)
        }

        return this
    }

}
