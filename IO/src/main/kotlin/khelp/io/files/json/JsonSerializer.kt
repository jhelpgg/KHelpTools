package khelp.io.files.json

import khelp.utilities.serialization.ParsableSerializable
import khelp.utilities.serialization.Serializer
import java.util.Calendar
import java.util.Date
import java.util.Locale

class JsonSerializer : Serializer
{
    val objectJson = ObjectJson()

    override fun setBoolean(key : String, value : Boolean)
    {
        this.objectJson.putBoolean(key, value)
    }

    override fun setChar(key : String, value : Char)
    {
        this.objectJson.putString(key, value.toString())
    }

    override fun setByte(key : String, value : Byte)
    {
        this.objectJson.putNumber(key, value)
    }

    override fun setShort(key : String, value : Short)
    {
        this.objectJson.putNumber(key, value)
    }

    override fun setInt(key : String, value : Int)
    {
        this.objectJson.putNumber(key, value)
    }

    override fun setLong(key : String, value : Long)
    {
        this.objectJson.putNumber(key, value)
    }

    override fun setFloat(key : String, value : Float)
    {
        this.objectJson.putNumber(key, value)
    }

    override fun setDouble(key : String, value : Double)
    {
        this.objectJson.putNumber(key, value)
    }

    override fun setString(key : String, value : String)
    {
        this.objectJson.putString(key, value)
    }

    override fun <PS : ParsableSerializable> setParsableSerializable(key : String, value : PS)
    {
        val serializer = JsonSerializer()
        value.serialize(serializer)
        this.objectJson.putObject(key, serializer.objectJson)
    }

    override fun setBooleanArray(key : String, array : BooleanArray)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addBoolean(value)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setCharArray(key : String, array : CharArray)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addString(value.toString())
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setByteArray(key : String, array : ByteArray)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addNumber(value)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setShortArray(key : String, array : ShortArray)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addNumber(value)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setIntArray(key : String, array : IntArray)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addNumber(value)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setLongArray(key : String, array : LongArray)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addNumber(value)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setFloatArray(key : String, array : FloatArray)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addNumber(value)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setDoubleArray(key : String, array : DoubleArray)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addNumber(value)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setStringArray(key : String, array : Array<String>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addString(value)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setCalendarArray(key : String, array : Array<Calendar>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addNumber(value.timeInMillis)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setDateArray(key : String, array : Array<Date>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addNumber(value.time)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setLocaleArray(key : String, array : Array<Locale>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addString(value.toString())
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun <PS : ParsableSerializable> setParsableSerializableArray(key : String, array : Array<PS>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            val serializer = JsonSerializer()
            value.serialize(serializer)
            arrayJson.addObject(serializer.objectJson)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setBooleanList(key : String, array : List<Boolean>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addBoolean(value)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setCharList(key : String, array : List<Char>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addString(value.toString())
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setByteList(key : String, array : List<Byte>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addNumber(value)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setShortList(key : String, array : List<Short>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addNumber(value)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setIntList(key : String, array : List<Int>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addNumber(value)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setLongList(key : String, array : List<Long>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addNumber(value)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setFloatList(key : String, array : List<Float>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addNumber(value)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setDoubleList(key : String, array : List<Double>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addNumber(value)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setStringList(key : String, array : List<String>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addString(value)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setCalendarList(key : String, array : List<Calendar>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addNumber(value.timeInMillis)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setDateList(key : String, array : List<Date>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addNumber(value.time)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setLocaleList(key : String, array : List<Locale>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            arrayJson.addString(value.toString())
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun <PS : ParsableSerializable> setParsableSerializableList(key : String, array : List<PS>)
    {
        val arrayJson = ArrayJson()

        for (value in array)
        {
            val serializer = JsonSerializer()
            value.serialize(serializer)
            arrayJson.addObject(serializer.objectJson)
        }

        this.objectJson.putArray(key, arrayJson)
    }

    override fun setBooleanMap(key : String, map : Map<String, Boolean>)
    {
        val objectJson = ObjectJson()

        for ((keyMap, value) in map)
        {
            objectJson.putBoolean(keyMap, value)
        }

        this.objectJson.putObject(key, objectJson)
    }

    override fun setCharMap(key : String, map : Map<String, Char>)
    {
        val objectJson = ObjectJson()

        for ((keyMap, value) in map)
        {
            objectJson.putString(keyMap, value.toString())
        }

        this.objectJson.putObject(key, objectJson)
    }

    override fun setByteMap(key : String, map : Map<String, Byte>)
    {
        val objectJson = ObjectJson()

        for ((keyMap, value) in map)
        {
            objectJson.putNumber(keyMap, value)
        }

        this.objectJson.putObject(key, objectJson)
    }

    override fun setShortMap(key : String, map : Map<String, Short>)
    {
        val objectJson = ObjectJson()

        for ((keyMap, value) in map)
        {
            objectJson.putNumber(keyMap, value)
        }

        this.objectJson.putObject(key, objectJson)
    }

    override fun setIntMap(key : String, map : Map<String, Int>)
    {
        val objectJson = ObjectJson()

        for ((keyMap, value) in map)
        {
            objectJson.putNumber(keyMap, value)
        }

        this.objectJson.putObject(key, objectJson)
    }

    override fun setLongMap(key : String, map : Map<String, Long>)
    {
        val objectJson = ObjectJson()

        for ((keyMap, value) in map)
        {
            objectJson.putNumber(keyMap, value)
        }

        this.objectJson.putObject(key, objectJson)
    }

    override fun setFloatMap(key : String, map : Map<String, Float>)
    {
        val objectJson = ObjectJson()

        for ((keyMap, value) in map)
        {
            objectJson.putNumber(keyMap, value)
        }

        this.objectJson.putObject(key, objectJson)
    }

    override fun setDoubleMap(key : String, map : Map<String, Double>)
    {
        val objectJson = ObjectJson()

        for ((keyMap, value) in map)
        {
            objectJson.putNumber(keyMap, value)
        }

        this.objectJson.putObject(key, objectJson)
    }

    override fun setStringMap(key : String, map : Map<String, String>)
    {
        val objectJson = ObjectJson()

        for ((keyMap, value) in map)
        {
            objectJson.putString(keyMap, value)
        }

        this.objectJson.putObject(key, objectJson)
    }

    override fun setCalendarMap(key : String, map : Map<String, Calendar>)
    {
        val objectJson = ObjectJson()

        for ((keyMap, value) in map)
        {
            objectJson.putNumber(keyMap, value.timeInMillis)
        }

        this.objectJson.putObject(key, objectJson)
    }

    override fun setDateMap(key : String, map : Map<String, Date>)
    {
        val objectJson = ObjectJson()

        for ((keyMap, value) in map)
        {
            objectJson.putNumber(keyMap, value.time)
        }

        this.objectJson.putObject(key, objectJson)
    }

    override fun setLocaleMap(key : String, map : Map<String, Locale>)
    {
        val objectJson = ObjectJson()

        for ((keyMap, value) in map)
        {
            objectJson.putString(keyMap, value.toString())
        }

        this.objectJson.putObject(key, objectJson)
    }

    override fun <PS : ParsableSerializable> setParsableSerializableMap(key : String, map : Map<String, PS>)
    {
        val objectJson = ObjectJson()

        for ((keyMap, value) in map)
        {
            val serializer = JsonSerializer()
            value.serialize(serializer)
            objectJson.putObject(keyMap, serializer.objectJson)
        }

        this.objectJson.putObject(key, objectJson)
    }
}