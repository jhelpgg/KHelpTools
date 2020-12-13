package khelp.database.condition

import java.util.Calendar
import khelp.database.Column
import khelp.database.extensions.checkType
import khelp.database.type.DataDate
import khelp.database.type.DataTime
import khelp.database.type.DataType

infix fun Column.LOWER(string: String): Condition
{
    this.checkType(DataType.STRING)
    return Condition(arrayOf(this), "${this.name}<'$string'")
}

infix fun Column.LOWER(value: Byte): Condition
{
    this.checkType(DataType.BYTE)
    return Condition(arrayOf(this), "${this.name}<$value")
}

infix fun Column.LOWER(value: Short): Condition
{
    this.checkType(DataType.SHORT)
    return Condition(arrayOf(this), "${this.name}<$value")
}

infix fun Column.LOWER(value: Int): Condition
{
    this.checkType(DataType.INTEGER)
    return Condition(arrayOf(this), "${this.name}<$value")
}

infix fun Column.LOWER(value: Long): Condition
{
    this.checkType(DataType.LONG)
    return Condition(arrayOf(this), "${this.name}<$value")
}

infix fun Column.LOWER(value: Float): Condition
{
    this.checkType(DataType.FLOAT)
    return Condition(arrayOf(this), "${this.name}<$value")
}

infix fun Column.LOWER(value: Double): Condition
{
    this.checkType(DataType.DOUBLE)
    return Condition(arrayOf(this), "${this.name}<$value")
}

infix fun Column.LOWER(value: Calendar): Condition
{
    this.checkType(DataType.CALENDAR)
    return Condition(arrayOf(this), "${this.name}<${value.timeInMillis}")
}

infix fun Column.LOWER(value: DataDate): Condition
{
    this.checkType(DataType.DATE)
    return Condition(arrayOf(this), "${this.name}<${value.serialized}")
}

infix fun Column.LOWER(value: DataTime): Condition
{
    this.checkType(DataType.TIME)
    return Condition(arrayOf(this), "${this.name}<${value.serialized}")
}

