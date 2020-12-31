package khelp.utilities.collections

import khelp.utilities.elementExistsCheck
import khelp.utilities.extensions.appendHexadecimal
import kotlin.math.max
import kotlin.math.min

/**
 * Byte array where elements are internally manage in cycle
 */
class CycleByteArray(startSize: Int = 4096)
{
    private var array = ByteArray(max(8, startSize))
    private var readIndex = 0
    private var writeIndex = 0

    val size
        get() =
            if (this.writeIndex >= this.readIndex)
            {
                this.writeIndex - this.readIndex
            }
            else
            {
                this.array.size - this.readIndex + this.writeIndex
            }

    val empty get() = this.size == 0

    val notEmpty get() = this.size != 0

    fun write(byte: Byte)
    {
        this.expandIfNeed(1)
        this.array[this.writeIndex] = byte
        this.writeIndex = (this.writeIndex + 1) % this.array.size
    }

    fun write(byteArray: ByteArray, offset: Int = 0, length: Int = byteArray.size - offset): Int
    {
        var len = length
        var off = offset

        if (off < 0)
        {
            len += off
            off = 0
        }

        val size = min(len, byteArray.size - off)

        if (size <= 0)
        {
            return 0
        }

        this.expandIfNeed(size)
        val number = min(size, this.array.size - this.writeIndex)
        System.arraycopy(byteArray, off, this.array, this.writeIndex, number)

        if (number < size)
        {
            System.arraycopy(byteArray, off + number, this.array, 0, size - number)
        }

        this.writeIndex = (this.writeIndex + size) % this.array.size
        return size
    }

    fun read(): Byte
    {
        elementExistsCheck(this.size > 0) { "The array is empty" }
        val value = this.array[this.readIndex]
        this.readIndex = (this.readIndex + 1) % this.array.size
        return value
    }

    fun read(byteArray: ByteArray, offset: Int = 0, length: Int = byteArray.size - offset): Int
    {
        var len = length
        var off = offset

        if (off < 0)
        {
            len += off
            off = 0
        }

        val size = min(min(len, byteArray.size - off), this.size)

        if (size <= 0)
        {
            return 0
        }

        val number = min(size, this.array.size - this.readIndex)
        System.arraycopy(this.array, this.readIndex, byteArray, off, number)

        if (number < size)
        {
            System.arraycopy(this.array, 0, byteArray, off + number, size - number)
        }

        this.readIndex = (this.readIndex + size) % this.array.size
        return size
    }

    override fun toString(): String
    {
        val stringBuilder = StringBuilder()
        stringBuilder.append('[')
        var index = this.readIndex

        while (index != this.writeIndex)
        {
            stringBuilder.appendHexadecimal(this.array[index])
            index = (index + 1) % this.array.size

            if (index != this.writeIndex)
            {
                stringBuilder.append(" ; ")
            }
        }

        stringBuilder.append(']')
        return stringBuilder.toString()
    }

    internal fun debugString(): String
    {
        val stringBuilder = StringBuilder()

        for ((index, byte) in this.array.withIndex())
        {
            if (index == this.readIndex)
            {
                stringBuilder.append("R:")
            }

            if (index == this.writeIndex)
            {
                stringBuilder.append("W:")
            }

            stringBuilder.append(index)
            stringBuilder.append(':')
            stringBuilder.appendHexadecimal(byte)
            stringBuilder.append(' ')
        }

        return stringBuilder.toString()
    }

    private fun expandIfNeed(more: Int)
    {
        if (this.readIndex <= this.writeIndex)
        {
            var futureWriteIndex = this.writeIndex + more

            if (futureWriteIndex >= this.array.size)
            {
                futureWriteIndex -= this.array.size

                if (futureWriteIndex >= this.readIndex)
                {
                    var size = this.array.size + more
                    size += size shr 3
                    val temporary = ByteArray(size)
                    System.arraycopy(this.array, this.readIndex, temporary, 0, this.writeIndex - this.readIndex)
                    this.writeIndex -= this.readIndex
                    this.readIndex = 0
                    this.array = temporary
                }
            }

            return
        }

        if (this.writeIndex + more >= this.readIndex)
        {
            var size = this.array.size + more
            size += size shr 3
            val temporary = ByteArray(size)
            System.arraycopy(this.array, this.readIndex, temporary, 0, this.array.size - this.readIndex)
            System.arraycopy(this.array, 0, temporary, this.array.size - this.readIndex, this.writeIndex)
            this.writeIndex += this.array.size - this.readIndex
            this.readIndex = 0
            this.array = temporary
        }
    }
}