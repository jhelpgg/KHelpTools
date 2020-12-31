package khelp.utilities.serialization

import java.util.Calendar
import khelp.utilities.extensions.calendar
import khelp.utilities.math.EPSILON
import kotlin.math.PI
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ParsableSerializableTests
{
    private fun assertEquals(data1Expected: Data1, data1Actual: Data1, additionalMessage: String = "")
    {
        Assertions.assertEquals(data1Expected.boolean, data1Actual.boolean, "Different boolean$additionalMessage")
        Assertions.assertEquals(data1Expected.char, data1Actual.char, "Different char$additionalMessage")
        Assertions.assertEquals(data1Expected.data.size,
                                data1Actual.data.size,
                                "data have not same size$additionalMessage")

        for ((index, byte) in data1Expected.data.withIndex())
        {
            Assertions.assertEquals(byte, data1Actual.data[index], "Not same data at index $index$additionalMessage")
        }

        Assertions.assertEquals(data1Expected.calendar[Calendar.YEAR],
                                data1Actual.calendar[Calendar.YEAR],
                                "Different year in calendar$additionalMessage")
        Assertions.assertEquals(data1Expected.calendar[Calendar.MONTH],
                                data1Actual.calendar[Calendar.MONTH],
                                "Different month in calendar$additionalMessage")
        Assertions.assertEquals(data1Expected.calendar[Calendar.DAY_OF_MONTH],
                                data1Actual.calendar[Calendar.DAY_OF_MONTH],
                                "Different day in calendar$additionalMessage")

        Assertions.assertEquals(data1Expected.integers.size,
                                data1Actual.integers.size,
                                "integers have not same size$additionalMessage")

        for ((index, integer) in data1Expected.integers.withIndex())
        {
            Assertions.assertEquals(integer,
                                    data1Actual.integers[index],
                                    "Not same integer at index $index$additionalMessage")
        }
    }

    private fun assertEquals(data2Expected: Data2, data2Actual: Data2)
    {
        Assertions.assertEquals(data2Expected.doubles.size, data2Actual.doubles.size, "doubles have not same size")

        for ((index, double) in data2Expected.doubles.withIndex())
        {
            Assertions.assertEquals(double, data2Expected.doubles[index], EPSILON, "Not same double at index $index")
        }

        this.assertEquals(data2Expected.data1, data2Actual.data1, " : Different data1")

        Assertions.assertEquals(data2Expected.map.size, data2Actual.map.size, "map have not same size")

        for ((key, value) in data2Expected.map)
        {
            Assertions.assertEquals(value, data2Actual.map[key], "Different value for key $key")
        }

        Assertions.assertEquals(data2Expected.others.size, data2Actual.others.size, "others have not same size")

        for ((index, data1) in data2Expected.others.withIndex())
        {
            this.assertEquals(data1, data2Expected.others[index], " : Not same data1 at index $index")
        }
    }

    @Test
    fun data1Test()
    {
        val data1Source = Data1(true, 'G', byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), calendar(1985, 4, 1))
        data1Source.integers.addAll(listOf(741, -852, 963))
        val mapParserSerializer = MapParserSerializer()
        data1Source.serialize(mapParserSerializer)
        val data1Target = Data1(false, ' ', ByteArray(0))
        data1Target.parse(mapParserSerializer)
        this.assertEquals(data1Source, data1Target)
    }

    @Test
    fun data2Test()
    {
        var data1 = Data1(true, 'G', byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), calendar(1985, 4, 1))
        data1.integers.addAll(listOf(741, -852, 963))
        val data2Source = Data2(doubleArrayOf(PI, 0.123456789, -73.42), data1)
        data2Source.map["feet"] = "foot"
        data2Source.map["test"] = "try"
        data2Source.map["hello"] = "world"
        data2Source.others.add(Data1(true, 'K', byteArrayOf(52, -42, 73), calendar(1970, 1, 1)))
        data1 = Data1(false, '@', byteArrayOf(0x42, 85), calendar(2020, 12, 31, 11, 6, 0))
        data1.integers.addAll(listOf(77777777, 666))
        data2Source.others.add(data1)
        val mapParserSerializer = MapParserSerializer()
        data2Source.serialize(mapParserSerializer)
        val data2Target = Data2(DoubleArray(0), Data1(false, ' ', ByteArray(0)))
        data2Target.parse(mapParserSerializer)
        this.assertEquals(data2Source, data2Target)
    }
}