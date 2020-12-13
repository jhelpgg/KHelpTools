package khelp.security.rsa

import java.io.BufferedInputStream
import java.io.InputStream
import java.util.Objects
import khelp.io.extensions.readFully
import khelp.io.extensions.readSomeBytes
import khelp.utilities.collections.CycleByteArray

class RSADecryptInputStream(keyPair: RSAKeyPair, encryptedStream: InputStream) : InputStream()
{
    private val cipher = keyPair.cypher()
    private val bufferedInputStream = BufferedInputStream(encryptedStream)
    private val cycleByteArray = CycleByteArray()
    private var finished = false

    override fun close()
    {
        this.bufferedInputStream.close()
    }

    override fun read(): Int
    {
        this.assureHave(1)

        if (this.cycleByteArray.empty)
        {
            return -1
        }

        return this.cycleByteArray.read()
            .toInt() and 0xFF
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int
    {
        Objects.checkFromIndexSize(off, len, b.size)

        if (len == 0)
        {
            return 0
        }

        this.assureHave(len)

        if (this.cycleByteArray.empty)
        {
            return -1
        }

        return this.cycleByteArray.read(b, off, len)
    }

    override fun readNBytes(len: Int): ByteArray =
        this.readSomeBytes(len)

    override fun readNBytes(b: ByteArray, off: Int, len: Int): Int =
        this.readFully(b, off, len)

    private fun assureHave(number: Int)
    {
        if (this.finished || this.cycleByteArray.size >= number)
        {
            return
        }

        while (this.cycleByteArray.size < number)
        {
            var size = this.bufferedInputStream.read()

            if (size < 0)
            {
                this.finished = true
                return
            }

            if (size == 0)
            {
                size = 256
            }

            val temp = this.bufferedInputStream.readSomeBytes(size)

            if (temp.isEmpty())
            {
                this.finished = true
                return
            }

            this.cycleByteArray.write(this.cipher.doFinal(temp))
        }
    }
}