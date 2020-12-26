package khelp.database.query

import java.sql.ResultSet
import java.util.Calendar
import khelp.database.Column
import khelp.database.RowResultDSL
import khelp.database.Table
import khelp.database.type.DataDate
import khelp.database.type.DataTime
import khelp.database.type.DataType
import khelp.utilities.argumentCheck
import khelp.utilities.extensions.base64
import khelp.utilities.extensions.fullString
import khelp.utilities.extensions.parseToIntArray
import khelp.utilities.extensions.string

/**
 * Presents a row result
 *
 * See documentation for row result DSL syntax
 */
class DataRow internal constructor(private val resultSet: ResultSet, private val select: Select, val table: Table)
{

    /**Number of columns in the answer*/
    val numberOfColumns = this.select.numberColumns

    /**Column index in the answer*/
    fun columnIndex(columnName: String) = this.select.columnIndex(columnName)

    fun columnIndex(column: Column) = this.select.columnIndex(column)

    fun columnRange(columnName: String): Int
    {
        val index = this.columnIndex(columnName)
        return if (index >= 0)
        {
            index + 1
        }
        else
        {
            index
        }
    }

    fun columnRange(column: Column): Int
    {
        val index = this.columnIndex(column)
        return if (index >= 0)
        {
            index + 1
        }
        else
        {
            index
        }
    }

    /**Column at index in the answer*/
    fun column(columnRange: Int) = this.select[columnRange - 1]

    /**
     * Read table row ID from specified column
     */
    @RowResultDSL
    fun getID(column: Column): Int
    {
        column.checkType(DataType.ID)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getInt(index + 1)
    }

    /**
     * Read table row ID from specified column range
     *
     * **Warning** Column range start at **1** not **0**
     */
    @RowResultDSL
    fun getID(columnRange: Int) =
        this.getID(this.select[columnRange - 1])


    /**
     * Read table row ID from specified column
     */
    @RowResultDSL
    fun getString(column: Column): String
    {
        column.checkType(DataType.STRING)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getString(index + 1)
    }

    /**
     * Read table row ID from specified column range
     *
     * **Warning** Column range start at **1** not **0**
     */
    @RowResultDSL
    fun getString(columnRange: Int) =
        this.getString(this.select[columnRange - 1])

    /**
     * Read table row ID from specified column
     */
    @RowResultDSL
    fun getBoolean(column: Column): Boolean
    {
        column.checkType(DataType.BOOLEAN)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getBoolean(index + 1)
    }

    /**
     * Read table row ID from specified column range
     *
     * **Warning** Column range start at **1** not **0**
     */
    @RowResultDSL
    fun getBoolean(columnRange: Int) =
        this.getBoolean(this.select[columnRange - 1])

    /**
     * Read table row ID from specified column
     */
    @RowResultDSL
    fun getByte(column: Column): Byte
    {
        column.checkType(DataType.BYTE)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getByte(index + 1)
    }

    /**
     * Read table row ID from specified column range
     *
     * **Warning** Column range start at **1** not **0**
     */
    @RowResultDSL
    fun getByte(columnRange: Int) =
        this.getByte(this.select[columnRange - 1])

    /**
     * Read table row ID from specified column
     */
    @RowResultDSL
    fun getShort(column: Column): Short
    {
        column.checkType(DataType.SHORT)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getShort(index + 1)
    }

    /**
     * Read table row ID from specified column range
     *
     * **Warning** Column range start at **1** not **0**
     */
    @RowResultDSL
    fun getShort(columnRange: Int) =
        this.getShort(this.select[columnRange - 1])

    /**
     * Read table row ID from specified column
     */
    @RowResultDSL
    fun getInt(column: Column): Int
    {
        column.checkType(DataType.INTEGER)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getInt(index + 1)
    }

    /**
     * Read table row ID from specified column range
     *
     * **Warning** Column range start at **1** not **0**
     */
    @RowResultDSL
    fun getInt(columnRange: Int) =
        this.getInt(this.select[columnRange - 1])

    /**
     * Read table row ID from specified column
     */
    @RowResultDSL
    fun getLong(column: Column): Long
    {
        column.checkType(DataType.LONG)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getLong(index + 1)
    }

    /**
     * Read table row ID from specified column range
     *
     * **Warning** Column range start at **1** not **0**
     */
    @RowResultDSL
    fun getLong(columnRange: Int) =
        this.getLong(this.select[columnRange - 1])

    /**
     * Read table row ID from specified column
     */
    @RowResultDSL
    fun getFloat(column: Column): Float
    {
        column.checkType(DataType.FLOAT)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getDouble(index + 1)
            .toFloat()
    }

    /**
     * Read table row ID from specified column range
     *
     * **Warning** Column range start at **1** not **0**
     */
    @RowResultDSL
    fun getFloat(columnRange: Int) =
        this.getFloat(this.select[columnRange - 1])

    /**
     * Read table row ID from specified column
     */
    @RowResultDSL
    fun getDouble(column: Column): Double
    {
        column.checkType(DataType.DOUBLE)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getDouble(index + 1)
    }

    /**
     * Read table row ID from specified column range
     *
     * **Warning** Column range start at **1** not **0**
     */
    @RowResultDSL
    fun getDouble(columnRange: Int) =
        this.getDouble(this.select[columnRange - 1])

