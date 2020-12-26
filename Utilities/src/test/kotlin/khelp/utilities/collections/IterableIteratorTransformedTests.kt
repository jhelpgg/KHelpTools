package khelp.utilities.collections

import khelp.utilities.extensions.transform
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class IterableIteratorTransformedTests
{
    @Test
    fun iterableIteratorTransformedTest()
    {
        val iterable = listOf(41, 72, 665, 7777776).transform { value -> value + 1 }
        val iterator = iterable.iterator()
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(42, iterator.next())
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(73, iterator.next())
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(666, iterator.next())
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(7777777, iterator.next())
        Assertions.assertFalse(iterator.hasNext())

        try
        {
            iterator.next()
            Assertions.fail("Should throw NoSuchElementException since no more elements")
        }
        catch (exception: NoSuchElementException)
        {
            // That's what expected
        }

    }
}