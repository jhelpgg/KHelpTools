package khelp.utilities.extensions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class StringBuilderExtensionsTests
{
    @Test
    fun appendHexadecimalTest()
    {
        val stringBuilder = StringBuilder()
        stringBuilder.append("Yop ")
        stringBuilder.appendHexadecimal(0x01.toByte())
        stringBuilder.append(", ")
        stringBuilder.appendHexadecimal(0x7F.toByte())
        stringBuilder.append(", ")
        stringBuilder.appendHexadecimal(0x98.toByte())
        stringBuilder.append(", ")
        stringBuilder.appendHexadecimal(0xDE.toByte())
        Assertions.assertEquals("Yop 01, 7f, 98, de", stringBuilder.toString())
    }

    @Test
    fun appendMinimumDigitTest()
    {
        val stringBuilder = StringBuilder()
        stringBuilder.appendMinimumDigit(5, 5)
        stringBuilder.append(", ")
        stringBuilder.appendMinimumDigit(3, 1234)
        stringBuilder.append(", ")
        stringBuilder.appendMinimumDigit(3, -6)
        stringBuilder.append(", ")
        stringBuilder.appendMinimumDigit(9, 123456)
        stringBuilder.append(", ")
        stringBuilder.appendMinimumDigit(12, 987654321)
        Assertions.assertEquals("00005, 1234, -006, 000123456, 000987654321", stringBuilder.toString())
    }
}