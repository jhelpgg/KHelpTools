package khelp.database.condition

import java.util.Calendar
import khelp.database.Column
import khelp.database.type.DataDate
import khelp.database.type.DataTime
import khelp.database.type.DataType
import khelp.utilities.extensions.base64

/**
 * Create condition that select rows, in given column, wih values are primary key equals to given parameter
 */
infix fun Column.EQUALS_ID(id: Int): Condition
{
    this.checkType(DataType.ID)
    return Condition(arrayOf(this), "${this.name}=$id")
}

/**
 * Create condition that select rows, in given column, wih values are equals to given parameter
 */
infix fun Column.EQUALS(string: String): Condition
{
    this.checkType(DataType.STRING)
    return Condition(arrayOf(this), "${this.name}='$string'")
}

/**
 * Create condition that select rows, in given column, wih values are equals to given parameter
 */
infix fun Column.EQUALS(value: Boolean): Condition
{
    this.checkType(DataType.BOOLEAN)
    return if (value)
    {
        Condition(arrayOf(this), "${this.name}=TRUE")
    }
    else
    {
        Condition(arrayOf(this), "${this.name}=FALSE")
    }
}

/**
 * Create condition that select rows, in given column, wih values are equals to given parameter
 */
infix fun Column.EQUALS(value: Byte): Condition
{
    this.checkType(DataType.BYTE)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Create condition that select rows, in given column, wih values are equals to given parameter
 */
infix fun Column.EQUALS(value: Short): Condition
{
    this.checkType(DataType.SHORT)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Create condition that select rows, in given column, wih values are equals to given parameter
 */
infix fun Column.EQUALS(value: Int): Condition
{
    this.checkType(DataType.INTEGER)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Create condition that select rows, in given column, wih values are equals to given parameter
 */
infix fun Column.EQUALS(value: Long): Condition
{
    this.checkType(DataType.LONG)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Create condition that select rows, in given column, wih values are equals to given parameter
 */
infix fun Column.EQUALS(value: Float): Condition
{
    this.checkType(DataType.FLOAT)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Create condition that select rows, in given column, wih values are equals to given parameter
 */
infix fun Column.EQUALS(value: Double): Condition
{
    this.checkType(DataType.DOUBLE)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Create condition that select rows, in given column, wih values are equals to given parameter
 */
infix fun Column.EQUALS(value: ByteArray): Condition
{
    this.checkType(DataType.BYTE_ARRAY)
    return Condition(arrayOf(this), "${this.name}='${value.base64}'")
}

/**
 * Create condition that select rows, in given column, wih values are equals to given parameter
 */
infix fun Column.EQUALS(value: Calendar): Condition
{
    this.checkType(DataType.CALENDAR)
    return Condition(arrayOf(this), "${this.name}=${value.timeInMillis}")
}

/**
 * Create condition that select rows, in given column, wih values are equals to given parameter
 */
infix fun Column.EQUALS(value: DataDate): Condition
{
    this.checkType(DataType.DATE)
    return Condition(arrayOf(this), "${this.name}=${value.serialized}")
}

/**
 * Create condition that select rows, in given column, wih values are equals to given parameter
 */
infix fun Column.EQUALS(value: DataTime): Condition
{
    this.checkType(DataType.TIME)
    return Condition(arrayOf(this), "${this.name}=${value.serialized}")
}

/**
 * Create condition that select rows, in given column, wih values are equals to given parameter
 */
infix fun <E : Enum<E>> Column.EQUALS(value: E): Condition
{
    this.checkType(DataType.ENUM)
    return Condition(arrayOf(this), "${this.name}='${value::class.java.name}:${value.name}'")
}

internal infix fun Column.EQUALS_ENUM(value: Any): Condition
{
    this.checkType(DataType.ENUM)
    val valueEnum = value::class.java.getField("name")
        .get(value) as String
    return Condition(arrayOf(this),
                     "${this.name}='${value::class.java.name}:$valueEnum'")
}
