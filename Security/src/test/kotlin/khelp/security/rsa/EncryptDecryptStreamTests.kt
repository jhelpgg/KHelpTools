package khelp.security.rsa

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import khelp.io.extensions.readSomeBytes
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

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
        Assertions.assertEquals(1024, readData.size, "Data size")

        for (index in 0 until 1024)
        {
            Assertions.assertEquals(data[index], readData[index], "index=$index")
        }
    }
}