package ch.unibas.dmi.dbis.collector.core.dal.columns

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import java.sql.Timestamp
import java.time.Instant

// Only works for Postgres as of now.
fun Table.pgTimestampWithTimeZone(name: String): Column<Instant> =
    registerColumn(name, TimestampWithTimeZoneColumn())

class TimestampWithTimeZoneColumn : ColumnType() {

    override fun sqlType(): String = "TIMESTAMP WITH TIME ZONE"

    override fun valueFromDB(value: Any): Instant = when (value) {
        is Timestamp -> value.toInstant()
        else -> throw IllegalArgumentException("$value is not a java.sql.Timestamp object.")
    }

    override fun notNullValueToDB(value: Any): Any = when (value) {
        is Instant -> Timestamp.from(value)
        else -> throw IllegalArgumentException("$value is not a java.time.Instant object.")
    }

}
