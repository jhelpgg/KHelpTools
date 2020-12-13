package khelp.security.des

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import khelp.utilities.extensions.same

class CrypterDES(password: String)
{
    companion object
    {
        private fun computeKey(string: String): ByteArray
        {
            var hash: Long = 0
            string.toCharArray()
                .forEach { hash = 31L * hash + it.toLong() }
            val key = ByteArray(8)

            (0..7).forEach { index ->
                key[index] = (hash and 0xFF).toByte()
                hash = hash shr 8
            }

            return key
        }
    }

    private var lastPasswordCheckTime = 0L
    private var timeBetweenAttempt = 128L
    private val key = CrypterDES.computeKey(password)

    @Throws(IOException::class)
    fun encrypt(clearStream: InputStream, encryptedStream: OutputStream)
    {
        this.desOperation(Cipher.ENCRYPT_MODE, clearStream, encryptedStream)
    }

    @Throws(IOException::class)
    fun decrypt(encryptedStream: InputStream, clearStream: OutputStream)
    {
        this.desOperation(Cipher.DECRYPT_MODE, encryptedStream, clearStream)
    }

    @Synchronized
    fun passwordValid(password: String): Boolean
    {
        // To avoid multiple attempt in short time, wait more and more time between each attempt
        val waitBeforeTest = this.timeBetweenAttempt - (System.currentTimeMillis() - this.lastPasswordCheckTime)

        if (waitBeforeTest > 0L)
        {
            Thread.sleep(waitBeforeTest)
        }

        this.lastPasswordCheckTime = System.currentTimeMillis()
        this.timeBetweenAttempt *= 2L

        val key = CrypterDES.computeKey(password)
        val valid = this.key.same(key)
        //To avoid "time attack"
        Thread.sleep((1L..128L).random())
        return valid
    }

    internal fun sameKey(other: CrypterDES) =
        this.key.same(other.key)

    /**
     * Encrypt or decrypt data
     * @param mode Encryption or decryption mode
     * @param inputStream Stream to encrypt/decrypt
     * @param outputStream Stream where write the result
     */
    @Throws(IOException::class)
    private fun desOperation(mode: Int, inputStream: InputStream, outputStream: OutputStream)
    {
        val desKeySpec = DESKeySpec(this.key)
        val keyFactory = SecretKeyFactory.getInstance("DES")
        val secretKey = keyFactory.generateSecret(desKeySpec)
        val cipher = Cipher.getInstance("DES")
        cipher.init(mode, secretKey)
        val cipherInputStream = CipherInputStream(inputStream, cipher)
        val buffer = ByteArray(8)
        var read = cipherInputStream.read(buffer)

        while (read >= 0)
        {
            outputStream.write(buffer, 0, read)
            read = cipherInputStream.read(buffer)
        }

        outputStream.flush()
        outputStream.close()
        cipherInputStream.close()
    }
}
