package khelp.io.files.json

import khelp.thread.future.FutureResult
import khelp.thread.future.Promise
import khelp.utilities.serialization.ParsableSerializable
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.Reader
import java.io.Writer

fun <PS : ParsableSerializable> writeAsJson(parsableSerializable : PS, outputStream : OutputStream,
                                            compact : Boolean = true, autoClose : Boolean = true)
{
    writeAsJson(parsableSerializable, OutputStreamWriter(outputStream), compact, autoClose)
}

fun <PS : ParsableSerializable> writeAsJson(parsableSerializable : PS, writer : Writer,
                                            compact : Boolean = true, autoClose : Boolean = true)
{
    writeAsJson(parsableSerializable, BufferedWriter(writer), compact, autoClose)
}


fun <PS : ParsableSerializable> writeAsJson(parsableSerializable : PS, writer : BufferedWriter,
                                            compact : Boolean = true, autoClose : Boolean = true)
{
    val serializer = JsonSerializer()
    parsableSerializable.serialize(serializer)
    serializer.objectJson.serialize(JsonWriter(writer, compact, autoClose))
}

fun <PS : ParsableSerializable> readFromJson(instanceProvider : () -> PS, inputStream : InputStream,
                                             autoClose : Boolean = true) : FutureResult<PS> =
    readFromJson(instanceProvider, InputStreamReader(inputStream), autoClose)

fun <PS : ParsableSerializable> readFromJson(instanceProvider : () -> PS, reader : Reader,
                                             autoClose : Boolean = true) : FutureResult<PS> =
    readFromJson(instanceProvider, BufferedReader(reader), autoClose)

fun <PS : ParsableSerializable> readFromJson(instanceProvider : () -> PS, bufferedReader : BufferedReader,
                                             autoClose : Boolean = true) : FutureResult<PS>
{
    val promise = Promise<ObjectJson>()
    JsonParserReaderCallback.parse(bufferedReader,
                                   { objectJson -> promise.result(objectJson) },
                                   { promise.fail(JsonException("The stream represents an array not an object")) },
                                   { jsonReaderException -> promise.fail(jsonReaderException) },
                                   autoClose)

    return promise.futureResult
        .and { objectJson ->
            val instance = instanceProvider()
            instance.parse(JsonParser(objectJson))
            instance
        }
}
