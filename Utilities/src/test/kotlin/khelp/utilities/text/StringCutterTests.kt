package khelp.utilities.text

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class StringCutterTests
{
    @Test
    fun noEmpty()
    {
        val stringCutter = StringCutter("test|testing", '|')
        Assertions.assertEquals("test", stringCutter.next())
        Assertions.assertEquals("testing", stringCutter.next())
        Assertions.assertNull(stringCutter.next())
    }

    @Test
    fun firstEmpty()
    {
        val stringCutter = StringCutter("|test|testing", '|')
        Assertions.assertEquals("", stringCutter.next())
        Assertions.assertEquals("test", stringCutter.next())
        Assertions.assertEquals("testing", stringCutter.next())
        Assertions.assertNull(stringCutter.next())
    }

    @Test
    fun lastEmpty()
    {
        val stringCutter = StringCutter("test|testing|", '|')
        Assertions.assertEquals("test", stringCutter.next())
        Assertions.assertEquals("testing", stringCutter.next())
        Assertions.assertEquals("", stringCutter.next())
        Assertions.assertNull(stringCutter.next())
    }

    @Test
    fun middleEmpty()
    {
        val stringCutter = StringCutter("test||testing", '|')
        Assertions.assertEquals("test", stringCutter.next())
        Assertions.assertEquals("", stringCutter.next())
        Assertions.assertEquals("testing", stringCutter.next())
        Assertions.assertNull(stringCutter.next())
    }
}