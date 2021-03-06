package khelp.database.condition

import java.util.regex.Pattern
import khelp.database.COLUMN_ID
import khelp.database.Column
import khelp.database.Table
import khelp.database.type.DataType
import khelp.utilities.regex.RegularExpression

/**
 * Create condition that select rows, in given column, with values match given regular expression
 */
fun Column.regex(table: Table, pattern: Pattern): Condition
{
    table.checkColumn(this)
    this.checkType(DataType.STRING)
    val identifiers = ArrayList<Int>()
    val result = table.select {
        +COLUMN_ID
        +this@regex
    }

    while (result.hasNext)
    {
        result.next {
            if (pattern.matcher(getString(2))
                    .matches())
            {
                identifiers += getID(1)
            }
        }
    }

    result.close()
    val ids = IntArray(identifiers.size) { index -> identifiers[index] }
    return COLUMN_ID ONE_OF_ID ids
}

/**
 * Create condition that select rows, in given column, with values match given regular expression
 */
fun Column.regex(table: Table, regularExpression: RegularExpression): Condition
{
    table.checkColumn(this)
    this.checkType(DataType.STRING)
    val identifiers = ArrayList<Int>()
    val result = table.select {
        +COLUMN_ID
        +this@regex
    }

    while (result.hasNext)
    {
        result.next {
            if (regularExpression.matches(getString(2)))
            {
                identifiers += getID(1)
            }
        }
    }

    result.close()
    val ids = IntArray(identifiers.size) { index -> identifiers[index] }
    return COLUMN_ID ONE_OF_ID ids
}
