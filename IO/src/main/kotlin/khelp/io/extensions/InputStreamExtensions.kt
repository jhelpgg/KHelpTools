package khelp.io.extensions

import java.io.IOException
import java.io.InputStream
import java.math.BigInteger
import java.util.Objects

/**
 * Read stream to fill given array with the number of bytes requested.
 *
 * The method assure the number of bytes read are the required [length] except if stream have no enough bytes to read. In this case the maximum can be read is read.
 *
 * @return Number bytes read. Usually the required [length]. It is lower if no enough bytes left in stream.
 */
fun InputStream.readFully(byteArray: ByteArray, offset: Int = 0, length: Int = byteArray.size - offset): Int
{
    Objects.checkFromIndexSize(offset, byteArray.size, length)

    if (length == 0)
    {
        return 0
    }

    var read = this.read(byteArray, offset, length)

    if (read < 0)
    {
        return read
    }

    var totalRead = read

    while (totalRead < length)
    {
        read = this.read(byteArray, offset + totalRead, length - totalRead)

        if (read < 0)
        {
            return totalRead
        }

        totalRead += read
    }

    return totalRead
}

/**
 * Read at maximum given number of bytes. The method respect the number required if stream have enough dtata left.
 * @return Bytes read
 */
fun InputStream.readSomeBytes(number: Int): ByteArray
{
    val array = ByteArray(number)
    val read = this.readFully(array)

    if (read < number)
    {
        return array.copyOf(read)
    }

    return array
}

fun InputStream.readInt(): Int
{
    val data = this.readSomeBytes(4)

    if (data.size < 4)
    {
        throw IOException("No enough data to read Int")
    }

    return ((data[0].toInt() and 0xFF) shl 24) or
            ((data[1].toInt() and 0xFF) shl 16) or
            ((data[2].toInt() and 0xFF) shl 8) or
            (data[3].toInt() and 0xFF)
}

fun InputStream.readByteArray(): ByteArray
{
    val size = this.readInt()
    val data = this.readSomeBytes(size)

    if (data.size < size)
    {
        throw IOException("No enough data to read byte array size $size")
    }

    return data
}

fun InputStream.readBigInteger(): BigInteger =
    BigInteger(this.readByteArray())
