package khelp.database.condition

import java.util.Calendar
import khelp.database.Column
import khelp.database.extensions.checkType
import khelp.database.type.DataDate
import khelp.database.type.DataTime
import khelp.database.type.DataType
import khelp.utilities.extensions.base64

infix fun Column.EQUALS_ID(id: Int): Condition
{
    this.checkType(DataType.ID)
    return Condition(arrayOf(this), "${this.name}=$id")
}

infix fun Column.EQUALS(string: String): Condition
{
    this.checkType(DataType.STRING)
    return Condition(arrayOf(this), "${this.name}='$string'")
}

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

infix fun Column.EQUALS(value: Byte): Condition
{
    this.checkType(DataType.BYTE)
    return Condition(arrayOf(this), "${this.name}=$value")
}

infix fun Column.EQUALS(value: Short): Condition
{
    this.checkType(DataType.SHORT)
    return Condition(arrayOf(this), "${this.name}=$value")
}

infix fun Column.EQUALS(value: Int): Condition
{
    this.checkType(DataType.INTEGER)
    return Condition(arrayOf(this), "${this.name}=$value")
}

infix fun Column.EQUALS(value: Long): Condition
{
    this.checkType(DataType.LONG)
    return Condition(arrayOf(this), "${this.name}=$value")
}

infix fun Column.EQUALS(value: Float): Condition
{
    this.checkType(DataType.FLOAT)
    return Condition(arrayOf(this), "${this.name}=$value")
}

infix fun Column.EQUALS(value: Double): Condition
{
    this.checkType(DataType.DOUBLE)
    return Condition(arrayOf(this), "${this.name}=$value")
}

infix fun Column.EQUALS(value: ByteArray): Condition
{
    this.checkType(DataType.BYTE_ARRAY)
    return Condition(arrayOf(this), "${this.name}='${value.base64}'")
}

infix fun Column.EQUALS(value: Calendar): Condition
{
    this.checkType(DataType.CALENDAR)
    return Condition(arrayOf(this), "${this.name}=${value.timeInMillis}")
}

infix fun Column.EQUALS(value: DataDate): Condition
{
    this.checkType(DataType.DATE)
    return Condition(arrayOf(this), "${this.name}=${value.serialized}")
}

infix fun Column.EQUALS(value: DataTime): Condition
{
    this.checkType(DataType.TIME)
    return Condition(arrayOf(this), "${this.name}=${value.serialized}")
}

infix fun <E : Enum<E>> Column.EQUALS(value: E): Condition
{
    this.checkType(DataType.ENUM)
    return Condition(arrayOf(this), "${this.name}='${value::class.java.name}:${value.name}'")
}
