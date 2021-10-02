package khelp.io.files.json

import khelp.thread.Locker
import khelp.utilities.stream.StringInputStream
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class JsonReaderTests
{
    @Test
    fun readValidObjectJson()
    {
        val stringInputStream = StringInputStream("""
            {
                "person":
                {
                    "name":"Arthur",
                    "age":42
                },
                "friends":
                [
                    {
                        "name":"Albator",
                        "wanted":true
                    }
                ]
            }
        """.trimIndent())
        val locker = Locker()
        val collector = JsonReaderCallBackCollector(locker)

        val jsonReader = JsonReader(stringInputStream, collector)
        locker.lock()

        var info = collector.collected[0]
        Assertions.assertTrue(info is StartDocument)

        info = collector.collected[1]
        Assertions.assertTrue(info is StartObject)
        Assertions.assertEquals("", (info as StartObject).name)

        info = collector.collected[2]
        Assertions.assertTrue(info is StartObject)
        Assertions.assertEquals("person", (info as StartObject).name)

        info = collector.collected[3]
        Assertions.assertTrue(info is MeetString)
        Assertions.assertEquals("name", (info as MeetString).name)
        Assertions.assertEquals("Arthur", (info as MeetString).value)

        info = collector.collected[4]
        Assertions.assertTrue(info is MeetNumber)
        Assertions.assertEquals("age", (info as MeetNumber).name)
        Assertions.assertEquals(42.0, (info as MeetNumber).value, 0.1)

        info = collector.collected[5]
        Assertions.assertTrue(info is EndObject)

        info = collector.collected[6]
        Assertions.assertTrue(info is StartArray)
        Assertions.assertEquals("friends", (info as StartArray).name)

        info = collector.collected[7]
        Assertions.assertTrue(info is StartObject)
        Assertions.assertEquals("", (info as StartObject).name)

        info = collector.collected[8]
        Assertions.assertTrue(info is MeetString)
        Assertions.assertEquals("name", (info as MeetString).name)
        Assertions.assertEquals("Albator", (info as MeetString).value)

        info = collector.collected[9]
        Assertions.assertTrue(info is MeetBoolean)
        Assertions.assertEquals("wanted", (info as MeetBoolean).name)
        Assertions.assertEquals(true, (info as MeetBoolean).value)

        info = collector.collected[10]
        Assertions.assertTrue(info is EndObject)

        info = collector.collected[11]
        Assertions.assertTrue(info is EndArray)

        info = collector.collected[12]
        Assertions.assertTrue(info is EndObject)

        info = collector.collected[13]
        Assertions.assertTrue(info is EndDocument)
    }
}
