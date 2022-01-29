package khelp.bot.engine.dsl

import khelp.resources.ResourcesText
import khelp.utilities.regex.ANY
import khelp.utilities.regex.RegularExpression
import java.util.Locale

class Phrase(val keyText : String)
{
    private val captures = HashMap<String, String>()
    private var regex : RegularExpression = ANY

    fun capturesList() : Set<String> = this.captures.keys

    infix fun String.associate(value : String)
    {
        if (this@Phrase.captures.containsKey(this))
        {
            this@Phrase.captures[this] = value
        }
        else
        {
            throw IllegalArgumentException("Key $this not found")
        }
    }

    fun associated(key : String) : String =
        this.captures[key] ?: throw IllegalArgumentException("Key $key not found")

    fun obtainText(resourcesText : ResourcesText) : String {
        val value = resourcesText[this.keyText]
TODO()
    }

   // private fun extractCaptures
}
