package khelp.io

import khelp.io.extensions.createDirectory
import khelp.io.extensions.createFile
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.net.URLConnection

sealed class ReferenceSource
{
    abstract fun inputStream(path : String) : InputStream
    abstract fun outputStream(path : String) : OutputStream
    abstract fun url(path : String) : URL
}

class ExternalSource(directoryRelativePath : String = "") : ReferenceSource()
{
    private val baseDirectory = obtainExternalFile(directoryRelativePath)

    init
    {
        if (! this.baseDirectory.createDirectory())
        {
            throw IllegalArgumentException("Can't crete/obtain directory : ${this.baseDirectory.absolutePath}")
        }
    }

    override fun inputStream(path : String) : InputStream = FileInputStream(obtainFile(this.baseDirectory, path))

    override fun outputStream(path : String) : OutputStream
    {
        val file = obtainFile(this.baseDirectory, path)

        if (! file.createFile())
        {
            throw IllegalArgumentException("Can't crete/obtain file : ${file.absolutePath}")
        }

        return FileOutputStream(file)
    }

    override fun url(path : String) : URL = obtainFile(this.baseDirectory, path).toURI()
        .toURL()
}

class DirectorySource(private val directory : File) : ReferenceSource()
{
    init
    {
        if (! this.directory.createDirectory())
        {
            throw IllegalArgumentException("Can't crete/obtain directory : ${this.directory.absolutePath}")
        }
    }

    override fun inputStream(path : String) : InputStream = FileInputStream(obtainFile(this.directory, path))

    override fun outputStream(path : String) : OutputStream
    {
        val file = obtainFile(this.directory, path)

        if (! file.createFile())
        {
            throw IllegalArgumentException("Can't create/obtain file : ${file.absolutePath}")
        }

        return FileOutputStream(file)
    }

    override fun url(path : String) : URL = obtainFile(this.directory, path).toURI()
        .toURL()
}

class ClassSource(private val classReference : Class<*> = ReferenceSource::class.java) : ReferenceSource()
{
    override fun inputStream(path : String) : InputStream =
        this.classReference.getResourceAsStream(path)
        ?: throw IllegalArgumentException("Can't retrieve path '$path' from class ${this.classReference.name}")

    override fun outputStream(path : String) : OutputStream =
        throw UnsupportedOperationException("Can't write inside ClassSource reference")

    override fun url(path : String) : URL =
        this.classReference.getResource(path)
        ?: throw IllegalArgumentException("Can't retrieve path '$path' from class ${this.classReference.name}")
}

class UrlSource(private val urlBase : String) : ReferenceSource()
{
    override fun inputStream(path : String) : InputStream
    {
        val connection = this.createConnection(path)
        connection.doInput = true
        connection.connect()
        return connection.getInputStream()
    }

    override fun outputStream(path : String) : OutputStream
    {
        val connection = this.createConnection(path)
        connection.doOutput = true
        connection.connect()
        return connection.getOutputStream()
    }

    override fun url(path : String) : URL
    {
        val urlPath =
            when
            {
                this.urlBase.endsWith('/') && path.startsWith('/') -> this.urlBase + path.substring(1)
                this.urlBase.endsWith('/') || path.startsWith('/') -> this.urlBase + path
                else                                               -> this.urlBase + "/" + path
            }
        return URL(urlPath)
    }

    private fun createConnection(path : String) : URLConnection
    {
        val urlPath =
            when
            {
                this.urlBase.endsWith('/') && path.startsWith('/') -> this.urlBase + path.substring(1)
                this.urlBase.endsWith('/') || path.startsWith('/') -> this.urlBase + path
                else                                               -> this.urlBase + "/" + path
            }

        return URL(urlPath).openConnection()
    }
}
