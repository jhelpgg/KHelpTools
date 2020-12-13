package khelp.security.rsa

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import khelp.io.extensions.readSomeBytes
import org.junit.Assert
import org.junit.Test

class EncryptDecryptStreamTests
{
    @Test
    fun encryptDecrypt()
    {
        val keyPair = RSAKeyPair()
        val byteArrayOutputStream = ByteArrayOutputStream()
        val encryptStream = RSAEncryptOutputStream(keyPair.publicKey, byteArrayOutputStream)
        val data = ByteArray(1024) { index -> index.toByte() }
        encryptStream.write(data)
        encryptStream.flush()
        encryptStream.close()
        val clearStream = RSADecryptInputStream(keyPair, ByteArrayInputStream(byteArrayOutputStream.toByteArray()))
        val readData = clearStream.readSomeBytes(4096)
        clearStream.close()
        Assert.assertEquals("Data size", 1024, readData.size)

        for (index in 0 until 1024)
        {
            Assert.assertEquals("index=$index", data[index], readData[index])
        }
    }
}