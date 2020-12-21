package khelp.database.query

import java.util.Calendar
import java.util.regex.Pattern
import khelp.database.Table
import khelp.database.WhereDatabaseObjectDSL
import khelp.database.condition.Condition
import khelp.database.databaseobject.DatabaseObject
import khelp.database.type.DataDate
import khelp.database.type.DataTime
import khelp.utilities.extensions.transformInt
import khelp.utilities.regex.RegularExpression
import kotlin.reflect.KProperty1

@WhereDatabaseObjectDSL
class WhereDatabaseObject<DO : DatabaseObject>(table: Table) : Where(table)
{
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.EQUALS_ID(id: Int): Condition =
        name EQUALS_ID id

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Boolean>.EQUALS(value: Boolean): Condition =
        name EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.EQUALS(value: Byte): Condition =
        name EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.EQUALS(value: Short): Condition =
        name EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.EQUALS(value: Int): Condition =
        name EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.EQUALS(value: Long): Condition =
        name EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.EQUALS(value: Float): Condition =
        name EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.EQUALS(value: Double): Condition =
        name EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.EQUALS(value: String): Condition =
        name EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.EQUALS(value: Calendar): Condition =
        name EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.EQUALS(value: DataTime): Condition =
        name EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.EQUALS(value: DataDate): Condition =
        name EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, ByteArray>.EQUALS(value: ByteArray): Condition =
        name EQUALS value

    @WhereDatabaseObjectDSL
    infix fun <E : Enum<E>> KProperty1<DO, E>.EQUALS(value: E): Condition =
        name EQUALS value

    @WhereDatabaseObjectDSL
    infix fun <DO2 : DatabaseObject> KProperty1<DO, DO2>.EQUALS(value: DO2): Condition =
        name EQUALS value.databaseID

    //

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.NOT_EQUALS_ID(id: Int): Condition =
        name NOT_EQUALS_ID id

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Boolean>.NOT_EQUALS(value: Boolean): Condition =
        name NOT_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.NOT_EQUALS(value: Byte): Condition =
        name NOT_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.NOT_EQUALS(value: Short): Condition =
        name NOT_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.NOT_EQUALS(value: Int): Condition =
        name NOT_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.NOT_EQUALS(value: Long): Condition =
        name NOT_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.NOT_EQUALS(value: Float): Condition =
        name NOT_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.NOT_EQUALS(value: Double): Condition =
        name NOT_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.NOT_EQUALS(value: String): Condition =
        name NOT_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.NOT_EQUALS(value: Calendar): Condition =
        name NOT_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.NOT_EQUALS(value: DataTime): Condition =
        name NOT_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.NOT_EQUALS(value: DataDate): Condition =
        name NOT_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, ByteArray>.NOT_EQUALS(value: ByteArray): Condition =
        name NOT_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun <E : Enum<E>> KProperty1<DO, E>.NOT_EQUALS(value: E): Condition =
        name NOT_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun <DO2 : DatabaseObject> KProperty1<DO, DO2>.NOT_EQUALS(value: DO2): Condition =
        name NOT_EQUALS value.databaseID

    //

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.ONE_OF_ID(id: IntArray): Condition =
        name ONE_OF_ID id

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Boolean>.ONE_OF(value: BooleanArray): Condition =
        name ONE_OF value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.ONE_OF(value: ByteArray): Condition =
        name ONE_OF value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.ONE_OF(value: ShortArray): Condition =
        name ONE_OF value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.ONE_OF(value: IntArray): Condition =
        name ONE_OF value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.ONE_OF(value: LongArray): Condition =
        name ONE_OF value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.ONE_OF(value: FloatArray): Condition =
        name ONE_OF value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.ONE_OF(value: DoubleArray): Condition =
        name ONE_OF value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.ONE_OF(value: Array<String>): Condition =
        name ONE_OF value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.ONE_OF(value: Array<Calendar>): Condition =
        name ONE_OF value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.ONE_OF(value: Array<DataTime>): Condition =
        name ONE_OF value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.ONE_OF(value: Array<DataDate>): Condition =
        name ONE_OF value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, ByteArray>.ONE_OF(value: Array<ByteArray>): Condition =
        name ONE_OF value

    @WhereDatabaseObjectDSL
    infix fun <E : Enum<E>> KProperty1<DO, E>.ONE_OF(value: Array<E>): Condition =
        name ONE_OF value

    @WhereDatabaseObjectDSL
    infix fun <DO2 : DatabaseObject> KProperty1<DO, DO2>.ONE_OF(value: Array<DO2>): Condition =
        name ONE_OF value.transformInt { it.databaseID }

    //

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.LOWER(value: Byte): Condition =
        name LOWER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.LOWER(value: Short): Condition =
        name LOWER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.LOWER(value: Int): Condition =
        name LOWER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.LOWER(value: Long): Condition =
        name LOWER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.LOWER(value: Float): Condition =
        name LOWER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.LOWER(value: Double): Condition =
        name LOWER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.LOWER(value: String): Condition =
        name LOWER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.LOWER(value: Calendar): Condition =
        name LOWER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.LOWER(value: DataTime): Condition =
        name LOWER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.LOWER(value: DataDate): Condition =
        name LOWER value

    //

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.LOWER_EQUALS(value: Byte): Condition =
        name LOWER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.LOWER_EQUALS(value: Short): Condition =
        name LOWER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.LOWER_EQUALS(value: Int): Condition =
        name LOWER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.LOWER_EQUALS(value: Long): Condition =
        name LOWER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.LOWER_EQUALS(value: Float): Condition =
        name LOWER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.LOWER_EQUALS(value: Double): Condition =
        name LOWER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.LOWER_EQUALS(value: String): Condition =
        name LOWER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.LOWER_EQUALS(value: Calendar): Condition =
        name LOWER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.LOWER_EQUALS(value: DataTime): Condition =
        name LOWER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.LOWER_EQUALS(value: DataDate): Condition =
        name LOWER_EQUALS value

    //

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.UPPER(value: Byte): Condition =
        name UPPER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.UPPER(value: Short): Condition =
        name UPPER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.UPPER(value: Int): Condition =
        name UPPER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.UPPER(value: Long): Condition =
        name UPPER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.UPPER(value: Float): Condition =
        name UPPER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.UPPER(value: Double): Condition =
        name UPPER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.UPPER(value: String): Condition =
        name UPPER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.UPPER(value: Calendar): Condition =
        name UPPER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.UPPER(value: DataTime): Condition =
        name UPPER value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.UPPER(value: DataDate): Condition =
        name UPPER value

    //

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.UPPER_EQUALS(value: Byte): Condition =
        name UPPER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.UPPER_EQUALS(value: Short): Condition =
        name UPPER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.UPPER_EQUALS(value: Int): Condition =
        name UPPER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.UPPER_EQUALS(value: Long): Condition =
        name UPPER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.UPPER_EQUALS(value: Float): Condition =
        name UPPER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.UPPER_EQUALS(value: Double): Condition =
        name UPPER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.UPPER_EQUALS(value: String): Condition =
        name UPPER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.UPPER_EQUALS(value: Calendar): Condition =
        name UPPER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.UPPER_EQUALS(value: DataTime): Condition =
        name UPPER_EQUALS value

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.UPPER_EQUALS(value: DataDate): Condition =
        name UPPER_EQUALS value

    //

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, *>.IN(matchCreator: Match.() -> Unit): Condition =
        name IN matchCreator

    //

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.REGEX(pattern: Pattern): Condition =
        name REGEX pattern

    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.REGEX(regularExpression: RegularExpression): Condition =
        name REGEX regularExpression
}