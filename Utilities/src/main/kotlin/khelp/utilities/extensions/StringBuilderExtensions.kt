package khelp.utilities.extensions

/**
 * Append a byte on its hexadecimal version
 */
fun StringBuilder.appendHexadecimal(byte: Byte): StringBuilder
{
    val value = byte.toInt() and 0xFF
    this.append(Integer.toHexString((value shr 4) and 0xF))
    return this.append(Integer.toHexString(value and 0xF))
}

/**
 * Append an integer with a given minimum digit.
 *
 * If the number of digit of the integer is the given size or more, it print the integer as is.
 *
 * But if the number of digit of the integer is less the given number, some 0 are added to fit the size
 */
fun StringBuilder.appendMinimumDigit(minimumDigit: Int, value: Int): StringBuilder =
    if (value < 0)
    {
        this.append(String.format("%0${minimumDigit + 1}d", value))
    }
    else
    {
        this.append(String.format("%0${minimumDigit}d", value))
    }