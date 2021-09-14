package khelp.io

import khelp.io.extensions.createFile
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import khelp.utilities.log.exception
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


/**
 * One kilo-byte in bytes
 */
const val KILO_BYTES = 1024
/**
 * Size of a file header
 */
const val HEADER_SIZE = KILO_BYTES
/**
 * One mega-byte in bytes
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
fun <I : InputStream, O : OutputStream> treatInputOutputStream(producerInput: () -> I,
                                                               producerOutput: () -> O,
                                                               operation: (I, O) -> Unit,
                                                               onError: (IOException) -> Unit = {
                                                                   exception(it,
                                                                             "Issue on treat input/output streams!")
                                                               }): Boolean
{
    var ioException: IOException? = null
    var inputStream: I? = null
    var outputStream: O? = null

    try
    {
        inputStream = producerInput()
        outputStream = producerOutput()
        operation(inputStream, outputStream)
    }
    catch (io: IOException)
    {
        ioException = io
    }
    catch (e: Exception)
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
            catch (ignored: Exception)
            {
            }

            try
            {
                outputStream.close()
            }
            catch (ignored: Exception)
            {
            }

        }

        if (inputStream != null)
        {
            try
            {
                inputStream.close()
            }
            catch (ignored: Exception)
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
fun <I : InputStream> treatInputStream(producer: () -> I,
                                       operation: (I) -> Unit,
                                       onError: (IOException) -> Unit = {
                                           exception(it,
                                                     "Failed to treat input stream!")
                                       }): Boolean
{
    var ioException: IOException? = null
    var inputStream: I? = null

    try
    {
        inputStream = producer()
        operation(inputStream)
    }
    catch (io: IOException)
    {
        ioException = io
    }
    catch (e: Exception)
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
            catch (ignored: Exception)
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
fun <O : OutputStream> treatOutputStream(producer: () -> O,
                                         operation: (O) -> Unit,
                                         onError: (IOException) -> Unit = {
                                             exception(it,
                                                       "Failed to treat output stream")
                                         }): Boolean
{
    var ioException: IOException? = null
    var outputStream: O? = null

    try
    {
        outputStream = producer()
        operation(outputStream)
    }
    catch (io: IOException)
    {
        ioException = io
    }
    catch (e: Exception)
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
            catch (ignored: Exception)
            {
            }

            try
            {
                outputStream.close()
            }
            catch (ignored: Exception)
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
fun <I : InputStream> readLines(producerInput: () -> I,
                                lineReader: (String) -> Unit,
                                onError: (IOException) -> Unit = { exception(it, "Failed to read lines!!") }) =
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
fun write(inputStream: InputStream, fileDestination: File)
{
    if (!fileDestination.createFile())
    {
        throw IOException("Can't create the file " + fileDestination.absolutePath)
    }

    var exception: IOException? = null

    treatOutputStream({ FileOutputStream(fileDestination) },
                      { write(inputStream, it) },
                      { exception = it })

    if (exception != null)
    {
        throw exception!!
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
fun write(fileSource: File, outputStream: OutputStream)
{
    var exception: IOException? = null

    treatInputStream({ FileInputStream(fileSource) },
                     { write(it, outputStream) },
                     { exception = it })

    if (exception != null)
    {
        throw exception!!
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
fun write(inputStream: InputStream, outputStream: OutputStream)
{
    val buffer = ByteArray(BUFFER_SIZE)
    var read = inputStream.read(buffer)

    while (read >= 0)
    {
        outputStream.write(buffer, 0, read)
        read = inputStream.read(buffer)
    }
}

