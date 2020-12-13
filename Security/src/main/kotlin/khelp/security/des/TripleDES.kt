package khelp.security.des

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class TripleDES(login: String, password: String)
{
    private val first = CrypterDES(login)
    private val second = CrypterDES("${login}_${password}")
    private val third = CrypterDES(password)

    @Throws(IOException::class)
    fun encrypt(clearStream: InputStream, encryptedStream: OutputStream)
    {
        var byteArrayOutputStream = ByteArrayOutputStream()
        this.first.encrypt(clearStream, byteArrayOutputStream)
        var byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        byteArrayOutputStream = ByteArrayOutputStream()
        this.second.encrypt(byteArrayInputStream, byteArrayOutputStream)
        byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        this.third.encrypt(byteArrayInputStream, encryptedStream)
    }

    @Throws(IOException::class)
    fun decrypt(encryptedStream: InputStream, clearStream: OutputStream)
    {
        var byteArrayOutputStream = ByteArrayOutputStream()
        this.third.decrypt(encryptedStream, byteArrayOutputStream)
        var byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        byteArrayOutputStream = ByteArrayOutputStream()
        this.second.decrypt(byteArrayInputStream, byteArrayOutputStream)
        byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        this.first.decrypt(byteArrayInputStream, clearStream)
    }

    fun valid(login: String, password: String) =
        this.first.passwordValid(login) && this.third.passwordValid(password)

    internal fun sameKeys(other: TripleDES) =
        this.first.sameKey(other.first) && this.second.sameKey(other.second) && this.third.sameKey(other.third)
}