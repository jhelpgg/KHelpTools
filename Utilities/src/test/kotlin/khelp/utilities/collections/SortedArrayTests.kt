package khelp.utilities.collections

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


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
        Assertions.assertEquals(5, sortedArray.size)
        Assertions.assertEquals("array", sortedArray[0])
        Assertions.assertEquals("hello", sortedArray[1])
        Assertions.assertEquals("hello", sortedArray[2])
        Assertions.assertEquals("iterator", sortedArray[3])
        Assertions.assertEquals("world", sortedArray[4])
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
        Assertions.assertEquals(4, sortedArray.size)
        Assertions.assertEquals("array", sortedArray[0])
        Assertions.assertEquals("hello", sortedArray[1])
        Assertions.assertEquals("iterator", sortedArray[2])
        Assertions.assertEquals("world", sortedArray[3])
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
        Assertions.assertEquals(4, sortedArray.size)
        Assertions.assertEquals("array", sortedArray[0])
        Assertions.assertEquals("hello", sortedArray[1])
        Assertions.assertEquals("iterator", sortedArray[2])
        Assertions.assertEquals("world", sortedArray[3])
    }
}