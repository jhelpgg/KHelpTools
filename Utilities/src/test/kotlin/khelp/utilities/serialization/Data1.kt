package khelp.utilities.serialization

import java.util.Calendar

class Data1(var boolean: Boolean,
            var char: Char,
            var data: ByteArray,
            var calendar: Calendar = Calendar.getInstance()) : ParsableSerializable
{
    val integers = ArrayList<Int>()

    override fun serialize(serializer: Serializer)
    {
        serializer.setBoolean("boolean", this.boolean)
        serializer.setChar("char", this.char)
        serializer.setByteArray("data", this.data)
        serializer.setCalendar("calendar", this.calendar)
        serializer.setIntList("integers", this.integers)
    }

    override fun parse(parser: Parser)
    {
        this.boolean = parser.getBoolean("boolean")
        this.char = parser.getChar("char")
        this.data = parser.getByteArray("data")
        this.calendar = parser.getCalendar("calendar")
        this.integers.clear()
        parser.appendIntList("integers", this.integers)
    }
}