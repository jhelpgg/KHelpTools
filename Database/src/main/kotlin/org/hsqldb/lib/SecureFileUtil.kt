package org.hsqldb.lib

import java.io.InputStream
import java.io.OutputStream
import khelp.security.rsa.RSADecryptInputStream
import khelp.security.rsa.RSAEncryptOutputStream
import khelp.security.rsa.RSAKeyPair

private fun OutputStream.embed(): OutputStream =
    if (this is RSAEncryptOutputStream)
    {
        this.encryptedStream
    }
    else this

internal class SecureFileUtil : FileUtil()
{
    private val keyPairs = ArrayList<Pair<String, RSAKeyPair>>()
    private val reference = FileUtil.getFileUtil()

    fun associate(path: String, rsaKeyPair: RSAKeyPair)
    {
        this.keyPairs += Pair(path, rsaKeyPair)
    }

    private fun obtainKeyPair(path: String): RSAKeyPair
    {
        for ((source, keyPair) in this.keyPairs)
        {
            if (path.startsWith(source) && path.indexOf('/', source.length) < 0)
            {
                return keyPair
            }
        }

        throw RuntimeException("No key pair found for $path")
    }

    override fun openInputStreamElement(path: String): InputStream
    {
        return RSADecryptInputStream(this.obtainKeyPair(path), this.reference.openInputStreamElement(path))
    }

    override fun openOutputStreamElement(path: String): OutputStream
    {
        return RSAEncryptOutputStream(this.obtainKeyPair(path).publicKey, this.reference.openOutputStreamElement(path))
    }

    override fun isStreamElement(path: String): Boolean
    {
        return this.reference.isStreamElement(path)
    }

    override fun createParentDirs(path: String)
    {
        this.reference.createParentDirs(path)
    }

    override fun removeElement(path: String): Boolean
    {
        return this.reference.removeElement(path)
    }

    override fun renameElement(pathOld: String, pathNew: String): Boolean
    {
        return this.reference.renameElement(pathOld, pathNew)
    }

    override fun getFileSync(stream: OutputStream): FileAccess.FileSync
    {
        return this.reference.getFileSync(stream.embed())
    }

    override fun openOutputStreamElementAppend(streamName: String): OutputStream
    {
        return RSAEncryptOutputStream(this.obtainKeyPair(streamName).publicKey,
                                      this.reference.openOutputStreamElementAppend(streamName))
    }

    override fun renameElementOrCopy(oldName: String, newName: String): Boolean
    {
        return this.reference.renameElementOrCopy(oldName, newName)
    }
}
