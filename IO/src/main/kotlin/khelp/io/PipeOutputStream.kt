package khelp.io

import java.io.OutputStream

internal class PipeOutputStream(private val pipeStream: PipeStream) : OutputStream()
{
    override fun close()
    {
        this.pipeStream.close()
    }

    override fun write(byte: Int)
    {
        this.pipeStream.write(byte)
    }

    override fun write(byteArray: ByteArray)
    {
        this.pipeStream.write(byteArray)
    }

    override fun write(byteArray: ByteArray, offset: Int, length: Int)
    {
        this.pipeStream.write(byteArray, offset, length)
    }
}