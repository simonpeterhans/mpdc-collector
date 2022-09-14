package ch.unibas.dmi.dbis.collector.core.dal.storage

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

abstract class GenericTable<PK : Comparable<PK>>(tableName: String) : Table(tableName) {

    companion object {

        const val MAX_ENUM_LENGTH = 255

    }

    abstract val keyColumn: Column<PK>
    abstract override val primaryKey: PrimaryKey

}
