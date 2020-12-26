package khelp.security.rsa

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import khelp.security.des.TripleDES
import khelp.security.exception.LoginPasswordInvalidException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RSAKeyPairTests
{
    @Test
    fun security()
    {
        val keyPair = RSAKeyPair()
        val byteArrayOutputStream = ByteArrayOutputStream()
        keyPair.save(TripleDES("login", "password"), byteArrayOutputStream)
        val data = byteArrayOutputStream.toByteArray()

        RSAKeyPair(TripleDES("login", "password"), ByteArrayInputStream(data))

        try
        {
            RSAKeyPair(TripleDES("other login", "password"), ByteArrayInputStream(data))
            Assertions.fail("Should throw LoginPasswordInvalidException")
        }
        catch (exception: LoginPasswordInvalidException)
        {
            Assertions.assertSame(LoginPasswordInvalidException, exception)
        }


        try
        {
            RSAKeyPair(TripleDES("login", "other password"), ByteArrayInputStream(data))
            Assertions.fail("Should throw LoginPasswordInvalidException")
        }
        catch (exception: LoginPasswordInvalidException)
        {
            Assertions.assertSame(LoginPasswordInvalidException, exception)
        }

        try
        {
            RSAKeyPair(TripleDES("other login", "other password"), ByteArrayInputStream(data))
            Assertions.fail("Should throw LoginPasswordInvalidException")
        }
        catch (exception: LoginPasswordInvalidException)
        {
            Assertions.assertSame(LoginPasswordInvalidException, exception)
        }
    }
}