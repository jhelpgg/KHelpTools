package khelp.utilities.extensions

import java.io.ByteArrayInputStream
import java.util.Base64

val ByteArray.utf8 get() = String(this, Charsets.UTF_8)

val ByteArray.base64
    get() = Base64.getEncoder()
        .encodeToString(this)

/**
 * String representation with customizable header, separator and footer
 */
fun ByteArray.string(header: String = "[", separator: String = ", ", footer: String = "]"): String
{
    val stringBuilder = StringBuilder()
    stringBuilder.append(header)

    if (this.isNotEmpty())
    {
        stringBuilder.appendHexadecimal(this[0])

        for (index in 1 until this.size)
        {
            stringBuilder.append(separator)
            stringBuilder.appendHexadecimal(this[index])
        }
    }

    stringBuilder.append(footer)
    return stringBuilder.toString()
}

fun ByteArray.same(other: ByteArray): Boolean
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

fun ByteArray.parseToIntArray(): IntArray
{
    val byteArrayInputStream = ByteArrayInputStream(this)
    return IntArray(this.size / 4) {
        val data0 = byteArrayInputStream.read()
        val data1 = byteArrayInputStream.read()
        val data2 = byteArrayInputStream.read()
        val data3 = byteArrayInputStream.read()
        ((data0 and 0xFF) shl 24) or
                ((data1 and 0xFF) shl 16) or
                ((data2 and 0xFF) shl 8) or
                (data3 and 0xFF)
    }
}
