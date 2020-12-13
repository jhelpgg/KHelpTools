package khelp.database.query

import khelp.database.DeleteSL
import khelp.database.Table
import khelp.database.condition.Condition

class Delete(val table: Table)
{
    private var condition: Condition? = null

    @DeleteSL
    fun where(condition: Condition)
    {
        condition.checkAllColumns(this.table)
        this.condition = condition
    }

    internal fun deleteSQL(): String
    {
        val query = StringBuilder()
        query.append("DELETE FROM ")
        query.append(this.table.name)

        this.condition?.let { where ->
            query.append(" WHERE ")
            query.append(where.sqlCondition)
        }

        return query.toString()
    }
}