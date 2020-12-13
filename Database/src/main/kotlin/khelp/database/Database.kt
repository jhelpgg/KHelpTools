package khelp.database

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.sql.Connection
import khelp.database.condition.EQUALS
import khelp.database.extensions.validName
import khelp.database.query.Delete
import khelp.database.query.Insert
import khelp.database.query.Select
import khelp.database.query.Update
import khelp.database.type.DataType
import khelp.io.extensions.createFile
import khelp.security.des.TripleDES
import khelp.security.exception.LoginPasswordInvalidException
import khelp.security.rsa.RSAKeyPair


/**Table of meta data that stores the tables name*/
const val METADATA_TABLE_TABLE = "TABLE_OF_TABLES"

/**Table name column in [METADATA_TABLE_TABLE]*/
const val METADATA_TABLE_COLUMN_TABLE = "name"

/**Table of meta data that stores the columns description*/
const val METADATA_COLUMN_TABLE = "TABLE_OF_TABLES_COLUMNS"

/**Column name column in [METADATA_COLUMN_TABLE]*/
const val METADATA_COLUMN_COLUMN_NAME = "name"

/**Column type column in [METADATA_COLUMN_TABLE]*/
const val METADATA_COLUMN_COLUMN_TYPE = "type"

/**Table ID where the column lies column in [METADATA_COLUMN_TABLE]*/
const val METADATA_COLUMN_COLUMN_TABLE_ID = "tableID"

const val ROW_NOT_EXISTS = -1
const val ROW_NOT_UNIQUE = -2


class Database private constructor(login: String, password: String, path: String) : Iterable<Table>
{
    companion object
    {
        private var database: Database? = null
        private var lock = Object()

        fun database(login: String, password: String): Database
        {
            synchronized(lock)
            {
                if (database == null || database!!.closed)
                {
                    database = Database(login, password, "data/database")
                }

                if (!database!!.valid(login, password))
                {
                    throw LoginPasswordInvalidException
                }

                return database!!
            }
        }
    }

    private val tripleDES = TripleDES(login, password)
    private val databaseConnection: Connection
    private val tables = ArrayList<Table>()

    val metaDataTable: Table
    val metaColumnTable: Table

    val closed get() = this.databaseConnection.isClosed

    init
    {
        val keyFile = File("${path}.key")
        val rsaKeyPair: RSAKeyPair

        if (keyFile.exists())
        {
            rsaKeyPair = RSAKeyPair(this.tripleDES, FileInputStream(keyFile))
        }
        else
        {
            if (!keyFile.createFile())
            {
                throw IOException("Can't create key file : ${keyFile.absolutePath}")
            }

            rsaKeyPair = RSAKeyPair()
            val fileOutputStream = FileOutputStream(keyFile)
            rsaKeyPair.save(this.tripleDES, fileOutputStream)
            fileOutputStream.close()
        }

        this.databaseConnection = DatabaseAccess.createConnection(path, rsaKeyPair)
        this.databaseConnection.autoCommit = false

        this.metaDataTable = this.table(METADATA_TABLE_TABLE, true) {
            METADATA_TABLE_COLUMN_TABLE AS DataType.STRING
        }
        this.metaColumnTable = this.table(METADATA_COLUMN_TABLE, true) {
            METADATA_COLUMN_COLUMN_NAME AS DataType.STRING
            METADATA_COLUMN_COLUMN_TYPE AS DataType.ENUM
            METADATA_COLUMN_COLUMN_TABLE_ID AS DataType.INTEGER
        }
    }

    fun valid(login: String, password: String): Boolean
    {
        this.checkClose()
        return this.tripleDES.valid(login, password)
    }

    fun close()
    {
        this.checkClose()
        this.databaseConnection.commit()
        this.simpleQuery("SHUTDOWN")
        this.databaseConnection.close()
    }

    fun obtainTable(name: String): Table?
    {
        this.checkClose()
        return this.tables.firstOrNull { table -> name.equals(table.name, true) }
    }

    @CreateTableDSL
    fun table(name: String, creator: Table.() -> Unit): Table
    {
        this.checkClose()
        if (!name.validName())
        {
            throw IllegalArgumentException("Invalid table name : $name")
        }

        if (this.obtainTable(name) != null)
        {
            throw IllegalArgumentException("A table $name already exists")
        }

        return this.table(name, false, creator)
    }

    override fun iterator(): Iterator<Table>
    {
        this.checkClose()
        return this.tables.iterator()
    }

    internal fun select(select: Select): DataRowResult
    {
        this.checkClose()
        val statement = this.databaseConnection.createStatement()
        val resultSet = statement.executeQuery(select.selectSQL())
        return DataRowResult(statement, resultSet, select, select.table)
    }

