package khelp.utilities.serialization

import java.util.Calendar
import java.util.Date
import java.util.Locale
import khelp.utilities.extensions.toLocale

/**
 * Serializer used by [ParsableSerializable], to parse data from a specific format
 */
interface Parser
{
    fun getBoolean(key: String): Boolean

    fun getChar(key: String): Char

    fun getByte(key: String): Byte

    fun getShort(key: String): Short

    fun getInt(key: String): Int

    fun getLong(key: String): Long

    fun getFloat(key: String): Float

    fun getDouble(key: String): Double

    fun getString(key: String): String

    fun getCalendar(key: String): Calendar
    {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this.getLong(key)
        return calendar
    }

    fun getDate(key: String): Date =
        Date(this.getLong(key))

    fun getLocale(key: String): Locale =
        this.getString(key)
            .toLocale()

    fun <PS : ParsableSerializable> getParsableSerializable(key: String, instanceProvider: () -> PS): PS
    {
        val value = instanceProvider()
        val delegateParser = DelegateParser("${key}.", this)
        value.parse(delegateParser)
        return value
    }

    fun getBooleanArray(key: String): BooleanArray
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")
        return BooleanArray(size) { index ->
            this.getBoolean("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index")
        }
    }

    fun getCharArray(key: String): CharArray
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")
        return CharArray(size) { index ->
            this.getChar("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index")
        }
    }

    fun getByteArray(key: String): ByteArray
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")
        return ByteArray(size) { index ->
            this.getByte("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index")
        }
    }

    fun getShortArray(key: String): ShortArray
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")
        return ShortArray(size) { index ->
            this.getShort("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index")
        }
    }

    fun getIntArray(key: String): IntArray
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")
        return IntArray(size) { index ->
            this.getInt("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index")
        }
    }

    fun getLongArray(key: String): LongArray
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")
        return LongArray(size) { index ->
            this.getLong("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index")
        }
    }

    fun getFloatArray(key: String): FloatArray
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")
        return FloatArray(size) { index ->
            this.getFloat("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index")
        }
    }

    fun getDoubleArray(key: String): DoubleArray
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")
        return DoubleArray(size) { index ->
            this.getDouble("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index")
        }
    }

    fun getStringArray(key: String): Array<String>
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")
        return Array(size) { index ->
            this.getString("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index")
        }
    }

    fun getCalendarArray(key: String): Array<Calendar>
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")
        return Array(size) { index ->
            this.getCalendar("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index")
        }
    }

    fun getDateArray(key: String): Array<Date>
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")
        return Array(size) { index ->
            this.getDate("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index")
        }
    }

    fun getLocaleArray(key: String): Array<Locale>
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")
        return Array(size) { index ->
            this.getLocale("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index")
        }
    }

    fun <PS : ParsableSerializable> getParsableSerializableArray(key: String, instanceProvider: () -> PS): Array<PS>
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        @Suppress("UNCHECKED_CAST")
        val array = java.lang.reflect.Array.newInstance(instanceProvider().javaClass, size) as Array<PS>

        for (index in 0 until size)
        {
            array[index] = this.getParsableSerializable("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", instanceProvider)
        }

        return array
    }

    fun appendBooleanList(key: String, list: MutableList<Boolean>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            list.add(this.getBoolean("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index"))
        }
    }

    fun appendCharList(key: String, list: MutableList<Char>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            list.add(this.getChar("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index"))
        }
    }

    fun appendByteList(key: String, list: MutableList<Byte>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            list.add(this.getByte("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index"))
        }
    }

    fun appendShortList(key: String, list: MutableList<Short>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            list.add(this.getShort("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index"))
        }
    }

    fun appendIntList(key: String, list: MutableList<Int>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            list.add(this.getInt("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index"))
        }
    }

    fun appendLongList(key: String, list: MutableList<Long>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            list.add(this.getLong("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index"))
        }
    }

    fun appendFloatList(key: String, list: MutableList<Float>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            list.add(this.getFloat("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index"))
        }
    }

    fun appendDoubleList(key: String, list: MutableList<Double>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            list.add(this.getDouble("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index"))
        }
    }

    fun appendStringList(key: String, list: MutableList<String>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            list.add(this.getString("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index"))
        }
    }

    fun appendCalendarList(key: String, list: MutableList<Calendar>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            list.add(this.getCalendar("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index"))
        }
    }

    fun appendDateList(key: String, list: MutableList<Date>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            list.add(this.getDate("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index"))
        }
    }

    fun appendLocaleList(key: String, list: MutableList<Locale>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            list.add(this.getLocale("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index"))
        }
    }

    fun <PS : ParsableSerializable> appendParsableSerializableList(key: String,
                                                                   list: MutableList<PS>,
                                                                   instanceProvider: () -> PS)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            list.add(this.getParsableSerializable("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index", instanceProvider))
        }
    }

    fun putBooleanMap(key: String, map: MutableMap<String, Boolean>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            map[this.getString("${key}.$KEY_MAP_KEY_HEADER$index")] = this.getBoolean("${key}.$KEY_MAP_VALUE_HEADER$index")
        }
    }

    fun putCharMap(key: String, map: MutableMap<String, Char>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            map[this.getString("${key}.$KEY_MAP_KEY_HEADER$index")] = this.getChar("${key}.$KEY_MAP_VALUE_HEADER$index")
        }
    }

    fun putByteMap(key: String, map: MutableMap<String, Byte>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            map[this.getString("${key}.$KEY_MAP_KEY_HEADER$index")] = this.getByte("${key}.$KEY_MAP_VALUE_HEADER$index")
        }
    }

    fun putShortMap(key: String, map: MutableMap<String, Short>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            map[this.getString("${key}.$KEY_MAP_KEY_HEADER$index")] = this.getShort("${key}.$KEY_MAP_VALUE_HEADER$index")
        }
    }

    fun putIntMap(key: String, map: MutableMap<String, Int>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            map[this.getString("${key}.$KEY_MAP_KEY_HEADER$index")] = this.getInt("${key}.$KEY_MAP_VALUE_HEADER$index")
        }
    }

    fun putLongMap(key: String, map: MutableMap<String, Long>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            map[this.getString("${key}.$KEY_MAP_KEY_HEADER$index")] = this.getLong("${key}.$KEY_MAP_VALUE_HEADER$index")
        }
    }

    fun putFloatMap(key: String, map: MutableMap<String, Float>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            map[this.getString("${key}.$KEY_MAP_KEY_HEADER$index")] = this.getFloat("${key}.$KEY_MAP_VALUE_HEADER$index")
        }
    }

    fun putDoubleMap(key: String, map: MutableMap<String, Double>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            map[this.getString("${key}.$KEY_MAP_KEY_HEADER$index")] = this.getDouble("${key}.$KEY_MAP_VALUE_HEADER$index")
        }
    }

    fun putStringMap(key: String, map: MutableMap<String, String>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            map[this.getString("${key}.$KEY_MAP_KEY_HEADER$index")] = this.getString("${key}.$KEY_MAP_VALUE_HEADER$index")
        }
    }

    fun putCalendarMap(key: String, map: MutableMap<String, Calendar>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            map[this.getString("${key}.$KEY_MAP_KEY_HEADER$index")] = this.getCalendar("${key}.$KEY_MAP_VALUE_HEADER$index")
        }
    }

    fun putDateMap(key: String, map: MutableMap<String, Date>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            map[this.getString("${key}.$KEY_MAP_KEY_HEADER$index")] = this.getDate("${key}.$KEY_MAP_VALUE_HEADER$index")
        }
    }

    fun putLocaleMap(key: String, map: MutableMap<String, Locale>)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            map[this.getString("${key}.$KEY_MAP_KEY_HEADER$index")] = this.getLocale("${key}.$KEY_MAP_VALUE_HEADER$index")
        }
    }

    fun <PS : ParsableSerializable> putParsableSerializableMap(key: String,
                                                               map: MutableMap<String, PS>,
                                                               instanceProvider: () -> PS)
    {
        val size = this.getInt("${key}.$KEY_LIST_ARRAY_MAP_SIZE")

        for (index in 0 until size)
        {
            map[this.getString("${key}.$KEY_MAP_KEY_HEADER$index")] =
                this.getParsableSerializable("${key}.$KEY_LIST_ARRAY_INDEX_HEADER$index",
                                             instanceProvider)
        }
    }
}