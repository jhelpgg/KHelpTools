package khelp.io

import java.io.InputStream

internal class PipeInputStream(private val pipeStream: PipeStream) : InputStream()
{
    override fun read(): Int =
        this.pipeStream.read()

    override fun read(byteArray: ByteArray): Int =
        this.pipeStream.read(byteArray)

    override fun read(byteArray: ByteArray, offset: Int, length: Int): Int =
        this.pipeStream.read(byteArray, offset, length)
}