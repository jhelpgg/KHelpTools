package khelp.utilities.extensions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ByteArrayExtensionsTests
{
    @Test
    fun utf8Test()
    {
        val data = "Hello world UTF8".utf8
        Assertions.assertEquals("Hello world UTF8", data.utf8)
    }

    @Test
    fun base64Test()
    {
        val data = byteArrayOf(0.toByte(),42.toByte(),73.toByte()).base64
        Assertions.assertEquals("ACpJ", data)
    }

    @Test
    fun stringTest()
    {
        val array = byteArrayOf((0x2A).toByte(), (0xB7).toByte(), (0x7B).toByte())
        Assertions.assertEquals(">{2a | b7 | 7b}<",
                                array.string(">{", " | ", "}<"))
    }

    @Test
    fun sameTest()
    {
        val array = byteArrayOf(42.toByte(), (-73).toByte(), 123.toByte())
        var compare = byteArrayOf(42.toByte(), (-73).toByte(), 123.toByte())
        Assertions.assertTrue(array.same(compare))
        compare = byteArrayOf(42.toByte(), (-73).toByte(), 123.toByte(), (-12).toByte())
        Assertions.assertFalse(array.same(compare))
        compare = byteArrayOf(42.toByte(), 73.toByte(), 123.toByte())
        Assertions.assertFalse(array.same(compare))
    }

    @Test
    fun parseToIntArrayTest()
    {
        val int1 = 42
        val int2 = -666
        val int3 = 73
        val int4 = -7777777
        val data = byteArrayOf(
            ((int1 shr 24) and 0xFF).toByte(),
            ((int1 shr 16) and 0xFF).toByte(),
            ((int1 shr 8) and 0xFF).toByte(),
            (int1 and 0xFF).toByte(),
            ((int2 shr 24) and 0xFF).toByte(),
            ((int2 shr 16) and 0xFF).toByte(),
            ((int2 shr 8) and 0xFF).toByte(),
            (int2 and 0xFF).toByte(),
            ((int3 shr 24) and 0xFF).toByte(),
            ((int3 shr 16) and 0xFF).toByte(),
            ((int3 shr 8) and 0xFF).toByte(),
            (int3 and 0xFF).toByte(),
            ((int4 shr 24) and 0xFF).toByte(),
            ((int4 shr 16) and 0xFF).toByte(),
            ((int4 shr 8) and 0xFF).toByte(),
            (int4 and 0xFF).toByte(),
        )
        val array = data.parseToIntArray()
        Assertions.assertEquals(4, array.size)
        Assertions.assertEquals(int1, array[0])
        Assertions.assertEquals(int2, array[1])
        Assertions.assertEquals(int3, array[2])
        Assertions.assertEquals(int4, array[3])
    }
}