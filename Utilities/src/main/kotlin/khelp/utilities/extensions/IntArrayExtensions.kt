package khelp.utilities.extensions

import java.io.ByteArrayOutputStream

/**
 * String representation with customizable header, separator and footer
 */
fun IntArray.string(header: String = "[", separator: String = ", ", footer: String = "]"): String
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

fun IntArray.same(other: IntArray): Boolean
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

fun IntArray.serializeToByteArray(): ByteArray
{
    val byteArrayOutputStream = ByteArrayOutputStream()

    for (integer in this)
    {
        byteArrayOutputStream.write(byteArrayOf(((integer shr 24) and 0xFF).toByte(),
                                                ((integer shr 16) and 0xFF).toByte(),
                                                ((integer shr 8) and 0xFF).toByte(),
                                                (integer and 0xFF).toByte()))
    }

    byteArrayOutputStream.flush()
    byteArrayOutputStream.close()
    return byteArrayOutputStream.toByteArray()
}
