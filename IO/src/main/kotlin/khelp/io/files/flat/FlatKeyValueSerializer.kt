package khelp.io.files.flat

import khelp.utilities.serailzation.Serializer

internal class FlatKeyValueSerializer(private val flatKeyValueFile: FlatKeyValueFile) : Serializer
{
    override fun setBoolean(key: String, value: Boolean)
    {
        this.flatKeyValueFile[key] = value.toString()
    }

    override fun setChar(key: String, value: Char)
    {
        this.flatKeyValueFile[key] = value.toString()
    }

    override fun setByte(key: String, value: Byte)
    {
        this.flatKeyValueFile[key] = value.toString()
    }

    override fun setShort(key: String, value: Short)
    {
        this.flatKeyValueFile[key] = value.toString()
    }

    override fun setInt(key: String, value: Int)
    {
        this.flatKeyValueFile[key] = value.toString()
    }

    override fun setLong(key: String, value: Long)
    {
        this.flatKeyValueFile[key] = value.toString()
    }

    override fun setFloat(key: String, value: Float)
    {
        this.flatKeyValueFile[key] = value.toString()
    }

    override fun setDouble(key: String, value: Double)
    {
        this.flatKeyValueFile[key] = value.toString()
    }

    override fun setString(key: String, value: String)
    {
        this.flatKeyValueFile[key] =
            value.replace("\n", LINE_RETURN_REPLACER)
                .replace("\r", LINE_FEED_REPLACER)
    }
}