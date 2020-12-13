package khelp.database.query

import java.util.Calendar
import java.util.TreeSet
import khelp.database.COLUMN_ID
import khelp.database.Column
import khelp.database.InsertDSL
import khelp.database.Table
import khelp.database.condition.Condition
import khelp.database.condition.EQUALS_ID
import khelp.database.exception.NoValueDefinedAndNoDefaultValue
import khelp.database.extensions.checkColumn
import khelp.database.extensions.checkType
import khelp.database.type.DataDate
import khelp.database.type.DataTime
import khelp.database.type.DataType
import khelp.utilities.extensions.base64

class Insert internal constructor(val table: Table)
{
    private val columnValues = HashMap<Column, String>()
    internal var conditionUpdateOneMatch: Condition? = null

    @InsertDSL
    infix fun Column.IS(value: String)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.STRING)
        this@Insert.columnValues[this] = "'$value'"
    }

    @InsertDSL
    infix fun Column.IS(value: Boolean)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.BOOLEAN)
        this@Insert.columnValues[this] =
            if (value)
            {
                "TRUE"
            }
            else
            {
                "FALSE"
            }
    }

    @InsertDSL
    infix fun Column.IS(value: Byte)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.BYTE)
        this@Insert.columnValues[this] = value.toString()
    }

    @InsertDSL
    infix fun Column.IS(value: Short)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.SHORT)
        this@Insert.columnValues[this] = value.toString()
    }

    @InsertDSL
    infix fun Column.IS(value: Int)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.INTEGER)
        this@Insert.columnValues[this] = value.toString()
    }

    @InsertDSL
    infix fun Column.IS(value: Long)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.LONG)
        this@Insert.columnValues[this] = value.toString()
    }

    @InsertDSL
    infix fun Column.IS(value: Float)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.FLOAT)
        this@Insert.columnValues[this] = value.toString()
    }

    @InsertDSL
    infix fun Column.IS(value: Double)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.DOUBLE)
        this@Insert.columnValues[this] = value.toString()
    }

    @InsertDSL
    infix fun Column.IS(value: ByteArray)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.BYTE_ARRAY)
        this@Insert.columnValues[this] = "'${value.base64}'"
    }

    @InsertDSL
    infix fun Column.IS(value: Calendar)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.CALENDAR)
        this@Insert.columnValues[this] = value.timeInMillis.toString()
    }

    @InsertDSL
    infix fun Column.IS(value: DataDate)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.DATE)
        this@Insert.columnValues[this] = value.serialized.toString()
    }

    @InsertDSL
    infix fun Column.IS(value: DataTime)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.TIME)
        this@Insert.columnValues[this] = value.serialized.toString()
    }

    @InsertDSL
    infix fun <E : Enum<E>> Column.IS(value: E)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.ENUM)
        this@Insert.columnValues[this] = "'${value::class.java.name}:${value.name}'"
    }

    @InsertDSL
    fun updateIfExactlyOneRowMatch(condition: Condition)
    {
        condition.checkAllColumns(this.table)
        this.conditionUpdateOneMatch = condition
    }

    internal fun insertSQL(): String
    {
        val columnLeft = TreeSet<Column>()

        for (column in this.table)
        {
            if (column.type != DataType.ID)
            {
                columnLeft.add(column)
            }
        }

        val query = StringBuilder()
        query.append("INSERT INTO ")
        query.append(this.table.name)
        query.append(" (")
        var notFirst = false
        val queryValues = StringBuilder()
        queryValues.append(") VALUES (")

        for ((column, value) in this.columnValues)
        {
            if (notFirst)
            {
                query.append(", ")
                queryValues.append(", ")
            }

            query.append(column.name)
            queryValues.append(value)
            columnLeft.remove(column)
            notFirst = true
        }

        var defaultValue: String

        for (column in columnLeft)
        {
            if (notFirst)
            {
                query.append(", ")
            }

            defaultValue = column.type.defaultValueSerialized

            if (defaultValue.length == 0)
            {
                throw NoValueDefinedAndNoDefaultValue(column)
            }

            query.append(column.name)
            queryValues.append(defaultValue)
            notFirst = true
        }

        query.append(queryValues)
        query.append(")")

        return query.toString()
    }

    internal fun updateSQL(id: Int): String
    {
        val update = Update(this.table)
        update.transfer(this.columnValues)
        update.where(COLUMN_ID EQUALS_ID id)
        return update.updateSQL()
    }
}
