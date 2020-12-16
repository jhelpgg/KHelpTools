package khelp.utilities.collections

import org.junit.Assert
import org.junit.Test

class SortedArrayTests
{
    @Test
    fun naturalOrderNotUnique()
    {
        val sortedArray = sortedArray<String>()
        sortedArray += "hello"
        sortedArray += "world"
        sortedArray += "hello"
        sortedArray += "array"
        sortedArray += "iterator"
        Assert.assertEquals(5, sortedArray.size)
        Assert.assertEquals("array", sortedArray[0])
        Assert.assertEquals("hello", sortedArray[1])
        Assert.assertEquals("hello", sortedArray[2])
        Assert.assertEquals("iterator", sortedArray[3])
        Assert.assertEquals("world", sortedArray[4])
    }

    @Test
    fun naturalOrderUnique()
    {
        val sortedArray = sortedArray<String>(true)
        sortedArray += "hello"
        sortedArray += "world"
        sortedArray += "hello"
        sortedArray += "array"
        sortedArray += "iterator"
        Assert.assertEquals(4, sortedArray.size)
        Assert.assertEquals("array", sortedArray[0])
        Assert.assertEquals("hello", sortedArray[1])
        Assert.assertEquals("iterator", sortedArray[2])
        Assert.assertEquals("world", sortedArray[3])
    }

    @Test
    fun caseNotSensitiveOrder()
    {
        val sortedArray = SortedArray(CaseNotSensitiveOrder,true)
        sortedArray += "hello"
        sortedArray += "world"
        sortedArray += "HellO"
        sortedArray += "array"
        sortedArray += "iterator"
        Assert.assertEquals(4, sortedArray.size)
        Assert.assertEquals("array", sortedArray[0])
        Assert.assertEquals("hello", sortedArray[1])
        Assert.assertEquals("iterator", sortedArray[2])
        Assert.assertEquals("world", sortedArray[3])
    }
}