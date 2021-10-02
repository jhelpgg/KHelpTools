package khelp.io.files.json

import java.io.BufferedWriter
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.Writer
import java.util.Stack

class JsonWriter(private val writer : BufferedWriter, compact : Boolean = false,
                 private val autoClose : Boolean = true)
{
    companion object
    {
        private const val SPACE = 3
    }

    private var headerSize = if (compact) Int.MIN_VALUE else 0
    private val keyValueSeparatorArrayObject = if (compact) ":" else " :"
    private val keyValueSeparator = if (compact) ":" else " : "
    private val elementsStack = Stack<JsonElementType>()
    private var notFirst = false
    private var needComma = false
    val currentElement : JsonElementType get() = this.elementsStack.peek()

    constructor(writer : Writer, compact : Boolean = false, autoClose : Boolean = true)
            : this(BufferedWriter(writer), compact, autoClose)

    constructor(outputStream : OutputStream, compact : Boolean = false, autoClose : Boolean = true)
            : this(OutputStreamWriter(outputStream), compact, autoClose)

    init
    {
        this.elementsStack.push(JsonElementType.START_DOCUMENT)
    }

    @Throws(JsonWriterException::class)
    fun startObject()
    {
        if (this.elementsStack.peek() == JsonElementType.OBJECT)
        {
            throw JsonWriterException("Inside an object, a key is mandatory, so use startObject(String)")
        }

        this.writeHeader(this.needComma)
        this.write("{")
        this.elementsStack.push(JsonElementType.OBJECT)
        this.headerSize += JsonWriter.SPACE
        this.needComma = false
    }

    @Throws(JsonWriterException::class)
    fun startObject(name : String)
    {
        if (this.elementsStack.peek() != JsonElementType.OBJECT)
        {
            throw JsonWriterException("Outside of object, a key have no meaning, so use startObject()")
        }

        val key = name.trim()

        if (key.isEmpty())
        {
            throw JsonWriterException("Name must not be empty or full of white characters")
        }

        this.writeHeader(this.needComma)
        this.write("\"$key\"${this.keyValueSeparatorArrayObject}")
        this.writeHeader(false)
        this.write("{")
        this.elementsStack.push(JsonElementType.OBJECT)
        this.headerSize += JsonWriter.SPACE
    }

    @Throws(JsonWriterException::class)
    fun endObject()
    {
        if (this.elementsStack.pop() != JsonElementType.OBJECT)
        {
            throw JsonWriterException("Not inside an object but in ${this.elementsStack.peek()}")
        }

        this.headerSize -= JsonWriter.SPACE
        this.writeHeader(false)
        this.write("}")
        this.needComma = true
    }

    @Throws(JsonWriterException::class)
    fun startArray()
    {
        if (this.elementsStack.peek() == JsonElementType.OBJECT)
        {
            throw JsonWriterException("Inside an object, a key is mandatory, so use startArray(String)")
        }

        this.writeHeader(this.needComma)
        this.write("[")
        this.elementsStack.push(JsonElementType.ARRAY)
        this.headerSize += JsonWriter.SPACE
        this.needComma = false
    }

    @Throws(JsonWriterException::class)
    fun startArray(name : String)
    {
        if (this.elementsStack.peek() != JsonElementType.OBJECT)
        {
            throw JsonWriterException("Outside of object, a key have no meaning, so use startArray()")
        }

        val key = name.trim()

        if (key.isEmpty())
        {
            throw JsonWriterException("Name must not be empty or full of white characters")
        }

        this.writeHeader(this.needComma)
        this.write("\"$key\"${this.keyValueSeparatorArrayObject}")
        this.writeHeader(false)
        this.write("[")
        this.elementsStack.push(JsonElementType.ARRAY)
        this.headerSize += JsonWriter.SPACE
        this.needComma = false
    }

    @Throws(JsonWriterException::class)
    fun endArray()
    {
        if (this.elementsStack.pop() != JsonElementType.ARRAY)
        {
            throw JsonWriterException("Not inside an array but in ${this.elementsStack.peek()}")
        }

        this.headerSize -= JsonWriter.SPACE
        this.write("]")
        this.needComma = true
    }

    @Throws(JsonWriterException::class)
    fun putNull()
    {
        if (this.elementsStack.peek() != JsonElementType.ARRAY)
        {
            throw JsonWriterException("Must be inside an array, not a ${this.elementsStack.peek()}")
        }

        if (this.needComma)
        {
            this.write(",")

            if (this.headerSize >= 0)
            {
                this.write(" ")
            }
        }

        this.write("NULL")

        this.needComma = true
    }

    @Throws(JsonWriterException::class)
    fun putNull(name : String)
    {
        if (this.elementsStack.peek() != JsonElementType.OBJECT)
        {
            throw JsonWriterException("Must be inside an object, not a ${this.elementsStack.peek()}")
        }

        val key = name.trim()

        if (key.isEmpty())
        {
            throw JsonWriterException("Name must not be empty or full of white characters")
        }

        this.writeHeader(this.needComma)
        this.write("\"$key\"${this.keyValueSeparator}NULL")
        this.needComma = true
    }

    @Throws(JsonWriterException::class)
    fun putBoolean(value : Boolean)
    {
        if (this.elementsStack.peek() != JsonElementType.ARRAY)
        {
            throw JsonWriterException("Must be inside an array, not a ${this.elementsStack.peek()}")
        }

        if (this.needComma)
        {
            this.write(",")

            if (this.headerSize >= 0)
            {
                this.write(" ")
            }
        }

        this.write("$value")
        this.needComma = true
    }

    @Throws(JsonWriterException::class)
    fun putBoolean(name : String, value : Boolean)
    {
        if (this.elementsStack.peek() != JsonElementType.OBJECT)
        {
            throw JsonWriterException("Must be inside an object, not a ${this.elementsStack.peek()}")
        }

        val key = name.trim()

        if (key.isEmpty())
        {
            throw JsonWriterException("Name must not be empty or full of white characters")
        }

        this.writeHeader(this.needComma)
        this.write("\"$key\"${this.keyValueSeparator}$value")
        this.needComma = true
    }

    @Throws(JsonWriterException::class)
    fun putNumber(value : Double)
    {
        if (this.elementsStack.peek() != JsonElementType.ARRAY)
        {
            throw JsonWriterException("Must be inside an array, not a ${this.elementsStack.peek()}")
        }

        if (this.needComma)
        {
            this.write(",")

            if (this.headerSize >= 0)
            {
                this.write(" ")
            }
        }

        this.write("$value")
        this.needComma = true
    }

    @Throws(JsonWriterException::class)
    fun putNumber(name : String, value : Double)
    {
        if (this.elementsStack.peek() != JsonElementType.OBJECT)
        {
            throw JsonWriterException("Must be inside an object, not a ${this.elementsStack.peek()}")
        }

        val key = name.trim()

        if (key.isEmpty())
        {
            throw JsonWriterException("Name must not be empty or full of white characters")
        }

        this.writeHeader(this.needComma)
        this.write("\"$key\"${this.keyValueSeparator}$value")
        this.needComma = true
    }

    @Throws(JsonWriterException::class)
    fun putString(value : String)
    {
        if (this.elementsStack.peek() != JsonElementType.ARRAY)
        {
            throw JsonWriterException("Must be inside an array, not a ${this.elementsStack.peek()}")
        }

        if (this.needComma)
        {
            this.write(",")

            if (this.headerSize >= 0)
            {
                this.write(" ")
            }
        }

        this.write("\"${
            value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
        }\"")
        this.needComma = true
    }

    @Throws(JsonWriterException::class)
    fun putString(name : String, value : String)
    {
        if (this.elementsStack.peek() != JsonElementType.OBJECT)
        {
            throw JsonWriterException("Must be inside an object, not a ${this.elementsStack.peek()}")
        }

        val key = name.trim()

        if (key.isEmpty())
        {
            throw JsonWriterException("Name must not be empty or full of white characters")
        }

        this.writeHeader(this.needComma)
        this.write("\"$key\"${this.keyValueSeparator}\"${
            value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
        }\"")
        this.needComma = true
    }

    @Throws(JsonWriterException::class)
    fun finish()
    {
        var element = this.elementsStack.pop()

        while (element != JsonElementType.START_DOCUMENT)
        {
            this.headerSize --
            this.writeHeader(false)

            if (element == JsonElementType.OBJECT)
            {
                this.write("}")
            }
            else
            {
                this.write("]")
            }

            element = this.elementsStack.pop()
        }

        try
        {
            this.writer.flush()
        }
        catch (_ : Exception)
        {
        }

        if (this.autoClose)
        {
            try
            {
                this.writer.close()
            }
            catch (_ : Exception)
            {
            }
        }
    }

    @Throws(JsonWriterException::class)
    private fun write(string : String)
    {
        try
        {
            this.writer.write(string)
        }
        catch (exception : Exception)
        {
            if (this.autoClose)
            {
                try
                {
                    this.writer.close()
                }
                catch (_ : Exception)
                {
                }
            }

            throw JsonWriterException("Issue while writing : $string", exception)
        }
    }

    @Throws(JsonWriterException::class)
    private fun writeHeader(needComma : Boolean)
    {
        try
        {
            if (needComma)
            {
                this.writer.write(",")
            }

            if (this.headerSize >= 0)
            {
                if (this.notFirst)
                {
                    this.writer.newLine()
                }

                this.notFirst = true

                for (space in 0 until this.headerSize)
                {
                    this.writer.write(" ")
                }
            }
        }
        catch (exception : Exception)
        {
            if (this.autoClose)
            {
                try
                {
                    this.writer.close()
                }
                catch (_ : Exception)
                {
                }
            }

            throw JsonWriterException("Issue while writing header", exception)
        }
    }
}