    internal fun insert(insert: Insert): Int
    {
        this.checkClose()
        var rowID = ROW_NOT_EXISTS

        insert.conditionUpdateOneMatch?.let { condition ->
            rowID = insert.table.rowID(condition)
        }

        if (rowID >= 0)
        {
            this.updateQuery(insert.updateSQL(rowID))
            return rowID
        }

        this.updateQuery(insert.insertSQL())
        return this.biggestID(insert.table)
    }

    internal fun update(update: Update): Int
    {
        this.checkClose()
        return this.updateQuery(update.updateSQL())
    }

    internal fun delete(delete: Delete): Int
    {
        this.checkClose()
        return this.updateQuery(delete.deleteSQL())
    }

    private fun biggestID(table: Table): Int
    {
        val result = table.select {
            +COLUMN_ID
            descendant(COLUMN_ID)
        }

        var id = 0

        if (result.hasNext)
        {
            result.next { id = getID(1) }
        }

        result.close()
        return id
    }

    @CreateTableDSL
    private fun table(name: String, readOnly: Boolean, tableCreator: Table.() -> Unit): Table
    {
        var table = this.obtainTable(name)

        if (table != null)
        {
            return table
        }

        table = Table(name, readOnly, this)
        tableCreator(table)
        table = this.createTable(table)
        this.tables += table
        return table
    }

    private fun createTable(table: Table): Table
    {
        if (!table.readOnly)
        {
            val id = this.metaDataTable.rowID(this.metaDataTable.getColumn(METADATA_TABLE_COLUMN_TABLE) EQUALS table.name)

            if (id != ROW_NOT_EXISTS)
            {
                val result = this.metaColumnTable.select {
                    +METADATA_COLUMN_COLUMN_NAME
                    +METADATA_COLUMN_COLUMN_TYPE
                    where(this.table.getColumn(METADATA_COLUMN_COLUMN_TABLE_ID) EQUALS id)

                }

                return this.createTable(table.name) {
                    while (result.hasNext)
                    {
                        result.next {
                            val type = getEnum<DataType>(2)

                            if (type != DataType.ID)
                            {
                                getString(1) AS type
                            }
                        }
                    }

                    result.close()
                }
            }
        }

        val query = StringBuilder()
        query.append("CREATE TABLE ")
        query.append(table.name)
        query.append(" (")
        var notFirst = false

        for (column in table)
        {
            if (notFirst)
            {
                query.append(" , ")
            }

            query.append(column.name)
            query.append(" ")
            query.append(column.type.typeSQL)
            notFirst = true
        }

        query.append(")")
        this.updateQuery(query.toString())

        if (!table.readOnly)
        {
            val tableID = this.insert(this.metaDataTable) {
                this.table.getColumn(METADATA_TABLE_COLUMN_TABLE) IS table.name
            }

            for (column in table)
            {
                this.insert(this.metaColumnTable) {
                    this.table.getColumn(METADATA_COLUMN_COLUMN_NAME) IS column.name
                    this.table.getColumn(METADATA_COLUMN_COLUMN_TYPE) IS column.type
                    this.table.getColumn(METADATA_COLUMN_COLUMN_TABLE_ID) IS tableID
                }
            }
        }

        return table
    }

    @CreateTableDSL
    private fun createTable(name: String, tableCreator: Table.() -> Unit): Table
    {
        val table = Table(name, false, this)
        tableCreator(table)
        return table
    }

    @InsertDSL
    private fun insert(table: Table, insertCreator: Insert.() -> Unit): Int
    {
        val insert = Insert(table)
        insertCreator(insert)
        return this.insert(insert)
    }

    @UpdateDSL
    private fun update(table: Table, updateCreator: Update.() -> Unit): Int
    {
        val update = Update(table)
        updateCreator(update)
        return this.update(update)
    }

    @DeleteSL
    private fun delete(table: Table, deleteCreator: Delete.() -> Unit): Int
    {
        val delete = Delete(table)
        deleteCreator(delete)
        return this.delete(delete)
    }

    /**
     * Make a query that no need result and not modify the database
     * @param query Query to do
     */
    private fun simpleQuery(query: String)
    {
        if (query.isNotEmpty())
        {
            val statement = this.databaseConnection.createStatement()
            statement.executeQuery(query)
            statement.close()
        }
    }

    /**
     * Make a query that modify the database: [createTable], [delete], [update], [insert]
     * @param query Query to do
     */
    private fun updateQuery(query: String): Int
    {
        if (query.isNotEmpty())
        {
            try
            {
                val statement = this.databaseConnection.createStatement()
                val count = statement.executeUpdate(query)
                statement.close()
                this.databaseConnection.commit()
                return count
            }
            catch (_: Exception)
            {
            }
        }

        return 0
    }

    private fun checkClose()
    {
        if (this.closed)
        {
            throw  IllegalStateException("The database is closed, call 'Database.database' to reopen it!")
        }
    }
}
