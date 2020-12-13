package khelp.security.des

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import khelp.utilities.stream.StringInputStream
import khelp.utilities.stream.StringOutputStream
import org.junit.Assert
import org.junit.Test

class TripleDESTests
{
    @Test
    fun encryptDecrypt()
    {
        val tripleDES = TripleDES("login", "password")
        val byteArrayOutputStream = ByteArrayOutputStream()
        tripleDES.encrypt(StringInputStream("Hello world! This is a test."), byteArrayOutputStream)
        val stringOutputStream = StringOutputStream()
        tripleDES.decrypt(ByteArrayInputStream(byteArrayOutputStream.toByteArray()), stringOutputStream)
        Assert.assertEquals("Hello world! This is a test.", stringOutputStream.string)
    }

    @Test
    fun encryptDecryptTwoInstances()
    {
        val tripleDES1 = TripleDES("login", "password")
        val byteArrayOutputStream = ByteArrayOutputStream()
        tripleDES1.encrypt(StringInputStream("Hello world! This is a test."), byteArrayOutputStream)

        val tripleDES2 = TripleDES("login", "password")
        val stringOutputStream = StringOutputStream()
        tripleDES2.decrypt(ByteArrayInputStream(byteArrayOutputStream.toByteArray()), stringOutputStream)
        Assert.assertEquals("Hello world! This is a test.", stringOutputStream.string)
    }

    @Test
    fun security()
    {
        val tripleDES = TripleDES("login", "password")
        Assert.assertTrue("login and password valid", tripleDES.valid("login", "password"))
        Assert.assertFalse("login invalid,  password valid", tripleDES.valid("other login", "password"))
        Assert.assertFalse("login valid,  password invalid", tripleDES.valid("login", "other password"))
        Assert.assertFalse("login and password invalid", tripleDES.valid("other login", "other password"))
    }
}