package khelp.utilities.reflect

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ReflectorTests
{
    @Test
    fun defaultValueTest()
    {
        Assertions.assertEquals(0, defaultValue(Int::class.java))
        val person = defaultValue(Person::class.java)
        println(person)
    }
}