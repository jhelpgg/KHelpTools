package khelp.resources

import khelp.io.readLines
import khelp.thread.TaskContext
import khelp.thread.observable.ObservableData
import java.util.Locale

class ResourcesText internal constructor(private var basePath : String, private val resources : Resources)
{
    companion object
    {
        private const val TEXT_SEPARATOR = "-<=-=>-"
        private const val START_COMMENT = "#-<*"
        private const val END_COMMENT = "*>-#"
    }

    private val observableData = ObservableData(this)
    private val texts = HashMap<String, String>()

    val observableChange = this.observableData.observable

    init
    {
        Resources.languageObservableData.observable.observedBy(TaskContext.IO, this::loadTexts)
    }

    operator fun get(key : String) : String =
        synchronized(this.texts) {
            this.texts[key] ?: if (this != defaultTexts) defaultTexts[key] else "/!\\ No key defined for $key /!\\"
        }

    private fun loadTexts(locale : Locale)
    {
        synchronized(this.texts) { this.texts.clear() }

        this.loadText(this.basePath)
        val language = locale.language

        if (language.isNotEmpty())
        {
            this.loadText("${this.basePath}_$language")

            val country = locale.country

            if (country.isNotEmpty())
            {
                this.loadText("${this.basePath}_${language}_$country")

                val variant = locale.variant

                if (variant.isNotEmpty())
                {
                    this.loadText("${this.basePath}_${language}_${country}_$variant")
                }
            }
        }

        this.observableData.value(this)
    }

    private fun loadText(path : String)
    {
        var comment = false
        var keyRead = false
        var notFirstLine = false
        var key = ""
        var text = StringBuilder()

        readLines({ this.resources.inputStream(path) },
                  { line ->
                      val lineTrim = line.trim()

                      when (lineTrim)
                      {
                          ResourcesText.START_COMMENT  -> comment = true
                          ResourcesText.END_COMMENT    -> comment = false
                          ResourcesText.TEXT_SEPARATOR ->
                              if (keyRead && ! comment)
                              {
                                  synchronized(this.texts)
                                  {
                                      this.texts[key] = this.resources.replaceResourcesLinkIn(text.toString())
                                  }

                                  keyRead = false
                                  key = ""
                              }
                          else ->
                              if (! comment)
                              {
                                  if (! keyRead && lineTrim.isNotEmpty())
                                  {
                                      keyRead = true
                                      key = lineTrim
                                      notFirstLine = false
                                      text = StringBuilder()
                                  }
                                  else if (keyRead)
                                  {
                                      if (notFirstLine)
                                      {
                                          text.append('\n')
                                          notFirstLine = true
                                      }

                                      text.append(line)
                                      notFirstLine = true
                                  }
                              }
                      }
                  },
                  {})
    }
}
