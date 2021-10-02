package khelp.io.files.json

import khelp.utilities.stream.StringOutputStream
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class JsonWriterTests
{
    @Test
    fun simpleWrite()
    {
        val stringOutputStream = StringOutputStream()
        val writer = JsonWriter(stringOutputStream)
        writer.startObject()
        writer.putString("name", "Arthur")
        writer.putNumber("age", 42.0)

        writer.startArray("friends")
        writer.startObject()
        writer.putString("name", "Albator")
        writer.putBoolean("wanted", true)
        writer.endObject()
        writer.putNumber(73.0)

        writer.endArray()
        writer.endObject()
        writer.finish()

        val lineSeparator = System.lineSeparator()

        val expected =
            "{$lineSeparator" +
            "   \"name\" : \"Arthur\",$lineSeparator" +
            "   \"age\" : 42.0,$lineSeparator" +
            "   \"friends\" :$lineSeparator" +
            "   [$lineSeparator" +
            "      {$lineSeparator" +
            "         \"name\" : \"Albator\",$lineSeparator" +
            "         \"wanted\" : true$lineSeparator" +
            "      }, 73.0]$lineSeparator" +
            "}"
        Assertions.assertEquals(expected, stringOutputStream.string)
    }

    @Test
    fun simpleWriteCompact()
    {
        val stringOutputStream = StringOutputStream()
        val writer = JsonWriter(stringOutputStream, compact = true)
        writer.startObject()
        writer.putString("name", "Arthur")
        writer.putNumber("age", 42.0)

        writer.startArray("friends")
        writer.startObject()
        writer.putString("name", "Albator")
        writer.putBoolean("wanted", true)
        writer.endObject()
        writer.putNumber(73.0)

        writer.endArray()
        writer.endObject()
        writer.finish()

        Assertions.assertEquals("{\"name\":\"Arthur\",\"age\":42.0,\"friends\":[{\"name\":\"Albator\",\"wanted\":true},73.0]}",
                                stringOutputStream.string)
    }
}
