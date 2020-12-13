package khelp.utilities.extensions

fun StringBuilder.appendHexadecimal(byte: Byte)
{
    val value = byte.toInt() and 0xFF
    this.append(Integer.toHexString((value shr 4) and 0xF))
    this.append(Integer.toHexString(value and 0xF))
}

fun StringBuilder.appendMinimumSize(minimumSize: Int, value: Int) =
    this.append(String.format("%0${minimumSize}d", value))