package khelp.io.files.json

import khelp.thread.Locker

class JsonReaderCallBackCollector(val locker : Locker) : JsonReaderCallBack
{
    val collected = ArrayList<JsonInfo>()

    override fun errorParsing(jsonReaderException : JsonReaderException)
    {
        this.collected.add(ErrorParsing(jsonReaderException))
        this.locker.unlock()
    }

    override fun startDocument()
    {
        this.collected.add(StartDocument)
    }

    override fun endDocument()
    {
        this.collected.add(EndDocument)
        this.locker.unlock()
    }

    override fun startObject()
    {
        this.collected.add(StartObject())
    }

    override fun startObject(name : String)
    {
        this.collected.add(StartObject(name))
    }

    override fun endObject()
    {
        this.collected.add(EndObject)
    }

    override fun startArray()
    {
        this.collected.add(StartArray())
    }

    override fun startArray(name : String)
    {
        this.collected.add(StartArray(name))
    }

    override fun endArray()
    {
        this.collected.add(EndArray)
    }

    override fun nullValue()
    {
        this.collected.add(MeetNull())
    }

    override fun nullValue(name : String)
    {
        this.collected.add(MeetNull(name))
    }

    override fun boolean(value : Boolean)
    {
        this.collected.add(MeetBoolean(value))
    }

    override fun boolean(name : String, value : Boolean)
    {
        this.collected.add(MeetBoolean(value, name))
    }

    override fun number(value : Double)
    {
        this.collected.add(MeetNumber(value))
    }

    override fun number(name : String, value : Double)
    {
        this.collected.add(MeetNumber(value, name))
    }

    override fun string(value : String)
    {
        this.collected.add(MeetString(value))
    }

    override fun string(name : String, value : String)
    {
        this.collected.add(MeetString(value, name))
    }
}