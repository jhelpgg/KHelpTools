package khelp.io.files.json

import java.io.BufferedReader
import java.io.InputStream
import java.io.Reader
import java.util.Stack

class JsonParserReaderCallback(private val objectParsed : (ObjectJson) -> Unit,
                               private val arrayParsed : (ArrayJson) -> Unit,
                               private val parsingError : (JsonReaderException) -> Unit) : JsonReaderCallBack
{
    companion object
    {
        fun parse(bufferedReader : BufferedReader,
                  objectParsed : (ObjectJson) -> Unit,
                  arrayParsed : (ArrayJson) -> Unit,
                  parsingError : (JsonReaderException) -> Unit,
                  autoClose : Boolean = true)
        {
            JsonReader(bufferedReader, JsonParserReaderCallback(objectParsed, arrayParsed, parsingError), autoClose)
        }

        fun parse(reader : Reader,
                  objectParsed : (ObjectJson) -> Unit,
                  arrayParsed : (ArrayJson) -> Unit,
                  parsingError : (JsonReaderException) -> Unit,
                  autoClose : Boolean = true)
        {
            JsonReader(reader, JsonParserReaderCallback(objectParsed, arrayParsed, parsingError), autoClose)
        }

        fun parse(inputStream : InputStream,
                  objectParsed : (ObjectJson) -> Unit,
                  arrayParsed : (ArrayJson) -> Unit,
                  parsingError : (JsonReaderException) -> Unit,
                  autoClose : Boolean = true)
        {
            JsonReader(inputStream, JsonParserReaderCallback(objectParsed, arrayParsed, parsingError), autoClose)
        }
    }

    private var started = false
    private var mainIsObject = true
    private lateinit var mainObject : ObjectJson
    private lateinit var mainArray : ArrayJson
    private val stackCurrent = Stack<Pair<ObjectJson?, ArrayJson?>>()

    override fun errorParsing(jsonReaderException : JsonReaderException)
    {
        this.parsingError(jsonReaderException)
    }

    override fun endDocument()
    {
        if (this.mainIsObject)
        {
            this.objectParsed(this.mainObject)
        }
        else
        {
            this.arrayParsed(this.mainArray)
        }
    }

    override fun startObject()
    {
        if (this.started)
        {
            this.stackCurrent.peek().second?.let { arrayJson ->
                val child = ObjectJson()
                arrayJson.addObject(child)
                this.stackCurrent.push(Pair(child, null))
            }
        }
        else
        {
            this.mainIsObject = true
            this.mainObject = ObjectJson()
            this.stackCurrent.push(Pair(this.mainObject, null))
            this.started = true
        }
    }

    override fun startObject(name : String)
    {
        this.stackCurrent.peek().first?.let { objectJson ->
            val child = ObjectJson()
            objectJson.putObject(name, child)
            this.stackCurrent.push(Pair(child, null))
        }
    }

    override fun endObject()
    {
        this.stackCurrent.pop()
    }

    override fun startArray()
    {
        if (this.started)
        {
            this.stackCurrent.peek().second?.let { arrayJson ->
                val child = ArrayJson()
                arrayJson.addArray(child)
                this.stackCurrent.push(Pair(null, child))
            }
        }
        else
        {
            this.mainIsObject = false
            this.mainArray = ArrayJson()
            this.stackCurrent.push(Pair(null, this.mainArray))
            this.started = true
        }
    }

    override fun startArray(name : String)
    {
        this.stackCurrent.peek().first?.let { objectJson ->
            val child = ArrayJson()
            objectJson.putArray(name, child)
            this.stackCurrent.push(Pair(null, child))
        }
    }

    override fun endArray()
    {
        this.stackCurrent.pop()
    }

    override fun nullValue()
    {
        this.stackCurrent.peek().second?.addNull()
    }

    override fun nullValue(name : String)
    {
        this.stackCurrent.peek().first?.putNull(name)
    }

    override fun boolean(value : Boolean)
    {
        this.stackCurrent.peek().second?.addBoolean(value)
    }

    override fun boolean(name : String, value : Boolean)
    {
        this.stackCurrent.peek().first?.putBoolean(name, value)
    }

    override fun number(value : Double)
    {
        this.stackCurrent.peek().second?.addNumber(value)
    }

    override fun number(name : String, value : Double)
    {
        this.stackCurrent.peek().first?.putNumber(name, value)
    }

    override fun string(value : String)
    {
        this.stackCurrent.peek().second?.addString(value)
    }

    override fun string(name : String, value : String)
    {
        this.stackCurrent.peek().first?.putString(name, value)
    }
}