package khelp.utilities.serailzation

import java.util.Calendar
import java.util.Date
import java.util.Locale

class DelegateSerializer(private val headerKey: String, private val serializer: Serializer) : Serializer
{
    override fun setBoolean(key: String, value: Boolean)
    {
        this.serializer.setBoolean("${headerKey}$key", value)
    }

    override fun setChar(key: String, value: Char)
    {
        this.serializer.setChar("${headerKey}$key", value)
    }

    override fun setByte(key: String, value: Byte)
    {
        this.serializer.setByte("${headerKey}$key", value)
    }

    override fun setShort(key: String, value: Short)
    {
        this.serializer.setShort("${headerKey}$key", value)
    }

    override fun setInt(key: String, value: Int)
    {
        this.serializer.setInt("${headerKey}$key", value)
    }

    override fun setLong(key: String, value: Long)
    {
        this.serializer.setLong("${headerKey}$key", value)
    }

    override fun setFloat(key: String, value: Float)
    {
        this.serializer.setFloat("${headerKey}$key", value)
    }

    override fun setDouble(key: String, value: Double)
    {
        this.serializer.setDouble("${headerKey}$key", value)
    }

    override fun setString(key: String, value: String)
    {
        this.serializer.setString("${headerKey}$key", value)
    }

    override fun setCalendar(key: String, value: Calendar)
    {
        this.serializer.setCalendar("${headerKey}$key", value)
    }

    override fun setDate(key: String, value: Date)
    {
        this.serializer.setDate("${headerKey}$key", value)
    }

    override fun setLocale(key: String, value: Locale)
    {
        this.serializer.setLocale("${headerKey}$key", value)
    }

    override fun <PS : ParsableSerializable> setParsableSerializable(key: String, value: PS)
    {
        this.serializer.setParsableSerializable("${headerKey}$key", value)
    }

    override fun setBooleanArray(key: String, array: BooleanArray)
    {
        this.serializer.setBooleanArray("${headerKey}$key", array)
    }

    override fun setCharArray(key: String, array: CharArray)
    {
        this.serializer.setCharArray("${headerKey}$key", array)
    }

    override fun setByteArray(key: String, array: ByteArray)
    {
        this.serializer.setByteArray("${headerKey}$key", array)
    }

    override fun setShortArray(key: String, array: ShortArray)
    {
        this.serializer.setShortArray("${headerKey}$key", array)
    }

    override fun setIntArray(key: String, array: IntArray)
    {
        this.serializer.setIntArray("${headerKey}$key", array)
    }

    override fun setLongArray(key: String, array: LongArray)
    {
        this.serializer.setLongArray("${headerKey}$key", array)
    }

    override fun setFloatArray(key: String, array: FloatArray)
    {
        this.serializer.setFloatArray("${headerKey}$key", array)
    }

    override fun setDoubleArray(key: String, array: DoubleArray)
    {
        this.serializer.setDoubleArray("${headerKey}$key", array)
    }

    override fun setStringArray(key: String, array: Array<String>)
    {
        this.serializer.setStringArray("${headerKey}$key", array)
    }

    override fun setCalendarArray(key: String, array: Array<Calendar>)
    {
        this.serializer.setCalendarArray("${headerKey}$key", array)
    }

    override fun setDateArray(key: String, array: Array<Date>)
    {
        this.serializer.setDateArray("${headerKey}$key", array)
    }

    override fun setLocaleArray(key: String, array: Array<Locale>)
    {
        this.serializer.setLocaleArray("${headerKey}$key", array)
    }

    override fun <PS : ParsableSerializable> setParsableSerializableArray(key: String, array: Array<PS>)
    {
        this.serializer.setParsableSerializableArray("${headerKey}$key", array)
    }

    override fun setBooleanList(key: String, array: List<Boolean>)
    {
        this.serializer.setBooleanList("${headerKey}$key", array)
    }

    override fun setCharList(key: String, array: List<Char>)
    {
        this.serializer.setCharList("${headerKey}$key", array)
    }

    override fun setByteList(key: String, array: List<Byte>)
    {
        this.serializer.setByteList("${headerKey}$key", array)
    }

    override fun setShortList(key: String, array: List<Short>)
    {
        this.serializer.setShortList("${headerKey}$key", array)
    }

    override fun setIntList(key: String, array: List<Int>)
    {
        this.serializer.setIntList("${headerKey}$key", array)
    }

    override fun setLongList(key: String, array: List<Long>)
    {
        this.serializer.setLongList("${headerKey}$key", array)
    }

    override fun setFloatList(key: String, array: List<Float>)
    {
        this.serializer.setFloatList("${headerKey}$key", array)
    }

    override fun setDoubleList(key: String, array: List<Double>)
    {
        this.serializer.setDoubleList("${headerKey}$key", array)
    }

    override fun setStringList(key: String, array: List<String>)
    {
        this.serializer.setStringList("${headerKey}$key", array)
    }

    override fun setCalendarList(key: String, array: List<Calendar>)
    {
        this.serializer.setCalendarList("${headerKey}$key", array)
    }

    override fun setDateList(key: String, array: List<Date>)
    {
        this.serializer.setDateList("${headerKey}$key", array)
    }

    override fun setLocaleList(key: String, array: List<Locale>)
    {
        this.serializer.setLocaleList("${headerKey}$key", array)
    }

    override fun <PS : ParsableSerializable> setParsableSerializableList(key: String, array: List<PS>)
    {
        this.serializer.setParsableSerializableList("${headerKey}$key", array)
    }

    override fun setBooleanMap(key: String, map: Map<String, Boolean>)
    {
        this.serializer.setBooleanMap("${headerKey}$key", map)
    }

    override fun setCharMap(key: String, map: Map<String, Char>)
    {
        this.serializer.setCharMap("${headerKey}$key", map)
    }

    override fun setByteMap(key: String, map: Map<String, Byte>)
    {
        this.serializer.setByteMap("${headerKey}$key", map)
    }

    override fun setShortMap(key: String, map: Map<String, Short>)
    {
        this.serializer.setShortMap("${headerKey}$key", map)
    }

    override fun setIntMap(key: String, map: Map<String, Int>)
    {
        this.serializer.setIntMap("${headerKey}$key", map)
    }

    override fun setLongMap(key: String, map: Map<String, Long>)
    {
        this.serializer.setLongMap("${headerKey}$key", map)
    }

    override fun setFloatMap(key: String, map: Map<String, Float>)
    {
        this.serializer.setFloatMap("${headerKey}$key", map)
    }

    override fun setDoubleMap(key: String, map: Map<String, Double>)
    {
        this.serializer.setDoubleMap("${headerKey}$key", map)
    }

    override fun setStringMap(key: String, map: Map<String, String>)
    {
        this.serializer.setStringMap("${headerKey}$key", map)
    }

    override fun setCalendarMap(key: String, map: Map<String, Calendar>)
    {
        this.serializer.setCalendarMap("${headerKey}$key", map)
    }

    override fun setDateMap(key: String, map: Map<String, Date>)
    {
        this.serializer.setDateMap("${headerKey}$key", map)
    }

    override fun setLocaleMap(key: String, map: Map<String, Locale>)
    {
        this.serializer.setLocaleMap("${headerKey}$key", map)
    }

    override fun <PS : ParsableSerializable> setParsableSerializableMap(key: String, map: Map<String, PS>)
    {
        this.serializer.setParsableSerializableMap("${headerKey}$key", map)
    }
}