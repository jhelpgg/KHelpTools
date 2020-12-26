package khelp.utilities.collections

import khelp.utilities.extensions.select
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class IterableIteratorSelectedTests
{
    @Test
    fun iterableSelectedTest()
    {
        val iterable = listOf(42, 73, 666, 7777777).select { value -> value % 2 == 0 }
        val iterator = iterable.iterator()
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(42, iterator.next())
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(666, iterator.next())
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