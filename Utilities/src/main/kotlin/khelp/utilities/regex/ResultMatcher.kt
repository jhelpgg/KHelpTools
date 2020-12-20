package khelp.utilities.regex

import java.util.regex.Matcher
import khelp.utilities.regex.dsl.MatchDSL
import khelp.utilities.regex.dsl.MatcherHeader
import khelp.utilities.regex.dsl.MatcherIntermediate
import khelp.utilities.regex.dsl.MatcherMatch
import khelp.utilities.regex.dsl.MatcherReplacement
import khelp.utilities.regex.dsl.MatcherTail
import khelp.utilities.regex.dsl.ReplacementDSL

class ResultMatcher internal constructor(private val regularExpression: RegularExpression,
                                         private val matcher: Matcher,
                                         private val text: String)
{
    fun group(regularExpressionGroup: RegularExpressionGroup): String
    {
        if (regularExpressionGroup !in this.regularExpression)
        {
            throw IllegalArgumentException("The given group is not linked to regular expression")
        }

        return this.matcher.group(regularExpressionGroup.groupID)
    }

    fun find(): Boolean =
        this.matcher.find()

    fun start(): Int =
        this.matcher.start()

    fun start(regularExpressionGroup: RegularExpressionGroup): Int
    {
        if (regularExpressionGroup !in this.regularExpression)
        {
            throw IllegalArgumentException("The given group is not linked to regular expression")
        }

        return this.matcher.start(regularExpressionGroup.groupID)
    }

    fun end(): Int =
        this.matcher.end()

    fun end(regularExpressionGroup: RegularExpressionGroup): Int
    {
        if (regularExpressionGroup !in this.regularExpression)
        {
            throw IllegalArgumentException("The given group is not linked to regular expression")
        }

        return this.matcher.end(regularExpressionGroup.groupID)
    }

    @ReplacementDSL
    fun appendReplacement(stringBuilder: StringBuilder, replacementCreator: MatcherReplacement.() -> Unit)
    {
        val matcherReplacement = MatcherReplacement(this.regularExpression)
        replacementCreator(matcherReplacement)
        this.matcher.appendReplacement(stringBuilder, matcherReplacement.replacement.toString())
    }

    fun appendTail(stringBuilder: StringBuilder)
    {
        this.matcher.appendTail(stringBuilder)
    }

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