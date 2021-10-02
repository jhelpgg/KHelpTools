package khelp.io.files.json

import khelp.utilities.extensions.toLocale
import khelp.utilities.serialization.ParsableSerializable
import khelp.utilities.serialization.Parser
import java.util.Calendar
import java.util.Date
import java.util.Locale

class JsonParser(private val objectJson : ObjectJson) : Parser
{
    override fun getBoolean(key : String) : Boolean = this.objectJson.boolean(key)

    override fun getChar(key : String) : Char
    {
        val string = this.objectJson.string(key)
        return if (string.isEmpty()) ' ' else string[0]
    }

    override fun getByte(key : String) : Byte = this.objectJson.byte(key)

    override fun getShort(key : String) : Short = this.objectJson.short(key)

    override fun getInt(key : String) : Int = this.objectJson.int(key)

    override fun getLong(key : String) : Long = this.objectJson.long(key)

    override fun getFloat(key : String) : Float = this.objectJson.float(key)

    override fun getDouble(key : String) : Double = this.objectJson.double(key)

    override fun getString(key : String) : String = this.objectJson.string(key)

    override fun <PS : ParsableSerializable> getParsableSerializable(key : String, instanceProvider : () -> PS) : PS
    {
        val jsonParser = JsonParser(this.objectJson.objectJson(key))
        val instance = instanceProvider()
        instance.parse(jsonParser)
        return instance
    }

    override fun getBooleanArray(key : String) : BooleanArray
    {
        val array = this.objectJson.arrayJson(key)
        return BooleanArray(array.size) { index -> array.boolean(index) }
    }

    override fun getCharArray(key : String) : CharArray
    {
        val array = this.objectJson.arrayJson(key)
        return CharArray(array.size) { index ->
            val string = array.string(index)
            if (string.isEmpty()) ' ' else string[0]
        }
    }

    override fun getByteArray(key : String) : ByteArray
    {
        val array = this.objectJson.arrayJson(key)
        return ByteArray(array.size) { index -> array.byte(index) }
    }

    override fun getShortArray(key : String) : ShortArray
    {
        val array = this.objectJson.arrayJson(key)
        return ShortArray(array.size) { index -> array.short(index) }
    }

    override fun getIntArray(key : String) : IntArray
    {
        val array = this.objectJson.arrayJson(key)
        return IntArray(array.size) { index -> array.int(index) }
    }

    override fun getLongArray(key : String) : LongArray
    {
        val array = this.objectJson.arrayJson(key)
        return LongArray(array.size) { index -> array.long(index) }
    }

    override fun getFloatArray(key : String) : FloatArray
    {
        val array = this.objectJson.arrayJson(key)
        return FloatArray(array.size) { index -> array.float(index) }
    }

    override fun getDoubleArray(key : String) : DoubleArray
    {
        val array = this.objectJson.arrayJson(key)
        return DoubleArray(array.size) { index -> array.double(index) }
    }

    override fun getStringArray(key : String) : Array<String>
    {
        val array = this.objectJson.arrayJson(key)
        return Array<String>(array.size) { index -> array.string(index) }
    }

    override fun getCalendarArray(key : String) : Array<Calendar>
    {
        val array = this.objectJson.arrayJson(key)
        return Array<Calendar>(array.size) { index ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = array.long(index)
            calendar
        }
    }

    override fun getDateArray(key : String) : Array<Date>
    {
        val array = this.objectJson.arrayJson(key)
        return Array<Date>(array.size) { index -> Date(array.long(index)) }
    }

    override fun getLocaleArray(key : String) : Array<Locale>
    {
        val array = this.objectJson.arrayJson(key)
        return Array<Locale>(array.size) { index ->
            array.string(index)
                .toLocale()
        }
    }

    override fun <PS : ParsableSerializable> getParsableSerializableArray(key : String,
                                                                          instanceProvider : () -> PS) : Array<PS>
    {
        val arrayJson = this.objectJson.arrayJson(key)

        @Suppress("UNCHECKED_CAST")
        val array = java.lang.reflect.Array.newInstance(instanceProvider().javaClass, arrayJson.size) as Array<PS>

        for (index in 0 until arrayJson.size)
        {
            val jsonParser = JsonParser(arrayJson.objectJson(index))
            val instance = instanceProvider()
            instance.parse(jsonParser)
            array[index] = instance
        }

        return array
    }

    override fun appendBooleanList(key : String, list : MutableList<Boolean>)
    {
        val array = this.objectJson.arrayJson(key)

        for (index in 0 until array.size)
        {
            list.add(array.boolean(index))
        }
    }

    override fun appendCharList(key : String, list : MutableList<Char>)
    {
        val array = this.objectJson.arrayJson(key)

        for (index in 0 until array.size)
        {
            val string = array.string(index)
            list.add(if (string.isEmpty()) ' ' else string[0])
        }
    }

    override fun appendByteList(key : String, list : MutableList<Byte>)
    {
        val array = this.objectJson.arrayJson(key)

        for (index in 0 until array.size)
        {
            list.add(array.byte(index))
        }
    }

    override fun appendShortList(key : String, list : MutableList<Short>)
    {
        val array = this.objectJson.arrayJson(key)

        for (index in 0 until array.size)
        {
            list.add(array.short(index))
        }
    }

    override fun appendIntList(key : String, list : MutableList<Int>)
    {
        val array = this.objectJson.arrayJson(key)

        for (index in 0 until array.size)
        {
            list.add(array.int(index))
        }
    }

