package khelp.io.files.json

import khelp.thread.Locker
import khelp.utilities.stream.StringInputStream
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class JsonParserReaderCallbackTests
{

    @Test
    fun parse()
    {
        val locker = Locker()
        var objectCollected : ObjectJson? = null
        var arrayCollected : ArrayJson? = null
        var errorCollected : JsonReaderException? = null
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
        JsonParserReaderCallback.parse(stringInputStream,
                                       { objectJson ->
                             objectCollected = objectJson
                             locker.unlock()
                         },
                                       { arrayJson ->
                             arrayCollected = arrayJson
                             locker.unlock()
                         },
                                       { error ->
                             errorCollected = error
                             locker.unlock()
                         })
        locker.lock()
        Assertions.assertNotNull(objectCollected)
        Assertions.assertNull(arrayCollected)
        Assertions.assertNull(errorCollected, "$errorCollected")

        val objectJson = objectCollected !!
        Assertions.assertTrue("person" in objectJson)
        Assertions.assertEquals(JsonDataType.OBJECT, objectJson.type("person"))
        val person = objectJson.objectJson("person")
        Assertions.assertEquals("Arthur", person.string("name"))
        Assertions.assertEquals(42, person.int("age"))

        Assertions.assertTrue("friends" in objectJson)
        Assertions.assertEquals(JsonDataType.ARRAY, objectJson.type("friends"))
        val friends = objectJson.arrayJson("friends")
        Assertions.assertEquals(1, friends.size)
        val child = friends.objectJson(0)
        Assertions.assertEquals("Albator", child.string("name"))
        Assertions.assertTrue(child.boolean("wanted"))

        Assertions.assertFalse("name" in objectJson)
        try
        {
            objectJson.type("name")
            Assertions.fail("Should throw a JsonException this key not exists")
        }
        catch (jsonException : JsonException)
        {
            // That is what we expect
        }
    }
}