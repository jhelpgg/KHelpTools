package khelp.utilities.regex

import khelp.utilities.extensions.regularExpression
import khelp.utilities.stateCheck
import khelp.utilities.text.CharactersInterval

/**
 * Capturing group to capture things in regular expression
 */
class RegularExpressionGroup internal constructor(internal val regularExpression: RegularExpression) :
    RegularExpressionElement()
{
    internal var firstUse: Boolean = true
    internal var groupID: Int = -1
    internal var parent: RegularExpression? = null
        private set

    operator fun plus(regularExpression: RegularExpression): RegularExpression =
        when
        {
            regularExpression.patternComputed.get() ->
                throw  IllegalArgumentException("The given regular expression already have computed its pattern, so can't be combined")
            else                                    ->
            {
                val parent = RegularExpression("%s%s", this, regularExpression)
                this.setParent(parent)
                parent
            }
        }

    operator fun plus(regularExpression: RegularExpressionGroup): RegularExpression
    {
        val parent = RegularExpression("%s%s", this, regularExpression)
        this.setParent(parent)
        regularExpression.setParent(parent)
        return parent
    }

    operator fun plus(char: Char): RegularExpression =
        this + char.regularExpression

    operator fun plus(charArray: CharArray): RegularExpression =
        this + charArray.regularExpression

    operator fun plus(text: String): RegularExpression =
        this + text.regularExpression

    operator fun plus(charactersInterval: CharactersInterval): RegularExpression =
        this + charactersInterval.regularExpression

    infix fun OR(charactersInterval: CharactersInterval): RegularExpression =
        this OR charactersInterval.regularExpression

    infix fun OR(char: Char): RegularExpression =
        this OR char.regularExpression

    infix fun OR(charArray: CharArray): RegularExpression =
        this OR charArray.regularExpression

    infix fun OR(text: String): RegularExpression =
        this OR text.regularExpression

    infix fun OR(regularExpression: RegularExpression): RegularExpression =
        when
        {
            regularExpression.patternComputed.get() ->
                throw  IllegalArgumentException("The given regular expression already have computed its pattern, so can't be combined")
            else                                    ->
            {
                val parent = RegularExpression("(?:%s|(?:%s))", this, regularExpression)
                this.setParent(parent)
                parent
            }
        }

    infix fun OR(regularExpression: RegularExpressionGroup): RegularExpression
    {
        val parent = RegularExpression("(?:%s|%s)", this, regularExpression)
        this.setParent(parent)
        regularExpression.setParent(parent)
        return parent
    }

    fun zeroOrMore(): RegularExpression
    {
        val parent = RegularExpression("%s*", this)
        this.setParent(parent)
        return parent
    }

    fun oneOrMore(): RegularExpression
    {
        val parent = RegularExpression("%s+", this)
        this.setParent(parent)
        return parent
    }

    fun zeroOrOne(): RegularExpression
    {
        val parent = RegularExpression("%s?", this)
        this.setParent(parent)
        return parent
    }

    fun exactTimes(times: Int): RegularExpression =
        when
        {
            times <= 0 -> throw IllegalArgumentException("times must >0, not $times")
            times == 1 ->
            {
                val parent = RegularExpression("%s", this)
                this.setParent(parent)
                parent
            }
            else       ->
            {
                val parent = RegularExpression("%s{$times}", this)
                this.setParent(parent)
                parent
            }
        }

    fun atLeast(times: Int): RegularExpression =
        when
        {
            times <= 0 -> this.zeroOrMore()
            times == 1 -> this.oneOrMore()
            else       ->
            {
                val parent = RegularExpression("%s{$times,}", this)
                this.setParent(parent)
                parent
            }
        }

    fun atMost(times: Int): RegularExpression =
        when
        {
            times <= 0 -> throw IllegalArgumentException("times must >0, not $times")
            times == 1 -> this.zeroOrOne()
            else       ->
            {
                val parent = RegularExpression("%s{0,$times}", this)
                this.setParent(parent)
                parent
            }
        }

    fun between(minimum: Int, maximum: Int): RegularExpression =
        when
        {
            minimum > maximum            -> throw IllegalArgumentException("minimum $minimum is not lower or equals to maximum $maximum")
            minimum < 0                  -> throw IllegalArgumentException("minimum must be >=0, not $minimum")
            maximum <= 0                 -> throw IllegalArgumentException("maximum must be >0, not $maximum")
            minimum == 0 && maximum == 1 -> this.zeroOrOne()
            minimum == maximum           -> this.exactTimes(minimum)
            else                         ->
            {
                val parent = RegularExpression("%s{$minimum,$maximum}", this)
                this.setParent(parent)
                parent
            }
        }


    internal fun setParent(parent: RegularExpression)
    {
        if (this.parent != null)
        {
            stateCheck(parent.insideHierarchy(this.parent!!)) { "The group have already an other parent" }
        }
        else
        {
            this.parent = parent
        }
    }

    override fun regexString(resolveGroup: Boolean): String
    {
        if (this.firstUse)
        {
            this.firstUse = false
            return "(${this.regularExpression.regexString(false)})"
        }

        return "\\${this.groupID}"
    }
}
