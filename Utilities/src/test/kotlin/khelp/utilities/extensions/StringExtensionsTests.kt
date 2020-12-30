package khelp.utilities.extensions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class StringExtensionsTests
{
    @Test
    fun ellipseIfMoreThanTest()
    {
        Assertions.assertEquals("", "".ellipseIfMoreThan(7))
        Assertions.assertEquals("12345", "12345".ellipseIfMoreThan(7))
        Assertions.assertEquals("1234567", "1234567".ellipseIfMoreThan(7))
        Assertions.assertEquals("12...78", "12345678".ellipseIfMoreThan(7))
        Assertions.assertEquals("12...89", "123456789".ellipseIfMoreThan(7))
        Assertions.assertEquals("12...9", "123456789".ellipseIfMoreThan(6))
    }

    @Test
    fun onlyFirstTest()
    {
        Assertions.assertEquals("", "".onlyFirst(7))
        Assertions.assertEquals("12345", "12345".onlyFirst(7))
        Assertions.assertEquals("1234567", "1234567".onlyFirst(7))
        Assertions.assertEquals("1234...", "12345678".onlyFirst(7))
        Assertions.assertEquals("1234...", "123456789".onlyFirst(7))
        Assertions.assertEquals("123...", "123456789".onlyFirst(6))
    }

    @Test
    fun onlyLastTest()
    {
        Assertions.assertEquals("", "".onlyLast(7))
        Assertions.assertEquals("12345", "12345".onlyLast(7))
        Assertions.assertEquals("1234567", "1234567".onlyLast(7))
        Assertions.assertEquals("...5678", "12345678".onlyLast(7))
        Assertions.assertEquals("...6789", "123456789".onlyLast(7))
        Assertions.assertEquals("...789", "123456789".onlyLast(6))
    }
}