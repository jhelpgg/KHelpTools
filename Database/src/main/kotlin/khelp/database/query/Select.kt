package khelp.database.query

import khelp.database.Column
import khelp.database.SelectDSL
import khelp.database.Table
import khelp.database.WhereDSL
import khelp.database.condition.Condition

/**
 * Select rows in table.
 * If no columns are selected, it slects automatically all columns in table at declaration order
 *
 * See documentation about selection DSL syntax
 */
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

    /**
     * Select a column to present
     */
    @SelectDSL
    operator fun Column.unaryPlus()
    {
        this.name.unaryPlus()
    }

    /**
     * Select a column to present
     */
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

    /**
     * Condition that selected rows must match
     */
    @WhereDSL
    fun where(whereCreator: Where.() -> Unit)
    {
        val where = Where(this.table)
        whereCreator(where)
        val condition = where.condition ?: throw IllegalStateException("Choose a condition with 'condition ='")
        condition.checkAllColumns(this.table)
        this.condition = condition
    }

    /**
     * If specified, it will sort result on given column in ascendant order
     */
    @SelectDSL
    fun ascendant(column: Column)
    {
        this.table.checkColumn(column)
        this.columnOrder = column
        this.ascendant = true
    }

    /**
     * If specified, it will sort result on given column in ascendant order
     */
    @SelectDSL
    fun ascendant(columnName: String)
    {
        this.ascendant(this.table.getColumn(columnName))
    }

    /**
     * If specified, it will sort result on given column in descendant order
     */
    @SelectDSL
    fun descendant(column: Column)
    {
        this.table.checkColumn(column)
        this.columnOrder = column
        this.ascendant = false
    }

    /**
     * If specified, it will sort result on given column in descendant order
     */
    @SelectDSL
    fun descendant(columnName: String)
    {
        this.descendant(this.table.getColumn(columnName))
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