package khelp.database.databaseobject

import java.util.Calendar
import khelp.database.Column
import khelp.database.Database
import khelp.database.DeleteDatabaseObjectDSL
import khelp.database.SelectDatabaseObjectDSL
import khelp.database.Table
import khelp.database.condition.AND
import khelp.database.condition.Condition
import khelp.database.condition.EQUALS
import khelp.database.condition.EQUALS_ENUM
import khelp.database.condition.NEVER_MATCH_CONDITION
import khelp.database.query.DeleteDatabaseObject
import khelp.database.query.SelectDatabaseObject
import khelp.database.type.DataDate
import khelp.database.type.DataTime
import khelp.thread.delay
import khelp.thread.future.FutureResult

abstract class DatabaseObject(internal val database: Database)
{
    companion object
    {
        fun table(database: Database, clazz: Class<out DatabaseObject>): Table =
            DataObjectManager.tableDescription(database, clazz).table

        @SelectDatabaseObjectDSL
        inline fun <reified DO : DatabaseObject> select(database: Database,
                                                        selectCreator: SelectDatabaseObject<DO>.() -> Unit): DatabaseObjectResult<DO>
        {
            val table = DatabaseObject.table(database, DO::class.java)
            val selectDatabaseObject = SelectDatabaseObject<DO>(table)
            selectCreator(selectDatabaseObject)
            val result = database.select(selectDatabaseObject.select)
            return DatabaseObjectResult(database, DO::class, result)
        }

        @DeleteDatabaseObjectDSL
        inline fun <reified DO : DatabaseObject> delete(database: Database,
                                                        deleteCreator: DeleteDatabaseObject<DO>.() -> Unit): Int
        {
            val table = DatabaseObject.table(database, DO::class.java)
            val deleteDatabaseObject = DeleteDatabaseObject<DO>(table)
            deleteCreator(deleteDatabaseObject)
            return database.delete(deleteDatabaseObject.delete)
        }
    }

    var databaseID: Int = -1
        private set
    private val futureResult: FutureResult<Unit>

    init
    {
        this.futureResult = delay(16) { this.update() }
    }

    fun <DO : DatabaseObject> waitCreated(): DO
    {
        this.futureResult.waitCompletion()
        return this as DO
    }

