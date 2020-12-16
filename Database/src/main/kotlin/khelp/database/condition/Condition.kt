package khelp.database.condition

import khelp.database.COLUMN_ID
import khelp.database.Column
import khelp.database.Table

class Condition internal constructor(internal val columns: Array<Column>, internal val sqlCondition: String)
{
    fun checkAllColumns(table: Table)
    {
        for (column in this.columns)
        {
            table.checkColumn(column)
        }
    }
}

val NEVER_MATCH_CONDITION = COLUMN_ID EQUALS_ID -123

