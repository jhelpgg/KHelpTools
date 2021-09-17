package khelp.preferences

import khelp.utilities.serialization.ParsableSerializable

class PreferencesEditor internal constructor(private val preferences : Preferences)
{
    infix fun String.IS(value : Boolean)
    {
        this@PreferencesEditor.preferences.preferences[this] = value.toString()
    }

    infix fun String.IS(value : Int)
    {
        this@PreferencesEditor.preferences.preferences[this] = value.toString()
    }

    infix fun String.IS(value : Long)
    {
        this@PreferencesEditor.preferences.preferences[this] = value.toString()
    }

    infix fun String.IS(value : Float)
    {
        this@PreferencesEditor.preferences.preferences[this] = value.toString()
    }

    infix fun String.IS(value : Double)
    {
        this@PreferencesEditor.preferences.preferences[this] = value.toString()
    }

    infix fun String.IS(value : String)
    {
        this@PreferencesEditor.preferences.preferences[this] = value
    }

    infix fun <E : Enum<E>> String.IS(value : E)
    {
        this@PreferencesEditor.preferences.preferences[this] = value.name
    }

    infix fun <PS : ParsableSerializable> String.IS(value : PS)
    {
        value.serialize(PreferencesSerializer(this, this@PreferencesEditor.preferences))
    }
}
