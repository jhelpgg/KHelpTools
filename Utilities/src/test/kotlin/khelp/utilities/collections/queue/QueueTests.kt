package khelp.utilities.collections.queue

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class QueueTests
{
    @Test
    fun emptyNotEmptyTest()
    {
        val queue = Queue<String>()
        Assertions.assertTrue(queue.empty)
        Assertions.assertTrue(queue.isEmpty())
        Assertions.assertFalse(queue.notEmpty)
        queue.inQueue("Something")
        Assertions.assertFalse(queue.empty)
        Assertions.assertFalse(queue.isEmpty())
        Assertions.assertTrue(queue.notEmpty)
    }

    @Test
    fun inQueueTest()
    {
        val queue = Queue<String>()
        Assertions.assertTrue(queue.empty)
        Assertions.assertFalse(queue.notEmpty)
        Assertions.assertEquals(0, queue.size)
        queue.inQueue("Something")
        Assertions.assertFalse(queue.empty)
        Assertions.assertTrue(queue.notEmpty)
        Assertions.assertEquals(1, queue.size)
        queue.inQueue("Other Thing")
        Assertions.assertEquals(2, queue.size)

        Assertions.assertEquals("Something", queue.outQueue())
        Assertions.assertFalse(queue.empty)
        Assertions.assertTrue(queue.notEmpty)
        Assertions.assertEquals(1, queue.size)
        Assertions.assertEquals("Other Thing", queue.outQueue())
        Assertions.assertTrue(queue.empty)
        Assertions.assertFalse(queue.notEmpty)
        Assertions.assertEquals(0, queue.size)
    }

    @Test
    fun outQueueTest()
    {
        val queue = Queue<String>()
        Assertions.assertTrue(queue.empty)
        Assertions.assertFalse(queue.notEmpty)
        Assertions.assertEquals(0, queue.size)

        try
        {
            queue.outQueue()
            Assertions.fail("Must throw IllegalStateException since queue is empty")
        }
        catch (exception: IllegalStateException)
        {
            // That's what we expected
        }

        queue.ahead("Something")
        Assertions.assertFalse(queue.empty)
        Assertions.assertTrue(queue.notEmpty)
        Assertions.assertEquals(1, queue.size)
        queue.ahead("Other Thing")
        Assertions.assertEquals(2, queue.size)

        Assertions.assertEquals("Other Thing", queue.outQueue())
        Assertions.assertFalse(queue.empty)
        Assertions.assertTrue(queue.notEmpty)
        Assertions.assertEquals(1, queue.size)
        Assertions.assertEquals("Something", queue.outQueue())
        Assertions.assertTrue(queue.empty)
        Assertions.assertFalse(queue.notEmpty)
        Assertions.assertEquals(0, queue.size)

        try
        {
            queue.outQueue()
            Assertions.fail("Must throw IllegalStateException since queue is empty")
        }
        catch (exception: IllegalStateException)
        {
            // That's what we expected
        }
    }

    @Test
    fun aheadTest()
    {
        val queue = Queue<String>()
        Assertions.assertTrue(queue.empty)
        Assertions.assertFalse(queue.notEmpty)
        Assertions.assertEquals(0, queue.size)
        queue.ahead("Something")
        Assertions.assertFalse(queue.empty)
        Assertions.assertTrue(queue.notEmpty)
        Assertions.assertEquals(1, queue.size)
        queue.ahead("Other Thing")
        Assertions.assertEquals(2, queue.size)

        Assertions.assertEquals("Other Thing", queue.outQueue())
        Assertions.assertFalse(queue.empty)
        Assertions.assertTrue(queue.notEmpty)
        Assertions.assertEquals(1, queue.size)
        Assertions.assertEquals("Something", queue.outQueue())
        Assertions.assertTrue(queue.empty)
        Assertions.assertFalse(queue.notEmpty)
        Assertions.assertEquals(0, queue.size)
    }

    @Test
    fun peekTest()
    {
        val queue = Queue<String>()
        Assertions.assertTrue(queue.empty)
        Assertions.assertFalse(queue.notEmpty)
        Assertions.assertEquals(0, queue.size)

        try
        {
            queue.peek()
            Assertions.fail("Must throw IllegalStateException since queue is empty")
        }
        catch (exception: IllegalStateException)
        {
            // That's what we expected
        }

        queue.inQueue("Something")
        Assertions.assertFalse(queue.empty)
        Assertions.assertTrue(queue.notEmpty)
        Assertions.assertEquals(1, queue.size)
        Assertions.assertEquals("Something", queue.peek())
        queue.inQueue("Other Thing")
        Assertions.assertEquals(2, queue.size)
        Assertions.assertEquals("Something", queue.peek())
        queue.ahead("Ahead")
        Assertions.assertEquals(3, queue.size)
        Assertions.assertEquals("Ahead", queue.peek())
    }

    @Test
    fun forEachTest()
    {
        val queue = Queue<Int>()
        queue.inQueue(42)
        queue.inQueue(73)
        queue.inQueue(666)
        val collector = ArrayList<Int>()
        queue.forEach { value -> collector.add(value) }
        Assertions.assertEquals(3, collector.size)
        Assertions.assertEquals(42, collector[0])
        Assertions.assertEquals(73, collector[1])
        Assertions.assertEquals(666, collector[2])
        Assertions.assertEquals(3, queue.size)
    }

    @Test
    fun removeIfTest()
    {
        val queue = Queue<Int>()
        queue.inQueue(42)
        queue.inQueue(73)
        queue.inQueue(666)
        queue.inQueue(77777777)
        Assertions.assertEquals(4, queue.size)
        queue.removeIf { value -> value % 2 == 1 }
        Assertions.assertEquals(2, queue.size)
        Assertions.assertEquals(42, queue.outQueue())
        Assertions.assertEquals(666, queue.outQueue())

        queue.inQueue(42)
        queue.inQueue(73)
        queue.inQueue(666)
        queue.inQueue(77777777)
        Assertions.assertEquals(4, queue.size)
        queue.removeIf { value -> value % 2 == 0 }
        Assertions.assertEquals(2, queue.size)
        Assertions.assertEquals(73, queue.outQueue())
        Assertions.assertEquals(77777777, queue.outQueue())
    }

    @Test
    fun clearTest()
    {
        val queue = Queue<Int>()
        queue.inQueue(42)
        queue.inQueue(73)
        queue.inQueue(666)
        queue.inQueue(77777777)
        Assertions.assertEquals(4, queue.size)
        queue.clear()
        Assertions.assertTrue(queue.empty)
    }

    @Test
    fun iteratorTest()
    {
        val queue = Queue<Int>()
        queue.inQueue(42)
        queue.inQueue(73)
        queue.inQueue(666)
        queue.inQueue(77777777)
        val iterator = queue.iterator()
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(42, iterator.next())
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(73, iterator.next())
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(666, iterator.next())
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(77777777, iterator.next())
        Assertions.assertFalse(iterator.hasNext())
        Assertions.assertEquals(4, queue.size)
    }

    @Test
    fun containsTest()
    {
        val queue = Queue<Int>()
        queue.inQueue(42)
        queue.inQueue(73)
        queue.inQueue(666)
        queue.inQueue(77777777)
        Assertions.assertTrue(73 in queue)
        Assertions.assertFalse(58 in queue)
        Assertions.assertEquals(4, queue.size)
    }

    @Test
    fun containsAllTest()
    {
        val queue = Queue<Int>()
        queue.inQueue(42)
        queue.inQueue(73)
        queue.inQueue(666)
        queue.inQueue(77777777)
        Assertions.assertTrue(queue.containsAll(listOf(73, 42)))
        Assertions.assertFalse(queue.containsAll(listOf(73, 85, 42)))
    }

    @Test
    fun toStringTest()
    {
        val queue = Queue<Int>()
        Assertions.assertEquals("[]", queue.toString())
        queue.inQueue(42)
        Assertions.assertEquals("[42]", queue.toString())
        queue.inQueue(73)
        Assertions.assertEquals("[42 ; 73]", queue.toString())
        queue.inQueue(666)
        Assertions.assertEquals("[42 ; 73 ; 666]", queue.toString())
    }
}