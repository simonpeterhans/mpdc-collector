package ch.unibas.dmi.dbis.collector.core.dal.storage

import org.jetbrains.exposed.sql.Column

abstract class GenericRefTable<PK : Comparable<PK>, FK : Comparable<FK>>(tableName: String) :
    GenericTable<PK>(tableName) {

    abstract val refColumn: Column<FK>

}
