package khelp.io.files.flat

import khelp.utilities.serailzation.Parser

internal class FlatKeyValueParser(private val flatKeyValueFile: FlatKeyValueFile) : Parser
{
    override fun getBoolean(key: String): Boolean =
        this.flatKeyValueFile[key].equals("TRUE", true)

    override fun getChar(key: String): Char
    {
        val value = this.flatKeyValueFile[key] ?: return ' '

        if (value.isEmpty())
        {
            return ' '
        }

        return value[0]
    }

    override fun getByte(key: String): Byte =
        this.getDouble(key)
            .toInt()
            .toByte()

    override fun getShort(key: String): Short =
        this.getDouble(key)
            .toInt()
            .toShort()

    override fun getInt(key: String): Int =
        this.getDouble(key)
            .toInt()

    override fun getLong(key: String): Long =
        this.getDouble(key)
            .toLong()

    override fun getFloat(key: String): Float =
        this.getDouble(key)
            .toFloat()

    override fun getDouble(key: String): Double
    {
        val value = this.flatKeyValueFile[key] ?: return 0.0

        return try
        {
            value.toDouble()
        }
        catch (_: Exception)
        {
            0.0
        }
    }

    override fun getString(key: String): String =
        this.flatKeyValueFile[key]
            ?.replace(LINE_RETURN_REPLACER, "\n")
            ?.replace(LINE_FEED_REPLACER, "\r")
        ?: ""
}