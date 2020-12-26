package khelp.utilities.regex.dsl

import khelp.utilities.argumentCheck
import khelp.utilities.regex.RegularExpression
import khelp.utilities.regex.RegularExpressionGroup

@ReplacementDSL
class MatcherReplacement internal constructor(private val regularExpression: RegularExpression)
{
    internal val replacement = StringBuilder()

    @ReplacementDSL
    operator fun String.unaryPlus()
    {
        this@MatcherReplacement.replacement.append(this)
    }

    @ReplacementDSL
    operator fun RegularExpressionGroup.unaryPlus()
    {
        argumentCheck(this in this@MatcherReplacement.regularExpression) { "The given group is not linked to regular expression" }
        this@MatcherReplacement.replacement.append('$')
        this@MatcherReplacement.replacement.append(this.groupID)
    }
}