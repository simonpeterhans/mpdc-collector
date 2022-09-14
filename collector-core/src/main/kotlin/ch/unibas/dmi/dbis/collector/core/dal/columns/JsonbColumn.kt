package ch.unibas.dmi.dbis.collector.core.dal.columns

import com.fasterxml.jackson.databind.ObjectMapper
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.postgresql.util.PGobject

// Only works for Postgres as of now.
fun <T : Any> Table.pgJsonb(name: String, clazz: Class<T>, jsonMapper: ObjectMapper): Column<T> =
    registerColumn(name, JsonbColumnType(clazz, jsonMapper))

class JsonbColumnType<T : Any>(
    private val clazz: Class<T>,
    private val jsonMapper: ObjectMapper
) : ColumnType() {

    override fun sqlType() = "JSONB"

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        val obj = PGobject()
        obj.type = "JSONB"
        obj.value = when (value) {
            null -> null
            else -> value as String
        }

        super.setParameter(stmt, index, obj)
    }

    override fun valueFromDB(value: Any): Any {
        if (value !is PGobject) {
            return value
        }

        return try {
            jsonMapper.readValue(value.value, clazz)
        } catch (e: Exception) {
            throw RuntimeException("Failed to parse JSON string with exception ${e.javaClass} for string: $value")
        }
    }

    override fun notNullValueToDB(value: Any): Any = jsonMapper.writeValueAsString(value)

}
