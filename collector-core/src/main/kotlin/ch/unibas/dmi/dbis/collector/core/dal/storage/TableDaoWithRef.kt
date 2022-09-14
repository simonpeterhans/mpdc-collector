package ch.unibas.dmi.dbis.collector.core.dal.storage

import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

abstract class TableDaoWithRef<PK : Comparable<PK>, FK : Comparable<FK>, D : DtoWithRef<PK, FK>> :
    TableDao<PK, D>(), DaoWithRef<PK, FK, D> {

    abstract override val table: GenericRefTable<PK, FK>

    override fun getByRef(ref: FK): List<D> = table.select {
        table.refColumn eq ref
    }.map {
        it.toDto()
    }.toList()

    override fun findByRef(ref: FK): D = table.select {
        table.refColumn eq ref
    }.limit(1).map {
        it.toDto()
    }.firstOrNull() ?: throw IdNotFoundException(
        "Row with ref $ref in table ${table.tableName} not found (${table.refColumn.name} = $ref)."
    )

    override fun existsByRef(ref: FK): Boolean = table.select {
        table.refColumn eq ref
    }.limit(1).count() > 0

    override fun updateDtoByRef(dto: D): Boolean {
        val key: FK = dto.ref ?: return false
        return table.update({ table.refColumn eq key }) {
            dtoToStatement(it, dto)
        } > 0
    }

    override fun upsertDtoByRef(dto: D): PK? {
        return if (!existsByRef(dto.ref ?: return null)) {
            insertDto(dto)
        } else {
            updateDtoByRef(dto)
            null // Null means nothing has been inserted.
        }
    }

    override fun deleteByRef(ref: FK): Boolean = table.deleteWhere {
        table.refColumn eq ref
    } > 0

    override fun deleteByRefIfExists(ref: FK): Boolean {
        if (existsByRef(ref)) {
            return deleteByRef(ref)
        }

        return false
    }

}
