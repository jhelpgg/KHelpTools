package khelp.security.rsa

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.Signature
import java.security.spec.RSAPrivateKeySpec
import javax.crypto.Cipher
import khelp.io.extensions.readBigInteger
import khelp.io.extensions.readFully
import khelp.io.extensions.writeBigInteger
import khelp.security.des.TripleDES
import khelp.security.exception.LoginPasswordInvalidException

/**
 * RSA key pai.
 *
 * Since it contains the private key, be careful if share the instance.
 *
 * Only save it in a trust and secure place.
 */
class RSAKeyPair
{
    private val privateKey: PrivateKey
    val publicKey: RSAPublicKey
    private var tripleDES: TripleDES? = null

    constructor()
    {
        val generator = KeyPairGenerator.getInstance(ALGORITHM_RSA)
        generator.initialize(2048)

        val keyPair = generator.generateKeyPair()
        this.privateKey = keyPair.private
        this.publicKey = RSAPublicKey(keyPair.public)
    }

    constructor(tripleDES: TripleDES, inputStream: InputStream)
    {
        try
        {
            this.tripleDES = tripleDES
            val byteArrayOutputStream = ByteArrayOutputStream()
            tripleDES.decrypt(inputStream, byteArrayOutputStream)
            inputStream.close()
            val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
            val modulus = byteArrayInputStream.readBigInteger()
            val exponent = byteArrayInputStream.readBigInteger()
            val privateKeySpec = RSAPrivateKeySpec(modulus, exponent)
            this.privateKey = KeyFactory.getInstance(ALGORITHM_RSA)
                .generatePrivate(privateKeySpec)
            this.publicKey = RSAPublicKey(byteArrayInputStream)
        }
        catch (exception: Exception)
        {
            throw LoginPasswordInvalidException
        }
    }

    fun save(tripleDES: TripleDES, outputStream: OutputStream)
    {
        val actualTripleDES = this.tripleDES

        if (actualTripleDES == null)
        {
            this.tripleDES = tripleDES
        }
        else if (!actualTripleDES.sameKeys(tripleDES))
        {
            Thread.sleep((128L..1024L).random())
            throw LoginPasswordInvalidException
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        val keyFactory = KeyFactory.getInstance(ALGORITHM_RSA)
        val privateKeySpec = keyFactory.getKeySpec(this.privateKey,
                                                   RSAPrivateKeySpec::class.java)
        byteArrayOutputStream.writeBigInteger(privateKeySpec.modulus)
        byteArrayOutputStream.writeBigInteger(privateKeySpec.privateExponent)
        this.publicKey.save(byteArrayOutputStream)
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        tripleDES.encrypt(byteArrayInputStream, outputStream)
        outputStream.flush()
        outputStream.close()
    }

    internal fun cypher(): Cipher
    {
        val cipher = Cipher.getInstance(RSA_CIPHER)
        cipher.init(Cipher.DECRYPT_MODE, this.privateKey)
        return cipher
    }

    @Throws(IOException::class)
    fun decrypt(inputStream: InputStream, outputStream: OutputStream)
    {
        val cipher = Cipher.getInstance(RSA_CIPHER)
        cipher.init(Cipher.DECRYPT_MODE, this.privateKey)
        val temp = ByteArray(256)
        var size = inputStream.read()

        if (size == 0)
        {
            size = 256
        }

        var read: Int = inputStream.readFully(temp, 0, size)

        while (read >= 0 && size >= 0)
        {
            outputStream.write(cipher.doFinal(temp, 0, read))
            size = inputStream.read()

            if (size >= 0)
            {
                if (size == 0)
                {
                    size = 256
                }

                read = inputStream.readFully(temp, 0, size)
            }
        }

        outputStream.flush()
    }

    @Throws(IOException::class)
    fun sign(message: InputStream, signature: OutputStream)
    {
        val sign = Signature.getInstance(RSA_SIGNATURE)
        sign.initSign(this.privateKey)
        val temp = ByteArray(4096)
        var read = message.read(temp)

        while (read >= 0)
        {
            sign.update(temp, 0, read)
            read = message.read(temp)
        }

        signature.write(sign.sign())
        signature.flush()
    }
}