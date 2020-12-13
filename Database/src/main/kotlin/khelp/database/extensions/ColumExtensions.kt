package khelp.database.extensions

import khelp.database.Column
import khelp.database.exception.InvalidColumnTypeException
import khelp.database.type.DataType

fun Column.checkType(expectedType: DataType)
{
    if (this.type != expectedType)
    {
        throw InvalidColumnTypeException(this.type, expectedType)
    }
}