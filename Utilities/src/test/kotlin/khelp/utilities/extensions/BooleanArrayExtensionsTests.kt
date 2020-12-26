package khelp.utilities.extensions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BooleanArrayExtensionsTests
{

    @Test
    fun stringTest()
    {
        val array = booleanArrayOf(true, false, false, true, true)
        Assertions.assertEquals(">{true | false | false | true | true}<",
                                array.string(">{", " | ", "}<"))
    }

    @Test
    fun sameTest()
    {
        val array = booleanArrayOf(true, false, false, true, true)
        var compare = booleanArrayOf(true, false, false, true, true)
        Assertions.assertTrue(array.same(compare))
        compare = booleanArrayOf(true, false, false, true)
        Assertions.assertFalse(array.same(compare))
        compare = booleanArrayOf(true, false, false, false, true)
        Assertions.assertFalse(array.same(compare))
    }
}