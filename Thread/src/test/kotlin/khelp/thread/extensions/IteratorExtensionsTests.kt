package khelp.thread.extensions

import java.util.TreeSet
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class IteratorExtensionsTests
{
    @Test
    fun forEachParallelTest()
    {
        val list = listOf("42", "73", "666", "7777777")
        val collector = TreeSet<Int>()
        val future = list.iterator()
            .forEachParallel { string ->
                synchronized(collector)
                {
                    collector.add(string.toInt())
                }
            }
        future.waitCompletion()
        Assertions.assertEquals(4, collector.size)
        Assertions.assertTrue(42 in collector, "42 must be collected")
        Assertions.assertTrue(73 in collector, "73 must be collected")
        Assertions.assertTrue(666 in collector, "666 must be collected")
        Assertions.assertTrue(7777777 in collector, "7777777 must be collected")
    }
}