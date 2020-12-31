package khelp.utilities.serialization

import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Serializer used by [ParsableSerializable], to serialize data to a specific format
 */
interface Serializer
{
    fun setBoolean(key: String, value: Boolean)

    fun setChar(key: String, value: Char)

    fun setByte(key: String, value: Byte)

    fun setShort(key: String, value: Short)

    fun setInt(key: String, value: Int)

    fun setLong(key: String, value: Long)

    fun setFloat(key: String, value: Float)

    fun setDouble(key: String, value: Double)

    fun setString(key: String, value: String)

    fun setCalendar(key: String, value: Calendar)
    {
        this.setLong(key, value.timeInMillis)
    }

    fun setDate(key: String, value: Date)
    {
        this.setLong(key, value.time)
    }

    fun setLocale(key: String, value: Locale)
    {
        this.setString(key, value.toString())
    }

    fun <PS : ParsableSerializable> setParsableSerializable(key: String, value: PS)
    {
        val delegateSerializer = DelegateSerializer("${key}.", this)
        value.serialize(delegateSerializer)
    }

    fun setBooleanArray(key: String, array: BooleanArray)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setBoolean("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setCharArray(key: String, array: CharArray)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setChar("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setByteArray(key: String, array: ByteArray)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setByte("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setShortArray(key: String, array: ShortArray)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setShort("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setIntArray(key: String, array: IntArray)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setInt("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setLongArray(key: String, array: LongArray)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setLong("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setFloatArray(key: String, array: FloatArray)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setFloat("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setDoubleArray(key: String, array: DoubleArray)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setDouble("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setStringArray(key: String, array: Array<String>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setString("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setCalendarArray(key: String, array: Array<Calendar>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setCalendar("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setDateArray(key: String, array: Array<Date>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setDate("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setLocaleArray(key: String, array: Array<Locale>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setLocale("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun <PS : ParsableSerializable> setParsableSerializableArray(key: String, array: Array<PS>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setParsableSerializable("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setBooleanList(key: String, array: List<Boolean>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setBoolean("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setCharList(key: String, array: List<Char>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setChar("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setByteList(key: String, array: List<Byte>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setByte("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setShortList(key: String, array: List<Short>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setShort("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setIntList(key: String, array: List<Int>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setInt("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setLongList(key: String, array: List<Long>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setLong("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setFloatList(key: String, array: List<Float>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setFloat("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setDoubleList(key: String, array: List<Double>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setDouble("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setStringList(key: String, array: List<String>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setString("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setCalendarList(key: String, array: List<Calendar>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setCalendar("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setDateList(key: String, array: List<Date>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setDate("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setLocaleList(key: String, array: List<Locale>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setLocale("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun <PS : ParsableSerializable> setParsableSerializableList(key: String, array: List<PS>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", array.size)

        for ((index, value) in array.withIndex())
        {
            this.setParsableSerializable("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", value)
        }
    }

    fun setBooleanMap(key: String, map: Map<String, Boolean>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", map.size)
        var index = 0

        for ((mapKey, value) in map)
        {
            this.setString("${key}.$KEY_MAP_KEY_HEADER$index", mapKey)
            this.setBoolean("${key}.$KEY_MAP_VALUE_HEADER$index", value)
            index++
        }
    }

    fun setCharMap(key: String, map: Map<String, Char>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", map.size)
        var index = 0

        for ((mapKey, value) in map)
        {
            this.setString("${key}.$KEY_MAP_KEY_HEADER$index", mapKey)
            this.setChar("${key}.$KEY_MAP_VALUE_HEADER$index", value)
            index++
        }
    }

    fun setByteMap(key: String, map: Map<String, Byte>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", map.size)
        var index = 0

        for ((mapKey, value) in map)
        {
            this.setString("${key}.$KEY_MAP_KEY_HEADER$index", mapKey)
            this.setByte("${key}.$KEY_MAP_VALUE_HEADER$index", value)
            index++
        }
    }

    fun setShortMap(key: String, map: Map<String, Short>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", map.size)
        var index = 0

        for ((mapKey, value) in map)
        {
            this.setString("${key}.$KEY_MAP_KEY_HEADER$index", mapKey)
            this.setShort("${key}.$KEY_MAP_VALUE_HEADER$index", value)
            index++
        }
    }

    fun setIntMap(key: String, map: Map<String, Int>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", map.size)
        var index = 0

        for ((mapKey, value) in map)
        {
            this.setString("${key}.$KEY_MAP_KEY_HEADER$index", mapKey)
            this.setInt("${key}.$KEY_MAP_VALUE_HEADER$index", value)
            index++
        }
    }

    fun setLongMap(key: String, map: Map<String, Long>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", map.size)
        var index = 0

        for ((mapKey, value) in map)
        {
            this.setString("${key}.$KEY_MAP_KEY_HEADER$index", mapKey)
            this.setLong("${key}.$KEY_MAP_VALUE_HEADER$index", value)
            index++
        }
    }

    fun setFloatMap(key: String, map: Map<String, Float>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", map.size)
        var index = 0

        for ((mapKey, value) in map)
        {
            this.setString("${key}.$KEY_MAP_KEY_HEADER$index", mapKey)
            this.setFloat("${key}.$KEY_MAP_VALUE_HEADER$index", value)
            index++
        }
    }

    fun setDoubleMap(key: String, map: Map<String, Double>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", map.size)
        var index = 0

        for ((mapKey, value) in map)
        {
            this.setString("${key}.$KEY_MAP_KEY_HEADER$index", mapKey)
            this.setDouble("${key}.$KEY_MAP_VALUE_HEADER$index", value)
            index++
        }
    }

    fun setStringMap(key: String, map: Map<String, String>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", map.size)
        var index = 0

        for ((mapKey, value) in map)
        {
            this.setString("${key}.$KEY_MAP_KEY_HEADER$index", mapKey)
            this.setString("${key}.$KEY_MAP_VALUE_HEADER$index", value)
            index++
        }
    }

    fun setCalendarMap(key: String, map: Map<String, Calendar>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", map.size)
        var index = 0

        for ((mapKey, value) in map)
        {
            this.setString("${key}.$KEY_MAP_KEY_HEADER$index", mapKey)
            this.setCalendar("${key}.$KEY_MAP_VALUE_HEADER$index", value)
            index++
        }
    }

    fun setDateMap(key: String, map: Map<String, Date>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", map.size)
        var index = 0

        for ((mapKey, value) in map)
        {
            this.setString("${key}.$KEY_MAP_KEY_HEADER$index", mapKey)
            this.setDate("${key}.$KEY_MAP_VALUE_HEADER$index", value)
            index++
        }
    }

    fun setLocaleMap(key: String, map: Map<String, Locale>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", map.size)
        var index = 0

        for ((mapKey, value) in map)
        {
            this.setString("${key}.$KEY_MAP_KEY_HEADER$index", mapKey)
            this.setLocale("${key}.$KEY_MAP_VALUE_HEADER$index", value)
            index++
        }
    }

    fun <PS : ParsableSerializable> setParsableSerializableMap(key: String, map: Map<String, PS>)
    {
        this.setInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE", map.size)
        var index = 0

        for ((mapKey, value) in map)
        {
            this.setString("${key}.$KEY_MAP_KEY_HEADER$index", mapKey)
            this.setParsableSerializable("${key}.$KEY_MAP_VALUE_HEADER$index", value)
            index++
        }
    }
}