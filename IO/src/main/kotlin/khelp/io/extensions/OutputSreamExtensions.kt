package khelp.io.extensions

import java.io.OutputStream
import java.math.BigInteger

fun OutputStream.writeInt(int: Int)
{
    this.write(byteArrayOf(((int shr 24) and 0xFF).toByte(),
                           ((int shr 16) and 0xFF).toByte(),
                           ((int shr 8) and 0xFF).toByte(),
                           (int and 0xFF).toByte()))
}

fun OutputStream.writeByteArray(byteArray: ByteArray)
{
    this.writeInt(byteArray.size)
    this.write(byteArray)
}

fun OutputStream.writeBigInteger(bigInteger: BigInteger)
{
    this.writeByteArray(bigInteger.toByteArray())
}
