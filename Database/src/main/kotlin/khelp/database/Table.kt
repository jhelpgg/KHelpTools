package khelp.database

import khelp.database.condition.Condition
import khelp.database.extensions.checkReadOnly
import khelp.database.extensions.validName
import khelp.database.query.Delete
import khelp.database.query.Insert
import khelp.database.query.InsertList
import khelp.database.query.Select
import khelp.database.query.Update
import khelp.database.query.Where
import khelp.database.type.DataType
import khelp.utilities.extensions.transform

class Table internal constructor(val name: String, val readOnly: Boolean, private val database: Database) :
    Iterable<Column>
{
    private val columns = ArrayList<Column>()
    val numberColumns get() = this.columns.size

    init
    {
        if (!this.name.validName())
        {
            throw IllegalArgumentException("Invalid table name : ${this.name}")
        }

        this.columns += COLUMN_ID
    }

    operator fun get(index: Int) =
        this.columns[index]

    fun getColumn(nameSearched: String) =
        this.columns.first { column -> nameSearched.equals(column.name, true) }

    fun obtainColumn(nameSearched: String) =
        this.columns.firstOrNull { column -> nameSearched.equals(column.name, true) }

    operator fun contains(column: Column) =
        column in this.columns

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

    override fun iterator(): Iterator<Column> =
        this.columns.iterator()

    fun columnNames() =
        this.columns.transform { column -> column.name }

    @SelectDSL
    fun select(selectCreator: Select.() -> Unit): DataRowResult
    {
        val select = Select(this)
        selectCreator(select)
        return this.database.select(select)
    }

    @InsertDSL
    fun insert(insertCreator: Insert.() -> Unit): Int
    {
        this.checkReadOnly()
        val insert = Insert(this)
        insertCreator(insert)
        return this.database.insert(insert)
    }

    @InsertDSL
    fun insertList(insertListCreator: InsertList.() -> Unit)
    {
        insertListCreator(InsertList(this))
    }

    @UpdateDSL
    fun update(updateCreator: Update.() -> Unit): Int
    {
        this.checkReadOnly()
        val update = Update(this)
        updateCreator(update)
        return this.database.update(update)
    }

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
    fun rowID(whereCreator: Where.()->Unit): Int
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