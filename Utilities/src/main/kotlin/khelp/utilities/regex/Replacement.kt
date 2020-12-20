package khelp.utilities.regex

import java.util.regex.Pattern

class Replacement internal constructor(private val regularExpression: RegularExpression, private val pattern: Pattern, private val replacement:String)
{
    fun replaceAll(text: String): String =
        this.pattern.matcher(text)
            .replaceAll(this.replacement)

    fun replaceFirst(text: String): String =
        this.pattern.matcher(text)
            .replaceFirst(this.replacement)
}