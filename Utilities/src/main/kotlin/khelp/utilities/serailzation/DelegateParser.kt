package khelp.utilities.serailzation

import java.util.Calendar
import java.util.Date
import java.util.Locale

class DelegateParser(private val headerKey: String, private val parser: Parser) : Parser
{
    override fun getBoolean(key: String): Boolean =
        this.parser.getBoolean("${headerKey}.$key")

    override fun getChar(key: String): Char =
        this.parser.getChar("${headerKey}.$key")

    override fun getByte(key: String): Byte =
        this.parser.getByte("${headerKey}.$key")

    override fun getShort(key: String): Short =
        this.parser.getShort("${headerKey}.$key")

    override fun getInt(key: String): Int =
        this.parser.getInt("${headerKey}.$key")

    override fun getLong(key: String): Long =
        this.parser.getLong("${headerKey}.$key")

    override fun getFloat(key: String): Float =
        this.parser.getFloat("${headerKey}.$key")

    override fun getDouble(key: String): Double =
        this.parser.getDouble("${headerKey}.$key")

    override fun getString(key: String): String =
        this.parser.getString("${headerKey}.$key")

    override fun getCalendar(key: String): Calendar =
        this.parser.getCalendar("${headerKey}.$key")

    override fun getDate(key: String): Date =
        this.parser.getDate("${headerKey}.$key")

    override fun getLocale(key: String): Locale =
        this.parser.getLocale("${headerKey}.$key")

    override fun <PS : ParsableSerializable> getParsableSerializable(key: String, instanceProvider: () -> PS): PS =
        this.parser.getParsableSerializable("${headerKey}.$key", instanceProvider)

    override fun getBooleanArray(key: String): BooleanArray =
        this.parser.getBooleanArray("${headerKey}.$key")

    override fun getCharArray(key: String): CharArray =
        this.parser.getCharArray("${headerKey}.$key")

    override fun getByteArray(key: String): ByteArray =
        this.parser.getByteArray("${headerKey}.$key")

    override fun getShortArray(key: String): ShortArray =
        this.parser.getShortArray("${headerKey}.$key")

    override fun getIntArray(key: String): IntArray =
        this.parser.getIntArray("${headerKey}.$key")

    override fun getLongArray(key: String): LongArray =
        this.parser.getLongArray("${headerKey}.$key")

    override fun getFloatArray(key: String): FloatArray =
        this.parser.getFloatArray("${headerKey}.$key")

    override fun getDoubleArray(key: String): DoubleArray =
        this.parser.getDoubleArray("${headerKey}.$key")

    override fun getStringArray(key: String): Array<String> =
        this.parser.getStringArray("${headerKey}.$key")

    override fun getCalendarArray(key: String): Array<Calendar> =
        this.parser.getCalendarArray("${headerKey}.$key")

    override fun getDateArray(key: String): Array<Date> =
        this.parser.getDateArray("${headerKey}.$key")

    override fun getLocaleArray(key: String): Array<Locale> =
        this.parser.getLocaleArray("${headerKey}.$key")

    override fun <PS : ParsableSerializable> getParsableSerializableArray(key: String,
                                                                          instanceProvider: () -> PS): Array<PS> =
        this.parser.getParsableSerializableArray("${headerKey}.$key", instanceProvider)

    override fun appendBooleanList(key: String, list: MutableList<Boolean>)
    {
        this.parser.appendBooleanList("${headerKey}.$key", list)
    }

    override fun appendCharList(key: String, list: MutableList<Char>)
    {
        this.parser.appendCharList("${headerKey}.$key", list)
    }

    override fun appendByteList(key: String, list: MutableList<Byte>)
    {
        this.parser.appendByteList("${headerKey}.$key", list)
    }

    override fun appendShortList(key: String, list: MutableList<Short>)
    {
        this.parser.appendShortList("${headerKey}.$key", list)
    }

    override fun appendIntList(key: String, list: MutableList<Int>)
    {
        this.parser.appendIntList("${headerKey}.$key", list)
    }

    override fun appendLongList(key: String, list: MutableList<Long>)
    {
        this.parser.appendLongList("${headerKey}.$key", list)
    }

    override fun appendFloatList(key: String, list: MutableList<Float>)
    {
        this.parser.appendFloatList("${headerKey}.$key", list)
    }

    override fun appendDoubleList(key: String, list: MutableList<Double>)
    {
        this.parser.appendDoubleList("${headerKey}.$key", list)
    }

    override fun appendStringList(key: String, list: MutableList<String>)
    {
        this.parser.appendStringList("${headerKey}.$key", list)
    }

    override fun appendCalendarList(key: String, list: MutableList<Calendar>)
    {
        this.parser.appendCalendarList("${headerKey}.$key", list)
    }

    override fun appendDateList(key: String, list: MutableList<Date>)
    {
        this.parser.appendDateList("${headerKey}.$key", list)
    }

    override fun appendLocaleList(key: String, list: MutableList<Locale>)
    {
        this.parser.appendLocaleList("${headerKey}.$key", list)
    }

    override fun <PS : ParsableSerializable> appendParsableSerializableList(key: String,
                                                                            list: MutableList<PS>,
                                                                            instanceProvider: () -> PS)
    {
        this.parser.appendParsableSerializableList("${headerKey}.$key", list, instanceProvider)
    }

    override fun putBooleanMap(key: String, map: MutableMap<String, Boolean>)
    {
        this.parser.putBooleanMap("${headerKey}.$key", map)
    }

    override fun putCharMap(key: String, map: MutableMap<String, Char>)
    {
        this.parser.putCharMap("${headerKey}.$key", map)
    }

    override fun putByteMap(key: String, map: MutableMap<String, Byte>)
    {
        this.parser.putByteMap("${headerKey}.$key", map)
    }

    override fun putShortMap(key: String, map: MutableMap<String, Short>)
    {
        this.parser.putShortMap("${headerKey}.$key", map)
    }

    override fun putIntMap(key: String, map: MutableMap<String, Int>)
    {
        this.parser.putIntMap("${headerKey}.$key", map)
    }

    override fun putLongMap(key: String, map: MutableMap<String, Long>)
    {
        this.parser.putLongMap("${headerKey}.$key", map)
    }

    override fun putFloatMap(key: String, map: MutableMap<String, Float>)
    {
        this.parser.putFloatMap("${headerKey}.$key", map)
    }

    override fun putDoubleMap(key: String, map: MutableMap<String, Double>)
    {
        this.parser.putDoubleMap("${headerKey}.$key", map)
    }

    override fun putStringMap(key: String, map: MutableMap<String, String>)
    {
        this.parser.putStringMap("${headerKey}.$key", map)
    }

    override fun putCalendarMap(key: String, map: MutableMap<String, Calendar>)
    {
        this.parser.putCalendarMap("${headerKey}.$key", map)
    }

    override fun putDateMap(key: String, map: MutableMap<String, Date>)
    {
        this.parser.putDateMap("${headerKey}.$key", map)
    }

    override fun putLocaleMap(key: String, map: MutableMap<String, Locale>)
    {
        this.parser.putLocaleMap("${headerKey}.$key", map)
    }

    override fun <PS : ParsableSerializable> putParsableSerializableMap(key: String,
                                                                        map: MutableMap<String, PS>,
                                                                        instanceProvider: () -> PS)
    {
        this.parser.putParsableSerializableMap("${headerKey}.$key", map, instanceProvider)
    }
}
