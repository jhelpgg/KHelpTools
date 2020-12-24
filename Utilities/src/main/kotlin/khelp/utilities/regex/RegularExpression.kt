package khelp.utilities.regex

import java.util.Stack
import java.util.concurrent.atomic.AtomicBoolean
import java.util.regex.Pattern
import khelp.utilities.extensions.regularExpression
import khelp.utilities.extensions.toUnicode
import khelp.utilities.regex.dsl.MatcherReplacement
import khelp.utilities.regex.dsl.ReplacementDSL
import khelp.utilities.text.CharactersInterval
import khelp.utilities.text.EmptyCharactersInterval
import khelp.utilities.text.SimpleCharactersInterval
import khelp.utilities.text.UnionCharactersInterval

class RegularExpression internal constructor(private val format: String,
                                             private val regularExpressionParameter: RegularExpressionElement? = null,
                                             private val regularExpressionSecondParameter: RegularExpressionElement? = null)
    : RegularExpressionElement()
{
    companion object
    {
        fun interval(charactersInterval: CharactersInterval): RegularExpression
        {
            val format =
                when (charactersInterval)
                {
                    is EmptyCharactersInterval  -> throw IllegalArgumentException("Empty interval can't be convert to regular expression")
                    is SimpleCharactersInterval -> charactersInterval.format("[", "]", "[", "]", "-", true)
                    is UnionCharactersInterval  ->
                    {
                        val stringBuilder = StringBuilder()
                        stringBuilder.append('[')

                        for (simpleInterval in charactersInterval.simpleIntervals)
                        {
                            stringBuilder.append(simpleInterval.minimum.toUnicode())

                            if (simpleInterval.minimum < simpleInterval.maximum)
                            {
                                stringBuilder.append('-')
                                stringBuilder.append(simpleInterval.maximum.toUnicode())
                            }
                        }

                        stringBuilder.append(']')
                        stringBuilder.toString()
                    }
                }

            return RegularExpression(format)
        }

        fun allCharactersExclude(charactersInterval: CharactersInterval): RegularExpression
        {
            val format =
                when (charactersInterval)
                {
                    is EmptyCharactersInterval  -> return ANY
                    is SimpleCharactersInterval -> charactersInterval.format("[^", "]", "[^", "]", "-", true)
                    is UnionCharactersInterval  ->
                    {
                        val stringBuilder = StringBuilder()
                        stringBuilder.append("[^")

                        for (simpleInterval in charactersInterval.simpleIntervals)
                        {
                            stringBuilder.append(simpleInterval.minimum.toUnicode())

                            if (simpleInterval.minimum < simpleInterval.maximum)
                            {
                                stringBuilder.append('-')
                                stringBuilder.append(simpleInterval.maximum.toUnicode())
                            }
                        }

                        stringBuilder.append(']')
                        stringBuilder.toString()
                    }
                }

            return RegularExpression(format)
        }

        fun text(text: String): RegularExpression =
            RegularExpression(Pattern.quote(text))
    }

    internal val patternComputed = AtomicBoolean(false)
    private lateinit var pattern: Pattern

    fun matches(text: String): Boolean =
        this.obtainMatcher(text)
            .matches()

    @ReplacementDSL
    fun replacement(replacementCreator: MatcherReplacement.() -> Unit): Replacement
    {
        // Pattern must be evaluate before create match, to be sure group IDs have been computed
        val pattern = this.getPattern()
        val matcherReplacement = MatcherReplacement(this)
        replacementCreator(matcherReplacement)
        return Replacement(this, pattern, matcherReplacement.replacement.toString())
    }

    fun matcher(text: String): ResultMatcher =
        ResultMatcher(this, this.obtainMatcher(text), text)

    fun split(text: String, limit: Int = Int.MAX_VALUE): Array<String> =
        this.getPattern()
            .split(text, limit)

    operator fun plus(regularExpression: RegularExpression): RegularExpression =
        when
        {
            this.patternComputed.get()              ->
                throw  IllegalStateException("This regular expression already have computed its pattern, so can't be combined")
            regularExpression.patternComputed.get() ->
                throw  IllegalArgumentException("The given regular expression already have computed its pattern, so can't be combined")
            else                                    ->
                RegularExpression("%s%s", this, regularExpression)
        }

    operator fun plus(regularExpressionGroup: RegularExpressionGroup): RegularExpression
    {
        if (this.patternComputed.get())
        {
            throw  IllegalStateException("This regular expression already have computed its pattern, so can't be combined")
        }

        val parent = RegularExpression("%s%s", this, regularExpressionGroup)
        regularExpressionGroup.setParent(parent)
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
            this.patternComputed.get()              ->
                throw  IllegalStateException("This regular expression already have computed its pattern, so can't be combined")
            regularExpression.patternComputed.get() ->
                throw  IllegalArgumentException("The given regular expression already have computed its pattern, so can't be combined")
            else                                    ->
                RegularExpression("(?:(?:%s)|(?:%s))", this, regularExpression)
        }

    infix fun OR(regularExpression: RegularExpressionGroup): RegularExpression =
        when
        {
            this.patternComputed.get() ->
                throw  IllegalStateException("This regular expression already have computed its pattern, so can't be combined")
            else                       ->
            {
                val parent = RegularExpression("(?:(?:%s)|%s)", this, regularExpression)
                regularExpression.setParent(parent)
                parent
            }
        }

    fun zeroOrMore(): RegularExpression =
        if (this.patternComputed.get())
        {
            throw  IllegalStateException("This regular expression already have computed its pattern, so can't be combined")
        }
        else
        {
            RegularExpression("(?:%s)*", this)
        }

    fun oneOrMore(): RegularExpression =
        if (this.patternComputed.get())
        {
            throw  IllegalStateException("This regular expression already have computed its pattern, so can't be combined")
        }
        else
        {
            RegularExpression("(?:%s)+", this)
        }

    fun zeroOrOne(): RegularExpression =
        if (this.patternComputed.get())
        {
            throw  IllegalStateException("This regular expression already have computed its pattern, so can't be combined")
        }
        else
        {
            RegularExpression("(?:%s)?", this)
        }

    fun exactTimes(times: Int): RegularExpression =
        when
        {
            this.patternComputed.get() ->
                throw  IllegalStateException("This regular expression already have computed its pattern, so can't be combined")
            times <= 0                 -> throw IllegalArgumentException("times must >0, not $times")
            times == 1                 -> this
            else                       -> RegularExpression("(?:%s){$times}", this)
        }

    fun atLeast(times: Int): RegularExpression =
        when
        {
            this.patternComputed.get() ->
                throw  IllegalStateException("This regular expression already have computed its pattern, so can't be combined")
            times <= 0                 -> this.zeroOrMore()
            times == 1                 -> this.oneOrMore()
            else                       -> RegularExpression("(?:%s){$times,}", this)
        }

    fun atMost(times: Int): RegularExpression =
        when
        {
            this.patternComputed.get() ->
                throw  IllegalStateException("This regular expression already have computed its pattern, so can't be combined")
            times <= 0                 -> throw IllegalArgumentException("times must >0, not $times")
            times == 1                 -> this.zeroOrOne()
            else                       -> RegularExpression("(?:%s){0,$times}", this)
        }

    fun between(minimum: Int, maximum: Int): RegularExpression =
        when
        {
            this.patternComputed.get()   ->
                throw  IllegalStateException("This regular expression already have computed its pattern, so can't be combined")
            minimum > maximum            -> throw IllegalArgumentException("minimum $minimum is not lower or equals to maximum $maximum")
            minimum < 0                  -> throw IllegalArgumentException("minimum must be >=0, not $minimum")
            maximum <= 0                 -> throw IllegalArgumentException("maximum must be >0, not $maximum")
            minimum == 0 && maximum == 1 -> this.zeroOrOne()
            minimum == maximum           -> this.exactTimes(minimum)
            else                         -> RegularExpression("(?:%s){$minimum,$maximum}", this)
        }

    fun group(): RegularExpressionGroup =
        if (this.patternComputed.get())
        {
            throw  IllegalStateException("This regular expression already have computed its pattern, so can't be combined")
        }
        else
        {
            RegularExpressionGroup(this)
        }

    operator fun contains(regularExpressionElement: RegularExpressionElement): Boolean
    {
        val stack = Stack<RegularExpressionElement>()
        stack.push(this)
        var current: RegularExpressionElement

        while (stack.isNotEmpty())
        {
            current = stack.pop()

            if (current == regularExpressionElement)
            {
                return true
            }

            when (current)
            {
                is RegularExpressionGroup ->
                    stack.push(current.regularExpression)
                is RegularExpression      ->
                {
                    current.regularExpressionParameter?.let { element -> stack.push(element) }
                    current.regularExpressionSecondParameter?.let { element -> stack.push(element) }
                }
            }
        }

        return false
    }

    override fun regexString(resolveGroup: Boolean): String
    {
        if (resolveGroup)
        {
            val stackRegularExpression = Stack<RegularExpressionElement>()
            var current: RegularExpressionElement = this
            var groupID = 1

            while (true)
            {
                when (current)
                {
                    is RegularExpressionGroup ->
                    {
                        current.firstUse = true

                        if (current.groupID < 0)
                        {
                            current.groupID = groupID
                            groupID++
                        }

                        current = current.regularExpression
                    }
                    is RegularExpression      ->
                        if (current.regularExpressionParameter == null)
                        {
                            if (stackRegularExpression.empty())
                            {
                                break
                            }
                            else
                            {
                                current = stackRegularExpression.pop()
                            }
                        }
                        else
                        {
                            if (current.regularExpressionSecondParameter != null)
                            {
                                stackRegularExpression.push(current.regularExpressionSecondParameter)
                            }

                            current = current.regularExpressionParameter!!
                        }
                }
            }
        }

        return when
        {
            this.regularExpressionParameter == null       ->
                this.format
            this.regularExpressionSecondParameter == null ->
            {
                val result = String.format(this.format, this.regularExpressionParameter.regexString(false))

                if (this.regularExpressionParameter is RegularExpressionGroup)
                {
                    this.regularExpressionParameter.firstUse = false
                }

                result
            }
            else                                          ->
                String.format(this.format,
                              this.regularExpressionParameter.regexString(false),
                              this.regularExpressionSecondParameter.regexString(false))
        }
    }

    internal fun insideHierarchy(regularExpressionElement: RegularExpressionElement): Boolean
    {
        val stack = Stack<RegularExpressionElement>()
        stack.push(this)
        var current: RegularExpressionElement

        while (stack.isNotEmpty())
        {
            current = stack.pop()

            if (current == regularExpressionElement)
            {
                return true
            }

            when (current)
            {
                is RegularExpressionGroup ->
                    stack.push(current.regularExpression)
                is RegularExpression      ->
                {
                    current.regularExpressionParameter?.let { element -> stack.push(element) }
                    current.regularExpressionSecondParameter?.let { element -> stack.push(element) }
                }
            }
        }

        return false
    }


    private fun obtainMatcher(text: String) =
        this.getPattern()
            .matcher(text)

    private fun getPattern(): Pattern
    {
        if (this.patternComputed.compareAndSet(false, true))
        {
            this.pattern = Pattern.compile(this.regexString(true))
        }

        return this.pattern
    }
}

