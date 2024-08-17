package khelp.utilities.collections

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ArrayIntTests
{
    @Test
    fun sortedTests()
    {
        val array = ArrayInt(1, 2, 3, 4, 5, 6, 7, 8, 9, 42, 73)
        Assertions.assertEquals(SortedStatus.SORTED, array.sorted, array.toString())
        array.insert(45, 10)
        Assertions.assertEquals(SortedStatus.SORTED, array.sorted, array.toString())
        array[10] = 85
        Assertions.assertEquals(SortedStatus.NOT_SORTED, array.sorted, array.toString())
        array[10] = 45
        Assertions.assertEquals(SortedStatus.UNKNOWN, array.sorted, array.toString())
        array[10] = 44
        Assertions.assertEquals(SortedStatus.UNKNOWN, array.sorted, array.toString())
        Assertions.assertEquals(SortedStatus.SORTED, array.sorted(), array.toString())
        Assertions.assertEquals(SortedStatus.SORTED, array.sorted, array.toString())
        array[10] = 85
        Assertions.assertEquals(SortedStatus.NOT_SORTED, array.sorted, array.toString())
        array[10] = 666
        Assertions.assertEquals(SortedStatus.UNKNOWN, array.sorted, array.toString())
        Assertions.assertEquals(SortedStatus.NOT_SORTED, array.sorted(), array.toString())
        Assertions.assertEquals(SortedStatus.NOT_SORTED, array.sorted, array.toString())
        array.sort()
        Assertions.assertEquals(SortedStatus.SORTED, array.sorted, array.toString())
    }
}