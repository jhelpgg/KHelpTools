package khelp.io.files.json

import khelp.thread.TaskContext
import khelp.thread.parallel
import khelp.utilities.text.DEFAULT_SEPARATORS
import khelp.utilities.text.StringExtractor
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.util.Stack

class JsonReader(private val reader : BufferedReader,
                 private val callback : JsonReaderCallBack,
                 private val autoCloseReader : Boolean = true)
{
    companion object
    {
        private const val JSON_SEPARATORS = "{}[],:$DEFAULT_SEPARATORS"
    }

    private val elementsStack = Stack<JsonElementType>()
    private var key = ""

    constructor(reader : Reader,
                callback : JsonReaderCallBack,
                autoCloseReader : Boolean = true) : this(BufferedReader(reader), callback, autoCloseReader)

    constructor(inputStream : InputStream,
                callback : JsonReaderCallBack,
                autoCloseStream : Boolean = true) : this(InputStreamReader(inputStream), callback, autoCloseStream)

    init
    {
        parallel(TaskContext.IO, this::parsingTask)
    }

    private fun parsingTask()
    {
        this.callback.startDocument()
        this.elementsStack.push(JsonElementType.START_DOCUMENT)

        try
        {
            this.parsing()

            if (this.elementsStack.peek() == JsonElementType.END_DOCUMENT)
            {
                this.callback.endDocument()
            }
            else
            {
                this.callback.errorParsing(
                    JsonReaderException("Unexpected en of document. JSON not properly closed", - 1, - 1))
            }
        }
        catch (exception : Exception)
        {
            this.callback.errorParsing(JsonReaderException("Stream read issue while parsing JSON", - 1, - 1, exception))
        }
        finally
        {
            if (this.autoCloseReader)
            {
                try
                {
                    this.reader.close()
                }
                catch (_ : Exception)
                {
                }
            }
        }
    }

    @Throws(IOException::class, JsonReaderException::class)
    private fun parsing()
    {
        var lineNumber = 0

        var line = this.reader.readLine()

        while (line != null)
        {
            lineNumber ++
            val offset = line.indexOfFirst { character -> character > ' ' }
            line = line.trim()

            if (line.isNotEmpty())
            {
                this.parseLine(line, lineNumber, offset)
            }

            line = this.reader.readLine()
        }
    }

    @Throws(JsonReaderException::class)
    private fun parseLine(line : String, lineNumber : Int, offset : Int)
    {
        val stringExtractor = StringExtractor(line, JsonReader.JSON_SEPARATORS, returnSeparators = true)

        var part = stringExtractor.next()

        while (part != null)
        {
            part = part.trim()

            if (part.isNotEmpty())
            {
                this.parsePart(part, stringExtractor.isString, stringExtractor.isSeparator,
                               lineNumber, offset + stringExtractor.currentWordStart())
            }

            part = stringExtractor.next()
        }
    }

    @Throws(JsonReaderException::class)
    private fun parsePart(part : String, isString : Boolean, isSeparator : Boolean, lineNumber : Int, index : Int)
    {
        val currentElement = this.elementsStack.peek()

        if (currentElement == JsonElementType.END_DOCUMENT)
        {
            this.reportException("Found something after end of document", lineNumber, index)
        }

        if (isSeparator)
        {
            when (part)
            {
                "{" ->
                    if (currentElement == JsonElementType.OBJECT)
                    {
                        if (this.key.isEmpty())
                        {
                            this.reportException("Start of value without a key inside an object", lineNumber, index)
                        }

                        this.callback.startObject(this.key)
                        this.key = ""
                        this.elementsStack.push(JsonElementType.OBJECT)
                    }
                    else
                    {
                        this.callback.startObject()
                        this.elementsStack.push(JsonElementType.OBJECT)
                    }
                "}" ->
                    if (currentElement == JsonElementType.OBJECT)
                    {
                        this.elementsStack.pop()

                        if (this.elementsStack.peek() == JsonElementType.START_DOCUMENT)
                        {
                            this.elementsStack.pop()
                            this.elementsStack.push(JsonElementType.END_DOCUMENT)
                        }

                        this.callback.endObject()
                    }
                    else
                    {
                        this.reportException("Unexpected close object", lineNumber, index)
                    }
                "[" ->
                    if (currentElement == JsonElementType.OBJECT)
                    {
                        if (this.key.isEmpty())
                        {
                            this.reportException("Start of value without a key inside an object", lineNumber, index)
                        }

                        this.callback.startArray(this.key)
                        this.key = ""
                        this.elementsStack.push(JsonElementType.ARRAY)
                    }
                    else
                    {
                        this.callback.startArray()
                        this.elementsStack.push(JsonElementType.ARRAY)
                    }
                "]" ->
                    if (currentElement == JsonElementType.ARRAY)
                    {
                        this.elementsStack.pop()

                        if (this.elementsStack.peek() == JsonElementType.START_DOCUMENT)
                        {
                            this.elementsStack.pop()
                            this.elementsStack.push(JsonElementType.END_DOCUMENT)
                        }

                        this.callback.endArray()
                    }
                    else
                    {
                        this.reportException("Unexpected close array", lineNumber, index)
                    }
                ":" ->
                    if (currentElement == JsonElementType.OBJECT)
                    {
                        if (this.key.isEmpty())
                        {
                            this.reportException("No key defined before key separator", lineNumber, index)
                        }
                    }
                    else
                    {
                        this.reportException("Find key separator outside of object", lineNumber, index)
                    }
            }
        }
        else if (isString)
        {
            when (currentElement)
            {
                JsonElementType.OBJECT ->
                    if (this.key.isEmpty())
                    {
                        this.key = part
                    }
                    else
                    {
                        this.callback.string(this.key, part)
                        this.key = ""
                    }
                JsonElementType.ARRAY  -> this.callback.string(part)
                else                   -> this.reportException("Value or key defined outside of Object or Array",
                                                                lineNumber, index)
            }
        }
        else
        {
            when (currentElement)
            {
                JsonElementType.OBJECT ->
                    if (this.key.isEmpty())
                    {
                        this.reportException("No key defined for the value", lineNumber, index)
                    }
                    else
                    {
                        when
                        {
                            "null".equals(part, true)  -> this.callback.nullValue(this.key)
                            "true".equals(part, true)  -> this.callback.boolean(this.key, true)
                            "false".equals(part, true) -> this.callback.boolean(this.key, false)
                            else                       ->
                                try
                                {
                                    this.callback.number(this.key, part.toDouble())
                                }
                                catch (exception : Exception)
                                {
                                    this.reportException("Not a valid number value", lineNumber, index, exception)
                                }
                        }

                        this.key = ""
                    }
                JsonElementType.ARRAY  ->
                    when
                    {
                        "null".equals(part, true)  -> this.callback.nullValue()
                        "true".equals(part, true)  -> this.callback.boolean(true)
                        "false".equals(part, true) -> this.callback.boolean(false)
                        else                       ->
                            try
                            {
                                this.callback.number(part.toDouble())
                            }
                            catch (exception : Exception)
                            {
                                this.reportException("Not a valid number value", lineNumber, index, exception)
                            }
                    }
                else                    -> this.reportException("Value defined outside of Object or Array",
                                                                lineNumber, index)
            }
        }
    }

    @Throws(JsonReaderException::class)
    private fun reportException(message : String, lineNumber : Int, index : Int, cause : Throwable? = null)
    {
        val exception = JsonReaderException("$message : line=$lineNumber character=$index", lineNumber, index, cause)
        this.callback.errorParsing(exception)
        throw exception
    }
}