package khelp.preferences

import khelp.utilities.serialization.Serializer

internal class PreferencesSerializer(private val key : String, private val preferences : Preferences) : Serializer
{
    override fun setBoolean(key : String, value : Boolean)
    {
        this.preferences.preferences["${this.key}.$key"] = value.toString()
    }

    override fun setChar(key : String, value : Char)
    {
        this.preferences.preferences["${this.key}.$key"] = value.toString()
    }

    override fun setByte(key : String, value : Byte)
    {
        this.preferences.preferences["${this.key}.$key"] = value.toString()
    }

    override fun setShort(key : String, value : Short)
    {
        this.preferences.preferences["${this.key}.$key"] = value.toString()
    }

    override fun setInt(key : String, value : Int)
    {
        this.preferences.preferences["${this.key}.$key"] = value.toString()
    }

    override fun setLong(key : String, value : Long)
    {
        this.preferences.preferences["${this.key}.$key"] = value.toString()
    }

    override fun setFloat(key : String, value : Float)
    {
        this.preferences.preferences["${this.key}.$key"] = value.toString()
    }

    override fun setDouble(key : String, value : Double)
    {
        this.preferences.preferences["${this.key}.$key"] = value.toString()
    }

    override fun setString(key : String, value : String)
    {
        this.preferences.preferences["${this.key}.$key"] = value
    }
}
