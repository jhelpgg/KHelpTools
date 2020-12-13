package khelp.utilities.collections

import org.junit.Assert
import org.junit.Test

class CycleByteArrayTests
{
    @Test
    fun write4read2write5read7()
    {
        val cycleByteArray = CycleByteArray(8)
        Assert.assertTrue(cycleByteArray.empty)
        Assert.assertEquals("[]", cycleByteArray.toString())
        cycleByteArray.write(1)
        cycleByteArray.write(2)
        cycleByteArray.write(3)
        cycleByteArray.write(4)
        Assert.assertEquals(4, cycleByteArray.size)
        Assert.assertEquals("[01 ; 02 ; 03 ; 04]", cycleByteArray.toString())
        var byte = cycleByteArray.read()
        Assert.assertEquals("[02 ; 03 ; 04]", cycleByteArray.toString())
        Assert.assertEquals(1.toByte(), byte)
        byte = cycleByteArray.read()
        Assert.assertEquals("[03 ; 04]", cycleByteArray.toString())
        Assert.assertEquals(2.toByte(), byte)
        cycleByteArray.write(byteArrayOf(5, 6, 7, 8, 9))
        Assert.assertEquals("[03 ; 04 ; 05 ; 06 ; 07 ; 08 ; 09]", cycleByteArray.toString())
        val buffer = ByteArray(7)
        val read = cycleByteArray.read(buffer)
        Assert.assertEquals(7, read)
        Assert.assertEquals("[]", cycleByteArray.toString())
        Assert.assertEquals(3.toByte(), buffer[0])
        Assert.assertEquals(4.toByte(), buffer[1])
        Assert.assertEquals(5.toByte(), buffer[2])
        Assert.assertEquals(6.toByte(), buffer[3])
        Assert.assertEquals(7.toByte(), buffer[4])
        Assert.assertEquals(8.toByte(), buffer[5])
        Assert.assertEquals(9.toByte(), buffer[6])
    }
}