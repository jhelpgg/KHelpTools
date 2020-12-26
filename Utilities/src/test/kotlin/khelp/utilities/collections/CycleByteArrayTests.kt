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
}