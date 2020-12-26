package khelp.security.des

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import khelp.utilities.stream.StringInputStream
import khelp.utilities.stream.StringOutputStream
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CrypterDESTests
{
    @Test
    fun encryptDecrypt()
    {
        val crypter = CrypterDES("Test")

        var byteArrayOutputStream = ByteArrayOutputStream()
        crypter.encrypt(StringInputStream("Hello"), byteArrayOutputStream)
        var byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        var stringOutputStream = StringOutputStream()
        crypter.decrypt(byteArrayInputStream, stringOutputStream)
        Assertions.assertEquals("Hello", stringOutputStream.string)

        byteArrayOutputStream = ByteArrayOutputStream()
        crypter.encrypt(StringInputStream("World"), byteArrayOutputStream)
        byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        stringOutputStream = StringOutputStream()
        crypter.decrypt(byteArrayInputStream, stringOutputStream)
        Assertions.assertEquals("World", stringOutputStream.string)

        byteArrayOutputStream = ByteArrayOutputStream()
        crypter.encrypt(StringInputStream("This is a test"), byteArrayOutputStream)
        byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        stringOutputStream = StringOutputStream()
        crypter.decrypt(byteArrayInputStream, stringOutputStream)
        Assertions.assertEquals("This is a test", stringOutputStream.string)
    }

    @Test
    fun encryptDecryptTwoInstances()
    {
        val crypter1 = CrypterDES("Test")
        val helloOutputStream = ByteArrayOutputStream()
        crypter1.encrypt(StringInputStream("Hello"), helloOutputStream)
        val worldOutputStream = ByteArrayOutputStream()
        crypter1.encrypt(StringInputStream("World"), worldOutputStream)
        val thisIsATestOutputStream = ByteArrayOutputStream()
        crypter1.encrypt(StringInputStream("This is a test"), thisIsATestOutputStream)

        val crypter2 = CrypterDES("Test")
        var stringOutputStream = StringOutputStream()
        crypter2.decrypt(ByteArrayInputStream(helloOutputStream.toByteArray()), stringOutputStream)
        Assertions.assertEquals("Hello", stringOutputStream.string)

        stringOutputStream = StringOutputStream()
        crypter2.decrypt(ByteArrayInputStream(worldOutputStream.toByteArray()), stringOutputStream)
        Assertions.assertEquals("World", stringOutputStream.string)

        stringOutputStream = StringOutputStream()
        crypter2.decrypt(ByteArrayInputStream(thisIsATestOutputStream.toByteArray()), stringOutputStream)
        Assertions.assertEquals("This is a test", stringOutputStream.string)
    }

    @Test
    fun security()
    {
        val crypter = CrypterDES("Password")
        Assertions.assertTrue(crypter.passwordValid("Password"),"Password should be valid")
        Assertions.assertFalse( crypter.passwordValid("Other password"),"Password should be invalid")
    }
}