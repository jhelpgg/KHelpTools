package khelp.database.condition

import java.util.Calendar
import khelp.database.Column
import khelp.database.type.DataDate
import khelp.database.type.DataTime
import khelp.database.type.DataType

/**
 * Create condition that select rows, in given column, wih values are upper or equals to given parameter
 */
infix fun Column.UPPER_EQUALS(string: String): Condition
{
    this.checkType(DataType.STRING)
    return Condition(arrayOf(this), "${this.name}>='$string'")
}

/**
 * Create condition that select rows, in given column, wih values are upper or equals to given parameter
 */
infix fun Column.UPPER_EQUALS(value: Byte): Condition
{
    this.checkType(DataType.BYTE)
    return Condition(arrayOf(this), "${this.name}>=$value")
}

/**
 * Create condition that select rows, in given column, wih values are upper or equals to given parameter
 */
infix fun Column.UPPER_EQUALS(value: Short): Condition
{
    this.checkType(DataType.SHORT)
    return Condition(arrayOf(this), "${this.name}>=$value")
}

/**
 * Create condition that select rows, in given column, wih values are upper or equals to given parameter
 */
infix fun Column.UPPER_EQUALS(value: Int): Condition
{
    this.checkType(DataType.INTEGER)
    return Condition(arrayOf(this), "${this.name}>=$value")
}

/**
 * Create condition that select rows, in given column, wih values are upper or equals to given parameter
 */
infix fun Column.UPPER_EQUALS(value: Long): Condition
{
    this.checkType(DataType.LONG)
    return Condition(arrayOf(this), "${this.name}>=$value")
}

/**
 * Create condition that select rows, in given column, wih values are upper or equals to given parameter
 */
infix fun Column.UPPER_EQUALS(value: Float): Condition
{
    this.checkType(DataType.FLOAT)
    return Condition(arrayOf(this), "${this.name}>=$value")
}

/**
 * Create condition that select rows, in given column, wih values are upper or equals to given parameter
 */
infix fun Column.UPPER_EQUALS(value: Double): Condition
{
    this.checkType(DataType.DOUBLE)
    return Condition(arrayOf(this), "${this.name}>=$value")
}

/**
 * Create condition that select rows, in given column, wih values are upper or equals to given parameter
 */
infix fun Column.UPPER_EQUALS(value: Calendar): Condition
{
    this.checkType(DataType.CALENDAR)
    return Condition(arrayOf(this), "${this.name}>=${value.timeInMillis}")
}

/**
 * Create condition that select rows, in given column, wih values are upper or equals to given parameter
 */
infix fun Column.UPPER_EQUALS(value: DataDate): Condition
{
    this.checkType(DataType.DATE)
    return Condition(arrayOf(this), "${this.name}>=${value.serialized}")
}

/**
 * Create condition that select rows, in given column, wih values are upper or equals to given parameter
 */
infix fun Column.UPPER_EQUALS(value: DataTime): Condition
{
    this.checkType(DataType.TIME)
    return Condition(arrayOf(this), "${this.name}>=${value.serialized}")
}

