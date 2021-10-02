package khelp.io.files.json

import khelp.utilities.log.exception

interface JsonReaderCallBack
{
    fun errorParsing(jsonReaderException : JsonReaderException)
    {
        exception(jsonReaderException, "Issue while parse the JSON stream")
    }

    fun startDocument() = Unit
    fun endDocument() = Unit

    fun startObject() = Unit
    fun startObject(name : String) = Unit
    fun endObject() = Unit

    fun startArray() = Unit
    fun startArray(name : String) = Unit
    fun endArray() = Unit

    fun nullValue() = Unit
    fun nullValue(name : String) = Unit

    fun boolean(value : Boolean) = Unit
    fun boolean(name : String, value : Boolean) = Unit

    fun number(value : Double) = Unit
    fun number(name : String, value : Double) = Unit

    fun string(value : String) = Unit
    fun string(name : String, value : String) = Unit
}
