package khelp.preferences

import khelp.io.extensions.createFile
import khelp.io.extensions.readInt
import khelp.io.extensions.writeInt
import khelp.io.obtainExternalFile
import khelp.io.treatInputStream
import khelp.io.treatOutputStream
import khelp.thread.parallel
import khelp.utilities.extensions.double
import khelp.utilities.extensions.utf8
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.atomic.AtomicBoolean

class Preferences(preferencesPath : String = "pref/preferences.pref")
{
    private val preferencesFile = obtainExternalFile(preferencesPath)
    private val preferences = HashMap<String, String>()
    private val saving = AtomicBoolean(false)
    private val saveAgain = AtomicBoolean(false)

    init
    {
        this.load()
    }

    operator fun get(key : String, defaultValue : Boolean) : Boolean
    {
        var value = this.preferences[key]?.equals("true", true)

        if (value == null)
        {
            value = defaultValue
            this[key] = value
        }

        return value
    }

    operator fun get(key : String, defaultValue : Int) : Int
    {
        var value = this.preferences[key]?.double(defaultValue.toDouble())
            ?.toInt()

        if (value == null)
        {
            value = defaultValue
            this[key] = value
        }

        return value
    }

    operator fun get(key : String, defaultValue : Long) : Long
    {
        var value = this.preferences[key]?.double(defaultValue.toDouble())
            ?.toLong()

        if (value == null)
        {
            value = defaultValue
            this[key] = value
        }

        return value
    }

    operator fun get(key : String, defaultValue : Float) : Float
    {
        var value = this.preferences[key]?.double(defaultValue.toDouble())
            ?.toFloat()

        if (value == null)
        {
            value = defaultValue
            this[key] = value
        }

        return value
    }

    operator fun get(key : String, defaultValue : Double) : Double
    {
        var value = this.preferences[key]?.double(defaultValue)

        if (value == null)
        {
            value = defaultValue
            this[key] = value
        }

        return value
    }

    operator fun get(key : String, defaultValue : String) : String
    {
        var value = this.preferences[key]

        if (value == null)
        {
            value = defaultValue
            this[key] = value
        }

        return value
    }

    operator fun <E : Enum<E>> get(key : String, defaultValue : E) : E
    {
        val serialized = this.preferences[key]

        for (value in defaultValue.javaClass.enumConstants)
        {
            if (value.name == serialized)
            {
                return value
            }
        }

        this[key] = defaultValue
        return defaultValue
    }

    operator fun set(key : String, value : Boolean)
    {
        this.preferences[key] = value.toString()
        this.save()
    }

    operator fun set(key : String, value : Int)
    {
        this.preferences[key] = value.toString()
        this.save()
    }

    operator fun set(key : String, value : Long)
    {
        this.preferences[key] = value.toString()
        this.save()
    }

    operator fun set(key : String, value : Float)
    {
        this.preferences[key] = value.toString()
        this.save()
    }

    operator fun set(key : String, value : Double)
    {
        this.preferences[key] = value.toString()
        this.save()
    }

    operator fun set(key : String, value : String)
    {
        this.preferences[key] = value
        this.save()
    }

    operator fun <E : Enum<E>> set(key : String, value : E)
    {
        this.preferences[key] = value.name
        this.save()
    }

    operator fun contains(key : String) : Boolean =
        key in this.preferences

    private fun load()
    {
        if (! this.preferencesFile.exists())
        {
            return
        }

        treatInputStream(
            { FileInputStream(this.preferencesFile) },
            { inputStream ->
                val length = inputStream.readInt()

                for (number in 0 until length)
                {
                    var size = inputStream.readInt()
                    var utf8 = inputStream.readNBytes(size)
                    val key = utf8.utf8
                    size = inputStream.readInt()
                    utf8 = inputStream.readNBytes(size)
                    val value = utf8.utf8
                    this.preferences[key] = value
                }
            },
            {})
    }

    private fun save()
    {
        if (this.saving.compareAndSet(false, true))
        {
            if (this.preferencesFile.createFile())
            {
                parallel { this.saveTask() }
            }
        }
        else
        {
            this.saveAgain.set(true)
        }
    }

    private fun saveTask()
    {
        while (this.saving.get())
        {
            treatOutputStream(
                { FileOutputStream(this.preferencesFile) },
                { outputStream ->
                    outputStream.writeInt(this.preferences.size)

                    for ((key, value) in this.preferences)
                    {
                        var utf8 = key.utf8
                        outputStream.writeInt(utf8.size)
                        outputStream.write(utf8)
                        utf8 = value.utf8
                        outputStream.writeInt(utf8.size)
                        outputStream.write(utf8)
                    }
                },
                {})

            this.saving.set(this.saveAgain.getAndSet(false))
        }
    }
}