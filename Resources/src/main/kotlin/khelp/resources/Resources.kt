package khelp.resources

import khelp.io.ReferenceSource
import khelp.thread.observable.ObservableData
import khelp.utilities.extensions.allCharactersExcludeThis
import khelp.utilities.extensions.regularExpression
import java.io.InputStream
import java.net.URL
import java.util.Locale
import java.util.concurrent.atomic.AtomicInteger

class Resources(private val source : ReferenceSource)
{
    companion object
    {
        val languageObservableData = ObservableData<Locale>(Locale.getDefault())

        @JvmStatic // Necessary to force be static because Kotlin companion object not well initialized at launch ...
        private val resourcePathGroup = '"'.allCharactersExcludeThis.oneOrMore()
            .group()

        @JvmStatic // Necessary to force be static because Kotlin companion object not well initialized at launch ...
        private val resourcesReferenceRegex = "\"resources:/".regularExpression + Resources.resourcePathGroup + '"'
    }

    private val resourcesTexts = HashMap<String, ResourcesText>()

    fun resourcesText(path : String) : ResourcesText =
        synchronized(this.resourcesTexts) { this.resourcesTexts.getOrPut(path) { ResourcesText(path, this) } }

    fun inputStream(path : String) : InputStream = this.source.inputStream(path)

    fun url(path : String) : URL = this.source.url(path)

    fun exists(path : String) : Boolean = this.source.exists(path)

    fun replaceResourcesLinkIn(string : String) : String
    {
        val stringBuilder = StringBuilder()
        var start = 0
        val matcher = Resources.resourcesReferenceRegex.matcher(string)

        while (matcher.find())
        {
            stringBuilder.append(string.substring(start, matcher.start()))
            stringBuilder.append('"')
            stringBuilder.append(this.url(matcher.group(Resources.resourcePathGroup)))
            stringBuilder.append('"')
            start = matcher.end()
        }

        stringBuilder.append(string.substring(start))
        return stringBuilder.toString()
    }

    override fun equals(other : Any?) : Boolean
    {
        if(this===other){
            return true
        }

        if(null==other || other !is Resources) {
            return false
        }

        return this.source == other.source
    }

    override fun hashCode() : Int = this.source.hashCode()

    override fun toString() : String = "Resources : ${this.source}"
}
