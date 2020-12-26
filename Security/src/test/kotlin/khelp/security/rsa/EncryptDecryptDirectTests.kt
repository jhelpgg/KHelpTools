package khelp.security.rsa

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import khelp.security.des.TripleDES
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EncryptDecryptDirectTests
{
    @Test
    fun encryptDecrypt()
    {
        val keyPair = RSAKeyPair()
        var byteArrayOutputStream = ByteArrayOutputStream()
        val data = ByteArray(1024) { index -> index.toByte() }
        keyPair.publicKey.encrypt(ByteArrayInputStream(data), byteArrayOutputStream)
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        byteArrayOutputStream = ByteArrayOutputStream()
        keyPair.decrypt(byteArrayInputStream, byteArrayOutputStream)
        val readData = byteArrayOutputStream.toByteArray()
        Assertions.assertEquals(1024, readData.size, "Data size")

        for (index in 0 until 1024)
        {
            Assertions.assertEquals(data[index], readData[index], "index=$index")
        }
    }

    @Test
    fun encryptDecryptTwoInstances()
    {
        val keyPair = RSAKeyPair()
        var byteArrayOutputStream = ByteArrayOutputStream()
        val data = ByteArray(1024) { index -> index.toByte() }
        keyPair.publicKey.encrypt(ByteArrayInputStream(data), byteArrayOutputStream)
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())

        byteArrayOutputStream = ByteArrayOutputStream()
        keyPair.save(TripleDES("login", "password"), byteArrayOutputStream)
        val keyPair2 = RSAKeyPair(TripleDES("login", "password"),
                                  ByteArrayInputStream(byteArrayOutputStream.toByteArray()))

        byteArrayOutputStream = ByteArrayOutputStream()
        keyPair2.decrypt(byteArrayInputStream, byteArrayOutputStream)
        val readData = byteArrayOutputStream.toByteArray()
        Assertions.assertEquals(1024, readData.size, "Data size")

        for (index in 0 until 1024)
        {
            Assertions.assertEquals(data[index], readData[index], "index=$index")
        }
    }
}