    /**
     * Read table row ID from specified column
     */
    @RowResultDSL
    fun getByteArray(column: Column): ByteArray
    {
        column.checkType(DataType.BYTE_ARRAY)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getString(index + 1).base64
    }

    /**
     * Read table row ID from specified column
     */
    @RowResultDSL
    fun getIntArray(column: Column): IntArray
    {
        column.checkType(DataType.INT_ARRAY)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getString(index + 1).base64.parseToIntArray()
    }

    /**
     * Read table row ID from specified column range
     *
     * **Warning** Column range start at **1** not **0**
     */
    @RowResultDSL
    fun getByteArray(columnRange: Int) =
        this.getByteArray(this.select[columnRange - 1])

    @RowResultDSL
    fun getIntArray(columnRange: Int) =
        this.getIntArray(this.select[columnRange - 1])

    /**
     * Read table row ID from specified column
     */
    @RowResultDSL
    fun getCalendar(column: Column): Calendar
    {
        column.checkType(DataType.CALENDAR)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this.resultSet.getLong(index + 1)
        return calendar
    }

    /**
     * Read table row ID from specified column range
     *
     * **Warning** Column range start at **1** not **0**
     */
    @RowResultDSL
    fun getCalendar(columnRange: Int) =
        this.getCalendar(this.select[columnRange - 1])

    /**
     * Read table row ID from specified column
     */
    @RowResultDSL
    fun getDate(column: Column): DataDate
    {
        column.checkType(DataType.DATE)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return DataDate(this.resultSet.getInt(index + 1))
    }

    /**
     * Read table row ID from specified column range
     *
     * **Warning** Column range start at **1** not **0**
     */
    @RowResultDSL
    fun getDate(columnRange: Int) =
        this.getDate(this.select[columnRange - 1])

    /**
     * Read table row ID from specified column
     */
    @RowResultDSL
    fun getTime(column: Column): DataTime
    {
        column.checkType(DataType.TIME)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return DataTime(this.resultSet.getInt(index + 1))
    }

    /**
     * Read table row ID from specified column range
     *
     * **Warning** Column range start at **1** not **0**
     */
    @RowResultDSL
    fun getTime(columnRange: Int) =
        this.getTime(this.select[columnRange - 1])

    /**
     * Read table row ID from specified column
     */
    @RowResultDSL
    fun <E : Enum<E>> getEnum(column: Column): E
    {
        column.checkType(DataType.ENUM)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        val serialized = this.resultSet.getString(index + 1)
        val indexSeparator = serialized.indexOf(':')
        val className = serialized.substring(0, indexSeparator)
        val enumName = serialized.substring(indexSeparator + 1)
        val valueOf = Class.forName(className)
            .getDeclaredMethod("valueOf", String::class.java)
        @Suppress("UNCHECKED_CAST")
        return valueOf.invoke(null, enumName) as E
    }

    @RowResultDSL
    internal fun getEnumAny(column: Column): Any
    {
        column.checkType(DataType.ENUM)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        val serialized = this.resultSet.getString(index + 1)
        val indexSeparator = serialized.indexOf(':')
        val className = serialized.substring(0, indexSeparator)
        val enumName = serialized.substring(indexSeparator + 1)
        val valueOf = Class.forName(className)
            .getDeclaredMethod("valueOf", String::class.java)
        return valueOf.invoke(null, enumName)
    }

    /**
     * Read table row ID from specified column range
     *
     * **Warning** Column range start at **1** not **0**
     */
    @RowResultDSL
    fun <E : Enum<E>> getEnum(columnRange: Int) =
        this.getEnum<E>(this.select[columnRange - 1])

    /**
     * Read table row ID from specified column and returns a string representation of the value
     *
     * **Warning** Column range start at **1** not **0**
     */
    @RowResultDSL
    fun toString(columnRange: Int) =
        when (this.select[columnRange - 1].type)
        {
            DataType.ID         -> this.resultSet.getInt(columnRange)
                .toString()
            DataType.STRING     -> this.resultSet.getString(columnRange)
            DataType.BOOLEAN    -> this.resultSet.getBoolean(columnRange)
                .toString()
            DataType.BYTE       -> this.resultSet.getByte(columnRange)
                .toString()
            DataType.SHORT      -> this.resultSet.getShort(columnRange)
                .toString()
            DataType.INTEGER    -> this.resultSet.getInt(columnRange)
                .toString()
            DataType.LONG       -> this.resultSet.getLong(columnRange)
                .toString()
            DataType.FLOAT      -> this.resultSet.getDouble(columnRange)
                .toString()
            DataType.DOUBLE     -> this.resultSet.getDouble(columnRange)
                .toString()
            DataType.BYTE_ARRAY -> this.resultSet.getString(columnRange).base64.string()
            DataType.INT_ARRAY  -> this.resultSet.getString(columnRange).base64.parseToIntArray()
                .string()
            DataType.CALENDAR   ->
            {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = this.resultSet.getLong(columnRange)
                calendar.fullString()
            }
            DataType.DATE       -> DataDate(this.resultSet.getInt(columnRange)).toString()
            DataType.TIME       -> DataTime(this.resultSet.getInt(columnRange)).toString()
            DataType.ENUM       -> this.resultSet.getString(columnRange)
        }
}
