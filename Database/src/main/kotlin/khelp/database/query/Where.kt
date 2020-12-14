package khelp.database.query

import java.util.Calendar
import java.util.regex.Pattern
import khelp.database.Column
import khelp.database.MatchDSL
import khelp.database.Table
import khelp.database.condition.Condition
import khelp.database.condition.EQUALS
import khelp.database.condition.EQUALS_ID
import khelp.database.condition.IN
import khelp.database.condition.LOWER
import khelp.database.condition.LOWER_EQUALS
import khelp.database.condition.NOT_EQUALS
import khelp.database.condition.NOT_EQUALS_ID
import khelp.database.condition.ONE_OF
import khelp.database.condition.ONE_OF_ID
import khelp.database.condition.UPPER
import khelp.database.condition.UPPER_EQUALS
import khelp.database.condition.regex
import khelp.database.type.DataDate
import khelp.database.type.DataTime

class Where(private val table: Table)
{
    var condition: Condition? = null

    // EQUALS

    infix fun String.EQUALS_ID(id: Int) = this@Where.table.getColumn(this) EQUALS_ID id

    infix fun String.EQUALS(value: String) = this@Where.table.getColumn(this) EQUALS value

    infix fun String.EQUALS(value: Boolean) = this@Where.table.getColumn(this) EQUALS value

    infix fun String.EQUALS(value: Byte) = this@Where.table.getColumn(this) EQUALS value

    infix fun String.EQUALS(value: Short) = this@Where.table.getColumn(this) EQUALS value

    infix fun String.EQUALS(value: Int) = this@Where.table.getColumn(this) EQUALS value

    infix fun String.EQUALS(value: Long) = this@Where.table.getColumn(this) EQUALS value

    infix fun String.EQUALS(value: Float) = this@Where.table.getColumn(this) EQUALS value

    infix fun String.EQUALS(value: Double) = this@Where.table.getColumn(this) EQUALS value

    infix fun String.EQUALS(value: ByteArray) = this@Where.table.getColumn(this) EQUALS value

    infix fun String.EQUALS(value: Calendar) = this@Where.table.getColumn(this) EQUALS value

    infix fun String.EQUALS(value: DataDate) = this@Where.table.getColumn(this) EQUALS value

    infix fun String.EQUALS(value: DataTime) = this@Where.table.getColumn(this) EQUALS value

    infix fun <E : Enum<E>> String.EQUALS(value: E) = this@Where.table.getColumn(this) EQUALS value

    // NOT_EQUALS

    infix fun String.NOT_EQUALS_ID(id: Int) = this@Where.table.getColumn(this) NOT_EQUALS_ID id

    infix fun String.NOT_EQUALS(value: String) = this@Where.table.getColumn(this) NOT_EQUALS value

    infix fun String.NOT_EQUALS(value: Boolean) = this@Where.table.getColumn(this) NOT_EQUALS value

    infix fun String.NOT_EQUALS(value: Byte) = this@Where.table.getColumn(this) NOT_EQUALS value

    infix fun String.NOT_EQUALS(value: Short) = this@Where.table.getColumn(this) NOT_EQUALS value

    infix fun String.NOT_EQUALS(value: Int) = this@Where.table.getColumn(this) NOT_EQUALS value

    infix fun String.NOT_EQUALS(value: Long) = this@Where.table.getColumn(this) NOT_EQUALS value

    infix fun String.NOT_EQUALS(value: Float) = this@Where.table.getColumn(this) NOT_EQUALS value

    infix fun String.NOT_EQUALS(value: Double) = this@Where.table.getColumn(this) NOT_EQUALS value

    infix fun String.NOT_EQUALS(value: ByteArray) = this@Where.table.getColumn(this) NOT_EQUALS value

    infix fun String.NOT_EQUALS(value: Calendar) = this@Where.table.getColumn(this) NOT_EQUALS value

    infix fun String.NOT_EQUALS(value: DataDate) = this@Where.table.getColumn(this) NOT_EQUALS value

    infix fun String.NOT_EQUALS(value: DataTime) = this@Where.table.getColumn(this) NOT_EQUALS value

    infix fun <E : Enum<E>> String.NOT_EQUALS(value: E) = this@Where.table.getColumn(this) NOT_EQUALS value

    // LOWER

    infix fun String.LOWER(value: String) = this@Where.table.getColumn(this) LOWER value

    infix fun String.LOWER(value: Byte) = this@Where.table.getColumn(this) LOWER value

    infix fun String.LOWER(value: Short) = this@Where.table.getColumn(this) LOWER value

    infix fun String.LOWER(value: Int) = this@Where.table.getColumn(this) LOWER value

    infix fun String.LOWER(value: Long) = this@Where.table.getColumn(this) LOWER value

    infix fun String.LOWER(value: Float) = this@Where.table.getColumn(this) LOWER value

    infix fun String.LOWER(value: Double) = this@Where.table.getColumn(this) LOWER value

    infix fun String.LOWER(value: Calendar) = this@Where.table.getColumn(this) LOWER value

    infix fun String.LOWER(value: DataDate) = this@Where.table.getColumn(this) LOWER value

    infix fun String.LOWER(value: DataTime) = this@Where.table.getColumn(this) LOWER value

    // LOWER_EQUALS

    infix fun String.LOWER_EQUALS(value: String) = this@Where.table.getColumn(this) LOWER_EQUALS value

    infix fun String.LOWER_EQUALS(value: Byte) = this@Where.table.getColumn(this) LOWER_EQUALS value

