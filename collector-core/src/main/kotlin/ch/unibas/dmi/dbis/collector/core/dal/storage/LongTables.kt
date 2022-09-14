package ch.unibas.dmi.dbis.collector.core.dal.storage

import org.jetbrains.exposed.sql.Column

abstract class LongDto : Dto<Long>()

abstract class LongDtoWithRef : DtoWithRef<Long, Long>()

abstract class LongDaoTable(tableName: String) : GenericTable<Long>(tableName) {

    final override val keyColumn: Column<Long> = long("id").autoIncrement()
    final override val primaryKey = PrimaryKey(keyColumn)

}

abstract class LongDaoRefTable(tableName: String) : GenericRefTable<Long, Long>(tableName) {

    final override val keyColumn: Column<Long> = long("id").autoIncrement()
    final override val primaryKey = PrimaryKey(keyColumn)

}

abstract class LongTableDao<D : Dto<Long>> : TableDao<Long, D>()

abstract class LongTableDaoWithRef<D : DtoWithRef<Long, Long>> : TableDaoWithRef<Long, Long, D>()
