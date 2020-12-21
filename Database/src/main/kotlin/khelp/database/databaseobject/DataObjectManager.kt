package khelp.database.databaseobject

import java.util.Calendar
import khelp.database.Database
import khelp.database.type.DataDate
import khelp.database.type.DataTime
import khelp.database.type.DataType

internal object DataObjectManager
{
    private val tables = HashMap<String, TableDescription>()

    internal fun tableDescription(databaseObject: DatabaseObject): TableDescription
    {
        val key = "${databaseObject.database.path}:${databaseObject::class.java.name}"
        val tableDescription = this.tables[key]

        if (tableDescription != null)
        {
            return tableDescription
        }

        return this.createUpdateTableDescription(databaseObject.database,
                                                 databaseObject::class.java,
                                                 "",
                                                 "")
    }

    internal fun tableDescription(database: Database,
                                  classDatabaseObject: Class<out DatabaseObject>): TableDescription =
        this.createUpdateTableDescription(database, classDatabaseObject, "", "")

    private fun createUpdateTableDescription(database: Database,
                                             classDatabaseObject: Class<out DatabaseObject>,
                                             foreignTable: String,
                                             foreignColumn: String): TableDescription
    {
        val key = "${database.path}:${classDatabaseObject.name}"
        var tableDescription = this.tables[key]

        if (tableDescription != null)
        {
            if (foreignTable.isNotEmpty() && tableDescription.table[0].foreignTable != foreignTable)
            {
                database.updateIDForeign(tableDescription.table, foreignTable, foreignColumn)
            }

            return tableDescription
        }

        val primaryKeys = ArrayList<String>()
        val tableName = classDatabaseObject.name.replace('.', '_')

         val table = database.table(tableName) {
            if (foreignTable.isNotEmpty())
            {
                idForeign(database.obtainTableOrReadIt(foreignTable)!!, foreignColumn)
            }

            for (field in classDatabaseObject.declaredFields)
            {
                val columnName = field.name

                if (field.isAnnotationPresent(PrimaryKey::class.java))
                {
                    primaryKeys += columnName
                }

                val type = field.type

                if (DatabaseObject::class.java.isAssignableFrom(type))
                {
                    createUpdateTableDescription(database, type as Class<out DatabaseObject>, tableName, columnName)
                    columnName AS DataType.INTEGER
                }
                else
                {
                    when (type)
                    {
                        Boolean::class.java   ->
                            columnName AS DataType.BOOLEAN
                        Byte::class.java      ->
                            columnName AS DataType.BYTE
                        Short::class.java     ->
                            columnName AS DataType.SHORT
                        Int::class.java       ->
                            columnName AS DataType.INTEGER
                        Long::class.java      ->
                            columnName AS DataType.LONG
                        Float::class.java     ->
                            columnName AS DataType.FLOAT
                        Double::class.java    ->
                            columnName AS DataType.DOUBLE
                        String::class.java    ->
                            columnName AS DataType.STRING
                        ByteArray::class.java ->
                            columnName AS DataType.BYTE_ARRAY
                        Calendar::class.java  ->
                            columnName AS DataType.CALENDAR
                        DataTime::class.java  ->
                            columnName AS DataType.TIME
                        DataDate::class.java  ->
                            columnName AS DataType.DATE
                        else                  ->
                            if (type.isEnum)
                            {
                                columnName AS DataType.ENUM
                            }
                    }
                }
            }
        }

        tableDescription = TableDescription(table, primaryKeys.toTypedArray())
        this.tables[key] = tableDescription
        return tableDescription
    }
}
