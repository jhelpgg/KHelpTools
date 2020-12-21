package khelp.database.query

import java.util.Calendar
import java.util.regex.Pattern
import khelp.database.DeleteDatabaseObjectDSL
import khelp.database.Table
import khelp.database.condition.AND
import khelp.database.databaseobject.DatabaseObject
import khelp.database.type.DataDate
import khelp.database.type.DataTime
import khelp.utilities.extensions.transformInt
import khelp.utilities.regex.RegularExpression
import kotlin.reflect.KProperty1

@DeleteDatabaseObjectDSL
class DeleteDatabaseObject<DO : DatabaseObject>(val table: Table)
{
    val delete: Delete = Delete(this.table)

    private fun deleter(deleteCreator: Delete.() -> Unit)
    {
        deleteCreator(delete)
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.EQUALS_ID(id: Int)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name EQUALS_ID id
                    }
                    else
                    {
                        cond AND (name EQUALS_ID id)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Boolean>.EQUALS(value: Boolean)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name EQUALS value
                    }
                    else
                    {
                        cond AND (name EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.EQUALS(value: Byte)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name EQUALS value
                    }
                    else
                    {
                        cond AND (name EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.EQUALS(value: Short)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name EQUALS value
                    }
                    else
                    {
                        cond AND (name EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.EQUALS(value: Int)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name EQUALS value
                    }
                    else
                    {
                        cond AND (name EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.EQUALS(value: Long)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name EQUALS value
                    }
                    else
                    {
                        cond AND (name EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.EQUALS(value: Float)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name EQUALS value
                    }
                    else
                    {
                        cond AND (name EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.EQUALS(value: Double)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name EQUALS value
                    }
                    else
                    {
                        cond AND (name EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, String>.EQUALS(value: String)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name EQUALS value
                    }
                    else
                    {
                        cond AND (name EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.EQUALS(value: Calendar)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name EQUALS value
                    }
                    else
                    {
                        cond AND (name EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.EQUALS(value: DataTime)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name EQUALS value
                    }
                    else
                    {
                        cond AND (name EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.EQUALS(value: DataDate)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name EQUALS value
                    }
                    else
                    {
                        cond AND (name EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, ByteArray>.EQUALS(value: ByteArray)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name EQUALS value
                    }
                    else
                    {
                        cond AND (name EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun <E : Enum<E>> KProperty1<DO, E>.EQUALS(value: E)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name EQUALS value
                    }
                    else
                    {
                        cond AND (name EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun <DO2 : DatabaseObject> KProperty1<DO, DO2>.EQUALS(value: DO2)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name EQUALS value.databaseID
                    }
                    else
                    {
                        cond AND (name EQUALS value.databaseID)
                    }
            }
        }
    }

    //

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.NOT_EQUALS_ID(id: Int)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name NOT_EQUALS_ID id
                    }
                    else
                    {
                        cond AND (name NOT_EQUALS_ID id)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Boolean>.NOT_EQUALS(value: Boolean)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name NOT_EQUALS value
                    }
                    else
                    {
                        cond AND (name NOT_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.NOT_EQUALS(value: Byte)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name NOT_EQUALS value
                    }
                    else
                    {
                        cond AND (name NOT_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.NOT_EQUALS(value: Short)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name NOT_EQUALS value
                    }
                    else
                    {
                        cond AND (name NOT_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.NOT_EQUALS(value: Int)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name NOT_EQUALS value
                    }
                    else
                    {
                        cond AND (name NOT_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.NOT_EQUALS(value: Long)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name NOT_EQUALS value
                    }
                    else
                    {
                        cond AND (name NOT_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.NOT_EQUALS(value: Float)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name NOT_EQUALS value
                    }
                    else
                    {
                        cond AND (name NOT_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.NOT_EQUALS(value: Double)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name NOT_EQUALS value
                    }
                    else
                    {
                        cond AND (name NOT_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, String>.NOT_EQUALS(value: String)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name NOT_EQUALS value
                    }
                    else
                    {
                        cond AND (name NOT_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.NOT_EQUALS(value: Calendar)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name NOT_EQUALS value
                    }
                    else
                    {
                        cond AND (name NOT_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.NOT_EQUALS(value: DataTime)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name NOT_EQUALS value
                    }
                    else
                    {
                        cond AND (name NOT_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.NOT_EQUALS(value: DataDate)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name NOT_EQUALS value
                    }
                    else
                    {
                        cond AND (name NOT_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, ByteArray>.NOT_EQUALS(value: ByteArray)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name NOT_EQUALS value
                    }
                    else
                    {
                        cond AND (name NOT_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun <E : Enum<E>> KProperty1<DO, E>.NOT_EQUALS(value: E)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name NOT_EQUALS value
                    }
                    else
                    {
                        cond AND (name NOT_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun <DO2 : DatabaseObject> KProperty1<DO, DO2>.NOT_EQUALS(value: DO2)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name NOT_EQUALS value.databaseID
                    }
                    else
                    {
                        cond AND (name NOT_EQUALS value.databaseID)
                    }
            }
        }
    }

    //

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.ONE_OF_ID(id: IntArray)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name ONE_OF_ID id
                    }
                    else
                    {
                        cond AND (name ONE_OF_ID id)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Boolean>.ONE_OF(value: BooleanArray)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name ONE_OF value
                    }
                    else
                    {
                        cond AND (name ONE_OF value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.ONE_OF(value: ByteArray)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name ONE_OF value
                    }
                    else
                    {
                        cond AND (name ONE_OF value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.ONE_OF(value: ShortArray)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name ONE_OF value
                    }
                    else
                    {
                        cond AND (name ONE_OF value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.ONE_OF(value: IntArray)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name ONE_OF value
                    }
                    else
                    {
                        cond AND (name ONE_OF value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.ONE_OF(value: LongArray)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name ONE_OF value
                    }
                    else
                    {
                        cond AND (name ONE_OF value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.ONE_OF(value: FloatArray)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name ONE_OF value
                    }
                    else
                    {
                        cond AND (name ONE_OF value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.ONE_OF(value: DoubleArray)
    {
        deleter {
            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name ONE_OF value
                    }
                    else
                    {
                        cond AND (name ONE_OF value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, String>.ONE_OF(value: Array<String>)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name ONE_OF value
                    }
                    else
                    {
                        cond AND (name ONE_OF value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.ONE_OF(value: Array<Calendar>)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name ONE_OF value
                    }
                    else
                    {
                        cond AND (name ONE_OF value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.ONE_OF(value: Array<DataTime>)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name ONE_OF value
                    }
                    else
                    {
                        cond AND (name ONE_OF value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.ONE_OF(value: Array<DataDate>)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name ONE_OF value
                    }
                    else
                    {
                        cond AND (name ONE_OF value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, ByteArray>.ONE_OF(value: Array<ByteArray>)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name ONE_OF value
                    }
                    else
                    {
                        cond AND (name ONE_OF value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun <E : Enum<E>> KProperty1<DO, E>.ONE_OF(value: Array<E>)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name ONE_OF value
                    }
                    else
                    {
                        cond AND (name ONE_OF value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun <DO2 : DatabaseObject> KProperty1<DO, DO2>.ONE_OF(value: Array<DO2>)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name ONE_OF value.transformInt { it.databaseID }
                    }
                    else
                    {
                        cond AND (name ONE_OF value.transformInt { it.databaseID })
                    }
            }
        }
    }

    //

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.LOWER(value: Byte)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER value
                    }
                    else
                    {
                        cond AND (name LOWER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.LOWER(value: Short)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER value
                    }
                    else
                    {
                        cond AND (name LOWER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.LOWER(value: Int)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER value
                    }
                    else
                    {
                        cond AND (name LOWER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.LOWER(value: Long)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER value
                    }
                    else
                    {
                        cond AND (name LOWER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.LOWER(value: Float)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER value
                    }
                    else
                    {
                        cond AND (name LOWER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.LOWER(value: Double)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER value
                    }
                    else
                    {
                        cond AND (name LOWER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, String>.LOWER(value: String)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER value
                    }
                    else
                    {
                        cond AND (name LOWER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.LOWER(value: Calendar)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER value
                    }
                    else
                    {
                        cond AND (name LOWER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.LOWER(value: DataTime)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER value
                    }
                    else
                    {
                        cond AND (name LOWER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.LOWER(value: DataDate)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER value
                    }
                    else
                    {
                        cond AND (name LOWER value)
                    }
            }
        }
    }


    //

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.LOWER_EQUALS(value: Byte)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER_EQUALS value
                    }
                    else
                    {
                        cond AND (name LOWER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.LOWER_EQUALS(value: Short)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER_EQUALS value
                    }
                    else
                    {
                        cond AND (name LOWER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.LOWER_EQUALS(value: Int)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER_EQUALS value
                    }
                    else
                    {
                        cond AND (name LOWER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.LOWER_EQUALS(value: Long)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER_EQUALS value
                    }
                    else
                    {
                        cond AND (name LOWER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.LOWER_EQUALS(value: Float)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER_EQUALS value
                    }
                    else
                    {
                        cond AND (name LOWER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.LOWER_EQUALS(value: Double)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER_EQUALS value
                    }
                    else
                    {
                        cond AND (name LOWER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, String>.LOWER_EQUALS(value: String)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER_EQUALS value
                    }
                    else
                    {
                        cond AND (name LOWER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.LOWER_EQUALS(value: Calendar)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER_EQUALS value
                    }
                    else
                    {
                        cond AND (name LOWER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.LOWER_EQUALS(value: DataTime)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER_EQUALS value
                    }
                    else
                    {
                        cond AND (name LOWER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.LOWER_EQUALS(value: DataDate)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name LOWER_EQUALS value
                    }
                    else
                    {
                        cond AND (name LOWER_EQUALS value)
                    }
            }
        }
    }

    //

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.UPPER(value: Byte)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER value
                    }
                    else
                    {
                        cond AND (name UPPER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.UPPER(value: Short)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER value
                    }
                    else
                    {
                        cond AND (name UPPER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.UPPER(value: Int)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER value
                    }
                    else
                    {
                        cond AND (name UPPER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.UPPER(value: Long)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER value
                    }
                    else
                    {
                        cond AND (name UPPER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.UPPER(value: Float)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER value
                    }
                    else
                    {
                        cond AND (name UPPER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.UPPER(value: Double)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER value
                    }
                    else
                    {
                        cond AND (name UPPER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, String>.UPPER(value: String)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER value
                    }
                    else
                    {
                        cond AND (name UPPER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.UPPER(value: Calendar)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER value
                    }
                    else
                    {
                        cond AND (name UPPER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.UPPER(value: DataTime)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER value
                    }
                    else
                    {
                        cond AND (name UPPER value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.UPPER(value: DataDate)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER value
                    }
                    else
                    {
                        cond AND (name UPPER value)
                    }
            }
        }
    }

    //

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.UPPER_EQUALS(value: Byte)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER_EQUALS value
                    }
                    else
                    {
                        cond AND (name UPPER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.UPPER_EQUALS(value: Short)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER_EQUALS value
                    }
                    else
                    {
                        cond AND (name UPPER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.UPPER_EQUALS(value: Int)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER_EQUALS value
                    }
                    else
                    {
                        cond AND (name UPPER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.UPPER_EQUALS(value: Long)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER_EQUALS value
                    }
                    else
                    {
                        cond AND (name UPPER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.UPPER_EQUALS(value: Float)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER_EQUALS value
                    }
                    else
                    {
                        cond AND (name UPPER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.UPPER_EQUALS(value: Double)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER_EQUALS value
                    }
                    else
                    {
                        cond AND (name UPPER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, String>.UPPER_EQUALS(value: String)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER_EQUALS value
                    }
                    else
                    {
                        cond AND (name UPPER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.UPPER_EQUALS(value: Calendar)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER_EQUALS value
                    }
                    else
                    {
                        cond AND (name UPPER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.UPPER_EQUALS(value: DataTime)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER_EQUALS value
                    }
                    else
                    {
                        cond AND (name UPPER_EQUALS value)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.UPPER_EQUALS(value: DataDate)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name UPPER_EQUALS value
                    }
                    else
                    {
                        cond AND (name UPPER_EQUALS value)
                    }
            }
        }
    }

    //

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, *>.IN(matchCreator: Match.() -> Unit)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name IN matchCreator
                    }
                    else
                    {
                        cond AND (name IN matchCreator)
                    }
            }
        }
    }

    //

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, String>.REGEX(pattern: Pattern)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name REGEX pattern
                    }
                    else
                    {
                        cond AND (name REGEX pattern)
                    }
            }
        }
    }

    @DeleteDatabaseObjectDSL
    infix fun KProperty1<DO, String>.REGEX(regularExpression: RegularExpression)
    {
        deleter {

            where {
                val cond = condition

                condition =
                    if (cond == null)
                    {
                        name REGEX regularExpression
                    }
                    else
                    {
                        cond AND (name REGEX regularExpression)
                    }
            }
        }
    }
}