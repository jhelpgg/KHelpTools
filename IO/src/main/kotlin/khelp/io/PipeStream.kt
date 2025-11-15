package khelp.io

import java.io.InputStream
import java.io.OutputStream
import khelp.utilities.collections.CycleByteArray
import kotlin.math.min

class PipeStream()
{
    private val cycleByteArray = CycleByteArray()
    private val lock = Object()
    private var closed = false
    val inputStream : InputStream = PipeInputStream(this)
    val outputStream : OutputStream = PipeOutputStream(this)

    internal fun close()
    {
        this.closed = true

        synchronized(this.lock)
        {
            this.lock.notifyAll()
        }
    }

    internal fun read() : Int =
        synchronized(this.lock)
        {
            while (!this.closed && this.cycleByteArray.empty)
            {
                this.lock.wait()
            }

            if (this.cycleByteArray.empty)
            {
                -1
            }
            else
            {
                this.cycleByteArray.read().toInt() and 0xFF
            }
        }

    internal fun read(byteArray : ByteArray, offset : Int = 0, length : Int = byteArray.size - offset) : Int
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

        return synchronized(this.lock)
        {
            while (!this.closed && this.cycleByteArray.size < size)
            {
                this.lock.wait()
            }

            if (this.cycleByteArray.empty)
            {
                -1
            }
            else
            {
                this.cycleByteArray.read(byteArray, off, size)
            }
        }
    }

    internal fun write(byte : Int)
    {
        synchronized(this.lock)
        {
            this.cycleByteArray.write(byte.toByte())
            this.lock.notifyAll()
        }
    }

    internal fun write(byteArray : ByteArray, offset : Int = 0, length : Int = byteArray.size - offset) : Int
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

        return synchronized(this.lock)
        {
            val written = this.cycleByteArray.write(byteArray, off, size)
            this.lock.notifyAll()
            written
        }
    }
}