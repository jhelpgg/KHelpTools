package khelp.database.databaseobject

import java.lang.reflect.Array
import java.util.Calendar
import khelp.database.DataRowResult
import khelp.database.Database
import khelp.database.type.DataDate
import khelp.database.type.DataTime
import kotlin.reflect.KClass

class DatabaseObjectResult<DO : DatabaseObject>(private val database: Database,
                                                private val dataObjectClass: KClass<DO>,
                                                private val rowResult: DataRowResult)
{
    val closed get() = this.rowResult.closed

    val hasNext get() = this.rowResult.hasNext

    fun close()
    {
        this.rowResult.close()
    }

    fun next(): DO
    {
        val constructor = this.dataObjectClass.constructors.first()
        val declaredConstructor = this.dataObjectClass.java.declaredConstructors[0]
        val parameters = arrayOfNulls<Any>(constructor.parameters.size)

        this.rowResult.next {
            for ((index, parameter) in constructor.parameters.withIndex())
            {
                val columnName = parameter.name ?: continue
                val type = declaredConstructor.parameters[index].type

                when
                {
                    type == Database::class.java                                                    ->
                        parameters[index] = database
                    type.isArray && DatabaseObject::class.java.isAssignableFrom(type.componentType) ->
                    {
                        val arrayInt = getIntArray(table.getColumn(columnName))
                        val componentType = declaredConstructor.parameterTypes[index].componentType
                        val array = Array.newInstance(componentType, arrayInt.size)

                        for ((indexElement, id) in arrayInt.withIndex())
                        {
                            val result = DatabaseObject.table(database,
                                                              componentType as Class<out DatabaseObject>)
                                .select { where { condition = "ID" EQUALS_ID id } }
                            val dor = DatabaseObjectResult(database,
                                                           componentType.kotlin as KClass<out DatabaseObject>,
                                                           result)
                            Array.set(array, indexElement, dor.next())
                            dor.close()
                        }
                        parameters[index] = array
                    }
                    DatabaseObject::class.java.isAssignableFrom(type)                               ->
                    {
                        val id = getInt(table.getColumn(columnName))
                        val result = DatabaseObject.table(database,
                                                          declaredConstructor.parameterTypes[index] as Class<out DatabaseObject>)
                            .select { where { condition = "ID" EQUALS_ID id } }
                        val dor = DatabaseObjectResult(database,
                                                       declaredConstructor.parameterTypes[index].kotlin as KClass<out DatabaseObject>,
                                                       result)

                        if (dor.hasNext)
                        {
                            parameters[index] = dor.next()
                        }

                        dor.close()
                    }
                    type == Boolean::class.java                                                     ->
                        parameters[index] = getBoolean(table.getColumn(columnName))
                    type == Byte::class.java                                                        ->
                        parameters[index] = getByte(table.getColumn(columnName))
                    type == Short::class.java                                                       ->
                        parameters[index] = getShort(table.getColumn(columnName))
                    type == Int::class.java                                                         ->
                        parameters[index] = getInt(table.getColumn(columnName))
                    type == Long::class.java                                                        ->
                        parameters[index] = getLong(table.getColumn(columnName))
                    type == Float::class.java                                                       ->
                        parameters[index] = getFloat(table.getColumn(columnName))
                    type == Double::class.java                                                      ->
                        parameters[index] = getDouble(table.getColumn(columnName))
                    type == String::class.java                                                      ->
                        parameters[index] = getString(table.getColumn(columnName))
                    type == ByteArray::class.java                                                   ->
                        parameters[index] = getByteArray(table.getColumn(columnName))
                    type == IntArray::class.java                                                    ->
                        parameters[index] = getIntArray(table.getColumn(columnName))
                    type == Calendar::class.java                                                    ->
                        parameters[index] = getCalendar(table.getColumn(columnName))
                    type == DataDate::class.java                                                    ->
                        parameters[index] = getDate(table.getColumn(columnName))
                    type == DataTime::class.java                                                    ->
                        parameters[index] = getTime(table.getColumn(columnName))
                    type.isEnum                                                                     ->
                        parameters[index] = getEnumAny(table.getColumn(columnName))
                }
            }
        }

        return (declaredConstructor.newInstance(*parameters) as DO).waitCreated()
    }
}