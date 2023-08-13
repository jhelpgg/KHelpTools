package khelp.io

import khelp.io.extensions.createDirectory
import khelp.io.extensions.createFile
import khelp.io.extensions.isVirtualLink
import khelp.utilities.log.exception
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.util.Stack
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream


/**
 * One kilobyte in bytes
 */
const val KILO_BYTES = 1024

/**
 * Size of a file header
 */
const val HEADER_SIZE = KILO_BYTES

/**
 * One megabyte in bytes
 */
const val MEGA_BYTES = 1024 * KILO_BYTES

/**
 * Buffer size
 */
const val BUFFER_SIZE = 4 * MEGA_BYTES


/**
 * Manage properly an input and an output streams, to simplify the open, close and error management
 * @param producerInput Function that create the input stream
 * @param producerOutput Function that create the output stream
 * @param operation Operation to do with input and output streams
 * @param onError Called if error happen
 * @param I Input stream type
 * @param O Output stream type
 * @return **`true`** If complete operation succeed without exception
 */
fun <I : InputStream, O : OutputStream> treatInputOutputStream(producerInput : () -> I,
                                                               producerOutput : () -> O,
                                                               operation : (I, O) -> Unit,
                                                               onError : (IOException) -> Unit = {
                                                                   exception(it,
                                                                             "Issue on treat input/output streams!")
                                                               }) : Boolean
{
    var ioException : IOException? = null
    var inputStream : I? = null
    var outputStream : O? = null

    try
    {
        inputStream = producerInput()
        outputStream = producerOutput()
        operation(inputStream, outputStream)
    }
    catch (io : IOException)
    {
        ioException = io
    }
    catch (e : Exception)
    {
        ioException = IOException("Failed to do operation!", e)
    }
    finally
    {
        if (outputStream != null)
        {
            try
            {
                outputStream.flush()
            }
            catch (ignored : Exception)
            {
            }

            try
            {
                outputStream.close()
            }
            catch (ignored : Exception)
            {
            }

        }

        if (inputStream != null)
        {
            try
            {
                inputStream.close()
            }
            catch (ignored : Exception)
            {
            }

        }
    }

    if (ioException != null)
    {
        onError(ioException)
        return false
    }

    return true
}

/**
 * Manage properly an input stream, to simplify the open, close and error management
 * @param producer Function that create the input stream
 * @param operation Operation to do with input stream
 * @param onError Called if error happen
 * @param I Input stream type
 * @return **`true`** If complete operation succeed without exception
 */
fun <I : InputStream> treatInputStream(producer : () -> I,
                                       operation : (I) -> Unit,
                                       onError : (IOException) -> Unit = {
                                           exception(it,
                                                     "Failed to treat input stream!")
                                       }) : Boolean
{
    var ioException : IOException? = null
    var inputStream : I? = null

    try
    {
        inputStream = producer()
        operation(inputStream)
    }
    catch (io : IOException)
    {
        ioException = io
    }
    catch (e : Exception)
    {
        ioException = IOException("Failed to do operation!", e)
    }
    finally
    {
        if (inputStream != null)
        {
            try
            {
                inputStream.close()
            }
            catch (ignored : Exception)
            {
            }

        }
    }

    if (ioException != null)
    {
        onError(ioException)
        return false
    }

    return true
}

/**
 * Manage properly an output streams to simplify the open, close and error management
 * @param producer Function that create the output stream
 * @param operation Operation to do with output stream
 * @param onError Called if error happen
 * @param O Output stream type
 * @return **`true`** If complete operation succeed without exception
 */
fun <O : OutputStream> treatOutputStream(producer : () -> O,
                                         operation : (O) -> Unit,
                                         onError : (IOException) -> Unit = {
                                             exception(it,
                                                       "Failed to treat output stream")
                                         }) : Boolean
{
    var ioException : IOException? = null
    var outputStream : O? = null

    try
    {
        outputStream = producer()
        operation(outputStream)
    }
    catch (io : IOException)
    {
        ioException = io
    }
    catch (e : Exception)
    {
        ioException = IOException("Failed to do operation!", e)
    }
    finally
    {
        if (outputStream != null)
        {
            try
            {
                outputStream.flush()
            }
            catch (ignored : Exception)
            {
            }

            try
            {
                outputStream.close()
            }
            catch (ignored : Exception)
            {
            }

        }
    }

    if (ioException != null)
    {
        onError(ioException)
        return false
    }

    return true
}

/**
 * Read text lines in given stream
 * @param producerInput Function that create the stream to read
 * @param lineReader Called on each line read. The parameter is the read line
 * @param onError Action to do on error
 * @param I Input stream type
 * @return **`true`** If complete operation succeed without exception
 */
fun <I : InputStream> readLines(producerInput : () -> I,
                                lineReader : (String) -> Unit,
                                onError : (IOException) -> Unit = { exception(it, "Failed to read lines!!") }) =
    treatInputStream(producerInput,
                     { inputStream ->
                         val bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
                         var line = bufferedReader.readLine()

                         while (line != null)
                         {
                             lineReader(line)
                             line = bufferedReader.readLine()
                         }

                         bufferedReader.close()
                     }, onError)

/**
 * Write a stream inside a file
 *
 * @param inputStream     Stream source
 * @param fileDestination File destination
 * @throws IOException On copying issue
 */
