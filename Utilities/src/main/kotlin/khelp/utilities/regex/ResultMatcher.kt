package khelp.utilities.regex

import java.util.regex.Matcher
import khelp.utilities.argumentCheck
import khelp.utilities.regex.dsl.MatchDSL
import khelp.utilities.regex.dsl.MatcherHeader
import khelp.utilities.regex.dsl.MatcherIntermediate
import khelp.utilities.regex.dsl.MatcherMatch
import khelp.utilities.regex.dsl.MatcherReplacement
import khelp.utilities.regex.dsl.MatcherTail
import khelp.utilities.regex.dsl.ReplacementDSL

/**
 * Result of match of text given at [RegularExpression.matcher].
 *
 * It contains information about match of the text and regular expression.
 *
 * It can indicates if text fully match with [matches],  or find the next matching part with [find]
 *
 * Other methods work only if [matches] is called and return `true` or [find] called at least one time
 */
class ResultMatcher internal constructor(private val regularExpression: RegularExpression,
                                         private val matcher: Matcher,
                                         private val text: String)
{
    /**
     * Indicates if the text fully matches.
     *
     * If match, groups are resolved and can be requested via `group()` and their position with `start(RegularExpressionGroup)`and `end(RegularExpressionGroup)`
     */
    fun matches(): Boolean =
        this.matcher.matches()

    /**
     * Search next text part that match to the regular expression
     *
     * If returns `true`:
     * * Groups are resolved and can be requested via `group()` and their position with `start(RegularExpressionGroup)`and `end(RegularExpressionGroup)`
     * * `start()` will indicates index in text where current match starts
     * * `end()` will indicates index just after in text where current match ends
     */
    fun find(): Boolean =
        this.matcher.find()

    /**
     * Obtain text captured for a group
     *
     * Have meaning if [matches] or [find] was called and returned `true`
     *
     * @throws IllegalArgumentException If group instance not inside regular expression source
     */
    @Throws(IllegalArgumentException::class)
    fun group(regularExpressionGroup: RegularExpressionGroup): String
    {
        argumentCheck(regularExpressionGroup in this.regularExpression) { "The given group is not linked to regular expression" }
        return this.matcher.group(regularExpressionGroup.groupID) ?: ""
    }

    /**
     * Index in text where start matching after [find] returns `true`
     */
    fun start(): Int =
        this.matcher.start()

    /**
     * Start index in text of a group
     *
     * Have meaning if [matches] or [find] was called and returned `true`
     *
     * @throws IllegalArgumentException If group instance not inside regular expression source
     */
    @Throws(IllegalArgumentException::class)
    fun start(regularExpressionGroup: RegularExpressionGroup): Int
    {
        argumentCheck(regularExpressionGroup in this.regularExpression) { "The given group is not linked to regular expression" }
        return this.matcher.start(regularExpressionGroup.groupID)
    }

    /**
     * Index in text where end matching after [find] returns `true`
     */
    fun end(): Int =
        this.matcher.end()

    /**
     * End index in text of a group
     *
     * Have meaning if [matches] or [find] was called and returned `true`
     *
     * @throws IllegalArgumentException If group instance not inside regular expression source
     */
    @Throws(IllegalArgumentException::class)
    fun end(regularExpressionGroup: RegularExpressionGroup): Int
    {
        argumentCheck(regularExpressionGroup in this.regularExpression) { "The given group is not linked to regular expression" }
        return this.matcher.end(regularExpressionGroup.groupID)
    }

    /**
     * Append in `StringBuilder` last not matching text and do replacement rule on current matching text part.
     *
     * Work only after a call to [find] returns `true`
     *
     * For exemple in :
     *
     * ```kotlin
     *   val text = "I want remove braquets [45], [66], [something], [] and replace them by parenthesis. [73, the magic number]. The end!"
     *   val group = ']'.allCharactersExcludeThis.zeroOrMore().group()
     *   val regex = '['.regularExpression + group + ']'
     *   val matcher = regex.matcher(text)
     *   val stringBuilder = StringBuilder()
     *
     *   while(matcher.find())
     *   {
     *       matcher.appendReplacement(stringBuilder) {
     *           +"("
     *           +group
     *           +")"
     *       }
     *   }
     * ```
     *
     * `stringBuilder` will contains
     * ```
     * I want remove braquets (45), (66), (something), () and replace them by parenthesis. (73, the magic number)
     * ```
     *
     * To get remaining text `. The end!`, use [appendTail] after while when [find] returns false
     */
    @ReplacementDSL
    fun appendReplacement(stringBuilder: StringBuilder, replacementCreator: MatcherReplacement.() -> Unit)
    {
        val matcherReplacement = MatcherReplacement(this.regularExpression)
        replacementCreator(matcherReplacement)
        this.matcher.appendReplacement(stringBuilder, matcherReplacement.replacement.toString())
    }

    /**
     * Append last remaining characters after [find] return `false`.
     *
     * For exemple in :
     * ```kotlin
     *   val text = "I want remove braquets [45], [66], [something], [] and replace them by parenthesis. [73, the magic number]. The end!"
     *   val group = ']'.allCharactersExcludeThis.zeroOrMore().group()
     *   val regex = '['.regularExpression + group + ']'
     *   val matcher = regex.matcher(text)
     *   val stringBuilder = StringBuilder()
*
     *   while(matcher.find())
     *   {
     *       matcher.appendReplacement(stringBuilder) {
     *           +"("
     *           +group
     *           +")"
     *       }
     *   }
*
     *   matcher.appendTail(stringBuilder)
     * ```
     *
     * `stringBuilder` will contains:
     * ```
     * I want remove braquets (45), (66), (something), () and replace them by parenthesis. (73, the magic number). The end!
     * ```
     */
    fun appendTail(stringBuilder: StringBuilder)
    {
        this.matcher.appendTail(stringBuilder)
    }

    /**
     * Do a treatment for each match and non matche parts of text. Don't use it with [find] or [matches].
     * This method calls [find] by itself.
     *
     * It gives opportunity to do special treatment for text before first match, text after last match, text between two matches and matching text
     *
     * For exemple in
     * ```kotlin
     *   val text = "I want remove braquets [45], [66], [something], [] and replace them by parenthesis. [73, the magic number]. The end!"
     *   val group = ']'.allCharactersExcludeThis.zeroOrMore()
     *                                           .group()
     *   val regex = '['.regularExpression + group + ']'
     *   val matcher = regex.matcher(text)
     *   val result = matcher.forEachMatch({ +header.onlyFirst(7) },
     *                                     { +intermediate.ellipseIfMoreThan(9) },
     *                                     { +tail.onlyLast(7) },
     *                                     {
     *                                         replace { matcher ->
     *                                             {
     *                                                 +"("
     *                                                 +group
     *                                                 +")"
     *                                             }
     *                                         }
     *                                     })
     * ```
     *
     * `result` will contains :
     * ```
     * I wa...(45), (66), (something), () an...s. (73, the magic number)...end!
     * ```
     */
    @MatchDSL
    fun forEachMatch(headerTreatment: MatcherHeader.() -> Unit = { +header },
                     intermediateTreatment: MatcherIntermediate.() -> Unit = { +intermediate },
                     tailTreatment: MatcherTail.() -> Unit = { +tail },
                     matchTreatment: MatcherMatch.() -> Unit): String
    {
        val stringBuilder = StringBuilder()
        var header = true
        var start = 0

        while (this.matcher.find())
        {
            if (header)
            {
                header = false
                val matcherHeader = MatcherHeader(this.text.substring(start, this.matcher.start()))
                headerTreatment(matcherHeader)
                stringBuilder.append(matcherHeader.toAppend.toString())
            }
            else
            {
                val matcherIntermediate = MatcherIntermediate(this.text.substring(start, this.matcher.start()))
                intermediateTreatment(matcherIntermediate)
                stringBuilder.append(matcherIntermediate.toAppend.toString())
            }

            val subText = this.text.substring(this.matcher.start(), this.matcher.end())
            val matcherMatch = MatcherMatch(this.regularExpression,
                                            this,
                                            subText)
            matchTreatment(matcherMatch)
            matcherMatch.replacement?.let { replacement ->
                stringBuilder.append(replacement.replaceFirst(subText))
            }
            start = this.matcher.end()
        }

        val matcherTail = MatcherTail(this.text.substring(start))
        tailTreatment(matcherTail)
        stringBuilder.append(matcherTail.toAppend.toString())
        return stringBuilder.toString()
    }
}