    infix fun String.LOWER_EQUALS(value: Short) = this@Where.table.getColumn(this) LOWER_EQUALS value

    infix fun String.LOWER_EQUALS(value: Int) = this@Where.table.getColumn(this) LOWER_EQUALS value

    infix fun String.LOWER_EQUALS(value: Long) = this@Where.table.getColumn(this) LOWER_EQUALS value

    infix fun String.LOWER_EQUALS(value: Float) = this@Where.table.getColumn(this) LOWER_EQUALS value

    infix fun String.LOWER_EQUALS(value: Double) = this@Where.table.getColumn(this) LOWER_EQUALS value

    infix fun String.LOWER_EQUALS(value: Calendar) = this@Where.table.getColumn(this) LOWER_EQUALS value

    infix fun String.LOWER_EQUALS(value: DataDate) = this@Where.table.getColumn(this) LOWER_EQUALS value

    infix fun String.LOWER_EQUALS(value: DataTime) = this@Where.table.getColumn(this) LOWER_EQUALS value

    // UPPER

    infix fun String.UPPER(value: String) = this@Where.table.getColumn(this) UPPER value

    infix fun String.UPPER(value: Byte) = this@Where.table.getColumn(this) UPPER value

    infix fun String.UPPER(value: Short) = this@Where.table.getColumn(this) UPPER value

    infix fun String.UPPER(value: Int) = this@Where.table.getColumn(this) UPPER value

    infix fun String.UPPER(value: Long) = this@Where.table.getColumn(this) UPPER value

    infix fun String.UPPER(value: Float) = this@Where.table.getColumn(this) UPPER value

    infix fun String.UPPER(value: Double) = this@Where.table.getColumn(this) UPPER value

    infix fun String.UPPER(value: Calendar) = this@Where.table.getColumn(this) UPPER value

    infix fun String.UPPER(value: DataDate) = this@Where.table.getColumn(this) UPPER value

    infix fun String.UPPER(value: DataTime) = this@Where.table.getColumn(this) UPPER value

    // UPPER_EQUALS

    infix fun String.UPPER_EQUALS(value: String) = this@Where.table.getColumn(this) UPPER_EQUALS value

    infix fun String.UPPER_EQUALS(value: Byte) = this@Where.table.getColumn(this) UPPER_EQUALS value

    infix fun String.UPPER_EQUALS(value: Short) = this@Where.table.getColumn(this) UPPER_EQUALS value

    infix fun String.UPPER_EQUALS(value: Int) = this@Where.table.getColumn(this) UPPER_EQUALS value

    infix fun String.UPPER_EQUALS(value: Long) = this@Where.table.getColumn(this) UPPER_EQUALS value

    infix fun String.UPPER_EQUALS(value: Float) = this@Where.table.getColumn(this) UPPER_EQUALS value

    infix fun String.UPPER_EQUALS(value: Double) = this@Where.table.getColumn(this) UPPER_EQUALS value

    infix fun String.UPPER_EQUALS(value: Calendar) = this@Where.table.getColumn(this) UPPER_EQUALS value

    infix fun String.UPPER_EQUALS(value: DataDate) = this@Where.table.getColumn(this) UPPER_EQUALS value

    infix fun String.UPPER_EQUALS(value: DataTime) = this@Where.table.getColumn(this) UPPER_EQUALS value

    // Match select

    @MatchDSL
    infix fun String.IN(matchCreator: Match.() -> Unit) = this@Where.table.getColumn(this) IN matchCreator

    // One of

    infix fun String.ONE_OF_ID(selection: IntArray) = this@Where.table.getColumn(this) ONE_OF_ID selection

    infix fun String.ONE_OF(selection: Array<String>) = this@Where.table.getColumn(this) ONE_OF selection

    infix fun String.ONE_OF(selection: BooleanArray) = this@Where.table.getColumn(this) ONE_OF selection

    infix fun String.ONE_OF(selection: ByteArray) = this@Where.table.getColumn(this) ONE_OF selection

    infix fun String.ONE_OF(selection: ShortArray) = this@Where.table.getColumn(this) ONE_OF selection

    infix fun String.ONE_OF(selection: IntArray) = this@Where.table.getColumn(this) ONE_OF selection

    infix fun String.ONE_OF(selection: LongArray) = this@Where.table.getColumn(this) ONE_OF selection

    infix fun String.ONE_OF(selection: FloatArray) = this@Where.table.getColumn(this) ONE_OF selection

    infix fun String.ONE_OF(selection: DoubleArray) = this@Where.table.getColumn(this) ONE_OF selection

    infix fun String.ONE_OF(selection: Array<ByteArray>) = this@Where.table.getColumn(this) ONE_OF selection

    infix fun String.ONE_OF(selection: Array<Calendar>) = this@Where.table.getColumn(this) ONE_OF selection

    infix fun String.ONE_OF(selection: Array<DataDate>) = this@Where.table.getColumn(this) ONE_OF selection

    infix fun String.ONE_OF(selection: Array<DataTime>) = this@Where.table.getColumn(this) ONE_OF selection

    infix fun <E : Enum<E>> String.ONE_OF(selection: Array<E>) = this@Where.table.getColumn(this) ONE_OF selection

    // Regex

    infix fun Column.REGEX(pattern: Pattern) =
        this.regex(this@Where.table, pattern)

    infix fun String.REGEX(pattern: Pattern) =
        this@Where.table.getColumn(this)
            .regex(this@Where.table, pattern)
}