    override fun appendLongList(key : String, list : MutableList<Long>)
    {
        val array = this.objectJson.arrayJson(key)

        for (index in 0 until array.size)
        {
            list.add(array.long(index))
        }
    }

    override fun appendFloatList(key : String, list : MutableList<Float>)
    {
        val array = this.objectJson.arrayJson(key)

        for (index in 0 until array.size)
        {
            list.add(array.float(index))
        }
    }

    override fun appendDoubleList(key : String, list : MutableList<Double>)
    {
        val array = this.objectJson.arrayJson(key)

        for (index in 0 until array.size)
        {
            list.add(array.double(index))
        }
    }

    override fun appendStringList(key : String, list : MutableList<String>)
    {
        val array = this.objectJson.arrayJson(key)

        for (index in 0 until array.size)
        {
            list.add(array.string(index))
        }
    }

    override fun appendCalendarList(key : String, list : MutableList<Calendar>)
    {
        val array = this.objectJson.arrayJson(key)

        for (index in 0 until array.size)
        {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = array.long(index)
            list.add(calendar)
        }
    }

    override fun appendDateList(key : String, list : MutableList<Date>)
    {
        val array = this.objectJson.arrayJson(key)

        for (index in 0 until array.size)
        {
            list.add(Date(array.long(index)))
        }
    }

    override fun appendLocaleList(key : String, list : MutableList<Locale>)
    {
        val array = this.objectJson.arrayJson(key)

        for (index in 0 until array.size)
        {
            list.add(array.string(index)
                         .toLocale())
        }
    }

    override fun <PS : ParsableSerializable> appendParsableSerializableList(key : String, list : MutableList<PS>,
                                                                            instanceProvider : () -> PS)
    {
        val array = this.objectJson.arrayJson(key)

        for (index in 0 until array.size)
        {
            val instance = instanceProvider()
            instance.parse(JsonParser(array.objectJson(index)))
            list.add(instance)
        }
    }

    override fun putBooleanMap(key : String, map : MutableMap<String, Boolean>)
    {
        val objectJson = this.objectJson.objectJson(key)

        for (keyMap in objectJson.keys())
        {
            map[keyMap] = objectJson.boolean(keyMap)
        }
    }

    override fun putCharMap(key : String, map : MutableMap<String, Char>)
    {
        val objectJson = this.objectJson.objectJson(key)

        for (keyMap in objectJson.keys())
        {
            val string = objectJson.string(keyMap)
            map[keyMap] = if (string.isEmpty()) ' ' else string[0]
        }
    }

    override fun putByteMap(key : String, map : MutableMap<String, Byte>)
    {
        val objectJson = this.objectJson.objectJson(key)

        for (keyMap in objectJson.keys())
        {
            map[keyMap] = objectJson.byte(keyMap)
        }
    }

    override fun putShortMap(key : String, map : MutableMap<String, Short>)
    {
        val objectJson = this.objectJson.objectJson(key)

        for (keyMap in objectJson.keys())
        {
            map[keyMap] = objectJson.short(keyMap)
        }
    }

    override fun putIntMap(key : String, map : MutableMap<String, Int>)
    {
        val objectJson = this.objectJson.objectJson(key)

        for (keyMap in objectJson.keys())
        {
            map[keyMap] = objectJson.int(keyMap)
        }
    }

    override fun putLongMap(key : String, map : MutableMap<String, Long>)
    {
        val objectJson = this.objectJson.objectJson(key)

        for (keyMap in objectJson.keys())
        {
            map[keyMap] = objectJson.long(keyMap)
        }
    }

    override fun putFloatMap(key : String, map : MutableMap<String, Float>)
    {
        val objectJson = this.objectJson.objectJson(key)

        for (keyMap in objectJson.keys())
        {
            map[keyMap] = objectJson.float(keyMap)
        }
    }

    override fun putDoubleMap(key : String, map : MutableMap<String, Double>)
    {
        val objectJson = this.objectJson.objectJson(key)

        for (keyMap in objectJson.keys())
        {
            map[keyMap] = objectJson.double(keyMap)
        }
    }

    override fun putStringMap(key : String, map : MutableMap<String, String>)
    {
        val objectJson = this.objectJson.objectJson(key)

        for (keyMap in objectJson.keys())
        {
            map[keyMap] = objectJson.string(keyMap)
        }
    }

    override fun putCalendarMap(key : String, map : MutableMap<String, Calendar>)
    {
        val objectJson = this.objectJson.objectJson(key)

        for (keyMap in objectJson.keys())
        {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = objectJson.long(keyMap)
            map[keyMap] = calendar
        }
    }

    override fun putDateMap(key : String, map : MutableMap<String, Date>)
    {
        val objectJson = this.objectJson.objectJson(key)

        for (keyMap in objectJson.keys())
        {
            map[keyMap] = Date(objectJson.long(keyMap))
        }
    }

    override fun putLocaleMap(key : String, map : MutableMap<String, Locale>)
    {
        val objectJson = this.objectJson.objectJson(key)

        for (keyMap in objectJson.keys())
        {
            map[keyMap] = objectJson.string(keyMap)
                .toLocale()
        }
    }

    override fun <PS : ParsableSerializable> putParsableSerializableMap(key : String, map : MutableMap<String, PS>,
                                                                        instanceProvider : () -> PS)
    {
        val objectJson = this.objectJson.objectJson(key)

        for (keyMap in objectJson.keys())
        {
            val instance = instanceProvider()
            instance.parse(JsonParser(objectJson.objectJson(keyMap)))
            map[keyMap] = instance
        }
    }
}