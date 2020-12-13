package khelp.database.query

import khelp.database.Column
import khelp.database.SelectDSL
import khelp.database.Table
import khelp.database.condition.Condition
import khelp.database.extensions.checkColumn

class Select internal constructor(val table: Table) : Iterable<Column>
{
    private val columns = ArrayList<Column>()
    private var condition: Condition? = null
    private var columnOrder: Column? = null
    private var ascendant = true

    val numberColumns
        get() =
            if (this.columns.isEmpty())
            {
                this.table.numberColumns
            }
            else
            {
                this.columns.size
            }

    operator fun get(index: Int) =
        if (this.columns.isEmpty())
        {
            this.table[index]
        }
        else
        {
            this.columns[index]
        }

    override fun iterator(): Iterator<Column> =
        if (this.columns.isEmpty())
        {
            this.table.iterator()
        }
        else
        {
            this.columns.iterator()
        }

    fun column(name: String): Column? =
        if (this.columns.isEmpty())
        {
            this.table.obtainColumn(name)
        }
        else
        {
            this.columns.firstOrNull { column -> name.equals(column.name, true) }
        }

    fun columnIndex(column: Column): Int =
        if (this.columns.isEmpty())
        {
            this.table.indexOf(column)
        }
        else
        {
            this.columns.indexOf(column)
        }

    fun columnIndex(name: String): Int
    {
        val column = column(name) ?: return -1
        return columnIndex(column)
    }

    @SelectDSL
    operator fun Column.unaryPlus()
    {
        this.name.unaryPlus()
    }

    @SelectDSL
    operator fun String.unaryPlus()
    {
        val column = this@Select.table.obtainColumn(this)
                     ?: throw IllegalArgumentException("No column $this in table${this@Select.table.name}")

        if (column !in this@Select.columns)
        {
            this@Select.columns += column
        }
    }

    @SelectDSL
    fun where(condition: Condition)
    {
        condition.checkAllColumns(this.table)
        this.condition = condition
    }

    @SelectDSL
    fun ascendant(column: Column)
    {
        this.table.checkColumn(column)
        this.columnOrder = column
        this.ascendant = true
    }

    @SelectDSL
    fun descendant(column: Column)
    {
        this.table.checkColumn(column)
        this.columnOrder = column
        this.ascendant = false
    }

    internal fun selectSQL(): String
    {
        val query = StringBuilder()
        query.append("SELECT ")
        query.append(this[0].name)

        for (index in 1 until this.numberColumns)
        {
            query.append(", ")
            query.append(this[index].name)
        }

        query.append(" FROM ")
        query.append(this.table.name)

        this.condition?.let { condition ->
            query.append(" WHERE ")
            query.append(condition.sqlCondition)
        }

        this.columnOrder?.let { column ->
            query.append(" ORDER BY ")
            query.append(column.name)

            if (this.ascendant)
            {
                query.append(" ASC")
            }
            else
            {
                query.append(" DESC")
            }
        }

        return query.toString()
    }
}