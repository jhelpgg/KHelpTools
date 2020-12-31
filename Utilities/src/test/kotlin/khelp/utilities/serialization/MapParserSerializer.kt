package khelp.utilities.serialization

class MapParserSerializer : Parser, Serializer
{
    private val map = HashMap<String, Any>()

    override fun getBoolean(key: String): Boolean =
        this.map[key] as? Boolean ?: false

    override fun getChar(key: String): Char =
        this.map[key] as? Char ?: ' '

    override fun getByte(key: String): Byte =
        this.map[key] as? Byte ?: 0.toByte()

    override fun getShort(key: String): Short =
        this.map[key] as? Short ?: 0.toShort()

    override fun getInt(key: String): Int =
        this.map[key] as? Int ?: 0

    override fun getLong(key: String): Long =
        this.map[key] as? Long ?: 0L

    override fun getFloat(key: String): Float =
        this.map[key] as? Float ?: 0f

    override fun getDouble(key: String): Double =
        this.map[key] as? Double ?: 0.0

    override fun getString(key: String): String =
        this.map[key] as? String ?: ""

    override fun setBoolean(key: String, value: Boolean)
    {
        this.map[key] = value
    }

    override fun setChar(key: String, value: Char)
    {
        this.map[key] = value
    }

    override fun setByte(key: String, value: Byte)
    {
        this.map[key] = value
    }

    override fun setShort(key: String, value: Short)
    {
        this.map[key] = value
    }

    override fun setInt(key: String, value: Int)
    {
        this.map[key] = value
    }

    override fun setLong(key: String, value: Long)
    {
        this.map[key] = value
    }

    override fun setFloat(key: String, value: Float)
    {
        this.map[key] = value
    }

    override fun setDouble(key: String, value: Double)
    {
        this.map[key] = value
    }

    override fun setString(key: String, value: String)
    {
        this.map[key] = value
    }
}