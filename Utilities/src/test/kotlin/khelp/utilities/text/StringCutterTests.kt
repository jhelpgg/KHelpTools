package khelp.utilities.text

import org.junit.Assert
import org.junit.Test

class StringCutterTests
{
    @Test
    fun noEmpty()
    {
        val stringCutter = StringCutter("test|testing", '|')
        Assert.assertEquals("test", stringCutter.next())
        Assert.assertEquals("testing", stringCutter.next())
        Assert.assertNull(stringCutter.next())
    }

    @Test
    fun firstEmpty()
    {
        val stringCutter = StringCutter("|test|testing", '|')
        Assert.assertEquals("", stringCutter.next())
        Assert.assertEquals("test", stringCutter.next())
        Assert.assertEquals("testing", stringCutter.next())
        Assert.assertNull(stringCutter.next())
    }

    @Test
    fun lastEmpty()
    {
        val stringCutter = StringCutter("test|testing|", '|')
        Assert.assertEquals("test", stringCutter.next())
        Assert.assertEquals("testing", stringCutter.next())
        Assert.assertEquals("", stringCutter.next())
        Assert.assertNull(stringCutter.next())
    }

    @Test
    fun middleEmpty()
    {
        val stringCutter = StringCutter("test||testing", '|')
        Assert.assertEquals("test", stringCutter.next())
        Assert.assertEquals("", stringCutter.next())
        Assert.assertEquals("testing", stringCutter.next())
        Assert.assertNull(stringCutter.next())
    }
}