    protected fun update()
    {
        val tableDescription = DataObjectManager.tableDescription(this)

        if (this.databaseID >= 0)
        {
            val result = tableDescription.table.select {
                +"ID"
                where { condition = "ID" EQUALS_ID databaseID }
            }

            if (!result.hasNext)
            {
                this.databaseID = -1
            }

            result.close()
        }

        val clazz = this::class.java
        val table = tableDescription.table

        if (this.databaseID < 0)
        {
            val primaryKeys = tableDescription.primaryKeys

            if (primaryKeys.isEmpty())
            {
                this.databaseID = table.insert {
                    for (field in clazz.declaredFields)
                    {
                        field.isAccessible = true
                        val type = field.type

                        when
                        {
                            DatabaseObject::class.java.isAssignableFrom(type) ->
                            {
                                val dataObject = (field[this@DatabaseObject] as DatabaseObject)
                                dataObject.update()
                                field.name IS dataObject.databaseID
                            }
                            type == Boolean::class.java                       ->
                                field.name IS field.getBoolean(this@DatabaseObject)
                            type == Byte::class.java                          ->
                                field.name IS field.getByte(this@DatabaseObject)
                            type == Short::class.java                         ->
                                field.name IS field.getShort(this@DatabaseObject)
                            type == Int::class.java                           ->
                                field.name IS field.getInt(this@DatabaseObject)
                            type == Long::class.java                          ->
                                field.name IS field.getLong(this@DatabaseObject)
                            type == Float::class.java                         ->
                                field.name IS field.getFloat(this@DatabaseObject)
                            type == Double::class.java                        ->
                                field.name IS field.getDouble(this@DatabaseObject)
                            type == String::class.java                        ->
                                field.name IS field.get(this@DatabaseObject) as String
                            type == ByteArray::class.java                     ->
                                field.name IS field.get(this@DatabaseObject) as ByteArray
                            type == Calendar::class.java                      ->
                                field.name IS field.get(this@DatabaseObject) as Calendar
                            type == DataTime::class.java                      ->
                                field.name IS field.get(this@DatabaseObject) as DataTime
                            type == DataDate::class.java                      ->
                                field.name IS field.get(this@DatabaseObject) as DataDate
                            type.isEnum                                       ->
                                field.name IS_ENUM field.get(this@DatabaseObject)
                        }
                    }
                }
            }
            else
            {
                this.databaseID = table.insert {
                    for (field in clazz.declaredFields)
                    {
                        field.isAccessible = true
                        val type = field.type

                        when
                        {
                            DatabaseObject::class.java.isAssignableFrom(type) ->
                            {
                                val dataObject = (field[this@DatabaseObject] as DatabaseObject)
                                dataObject.update()
                                field.name IS dataObject.databaseID
                            }
                            type == Boolean::class.java                       ->
                                field.name IS field.getBoolean(this@DatabaseObject)
                            type == Byte::class.java                          ->
                                field.name IS field.getByte(this@DatabaseObject)
                            type == Short::class.java                         ->
                                field.name IS field.getShort(this@DatabaseObject)
                            type == Int::class.java                           ->
                                field.name IS field.getInt(this@DatabaseObject)
                            type == Long::class.java                          ->
                                field.name IS field.getLong(this@DatabaseObject)
                            type == Float::class.java                         ->
                                field.name IS field.getFloat(this@DatabaseObject)
                            type == Double::class.java                        ->
                                field.name IS field.getDouble(this@DatabaseObject)
                            type == String::class.java                        ->
                                field.name IS field.get(this@DatabaseObject) as String
                            type == ByteArray::class.java                     ->
                                field.name IS field.get(this@DatabaseObject) as ByteArray
                            type == Calendar::class.java                      ->
                                field.name IS field.get(this@DatabaseObject) as Calendar
                            type == DataTime::class.java                      ->
                                field.name IS field.get(this@DatabaseObject) as DataTime
                            type == DataDate::class.java                      ->
                                field.name IS field.get(this@DatabaseObject) as DataDate
                            type.isEnum                                       ->
                                field.name IS_ENUM field.get(this@DatabaseObject)
                        }
                    }
                    updateIfExactlyOneRowMatch {
                        var cond = createConditionPrimaryKey(clazz, table.getColumn(primaryKeys[0]), primaryKeys[0])

                        for (index in 1 until primaryKeys.size)
                        {
                            cond = cond AND createConditionPrimaryKey(clazz,
                                                                      table.getColumn(primaryKeys[index]),
                                                                      primaryKeys[index])
                        }

                        condition = cond
                    }
                }
            }
        }
        else
        {
            this.databaseID = table.insert {
                for (field in clazz.declaredFields)
                {
                    field.isAccessible = true
                    val type = field.type

                    when
                    {
                        DatabaseObject::class.java.isAssignableFrom(type) ->
                        {
                            val dataObject = (field[this@DatabaseObject] as DatabaseObject)
                            dataObject.update()
                            field.name IS dataObject.databaseID
                        }
                        type == Boolean::class.java                       ->
                            field.name IS field.getBoolean(this@DatabaseObject)
                        type == Byte::class.java                          ->
                            field.name IS field.getByte(this@DatabaseObject)
                        type == Short::class.java                         ->
                            field.name IS field.getShort(this@DatabaseObject)
                        type == Int::class.java                           ->
                            field.name IS field.getInt(this@DatabaseObject)
                        type == Long::class.java                          ->
                            field.name IS field.getLong(this@DatabaseObject)
                        type == Float::class.java                         ->
                            field.name IS field.getFloat(this@DatabaseObject)
                        type == Double::class.java                        ->
                            field.name IS field.getDouble(this@DatabaseObject)
                        type == String::class.java                        ->
                            field.name IS field.get(this@DatabaseObject) as String
                        type == ByteArray::class.java                     ->
                            field.name IS field.get(this@DatabaseObject) as ByteArray
                        type == Calendar::class.java                      ->
                            field.name IS field.get(this@DatabaseObject) as Calendar
                        type == DataTime::class.java                      ->
                            field.name IS field.get(this@DatabaseObject) as DataTime
                        type == DataDate::class.java                      ->
                            field.name IS field.get(this@DatabaseObject) as DataDate
                        type.isEnum                                       ->
                            field.name IS_ENUM field.get(this@DatabaseObject)
                    }
                }
                updateIfExactlyOneRowMatch {
                    condition = "ID" EQUALS_ID databaseID
                }
            }
        }
    }

    private fun createConditionPrimaryKey(clazz: Class<out DatabaseObject>,
                                          column: Column,
                                          primaryKey: String): Condition
    {
        val field = clazz.getDeclaredField(primaryKey)
        field.isAccessible = true
        val type = field.type

        return when
        {
            DatabaseObject::class.java.isAssignableFrom(type) ->
            {
                val dataObject = (field[this@DatabaseObject] as DatabaseObject)
                dataObject.update()
                column EQUALS dataObject.databaseID
            }
            type == Boolean::class.java                       ->
                column EQUALS field.getBoolean(this@DatabaseObject)
            type == Byte::class.java                          ->
                column EQUALS field.getByte(this@DatabaseObject)
            type == Short::class.java                         ->
                column EQUALS field.getShort(this@DatabaseObject)
            type == Int::class.java                           ->
                column EQUALS field.getInt(this@DatabaseObject)
            type == Long::class.java                          ->
                column EQUALS field.getLong(this@DatabaseObject)
            type == Float::class.java                         ->
                column EQUALS field.getFloat(this@DatabaseObject)
            type == Double::class.java                        ->
                column EQUALS field.getDouble(this@DatabaseObject)
            type == String::class.java                        ->
                column EQUALS field.get(this@DatabaseObject) as String
            type == ByteArray::class.java                     ->
                column EQUALS field.get(this@DatabaseObject) as ByteArray
            type == Calendar::class.java                      ->
                column EQUALS field.get(this@DatabaseObject) as Calendar
            type == DataTime::class.java                      ->
                column EQUALS field.get(this@DatabaseObject) as DataTime
            type == DataDate::class.java                      ->
                column EQUALS field.get(this@DatabaseObject) as DataDate
            type.isEnum                                       ->
                column EQUALS_ENUM field.get(this@DatabaseObject)
            else                                              ->
                NEVER_MATCH_CONDITION
        }
    }
}