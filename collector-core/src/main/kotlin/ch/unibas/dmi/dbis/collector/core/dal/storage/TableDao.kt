package ch.unibas.dmi.dbis.collector.core.dal.storage

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder

abstract class TableDao<PK : Comparable<PK>, D : Dto<PK>> : Dao<PK, D> {

    protected abstract val table: GenericTable<PK>

    abstract fun rowToDto(row: ResultRow): D

    abstract fun dtoToStatement(it: UpdateBuilder<Int>, dto: D)

    fun ResultRow.toDto() = rowToDto(this)

    fun dropTable() {
        SchemaUtils.drop(table)
    }

    fun createTable(dropIfExists: Boolean) {
        if (dropIfExists) {
            SchemaUtils.drop(table)
        }

        SchemaUtils.create(table)
    }

    override fun getAll(): List<D> = table.selectAll().map { it.toDto() }

    override fun findByKey(key: PK): D? = table.select {
        table.keyColumn eq key
    }.limit(1).map {
        it.toDto()
    }.firstOrNull()

    override fun getByKey(key: PK): D = findByKey(key) ?: throw IdNotFoundException(
        "Row with ID $key in table ${table.tableName} not found (${table.keyColumn.name} = $key)."
    )

    override fun existsByKey(key: PK): Boolean = table.select {
        table.keyColumn eq key
    }.limit(1).count() > 0

    override fun insertDto(dto: D): PK = table.insert {
        if (dto.key != null) {
            it[table.keyColumn] = dto.key!!
        }
        dtoToStatement(it, dto)
    }[table.keyColumn]

    override fun updateDto(dto: D): Boolean {
        val key: PK = dto.key ?: return false
        return table.update({ table.keyColumn eq key }) {
            dtoToStatement(it, dto)
        } > 0
    }

    override fun upsertDto(dto: D): PK? {
        return if (!existsByKey(dto.key ?: return null)) {
            insertDto(dto)
        } else {
            updateDto(dto)
            null // Null means nothing has been inserted.
        }
    }

    override fun deleteByKey(key: PK) = table.deleteWhere {
        table.keyColumn eq key
    } > 0

    override fun deleteByKeyIfExists(key: PK): Boolean {
        if (existsByKey(key)) {
            return deleteByKey(key)
        }

        return false
    }

}
