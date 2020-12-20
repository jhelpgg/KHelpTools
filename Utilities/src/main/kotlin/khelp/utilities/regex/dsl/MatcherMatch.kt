package khelp.utilities.regex.dsl

import khelp.utilities.regex.RegularExpression
import khelp.utilities.regex.RegularExpressionGroup
import khelp.utilities.regex.Replacement
import khelp.utilities.regex.ResultMatcher

@MatchDSL
class MatcherMatch internal constructor(private val regularExpression: RegularExpression,
                                        private val matcher: ResultMatcher,
                                        val match: String)
{
    internal var replacement: Replacement? = null

    @MatchDSL
    fun value(regularExpressionGroup: RegularExpressionGroup): String =
            this.matcher.group(regularExpressionGroup)

    @ReplacementDSL
    fun replace(replacementBuilder: (MatcherMatch)->MatcherReplacement.() -> Unit)
    {
        this.replacement = this.regularExpression.replacement(replacementBuilder(this))
    }
}