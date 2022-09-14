@file:Suppress("RemoveRedundantQualifierName")

package ch.unibas.dmi.dbis.collector.core.dal.storage.query.platform

import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDtoWithRef
import ch.unibas.dmi.dbis.collector.core.dal.storage.LongTableDaoWithRef
import ch.unibas.dmi.dbis.collector.core.dal.storage.query.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.selectAll

abstract class PlatformSubQueryDto {

    abstract val queryDto: QueryDto
    abstract val subQueryDto: SubQueryDto

}

abstract class PlatformSuperQueryDto {

    abstract val queryDto: QueryDto
    abstract val superQueryDto: SuperQueryDto
    abstract val subQueryDtos: List<PlatformSubQueryDto>

}

abstract class PlatformQueryDao<D : LongDtoWithRef, Sub : PlatformSubQueryDto, Super : PlatformSuperQueryDto>
    : LongTableDaoWithRef<D>() {

    abstract fun rowToSubQueryDto(row: ResultRow): Sub

    abstract fun rowToSuperQueryDto(subQueries: List<Sub>, row: ResultRow): Super

    fun getAllSubQueries(): List<Sub> = table.innerJoin(
        QueryTable,
        { table.refColumn },
        { QueryTable.keyColumn }
    ).innerJoin(
        SubQueryTable,
        { QueryTable.keyColumn },
        { SubQueryTable.refColumn }
    ).selectAll().map {
        rowToSubQueryDto(it)
    }

    fun getSubQueriesBySuperId(superId: Long): List<Sub> = table.innerJoin(
        QueryTable,
        { table.refColumn },
        { QueryTable.keyColumn }
    ).innerJoin(
        SubQueryTable,
        { QueryTable.keyColumn },
        { SubQueryTable.refColumn },
        { SubQueryTable.superId eq superId }
    ).selectAll().map {
        rowToSubQueryDto(it)
    }

    fun getSubQueriesByIds(vararg subIds: Long): List<Sub> {
        if (subIds.isEmpty()) {
            return getAllSubQueries()
        }

        return table.innerJoin(
            QueryTable,
            { table.refColumn },
            { QueryTable.keyColumn }
        ).innerJoin(
            SubQueryTable,
            { QueryTable.keyColumn },
            { SubQueryTable.refColumn },
            { SubQueryTable.keyColumn inList subIds.toList() }
        ).selectAll().map {
            rowToSubQueryDto(it)
        }
    }

    fun getSubQueryById(subId: Long): Sub? {
        val queryList = getSubQueriesByIds(subId)
        return if (queryList.isEmpty()) {
            null
        } else {
            queryList.first()
        }
    }

    fun getAllSuperQueries(): List<Super> {
        // Collect all sub queries, then build sub queries based on the results.
        val subQueries = getAllSubQueries()

        // Get unique IDs.
        val superQueryIds = subQueries.map { it.subQueryDto.superId }.distinct()

        return table.innerJoin(
            QueryTable,
            { table.refColumn },
            { QueryTable.keyColumn }
        ).innerJoin(
            SuperQueryTable,
            { QueryTable.keyColumn },
            { SuperQueryTable.refColumn },
            { SuperQueryTable.keyColumn inList superQueryIds }
        ).selectAll().map {
            rowToSuperQueryDto(subQueries, it)
        }
    }

    fun getSuperQueryById(superId: Long): Super? {
        val subQueries = getSubQueriesBySuperId(superId)

        if (subQueries.isEmpty()) {
            return null
        }

        return table.innerJoin(
            QueryTable,
            { table.refColumn },
            { QueryTable.keyColumn }
        ).innerJoin(
            SuperQueryTable,
            { QueryTable.keyColumn },
            { SuperQueryTable.refColumn },
            { SuperQueryTable.keyColumn eq superId }
        ).selectAll().map {
            rowToSuperQueryDto(subQueries, it)
        }.first()
    }

}
