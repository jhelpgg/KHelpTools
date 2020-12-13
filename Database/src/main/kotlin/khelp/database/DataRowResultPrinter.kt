package khelp.database

import java.io.PrintStream
import kotlin.math.max

private fun rowString(row: Array<String>, maxWidths: IntArray): String
{
    val stringBuilder = StringBuilder()
    stringBuilder.append("|")
    row.indices.forEach { index ->
        val width = maxWidths[index]
        val value = row[index]
        val length = value.length
        val left = (width - length) / 2
        val right = width - length - left
        stringBuilder.append(" ")
        (0 until left).forEach { _ -> stringBuilder.append(" ") }
        stringBuilder.append(value)
        (0 until right).forEach { _ -> stringBuilder.append(" ") }
        stringBuilder.append(" |")
    }
    return stringBuilder.toString()
}

fun printDataRowResult(dataRowResult: DataRowResult, printStream: PrintStream)
{
    // Collect data and compute cells' width
    val numberColumn = dataRowResult.numberOfColumns
    val maxWidths = IntArray(numberColumn)
    val columns = Array(numberColumn) { index ->
        val column = dataRowResult.column(index).name
        maxWidths[index] = column.length
        column
    }

    val data = ArrayList<Array<String>>()

    while (dataRowResult.hasNext)
    {
        dataRowResult.next {
            data += Array<String>(numberColumn) { index ->
                val value = toString(index+1)
                maxWidths[index] = max(maxWidths[index], value.length)
                value
            }
        }
    }

    dataRowResult.close()

    // Print result
    val separatorCharacters = CharArray(2 + maxWidths.sum() + 2 * numberColumn + 2)
    separatorCharacters[0] = '+'
    var index = 1
    maxWidths.forEach { width ->
        separatorCharacters[index++] = '-'
        (0 until width).forEach { separatorCharacters[index++] = '-' }
        separatorCharacters[index++] = '-'
        separatorCharacters[index++] = '+'
    }
    val separator = String(separatorCharacters, 0, index)
    val table = dataRowResult.table.name
    val space = (separator.length - table.length) / 2 - 1
    val left = separator.length - table.length - space - 2

    printStream.print('+')
    printStream.print(String(CharArray(separator.length - 2) { '-' }))
    printStream.println('+')

    printStream.print('|')
    printStream.print(String(CharArray(space) { ' ' }))
    printStream.print(table)
    printStream.print(String(CharArray(left) { ' ' }))
    printStream.println('|')

    printStream.println(separator)
    printStream.println(rowString(columns, maxWidths))
    printStream.println(separator)

    for (row in data)
    {
        printStream.println(rowString(row, maxWidths))
    }

    printStream.println(separator)
}