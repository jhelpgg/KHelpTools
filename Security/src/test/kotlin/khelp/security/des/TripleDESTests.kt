package khelp.security.des

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import khelp.utilities.stream.StringInputStream
import khelp.utilities.stream.StringOutputStream
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

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
        Assertions.assertEquals("Hello world! This is a test.", stringOutputStream.string)
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
        Assertions.assertEquals("Hello world! This is a test.", stringOutputStream.string)
    }

    @Test
    fun security()
    {
        val tripleDES = TripleDES("login", "password")
        Assertions.assertTrue(tripleDES.valid("login", "password"), "login and password valid")
        Assertions.assertFalse(tripleDES.valid("other login", "password"), "login invalid,  password valid")
        Assertions.assertFalse(tripleDES.valid("login", "other password"), "login valid,  password invalid")
        Assertions.assertFalse(tripleDES.valid("other login", "other password"), "login and password invalid")
    }
}