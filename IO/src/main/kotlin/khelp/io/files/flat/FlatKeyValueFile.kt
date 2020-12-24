package khelp.io.files.flat

import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.util.concurrent.atomic.AtomicBoolean
import khelp.io.extensions.createFile
import khelp.io.readLines
import khelp.thread.parallel
import khelp.utilities.extensions.copy
import khelp.utilities.log.exception
import khelp.utilities.serailzation.Parser
import khelp.utilities.serailzation.Serializer

internal const val LINE_RETURN_REPLACER = "+-<n>-+"
internal const val LINE_FEED_REPLACER = "+-<r>-+"
private const val KEY_VALUE_SEPARATOR = " *-{(<=>)}-* "
private const val KEY_VALUE_SEPARATOR_SIZE = KEY_VALUE_SEPARATOR.length

class FlatKeyValueFile(private val file: File)
{
    val parser: Parser = FlatKeyValueParser(this)
    val serializer: Serializer = FlatKeyValueSerializer(this)

    private val keyValues = HashMap<String, String>()
    private val saving = AtomicBoolean(false)
    private val haveToSaveAgain = AtomicBoolean(false)

    init
    {
        if (!this.file.createFile())
        {
            throw IOException("Can't create file : ${file.absolutePath}")
        }

        this.load()
    }

    internal operator fun set(key: String, value: String)
    {
        synchronized(this.keyValues)
        {
            this.keyValues[key] = value
        }

        if (this.saving.compareAndSet(false, true))
        {
            parallel { this.save() }
        }
        else
        {
            this.haveToSaveAgain.set(true)
        }
    }

    internal operator fun get(key: String): String? =
        synchronized(this.keyValues) { this.keyValues[key] }

    private fun load()
    {
        readLines({ FileInputStream(this.file) },
                  { line ->
                      val index = line.indexOf(KEY_VALUE_SEPARATOR)

                      if (index > 0)
                      {
                          val key = line.substring(0, index)
                          val value = line.substring(index + KEY_VALUE_SEPARATOR_SIZE)
                          this.keyValues[key] = value
                      }
                  })
    }

    private fun save()
    {
        do
        {
            this.saving.set(true)
            val copy = synchronized(this.keyValues) { this.keyValues.copy() }

            var bufferedWriter: BufferedWriter? = null

            try
            {
                bufferedWriter = BufferedWriter(OutputStreamWriter(FileOutputStream(this.file)))

                for ((key, value) in copy)
                {
                    bufferedWriter.write(key)
                    bufferedWriter.write(KEY_VALUE_SEPARATOR)
                    bufferedWriter.write(value)
                    bufferedWriter.newLine()
                }

                bufferedWriter.flush()
            }
            catch (exception: Exception)
            {
                exception(exception, "Issue while saving to ${this.file.absolutePath}")
            }
            finally
            {
                try
                {
                    bufferedWriter?.close()
                }
                catch (_: Exception)
                {
                }
            }

            this.saving.set(this.haveToSaveAgain.get())
        }
        while (this.haveToSaveAgain.compareAndSet(true, false))
    }
}