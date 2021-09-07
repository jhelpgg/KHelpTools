package khelp.utilities.collections

import khelp.utilities.log.verbose
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class IntMapTests
{
    @Test
    fun usage()
    {
        val intMap = IntMap<String>()
        verbose("Creation : ", intMap.toString())

        Assertions.assertEquals(0, intMap.size)
        Assertions.assertTrue(intMap.empty)
        Assertions.assertFalse(intMap.notEmpty)
        Assertions.assertNull(intMap[45])

        intMap[45] = "tours"
        intMap[42] = "answer"
        intMap[73] = "magic"
        verbose("First elements : ", intMap.toString())

        Assertions.assertEquals(3, intMap.size)
        Assertions.assertFalse(intMap.empty)
        Assertions.assertTrue(intMap.notEmpty)
        Assertions.assertEquals("tours", intMap[45])
        Assertions.assertEquals("answer", intMap[42])
        Assertions.assertEquals("magic", intMap[73])
        Assertions.assertNull(intMap[666])


        val keys = ArrayList<Int>()
        val values = ArrayList<String>()

        for ((key, value) in intMap)
        {
            keys += key
            values += value
        }

        Assertions.assertEquals(3, keys.size)
        Assertions.assertEquals(3, values.size)
        Assertions.assertEquals(42, keys[0])
        Assertions.assertEquals("answer", values[0])
        Assertions.assertEquals(45, keys[1])
        Assertions.assertEquals("tours", values[1])
        Assertions.assertEquals(73, keys[2])
        Assertions.assertEquals("magic", values[2])

        keys.clear()
        values.clear()

        for (key in intMap.keys())
        {
            keys += key
        }

        for (value in intMap.values())
        {
            values += value
        }

        Assertions.assertEquals(3, keys.size)
        Assertions.assertEquals(3, values.size)
        Assertions.assertEquals(42, keys[0])
        Assertions.assertEquals("answer", values[0])
        Assertions.assertEquals(45, keys[1])
        Assertions.assertEquals("tours", values[1])
        Assertions.assertEquals(73, keys[2])
        Assertions.assertEquals("magic", values[2])

        intMap[45] = "replacement"
        verbose("replacement : ", intMap)
        Assertions.assertEquals(3, intMap.size)
        Assertions.assertFalse(intMap.empty)
        Assertions.assertTrue(intMap.notEmpty)
        Assertions.assertEquals("replacement", intMap[45])
        Assertions.assertEquals("answer", intMap[42])
        Assertions.assertEquals("magic", intMap[73])
        Assertions.assertNull(intMap[666])

        intMap.remove(7777777)
        verbose(intMap)
        verbose("Remove outside : ", intMap)
        Assertions.assertEquals(3, intMap.size)
        Assertions.assertFalse(intMap.empty)
        Assertions.assertTrue(intMap.notEmpty)
        Assertions.assertEquals("replacement", intMap[45])
        Assertions.assertEquals("answer", intMap[42])
        Assertions.assertEquals("magic", intMap[73])
        Assertions.assertNull(intMap[666])

        intMap.remove(45)
        verbose("Remove 45 : ", intMap)
        Assertions.assertEquals(2, intMap.size)
        Assertions.assertFalse(intMap.empty)
        Assertions.assertTrue(intMap.notEmpty)
        Assertions.assertNull(intMap[45])
        Assertions.assertEquals("answer", intMap[42])
        Assertions.assertEquals("magic", intMap[73])
        Assertions.assertNull(intMap[666])

        intMap.clear()
        verbose("Clear : ", intMap)
        Assertions.assertEquals(0, intMap.size)
        Assertions.assertTrue(intMap.empty)
        Assertions.assertFalse(intMap.notEmpty)
        Assertions.assertNull(intMap[45])
    }
}
