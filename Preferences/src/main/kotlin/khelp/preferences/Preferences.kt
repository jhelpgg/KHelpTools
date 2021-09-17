package khelp.preferences

import khelp.io.extensions.readInt
import khelp.io.extensions.writeInt
import khelp.io.obtainExternalFile
import khelp.io.treatInputStream
import khelp.io.treatOutputStream
import khelp.thread.Mutex
import khelp.utilities.extensions.double
import khelp.utilities.extensions.utf8
import khelp.utilities.serialization.ParsableSerializable
import java.io.FileInputStream
import java.io.FileOutputStream

class Preferences(preferencesPath : String = "pref/preferences.pref")
{
    private val preferencesFile = obtainExternalFile(preferencesPath)
    internal val preferences = HashMap<String, String>()
    private val mutex = Mutex()

    init
    {
        this.load()
    }

    operator fun get(key : String, defaultValue : Boolean) : Boolean =
        this.mutex { this.preferences[key]?.equals("true", true) ?: defaultValue }

    operator fun get(key : String, defaultValue : Int) : Int =
        this.mutex {
            this.preferences[key]
                ?.double(defaultValue.toDouble())
                ?.toInt()
            ?: defaultValue
        }

    operator fun get(key : String, defaultValue : Long) : Long =
        this.mutex {
            this.preferences[key]
                ?.double(defaultValue.toDouble())
                ?.toLong()
            ?: defaultValue
        }

    operator fun get(key : String, defaultValue : Float) : Float =
        this.mutex {
            this.preferences[key]
                ?.double(defaultValue.toDouble())
                ?.toFloat()
            ?: defaultValue
        }

    operator fun get(key : String, defaultValue : Double) : Double =
        this.mutex { this.preferences[key]?.double(defaultValue) ?: defaultValue }

    operator fun get(key : String, defaultValue : String) : String =
        this.mutex { this.preferences[key] ?: defaultValue }

    operator fun <E : Enum<E>> get(key : String, defaultValue : E) : E =
        this.mutex {
            val serialized = this.preferences[key]

            for (value in defaultValue.javaClass.enumConstants)
            {
                if (value.name == serialized)
                {
                    return@mutex value
                }
            }

            defaultValue
        }

    fun <PS : ParsableSerializable> obtain(key : String, creator : () -> PS) : PS =
        this.mutex {
            val parsableSerializable = creator()
            parsableSerializable.parse(PreferencesParser(key, this))
            parsableSerializable
        }

    fun edit(editor : PreferencesEditor.() -> Unit)
    {
        this.mutex {
            val preferencesEditor = PreferencesEditor(this)
            editor(preferencesEditor)
            this.save()
        }
    }

    operator fun contains(key : String) : Boolean =
        this.mutex { key in this.preferences }

    private fun save()
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
    }

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
}