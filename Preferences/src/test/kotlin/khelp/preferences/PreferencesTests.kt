package khelp.preferences

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PreferencesTests
{
    @Test
    fun preferencesReadWrite()
    {
        val addressJumpStreet = Address("Jump street")
        val addressBakerStreet = Address("Baker Street")
        val personMarch = Person("March", addressJumpStreet)
        val personMargaret = Person("Margaret", addressJumpStreet)
        val personSophie = Person("Sophie", addressBakerStreet)
        val personPaul = Person("Paul", addressBakerStreet)
        personMarch += personMargaret
        personMarch += personSophie
        personSophie += personPaul

        var preferences = Preferences()
        preferences.edit {
            "boolean" IS true
            "int" IS 42
            "long" IS 666L
            "float" IS 42.73f
            "double" IS 77.77
            "information" IS "This is an information"
            "numberType" IS NumberType.FLOAT
            "person" IS personMarch
        }

        preferences = Preferences()
        Assertions.assertEquals(true, preferences["boolean", false])
        Assertions.assertEquals(42, preferences["int", 0])
        Assertions.assertEquals(666L, preferences["long", 0L])
        Assertions.assertEquals(42.73f, preferences["float", 0f], 0.01f)
        Assertions.assertEquals(77.77, preferences["double", 0.0], 0.01)
        Assertions.assertEquals(NumberType.FLOAT, preferences["numberType", NumberType.BYTE])
        Assertions.assertEquals("This is an information", preferences["information", ""])
        var person = preferences.obtain("person") { Person() }
        Assertions.assertEquals("March", person.name)
        Assertions.assertEquals("Jump street", person.address.street)
        var iterator = person.iterator()
        Assertions.assertTrue(iterator.hasNext())
        person = iterator.next()
        Assertions.assertEquals("Margaret", person.name)
        Assertions.assertEquals("Jump street", person.address.street)
        Assertions.assertTrue(iterator.hasNext())
        person = iterator.next()
        Assertions.assertEquals("Sophie", person.name)
        Assertions.assertEquals("Baker Street", person.address.street)
        Assertions.assertFalse(iterator.hasNext())
        iterator = person.iterator()
        Assertions.assertTrue(iterator.hasNext())
        person = iterator.next()
        Assertions.assertEquals("Paul", person.name)
        Assertions.assertEquals("Baker Street", person.address.street)
        Assertions.assertFalse(iterator.hasNext())
        iterator = person.iterator()
        Assertions.assertFalse(iterator.hasNext())
    }
}
