package khelp.database.query

import java.util.Calendar
import java.util.regex.Pattern
import khelp.database.SelectDatabaseObjectDSL
import khelp.database.Table
import khelp.database.condition.AND
import khelp.database.databaseobject.DatabaseObject
import khelp.database.type.DataDate
import khelp.database.type.DataTime
import khelp.utilities.extensions.transformInt
import khelp.utilities.regex.RegularExpression
import kotlin.reflect.KProperty1

@SelectDatabaseObjectDSL
class SelectDatabaseObject<DO : DatabaseObject>(val table: Table)
{
    val select: Select = Select(this.table)

    private fun selector(selectCreator: Select.() -> Unit)
    {
        selectCreator(select)
    }

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.EQUALS_ID(id: Int)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Boolean>.EQUALS(value: Boolean)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.EQUALS(value: Byte)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.EQUALS(value: Short)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.EQUALS(value: Int)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.EQUALS(value: Long)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.EQUALS(value: Float)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.EQUALS(value: Double)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, String>.EQUALS(value: String)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.EQUALS(value: Calendar)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.EQUALS(value: DataTime)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.EQUALS(value: DataDate)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, ByteArray>.EQUALS(value: ByteArray)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun <E : Enum<E>> KProperty1<DO, E>.EQUALS(value: E)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun <DO2 : DatabaseObject> KProperty1<DO, DO2>.EQUALS(value: DO2)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.NOT_EQUALS_ID(id: Int)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Boolean>.NOT_EQUALS(value: Boolean)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.NOT_EQUALS(value: Byte)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.NOT_EQUALS(value: Short)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.NOT_EQUALS(value: Int)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.NOT_EQUALS(value: Long)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.NOT_EQUALS(value: Float)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.NOT_EQUALS(value: Double)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, String>.NOT_EQUALS(value: String)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.NOT_EQUALS(value: Calendar)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.NOT_EQUALS(value: DataTime)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.NOT_EQUALS(value: DataDate)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, ByteArray>.NOT_EQUALS(value: ByteArray)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun <E : Enum<E>> KProperty1<DO, E>.NOT_EQUALS(value: E)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun <DO2 : DatabaseObject> KProperty1<DO, DO2>.NOT_EQUALS(value: DO2)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.ONE_OF_ID(id: IntArray)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Boolean>.ONE_OF(value: BooleanArray)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.ONE_OF(value: ByteArray)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.ONE_OF(value: ShortArray)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.ONE_OF(value: IntArray)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.ONE_OF(value: LongArray)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.ONE_OF(value: FloatArray)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.ONE_OF(value: DoubleArray)
    {
        selector {
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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, String>.ONE_OF(value: Array<String>)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.ONE_OF(value: Array<Calendar>)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.ONE_OF(value: Array<DataTime>)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.ONE_OF(value: Array<DataDate>)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, ByteArray>.ONE_OF(value: Array<ByteArray>)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun <E : Enum<E>> KProperty1<DO, E>.ONE_OF(value: Array<E>)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun <DO2 : DatabaseObject> KProperty1<DO, DO2>.ONE_OF(value: Array<DO2>)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.LOWER(value: Byte)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.LOWER(value: Short)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.LOWER(value: Int)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.LOWER(value: Long)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.LOWER(value: Float)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.LOWER(value: Double)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, String>.LOWER(value: String)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.LOWER(value: Calendar)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.LOWER(value: DataTime)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.LOWER(value: DataDate)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.LOWER_EQUALS(value: Byte)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.LOWER_EQUALS(value: Short)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.LOWER_EQUALS(value: Int)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.LOWER_EQUALS(value: Long)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.LOWER_EQUALS(value: Float)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.LOWER_EQUALS(value: Double)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, String>.LOWER_EQUALS(value: String)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.LOWER_EQUALS(value: Calendar)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.LOWER_EQUALS(value: DataTime)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.LOWER_EQUALS(value: DataDate)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.UPPER(value: Byte)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.UPPER(value: Short)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.UPPER(value: Int)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.UPPER(value: Long)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.UPPER(value: Float)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.UPPER(value: Double)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, String>.UPPER(value: String)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.UPPER(value: Calendar)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.UPPER(value: DataTime)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.UPPER(value: DataDate)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.UPPER_EQUALS(value: Byte)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.UPPER_EQUALS(value: Short)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.UPPER_EQUALS(value: Int)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.UPPER_EQUALS(value: Long)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.UPPER_EQUALS(value: Float)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.UPPER_EQUALS(value: Double)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, String>.UPPER_EQUALS(value: String)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.UPPER_EQUALS(value: Calendar)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.UPPER_EQUALS(value: DataTime)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.UPPER_EQUALS(value: DataDate)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, *>.IN(matchCreator: Match.() -> Unit)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, String>.REGEX(pattern: Pattern)
    {
        selector {

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

    @SelectDatabaseObjectDSL
    infix fun KProperty1<DO, String>.REGEX(regularExpression: RegularExpression)
    {
        selector {

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