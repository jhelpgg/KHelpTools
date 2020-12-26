package khelp.utilities.extensions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ArrayExtensionsTests
{
    @Test
    fun mergeTest()
    {
        val array1 = arrayOf("hello", "world", "something", "other")
        val array2 = arrayOf("welcome", "to", "the", "world")
        val merge = array1.merge(array2)
        Assertions.assertEquals(7, merge.size)
        Assertions.assertTrue("hello" in merge)
        Assertions.assertTrue("world" in merge)
        Assertions.assertTrue("something" in merge)
        Assertions.assertTrue("other" in merge)
        Assertions.assertTrue("welcome" in merge)
        Assertions.assertTrue("to" in merge)
        Assertions.assertTrue("the" in merge)
    }

    @Test
    fun stringTest()
    {
        val array = arrayOf("welcome", "to", "the", "world")
        Assertions.assertEquals(">{welcome | to | the | world}<",
                                array.string(">{", " | ", "}<"))
    }

    @Test
    fun sameTest()
    {
        val array = arrayOf("welcome", "to", "the", "world")
        var compare = arrayOf("welcome", "to", "the", "world")
        Assertions.assertTrue(array.same(compare))
        compare = arrayOf("welcome", "to", "world")
        Assertions.assertFalse(array.same(compare))
        compare = arrayOf("welcome", "to", "the", "jungle")
        Assertions.assertFalse(array.same(compare))
    }

    @Test
    fun transformArrayTest()
    {
        val array = arrayOf("42", "73", "666")
        val result = array.transformArray { element -> "Number_$element" }
        Assertions.assertEquals(3, result.size)
        Assertions.assertEquals("Number_42", result[0])
        Assertions.assertEquals("Number_73", result[1])
        Assertions.assertEquals("Number_666", result[2])
    }

    @Test
    fun transformIntTest()
    {
        val array = arrayOf("42", "73", "666")
        val result = array.transformInt { element -> element.toInt() }
        Assertions.assertEquals(3, result.size)
        Assertions.assertEquals(42, result[0])
        Assertions.assertEquals(73, result[1])
        Assertions.assertEquals(666, result[2])
    }

    @Test
    fun transformLongTest()
    {
        val array = arrayOf("42", "73", "666")
        val result = array.transformLong { element -> element.toLong() }
        Assertions.assertEquals(3, result.size)
        Assertions.assertEquals(42L, result[0])
        Assertions.assertEquals(73L, result[1])
        Assertions.assertEquals(666L, result[2])
    }
}