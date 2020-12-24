package khelp.io

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import khelp.utilities.log.exception


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