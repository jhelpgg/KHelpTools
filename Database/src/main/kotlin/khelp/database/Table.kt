package khelp.database

import khelp.database.exception.TableHaveNoSuchColumnException
import khelp.database.exception.TableReadOnlyException
import khelp.database.extensions.validName
import khelp.database.query.Delete
import khelp.database.query.Insert
import khelp.database.query.InsertList
import khelp.database.query.Select
import khelp.database.query.Update
import khelp.database.query.Where
import khelp.database.type.DataType
import khelp.utilities.extensions.transform


/**
 * Database table description
 */
class Table internal constructor(val name: String, val readOnly: Boolean, private val database: Database) :
    Iterable<Column>
{
    private val columns = ArrayList<Column>()
    val numberColumns get() = this.columns.size
    private val columnID = Column("ID", DataType.ID)

    init
    {
        if (!this.name.validName())
        {
            throw IllegalArgumentException("Invalid table name : ${this.name}")
        }

        this.columns += this.columnID
    }

    /**
     * Check if column exists in table.
     * @throws TableHaveNoSuchColumnException If the column not inside the table
     */
    @Throws(TableHaveNoSuchColumnException::class)
    fun checkColumn(column: Column)
    {
        if (column !in this)
        {
            throw TableHaveNoSuchColumnException(this, column)
        }
    }

    /**
     * Check if table is read only
     * @throws TableReadOnlyException If the table is read only
     */
    @Throws(TableReadOnlyException::class)
    fun checkReadOnly()
    {
        if (this.readOnly)
        {
            throw TableReadOnlyException(this)
        }
    }

    /**
     * Get column by its index
     */
    operator fun get(index: Int) =
        this.columns[index]

    /**
     * Get column by name
     * @throws NoSuchElementException If column not exists
     */
    @Throws(NoSuchElementException::class)
    fun getColumn(nameSearched: String) =
        this.columns.first { column -> nameSearched.equals(column.name, true) }

    /**
     * Get column by name
     * @return `null` if column not exists
     */
    fun obtainColumn(nameSearched: String) =
        this.columns.firstOrNull { column -> nameSearched.equals(column.name, true) }

    operator fun contains(column: Column) =
        column in this.columns

    /**
     * Add table column on associate column name and its data type
     * Used by table creation DSL inside lambda defined by [Database.table]
     */
    @CreateTableDSL
    infix fun String.AS(dataType: DataType)
    {
        if (!this.validName())
        {
            throw IllegalArgumentException("Invalid column name : $this")
        }

        if (this@Table.obtainColumn(this) != null)
        {
            throw IllegalArgumentException("A column named $this already exists in table ${this@Table.name}")
        }

        if (dataType == DataType.ID)
        {
            throw IllegalArgumentException("The primary key was already automatically defined")
        }

        this@Table.columns += Column(this, dataType)
    }

    @CreateTableDSL
    fun idForeign(table: Table, column: String)
    {
        this.idForeign(table, table.getColumn(column))
    }

    @CreateTableDSL
    fun idForeign(table: Table, column: Column)
    {
        column.checkType(DataType.INTEGER)
        table.checkColumn(column)
        this.columnID.foreignTable = table.name
        this.columnID.foreignColumn = column.name
    }


    @CreateTableDSL
    infix fun String.FOREIGN(table: Table)
    {
        if (!this.validName())
        {
            throw IllegalArgumentException("Invalid column name : $name")
        }

        if (this@Table.obtainColumn(this) != null)
        {
            throw IllegalArgumentException("A column named $this already exists in table ${this@Table.name}")
        }

        val columnCreated = Column(this, DataType.INTEGER)
        columnCreated.foreignTable = table.name
        columnCreated.foreignColumn = "ID"
        this@Table.columns += columnCreated
    }

    override fun iterator(): Iterator<Column> =
        this.columns.iterator()

    fun columnNames() =
        this.columns.transform { column -> column.name }

    /**
     * Select elements from table.
     * See documentation for more explanation about select DSL syntax
     * @return Selection row's result
     */
    @SelectDSL
    fun select(selectCreator: Select.() -> Unit): DataRowResult
    {
        val select = Select(this)
        selectCreator(select)
        return this.database.select(select)
    }

    /**
     * Insert element in table
     * Se documentation for more explanation about insert DSL syntax
     * @return Row ID where element is insert or updated
     */
    @InsertDSL
    fun insert(insertCreator: Insert.() -> Unit): Int
    {
        this.checkReadOnly()
        val insert = Insert(this)
        insertCreator(insert)
        return this.database.insert(insert)
    }

    /**
     * Do a several insertion in same time.
     * See documentation for more explanation about insert list DSL syntax
     */
    @InsertDSL
    fun insertList(insertListCreator: InsertList.() -> Unit)
    {
        insertListCreator(InsertList(this))
    }

    /**
     * Update rows in table
     * See documentation for more explanation about update DSL syntax
     * @return Number of updated rows
     */
    @UpdateDSL
    fun update(updateCreator: Update.() -> Unit): Int
    {
        this.checkReadOnly()
        val update = Update(this)
        updateCreator(update)
        return this.database.update(update)
    }

    /**
     * Delete rows from table
     * See documentation for more explanation about delete DSL syntax
     * @return Number of deleted rows
     */
    @DeleteSL
    fun delete(deleteCreator: Delete.() -> Unit): Int
    {
        this.checkReadOnly()
        val delete = Delete(this)
        deleteCreator(delete)
        return this.database.delete(delete)
    }

    /**
     * Obtain ID of row match a condition.
     *
     * If no row match [ROW_NOT_EXISTS] is returned
     *
     * If at least 2 rows match [ROW_NOT_UNIQUE] is returned
     *
     * @return Row ID **OR** [ROW_NOT_EXISTS] **OR** [ROW_NOT_UNIQUE]
     */
    @WhereDSL
    fun rowID(whereCreator: Where.() -> Unit): Int
    {
        val result = this.select {
            +COLUMN_ID
            where(whereCreator)
        }

        if (!result.hasNext)
        {
            result.close()
            return ROW_NOT_EXISTS
        }

        var id: Int = -1
        result.next {
            id = this.getID(1)
        }

        if (result.hasNext)
        {
            result.close()
            return ROW_NOT_UNIQUE
        }

        result.close()
        return id
    }
}