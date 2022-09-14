package ch.unibas.dmi.dbis.collector.rest.handlers.common

import ch.unibas.dmi.dbis.collector.core.dal.repositories.EntityRepository
import ch.unibas.dmi.dbis.collector.core.dal.repositories.QueryRepository
import ch.unibas.dmi.dbis.collector.core.model.misc.SubQueryStats
import ch.unibas.dmi.dbis.collector.core.model.query.SuperQuery

object QueryInfoBuilding {

    fun buildStateMap(queries: List<SuperQuery>) = QueryRepository.getStatesByQueryIds(
        queries.map { superQuery ->
            superQuery.subQueries.map { subQuery ->
                subQuery.id!!
            }
        }.flatten()
    )

    fun buildStatsMap(queries: List<SuperQuery>): MutableMap<Long, SubQueryStats> {
        val statsMap = mutableMapOf<Long, SubQueryStats>()
        queries.forEach { superQuery ->
            superQuery.subQueries.forEach { subQuery ->
                statsMap[subQuery.id!!] = EntityRepository.getSubQueryStats(
                    subQuery.id!!,
                    superQuery.fetchMultimedia
                )
            }
        }

        return statsMap
    }

}
