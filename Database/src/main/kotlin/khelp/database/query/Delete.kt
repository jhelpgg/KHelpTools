package khelp.database.query

import khelp.database.DeleteSL
import khelp.database.Table
import khelp.database.WhereDSL
import khelp.database.condition.Condition

class Delete(val table: Table)
{
    private var condition: Condition? = null

    @WhereDSL
    fun where(whereCreator:Where.()->Unit)
    {
        val where = Where(this.table)
        whereCreator(where)
        val condition = where.condition ?: throw IllegalStateException("Choose a condition with 'condition ='")
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