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

internal class SecureFileUtil(private val rsaKeyPair: RSAKeyPair) : FileUtil()
{
    private val reference = FileUtil.getFileUtil()

    override fun openInputStreamElement(path: String): InputStream
    {
        return RSADecryptInputStream(this.rsaKeyPair, this.reference.openInputStreamElement(path))
    }

    override fun openOutputStreamElement(path: String): OutputStream
    {
        return RSAEncryptOutputStream(this.rsaKeyPair.publicKey, this.reference.openOutputStreamElement(path))
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
        return RSAEncryptOutputStream(this.rsaKeyPair.publicKey,
                                      this.reference.openOutputStreamElementAppend(streamName))
    }

    override fun renameElementOrCopy(oldName: String, newName: String): Boolean
    {
        return this.reference.renameElementOrCopy(oldName, newName)
    }
}
