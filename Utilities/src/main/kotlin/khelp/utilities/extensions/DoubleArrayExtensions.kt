package khelp.utilities.extensions

import java.util.Objects

/**
 * String representation with customizable header, separator and footer
 */
fun DoubleArray.string(header: String = "[", separator: String = ", ", footer: String = "]"): String
{
    val stringBuilder = StringBuilder()
    stringBuilder.append(header)

    if (this.isNotEmpty())
    {
        stringBuilder.append(this[0])

        for (index in 1 until this.size)
        {
            stringBuilder.append(separator)
            stringBuilder.append(this[index])
        }
    }

    stringBuilder.append(footer)
    return stringBuilder.toString()
}

fun DoubleArray.same(other: DoubleArray): Boolean
{
    val size = this.size

    if (size != other.size)
    {
        return false
    }

    for (index in 0 until size)
    {
        if (this[index] != other[index])
        {
            return false
        }
    }

    return true
}

val DoubleArray.hash: Int
    get()
    {
        var hash = this.size

        for (element in this)
        {
            hash = hash * 31 + Objects.hashCode(element)
        }

        return hash
    }
