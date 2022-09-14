package ch.unibas.dmi.dbis.collector.core.dal.storage.data

import ch.unibas.dmi.dbis.collector.core.dal.storage.LongDaoRefTable
import org.jetbrains.exposed.sql.ReferenceOption

abstract class PostExtensionTable(
    tableName: String,
) : LongDaoRefTable(tableName) {

    override val refColumn = long("post_id").index().references(
        PostTable.keyColumn,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )

}
