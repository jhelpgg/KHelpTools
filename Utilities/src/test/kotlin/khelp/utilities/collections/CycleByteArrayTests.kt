package khelp.utilities.collections

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CycleByteArrayTests
{
    @Test
    fun write4read2write5read7()
    {
        val cycleByteArray = CycleByteArray(8)
        Assertions.assertTrue(cycleByteArray.empty)
        Assertions.assertFalse(cycleByteArray.notEmpty)
        Assertions.assertEquals("[]", cycleByteArray.toString())
        cycleByteArray.write(1)
        cycleByteArray.write(2)
        cycleByteArray.write(3)
        cycleByteArray.write(4)
        Assertions.assertEquals(4, cycleByteArray.size)
        Assertions.assertEquals("[01 ; 02 ; 03 ; 04]", cycleByteArray.toString())
        var byte = cycleByteArray.read()
        Assertions.assertEquals("[02 ; 03 ; 04]", cycleByteArray.toString())
        Assertions.assertEquals(1.toByte(), byte)
        byte = cycleByteArray.read()
        Assertions.assertEquals("[03 ; 04]", cycleByteArray.toString())
        Assertions.assertEquals(2.toByte(), byte)
        cycleByteArray.write(byteArrayOf(5, 6, 7, 8, 9))
        Assertions.assertEquals("[03 ; 04 ; 05 ; 06 ; 07 ; 08 ; 09]", cycleByteArray.toString())
        val buffer = ByteArray(7)
        val read = cycleByteArray.read(buffer)
        Assertions.assertEquals(7, read)
        Assertions.assertEquals("[]", cycleByteArray.toString())
        Assertions.assertEquals(3.toByte(), buffer[0])
        Assertions.assertEquals(4.toByte(), buffer[1])
        Assertions.assertEquals(5.toByte(), buffer[2])
        Assertions.assertEquals(6.toByte(), buffer[3])
        Assertions.assertEquals(7.toByte(), buffer[4])
        Assertions.assertEquals(8.toByte(), buffer[5])
        Assertions.assertEquals(9.toByte(), buffer[6])
    }

    @Test
    fun outOffBoundsArgumentsInWrite()
    {
        val cycleByteArray = CycleByteArray(8)
        val data = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        cycleByteArray.write(data, -1, 5)
        Assertions.assertEquals(4, cycleByteArray.size)
        Assertions.assertEquals(0, cycleByteArray.read())
        Assertions.assertEquals(1, cycleByteArray.read())
        Assertions.assertEquals(2, cycleByteArray.read())
        Assertions.assertEquals(3, cycleByteArray.read())
        cycleByteArray.write(data, 2, -5)
        Assertions.assertTrue(cycleByteArray.empty)
        Assertions.assertFalse(cycleByteArray.notEmpty)
        cycleByteArray.write(data)
        cycleByteArray.write(data)
        cycleByteArray.write(data)
        Assertions.assertFalse(cycleByteArray.empty)
        Assertions.assertTrue(cycleByteArray.notEmpty)
    }

    @Test
    fun outOffBoundsArgumentsInRead()
    {
        val cycleByteArray = CycleByteArray(8)
        cycleByteArray.write(byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9))
        val data = ByteArray(10)
        var read = cycleByteArray.read(data, -1, 5)
        Assertions.assertEquals(4, read)
        Assertions.assertEquals(0, data[0])
        Assertions.assertEquals(1, data[1])
        Assertions.assertEquals(2, data[2])
        Assertions.assertEquals(3, data[3])
        read = cycleByteArray.read(data, 3, -5)
        Assertions.assertEquals(0, read)
    }

    @Test
    fun debugStringTest()
    {
        val cycleByteArray = CycleByteArray(8)
        Assertions.assertEquals("R:W:0:00 1:00 2:00 3:00 4:00 5:00 6:00 7:00 ", cycleByteArray.debugString())
        cycleByteArray.write(byteArrayOf(1, 2, 3, 4, 5, 6))
        Assertions.assertEquals("R:0:01 1:02 2:03 3:04 4:05 5:06 W:6:00 7:00 ", cycleByteArray.debugString())
        cycleByteArray.read(ByteArray(3))
        Assertions.assertEquals("0:01 1:02 2:03 R:3:04 4:05 5:06 W:6:00 7:00 ", cycleByteArray.debugString())
        cycleByteArray.write(byteArrayOf(0x11, 0x22, 0x33, 0x44))
        Assertions.assertEquals("0:33 1:44 W:2:03 R:3:04 4:05 5:06 6:11 7:22 ", cycleByteArray.debugString())
    }
}