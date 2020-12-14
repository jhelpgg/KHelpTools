package khelp.database.query

import java.util.Calendar
import khelp.database.Column
import khelp.database.Table
import khelp.database.UpdateDSL
import khelp.database.WhereDSL
import khelp.database.condition.Condition
import khelp.database.extensions.checkColumn
import khelp.database.extensions.checkType
import khelp.database.type.DataDate
import khelp.database.type.DataTime
import khelp.database.type.DataType
import khelp.utilities.extensions.base64

class Update internal constructor(val table: Table)
{
    private val columnValues = HashMap<Column, String>()
    private var condition: Condition? = null

    @UpdateDSL
    infix fun Column.IS(value: String)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.STRING)
        this@Update.columnValues[this] = "'$value'"
    }

    @UpdateDSL
    infix fun String.IS(value: String)
    {
        this@Update.table.getColumn(this) IS value
    }

    @UpdateDSL
    infix fun Column.IS(value: Boolean)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.BOOLEAN)
        this@Update.columnValues[this] =
            if (value)
            {
                "TRUE"
            }
            else
            {
                "FALSE"
            }
    }

    @UpdateDSL
    infix fun String.IS(value: Boolean)
    {
        this@Update.table.getColumn(this) IS value
    }

    @UpdateDSL
    infix fun Column.IS(value: Byte)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.BYTE)
        this@Update.columnValues[this] = value.toString()
    }

    @UpdateDSL
    infix fun String.IS(value: Byte)
    {
        this@Update.table.getColumn(this) IS value
    }

    @UpdateDSL
    infix fun Column.IS(value: Short)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.SHORT)
        this@Update.columnValues[this] = value.toString()
    }

    @UpdateDSL
    infix fun String.IS(value: Short)
    {
        this@Update.table.getColumn(this) IS value
    }

    @UpdateDSL
    infix fun Column.IS(value: Int)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.INTEGER)
        this@Update.columnValues[this] = value.toString()
    }

    @UpdateDSL
    infix fun String.IS(value: Int)
    {
        this@Update.table.getColumn(this) IS value
    }

    @UpdateDSL
    infix fun Column.IS(value: Long)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.LONG)
        this@Update.columnValues[this] = value.toString()
    }

    @UpdateDSL
    infix fun String.IS(value: Long)
    {
        this@Update.table.getColumn(this) IS value
    }

    @UpdateDSL
    infix fun Column.IS(value: Float)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.FLOAT)
        this@Update.columnValues[this] = value.toString()
    }

    @UpdateDSL
    infix fun String.IS(value: Float)
    {
        this@Update.table.getColumn(this) IS value
    }

    @UpdateDSL
    infix fun Column.IS(value: Double)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.DOUBLE)
        this@Update.columnValues[this] = value.toString()
    }

    @UpdateDSL
    infix fun String.IS(value: Double)
    {
        this@Update.table.getColumn(this) IS value
    }

    @UpdateDSL
    infix fun Column.IS(value: ByteArray)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.BYTE_ARRAY)
        this@Update.columnValues[this] = "'${value.base64}'"
    }

    @UpdateDSL
    infix fun String.IS(value: ByteArray)
    {
        this@Update.table.getColumn(this) IS value
    }

    @UpdateDSL
    infix fun Column.IS(value: Calendar)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.CALENDAR)
        this@Update.columnValues[this] = value.timeInMillis.toString()
    }

    @UpdateDSL
    infix fun String.IS(value: Calendar)
    {
        this@Update.table.getColumn(this) IS value
    }

    @UpdateDSL
    infix fun Column.IS(value: DataDate)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.DATE)
        this@Update.columnValues[this] = value.serialized.toString()
    }

    @UpdateDSL
    infix fun String.IS(value: DataDate)
    {
        this@Update.table.getColumn(this) IS value
    }

    @UpdateDSL
    infix fun Column.IS(value: DataTime)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.TIME)
        this@Update.columnValues[this] = value.serialized.toString()
    }

    @UpdateDSL
    infix fun String.IS(value: DataTime)
    {
        this@Update.table.getColumn(this) IS value
    }

    @UpdateDSL
    infix fun <E : Enum<E>> Column.IS(value: E)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.ENUM)
        this@Update.columnValues[this] = "'${value::class.java.name}:${value.name}'"
    }

    @UpdateDSL
    infix fun <E : Enum<E>> String.IS(value: E)
    {
        this@Update.table.getColumn(this) IS value
    }

    @WhereDSL
    fun where(whereCreator: Where.() -> Unit)
    {
        val where = Where(this.table)
        whereCreator(where)
        val condition = where.condition ?: throw IllegalStateException("Choose a condition with 'condition ='")
        condition.checkAllColumns(this.table)
        this.condition = condition
    }

    internal fun transfer(columnValues: HashMap<Column, String>)
    {
        for ((column, value) in columnValues)
        {
            this.columnValues[column] = value
        }
    }

    internal fun updateSQL(): String
    {
        if (this.columnValues.isEmpty())
        {
            throw IllegalStateException("Must update at least one thing")
        }

        val query = StringBuilder()
        query.append("UPDATE ")
        query.append(this.table.name)
        query.append(" SET ")
        var notFirst = false

        for ((column, value) in this.columnValues)
        {
            if (notFirst)
            {
                query.append(", ")
            }

            query.append(column.name)
            query.append('=')
            query.append(value)
            notFirst = true
        }

        this.condition?.let { condition ->
            query.append(" WHERE ")
            query.append(condition.sqlCondition)
        }

        return query.toString()
    }
}