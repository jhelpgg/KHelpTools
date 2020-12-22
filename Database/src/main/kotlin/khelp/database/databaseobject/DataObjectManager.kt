package khelp.database.databaseobject

import java.lang.reflect.Modifier
import java.util.Calendar
import khelp.database.Database
import khelp.database.type.DataDate
import khelp.database.type.DataTime
import khelp.database.type.DataType
import khelp.database.type.toDataType

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

        val oldTable = database.obtainTableOrReadIt(tableName, false)

        if (oldTable != null)
        {
            val fields = classDatabaseObject.declaredFields
            var oldSize = oldTable.numberColumns
            val newSize = fields.size
            var oldIndex = 1
            var newIndex = 0

            while (oldIndex < oldSize && newIndex < newSize)
            {
                if (oldTable[oldIndex].name == fields[newIndex].name)
                {
                    oldIndex++
                    newIndex++
                }
                else if (newIndex + 1 < newSize && oldTable[oldIndex].name == fields[newIndex + 1].name)
                {
                    val field = fields[newIndex]

                    if (field.type.isEnum || DatabaseObject::class.java.isAssignableFrom(field.type))
                    {
                        throw IllegalStateException("Can't add automatically enum or DataObject : ${field.name}")
                    }

                    oldTable.insertColumn(field.name, field.type.toDataType(), oldTable[oldIndex])
                    oldSize++
                    oldIndex++
                    newIndex++
                }
                else
                {
                    oldTable.removeColumn(oldTable[oldIndex])
                    oldSize--
                }
            }

            for (index in oldSize - 1 downTo oldIndex)
            {
                oldTable.removeColumn(oldTable[index])
            }

            for (index in newIndex until newSize)
            {
                val field = fields[index]

                if (field.type.isEnum || DatabaseObject::class.java.isAssignableFrom(field.type))
                {
                    throw IllegalStateException("Can't add automatically enum or DataObject : ${field.name}")
                }

                oldTable.appendColumn(field.name, field.type.toDataType())
            }
        }

        val table = database.table(tableName) {
            if (foreignTable.isNotEmpty())
            {
                idForeign(database.obtainTableOrReadIt(foreignTable, true)!!, foreignColumn)
            }

            for (field in classDatabaseObject.declaredFields)
            {
                val columnName = field.name

                if (field.isAnnotationPresent(PrimaryKey::class.java))
                {
                    if (!Modifier.isFinal(field.modifiers))
                    {
                        throw RuntimeException("Primary key field ${field.name} of ${classDatabaseObject.name} must be final (declared val)")
                    }

                    primaryKeys += columnName
                }

                val type = field.type

                if (type.isArray && DatabaseObject::class.java.isAssignableFrom(type.componentType))
                {
                    @Suppress("UNCHECKED_CAST")
                    createUpdateTableDescription(database,
                                                 type.componentType as Class<out DatabaseObject>,
                                                 "",
                                                 "")
                    columnName AS DataType.INT_ARRAY
                }
                else if (DatabaseObject::class.java.isAssignableFrom(type))
                {
                    @Suppress("UNCHECKED_CAST")
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
                        IntArray::class.java  ->
                            columnName AS DataType.INT_ARRAY
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
