package khelp.utilities.regex.dsl

@TailDSL
class MatcherTail internal constructor(val tail: String)
{
    internal val toAppend = StringBuilder()

    @TailDSL
    operator fun String.unaryPlus()
    {
        this@MatcherTail.toAppend.append(this)
    }
}