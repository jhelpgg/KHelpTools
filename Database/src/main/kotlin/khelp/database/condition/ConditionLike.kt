package khelp.database.condition

import khelp.database.Column
import khelp.database.type.DataType

infix fun Column.LIKE(pattern: String): Condition
{
    this.checkType(DataType.STRING)
    return Condition(arrayOf(this), "${this.name} LIKE '$pattern'")
}

