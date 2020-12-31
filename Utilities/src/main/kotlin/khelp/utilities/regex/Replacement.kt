package khelp.utilities.regex

import java.util.regex.Pattern

/**
 * Replacement for replace text with rule given by [RegularExpression.replacement]
 */
class Replacement internal constructor(private val pattern: Pattern, private val replacement: String)
{
    /**
     * Replace all matching part in text with embed rule
     */
    fun replaceAll(text: String): String =
        this.pattern.matcher(text)
            .replaceAll(this.replacement)

    /**
     * Replace only first match with embed rule
     */
    fun replaceFirst(text: String): String =
        this.pattern.matcher(text)
            .replaceFirst(this.replacement)
}