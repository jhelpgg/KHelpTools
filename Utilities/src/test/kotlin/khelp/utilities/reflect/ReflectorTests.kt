package khelp.utilities.reflect

import java.util.Calendar
import org.junit.Assert
import org.junit.Test

class ReflectorTests
{
    @Test
    fun defaultValueTest()
    {
        Assert.assertEquals(0, defaultValue(Int::class.java))
        val person = defaultValue(Person::class.java)
        println(person)
    }
}