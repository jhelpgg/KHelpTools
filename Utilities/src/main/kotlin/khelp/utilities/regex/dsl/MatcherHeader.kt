package khelp.utilities.regex.dsl

@HeaderDSL
class MatcherHeader internal constructor(val header: String)
{
    internal val toAppend = StringBuilder()

    @HeaderDSL
    operator fun String.unaryPlus()
    {
        this@MatcherHeader.toAppend.append(this)
    }
}