@Throws(IOException::class)
fun write(inputStream : InputStream, fileDestination : File)
{
    if (! fileDestination.createFile())
    {
        throw IOException("Can't create the file " + fileDestination.absolutePath)
    }

    var exception : IOException? = null

    treatOutputStream({ FileOutputStream(fileDestination) },
                      { write(inputStream, it) },
                      { exception = it })

    if (exception != null)
    {
        throw exception !!
    }
}

/**
 * Write a file inside a stream
 *
 * @param fileSource   Source file
 * @param outputStream Stream where write
 * @throws IOException On copying issue
 */
@Throws(IOException::class)
fun write(fileSource : File, outputStream : OutputStream)
{
    var exception : IOException? = null

    treatInputStream({ FileInputStream(fileSource) },
                     { write(it, outputStream) },
                     { exception = it })

    if (exception != null)
    {
        throw exception !!
    }
}

/**
 * Write a stream inside on other one
 *
 * @param inputStream  Stream source
 * @param outputStream Stream destination
 * @throws IOException On copying issue
 */
@Throws(IOException::class)
fun write(inputStream : InputStream, outputStream : OutputStream)
{
    val buffer = ByteArray(BUFFER_SIZE)
    var read = inputStream.read(buffer)

    while (read >= 0)
    {
        outputStream.write(buffer, 0, read)
        read = inputStream.read(buffer)
    }
}

/**
 * Zip a file or directory inside a file
 *
 * @param source      File/directory to zip
 * @param destination File destination
 * @throws IOException On zipping issue
 */
@Throws(IOException::class)
fun zip(source : File, destination : File, onlyContentIfDirectory : Boolean = false)
{
    if (! destination.createFile())
    {
        throw IOException("Can't create " + destination.absolutePath)
    }

    var fileOutputStream : FileOutputStream? = null

    try
    {
        fileOutputStream = FileOutputStream(destination)

        zip(source, fileOutputStream, onlyContentIfDirectory)
    }
    finally
    {
        if (fileOutputStream != null)
        {
            try
            {
                fileOutputStream.flush()
            }
            catch (ignored : Exception)
            {
                //Nothing to do
            }

            try
            {
                fileOutputStream.close()
            }
            catch (ignored : Exception)
            {
            }

        }
    }
}

/**
 * Zip a file or directory inside a stream
 *
 * @param source          File/directory to zip
 * @param outputStreamZip Where write the zip
 * @throws IOException On zipping issue
 */
@Throws(IOException::class)
fun zip(source : File, outputStreamZip : OutputStream, onlyContentIfDirectory : Boolean = false)
{
    var zipEntry : ZipEntry
    val zipOutputStream = ZipOutputStream(outputStreamZip)
    // For the best compression
    zipOutputStream.setLevel(9)

    var pair : Pair<String, File> = Pair(source.name, source)
    val stack = Stack<Pair<String, File>>()

    stack.push(pair)
    var ignore = source.isDirectory && onlyContentIfDirectory

    while (! stack.isEmpty())
    {
        pair = stack.pop()

        if (! pair.second.isVirtualLink)
        {
            if (pair.second.isDirectory)
            {
                val content = pair.second.listFiles()

                if (content != null)
                {
                    if (! ignore)
                    {
                        for (child in content)
                        {
                            stack.push(Pair(pair.first + "/" + child.name, child))
                        }
                    }
                    else
                    {
                        for (child in content)
                        {
                            stack.push(Pair(child.name, child))
                        }
                    }
                }
            }
            else if (! ignore)
            {
                zipEntry = ZipEntry(pair.first)
                // For the best compression
                zipEntry.method = ZipEntry.DEFLATED

                zipOutputStream.putNextEntry(zipEntry)

                write(pair.second, zipOutputStream)

                zipOutputStream.closeEntry()
            }
        }

        ignore = false
    }

    zipOutputStream.finish()
    zipOutputStream.flush()
}

/**
 * Unzip a file inside a directory
 *
 * @param directoryDestination Directory where unzip
 * @param zip                  Zip file
 * @throws IOException On extracting issue
 */
@Throws(IOException::class)
fun unzip(directoryDestination : File, zip : File)
{
    var fileInputStream : FileInputStream? = null

    try
    {
        fileInputStream = FileInputStream(zip)
        unzip(directoryDestination, fileInputStream)
    }
    finally
    {
        if (fileInputStream != null)
        {
            try
            {
                fileInputStream.close()
            }
            catch (ignored : Exception)
            {
                //Nothing to do
            }

        }
    }
}

/**
 * Unzip a stream inside a directory
 *
 * @param directoryDestination Directory where unzip
 * @param inputStreamZip       Stream to unzip
 * @throws IOException On unzipping issue
 */
@Throws(IOException::class)
fun unzip(directoryDestination : File, inputStreamZip : InputStream)
{
    var destination : File
    val zipInputStream = ZipInputStream(inputStreamZip)

    var zipEntry : ZipEntry? = zipInputStream.nextEntry
    var name : String

    while (zipEntry != null)
    {
        name = zipEntry.name
        destination = obtainFile(directoryDestination, name)

        if (name.endsWith("/"))
        {
            if (! destination.createDirectory())
            {
                throw IOException("Can't create the directory " + destination.absolutePath)
            }
        }
        else
        {
            if (! destination.createFile())
            {
                throw IOException("Can't create the file " + destination.absolutePath)
            }

            write(zipInputStream, destination)
        }

        zipInputStream.closeEntry()

        zipEntry = zipInputStream.nextEntry
    }
}

