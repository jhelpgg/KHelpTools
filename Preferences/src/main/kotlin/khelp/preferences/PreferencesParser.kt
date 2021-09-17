package khelp.preferences

import khelp.utilities.extensions.double
import khelp.utilities.serialization.Parser

internal class PreferencesParser(private val key : String, private val preferences : Preferences) : Parser
{
    override fun getBoolean(key : String) : Boolean =
        this.preferences.preferences["${this.key}.$key"]?.equals("true", true) ?: false

    override fun getChar(key : String) : Char
    {
        val character = this.preferences.preferences["${this.key}.$key"] ?: " "

        return if (character.isEmpty())
        {
            ' '
        }
        else character[0]
    }

    override fun getByte(key : String) : Byte =
        this.preferences.preferences["${this.key}.$key"]
            ?.double(0.0)
            ?.toInt()
            ?.toByte()
        ?: 0.toByte()

    override fun getShort(key : String) : Short =
        this.preferences.preferences["${this.key}.$key"]
            ?.double(0.0)
            ?.toInt()
            ?.toShort()
        ?: 0.toShort()

    override fun getInt(key : String) : Int =
        this.preferences.preferences["${this.key}.$key"]
            ?.double(0.0)
            ?.toInt()
        ?: 0

    override fun getLong(key : String) : Long =
        this.preferences.preferences["${this.key}.$key"]
            ?.double(0.0)
            ?.toLong()
        ?: 0L

    override fun getFloat(key : String) : Float =
        this.preferences.preferences["${this.key}.$key"]
            ?.double(0.0)
            ?.toFloat()
        ?: 0f

    override fun getDouble(key : String) : Double =
        this.preferences.preferences["${this.key}.$key"]?.double(0.0) ?: 0.0

    override fun getString(key : String) : String =
        this.preferences.preferences["${this.key}.$key"] ?: ""
}
