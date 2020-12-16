package khelp.security.rsa

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Signature
import java.security.spec.RSAPublicKeySpec
import javax.crypto.Cipher
import khelp.io.extensions.readBigInteger
import khelp.io.extensions.readFully
import khelp.io.extensions.writeBigInteger

/**
 * RSA public key can be shared to user.
 *
 * Used to encrypt data for [RSAKeyPair] owner
 */
class RSAPublicKey
{
    private val publicKey: PublicKey

    internal constructor(publicKey: PublicKey)
    {
        this.publicKey = publicKey
    }

    constructor(inputStream: InputStream)
    {
        val keyFactory = KeyFactory.getInstance(ALGORITHM_RSA)

        val modulus = inputStream.readBigInteger()
        val exponent = inputStream.readBigInteger()

        val publicKeySpec = RSAPublicKeySpec(modulus, exponent)
        this.publicKey = keyFactory.generatePublic(publicKeySpec)
    }

    fun save(outputStream: OutputStream)
    {
        val keyFactory = KeyFactory.getInstance(ALGORITHM_RSA)

        val publicKeySpec = keyFactory.getKeySpec(this.publicKey,
                                                  RSAPublicKeySpec::class.java)

        outputStream.writeBigInteger(publicKeySpec.modulus)
        outputStream.writeBigInteger(publicKeySpec.publicExponent)
        outputStream.flush()
    }

    internal fun cipher(): Cipher
    {
        val cipher = Cipher.getInstance(RSA_CIPHER)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return cipher
    }

    fun encrypt(clearStream: InputStream, encryptedStream: OutputStream)
    {
        val cipher = Cipher.getInstance(RSA_CIPHER)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val temp = ByteArray(245)
        var crypted: ByteArray

        var read: Int = clearStream.readFully(temp)

        while (read >= 0)
        {
            crypted = cipher.doFinal(cipher.update(temp, 0, read))
            encryptedStream.write(crypted.size % 256)
            encryptedStream.write(crypted)
            read = clearStream.readFully(temp)
        }

        encryptedStream.flush()
    }

    fun validSignature(message: InputStream, signature: InputStream): Boolean
    {
        val sign = Signature.getInstance(RSA_SIGNATURE)
        sign.initVerify(this.publicKey)
        var temp = ByteArray(4096)
        var read = message.read(temp)

        while (read >= 0)
        {
            sign.update(temp, 0, read)

            read = message.read(temp)
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        read = signature.read(temp)

        while (read >= 0)
        {
            byteArrayOutputStream.write(temp, 0, read)

            read = signature.read(temp)
        }

        byteArrayOutputStream.flush()
        temp = byteArrayOutputStream.toByteArray()
        byteArrayOutputStream.close()

        return sign.verify(temp)
    }
}
