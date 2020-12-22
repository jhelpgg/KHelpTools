package khelp.database.condition

import java.util.Calendar
import khelp.database.Column
import khelp.database.type.DataDate
import khelp.database.type.DataTime
import khelp.database.type.DataType
import khelp.utilities.extensions.base64
import khelp.utilities.extensions.serializeToByteArray
import khelp.utilities.extensions.string
import khelp.utilities.extensions.transformArray
import khelp.utilities.extensions.transformInt
import khelp.utilities.extensions.transformLong

/**
 * Create condition that select rows, in given column, wih values are primary key inside given array
 */
infix fun Column.ONE_OF_ID(selection: IntArray): Condition
{
    this.checkType(DataType.ID)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS_ID selection[0]
        else                ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("(", ", ", ")")}")
    }
}

/**
 * Create condition that select rows, in given column, wih values are inside given array
 */
infix fun Column.ONE_OF(selection: Array<String>): Condition
{
    this.checkType(DataType.STRING)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else                ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("('", "', '", "')")}")
    }
}

/**
 * Create condition that select rows, in given column, wih values are inside given array
 */
infix fun Column.ONE_OF(selection: BooleanArray): Condition
{
    this.checkType(DataType.BOOLEAN)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else                ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("(", ", ", ")")}")
    }
}

/**
 * Create condition that select rows, in given column, wih values are inside given array
 */
infix fun Column.ONE_OF(selection: ByteArray): Condition
{
    this.checkType(DataType.BYTE)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else                ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("(", ", ", ")")}")
    }
}

/**
 * Create condition that select rows, in given column, wih values are inside given array
 */
infix fun Column.ONE_OF(selection: ShortArray): Condition
{
    this.checkType(DataType.SHORT)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else                ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("(", ", ", ")")}")
    }
}

/**
 * Create condition that select rows, in given column, wih values are inside given array
 */
infix fun Column.ONE_OF(selection: IntArray): Condition
{
    this.checkType(DataType.INTEGER)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else                ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("(", ", ", ")")}")
    }
}

/**
 * Create condition that select rows, in given column, wih values are inside given array
 */
infix fun Column.ONE_OF(selection: LongArray): Condition
{
    this.checkType(DataType.LONG)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else                ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("(", ", ", ")")}")
    }
}

/**
 * Create condition that select rows, in given column, wih values are inside given array
 */
infix fun Column.ONE_OF(selection: FloatArray): Condition
{
    this.checkType(DataType.FLOAT)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else                ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("(", ", ", ")")}")
    }
}

/**
 * Create condition that select rows, in given column, wih values are inside given array
 */
infix fun Column.ONE_OF(selection: DoubleArray): Condition
{
    this.checkType(DataType.DOUBLE)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else                ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("(", ", ", ")")}")
    }
}

/**
 * Create condition that select rows, in given column, wih values are inside given array
 */
infix fun Column.ONE_OF(selection: Array<ByteArray>): Condition
{
    this.checkType(DataType.BYTE_ARRAY)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else                ->
            Condition(arrayOf(this),
                      "${this.name} IN ${
                          selection.transformArray { array -> array.base64 }
                              .string("('", "', '", "')")
                      }")
    }
}

/**
 * Create condition that select rows, in given column, wih values are inside given array
 */
infix fun Column.ONE_OF(selection: Array<IntArray>): Condition
{
    this.checkType(DataType.INT_ARRAY)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else                ->
            Condition(arrayOf(this),
                      "${this.name} IN ${
                          selection.transformArray { array -> array.serializeToByteArray().base64 }
                              .string("('", "', '", "')")
                      }")
    }
}

/**
 * Create condition that select rows, in given column, wih values are inside given array
 */
infix fun Column.ONE_OF(selection: Array<Calendar>): Condition
{
    this.checkType(DataType.CALENDAR)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else                ->
            Condition(arrayOf(this),
                      "${this.name} IN ${
                          selection.transformLong { array -> array.timeInMillis }
                              .string("(", ", ", ")")
                      }")
    }
}

/**
 * Create condition that select rows, in given column, wih values are inside given array
 */
infix fun Column.ONE_OF(selection: Array<DataDate>): Condition
{
    this.checkType(DataType.DATE)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else                ->
            Condition(arrayOf(this),
                      "${this.name} IN ${
                          selection.transformInt { array -> array.serialized }
                              .string("(", ", ", ")")
                      }")
    }
}

/**
 * Create condition that select rows, in given column, wih values are inside given array
 */
infix fun Column.ONE_OF(selection: Array<DataTime>): Condition
{
    this.checkType(DataType.TIME)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else                ->
            Condition(arrayOf(this),
                      "${this.name} IN ${
                          selection.transformInt { array -> array.serialized }
                              .string("(", ", ", ")")
                      }")
    }
}

/**
 * Create condition that select rows, in given column, wih values are inside given array
 */
infix fun <E : Enum<E>> Column.ONE_OF(selection: Array<E>): Condition
{
    this.checkType(DataType.ENUM)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else                ->
            Condition(arrayOf(this),
                      "${this.name} IN ${
                          selection.transformArray { array -> "${array::class.java.name}:${array.name}" }
                              .string("('", "', '", "')")
                      }")
    }
}
