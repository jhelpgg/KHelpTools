package khelp.utilities.reflect

import java.util.Calendar
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ReflectorTests
{
    @Test
    fun defaultValueTest()
    {
        Assertions.assertEquals(0, defaultValue(Int::class.java))
        val person = defaultValue(Person::class.java)
        Assertions.assertEquals("", person.name)
        Assertions.assertEquals(Gender.OTHER, person.gender)
        Assertions.assertEquals(1970, person.birthDate[Calendar.YEAR])
        Assertions.assertEquals(Calendar.JANUARY, person.birthDate[Calendar.MONTH])
        Assertions.assertEquals(1, person.birthDate[Calendar.DAY_OF_MONTH])
        Assertions.assertEquals("", person.address.place)
        Assertions.assertEquals(0, person.address.number)
        Assertions.assertEquals(0, person.address.data.size)
    }
}