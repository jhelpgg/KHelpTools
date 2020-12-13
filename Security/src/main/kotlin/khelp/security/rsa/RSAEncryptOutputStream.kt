package khelp.security.rsa

import java.io.BufferedOutputStream
import java.io.OutputStream
import java.util.Objects
import khelp.utilities.collections.CycleByteArray

class RSAEncryptOutputStream(publicKey: RSAPublicKey, val encryptedStream: OutputStream) : OutputStream()
{
    private val cipher = publicKey.cipher()
    private val bufferedOutputStream = BufferedOutputStream(encryptedStream)
    private val cycleByteArray = CycleByteArray()

    override fun close()
    {
        this.bufferedOutputStream.close()
    }

    override fun flush()
    {
        this.doTransfer(true)
        this.bufferedOutputStream.flush()
    }

    override fun write(b: Int)
    {
        this.write(byteArrayOf(b.toByte()), 0, 1)
    }

    override fun write(b: ByteArray, off: Int, len: Int)
    {
        Objects.checkFromIndexSize(off, len, b.size)

        if (len == 0)
        {
            return
        }

        this.cycleByteArray.write(b, off, len)
        this.doTransfer(false)
    }

    private fun doTransfer(flush: Boolean)
    {
        while (this.cycleByteArray.notEmpty && (flush || this.cycleByteArray.size >= 245))
        {
            val temp = ByteArray(245)
            val read = this.cycleByteArray.read(temp, 0, 245)
            val crypted = this.cipher.doFinal(cipher.update(temp, 0, read))
            this.bufferedOutputStream.write(crypted.size % 256)
            this.bufferedOutputStream.write(crypted)
        }
    }
}