package khelp.utilities.collections

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class SortedArrayTests
{
    @Test
    fun naturalOrderNotUnique()
    {
        val sortedArray = SortedArray<String>()
        Assertions.assertTrue(sortedArray.empty)
        Assertions.assertFalse(sortedArray.notEmpty)
        sortedArray += "hello"
        sortedArray += "world"
        sortedArray += "hello"
        sortedArray += "array"
        sortedArray += "iterator"
        Assertions.assertFalse(sortedArray.empty)
        Assertions.assertTrue(sortedArray.notEmpty)
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
        val sortedArray = SortedArray<String>(true)
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
        val sortedArray = SortedArray(CaseNotSensitiveOrder, true)
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

    @Test
    fun indexOfTest()
    {
        val sortedArray = SortedArray<String>(true)
        Assertions.assertTrue(sortedArray.empty)
        Assertions.assertFalse(sortedArray.notEmpty)
        sortedArray += "hello"
        sortedArray += "world"
        sortedArray += "hello"
        sortedArray += "array"
        sortedArray += "iterator"
        Assertions.assertFalse(sortedArray.empty)
        Assertions.assertTrue(sortedArray.notEmpty)
        Assertions.assertEquals(0, sortedArray.indexOf("array"))
        Assertions.assertEquals(1, sortedArray.indexOf("hello"))
        Assertions.assertEquals(2, sortedArray.indexOf("iterator"))
        Assertions.assertEquals(3, sortedArray.indexOf("world"))
        Assertions.assertTrue(sortedArray.indexOf("something") < 0)
    }

    @Test
    fun containsTest()
    {
        val sortedArray = SortedArray<String>(true)
        Assertions.assertTrue(sortedArray.empty)
        Assertions.assertFalse(sortedArray.notEmpty)
        sortedArray += "hello"
        sortedArray += "world"
        sortedArray += "hello"
        sortedArray += "array"
        sortedArray += "iterator"
        Assertions.assertFalse(sortedArray.empty)
        Assertions.assertTrue(sortedArray.notEmpty)
        Assertions.assertTrue("hello" in sortedArray)
        Assertions.assertFalse("other" in sortedArray)
    }

    @Test
    fun toStringTest()
    {
        val sortedArray = SortedArray<String>(true)
        Assertions.assertTrue(sortedArray.empty)
        Assertions.assertFalse(sortedArray.notEmpty)
        sortedArray += "the"
        sortedArray += "hello"
        sortedArray += "world"
        sortedArray += "hello"
        Assertions.assertEquals("[hello, the, world]", sortedArray.toString())
    }


    @Test
    fun plusAssignTest()
    {
        val sortedArray = SortedArray<String>(true)
        Assertions.assertTrue(sortedArray.empty)
        Assertions.assertFalse(sortedArray.notEmpty)
        sortedArray += "hello"
        sortedArray += "world"
        sortedArray += "hello"
        sortedArray += "array"
        sortedArray += "iterator"
        Assertions.assertTrue("iterator" in sortedArray)
        Assertions.assertFalse("something" in sortedArray)

        sortedArray += listOf("other", "something")
        Assertions.assertTrue("something" in sortedArray)
    }

    @Test
    fun minusAssignTest()
    {
        val sortedArray = SortedArray<String>(true)
        Assertions.assertTrue(sortedArray.empty)
        Assertions.assertFalse(sortedArray.notEmpty)
        sortedArray += "hello"
        sortedArray += "world"
        sortedArray += "hello"
        sortedArray += "array"
        sortedArray += "iterator"
        Assertions.assertTrue("iterator" in sortedArray)
        sortedArray -= "iterator"
        Assertions.assertFalse("iterator" in sortedArray)

        sortedArray -= listOf("array", "hello")
        Assertions.assertFalse("array" in sortedArray)
    }

    @Test
    fun remAssignTest()
    {
        val sortedArray = SortedArray<String>(true)
        sortedArray += "hello"
        sortedArray += "world"
        sortedArray += "array"
        sortedArray += "iterator"

        sortedArray %= listOf("hello", "world")
        Assertions.assertEquals(2, sortedArray.size)
        Assertions.assertEquals("hello", sortedArray[0])
        Assertions.assertEquals("world", sortedArray[1])
    }

    @Test
    fun removeIfTest()
    {
        val sortedArray = SortedArray<Int>(true)
        sortedArray += 42
        sortedArray += 73
        sortedArray += 666
        sortedArray += 7777777
        sortedArray.removeIf { value -> value > 100 }
        Assertions.assertEquals(2, sortedArray.size)
        Assertions.assertEquals(42, sortedArray[0])
        Assertions.assertEquals(73, sortedArray[1])
    }

    @Test
    fun clearTest()
    {
        val sortedArray = SortedArray<String>(true)
        sortedArray += "hello"
        sortedArray += "world"
        sortedArray += "array"
        sortedArray += "iterator"
        sortedArray.clear()
        Assertions.assertTrue(sortedArray.empty)
        Assertions.assertTrue(sortedArray.isEmpty())
    }

    @Test
    fun containsAllTest()
    {
        val sortedArray = SortedArray<String>(true)
        sortedArray += "hello"
        sortedArray += "world"
        sortedArray += "array"
        sortedArray += "iterator"
        Assertions.assertTrue(sortedArray.containsAll(listOf("hello", "world")))
        Assertions.assertFalse(sortedArray.containsAll(listOf("hello", "other", "world")))
    }

    @Test
    fun subListTest()
    {
        val sortedArray = SortedArray<String>(true)
        sortedArray += "array"
        sortedArray += "hello"
        sortedArray += "iterator"
        sortedArray += "world"
        val subList = sortedArray.subList(1, 3)
        Assertions.assertEquals(2, subList.size)
        Assertions.assertTrue(subList.unique)
        Assertions.assertEquals("hello", subList[0])
        Assertions.assertEquals("iterator", subList[1])
    }

    @Test
    fun immutableListTest()
    {
        val sortedArray = SortedArray<String>(true)
        sortedArray += "array"
        sortedArray += "hello"
        sortedArray += "iterator"
        sortedArray += "world"
        val immutableList = sortedArray.immutableList()
        Assertions.assertEquals(4, immutableList.size)
        Assertions.assertEquals("array", immutableList[0])
        Assertions.assertEquals("hello", immutableList[1])
        Assertions.assertEquals("iterator", immutableList[2])
        Assertions.assertEquals("world", immutableList[3])
    }
}