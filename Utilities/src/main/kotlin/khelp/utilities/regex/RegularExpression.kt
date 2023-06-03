package khelp.utilities.regex

import khelp.utilities.extensions.regularExpression
import khelp.utilities.extensions.toUnicode
import khelp.utilities.regex.dsl.MatcherReplacement
import khelp.utilities.regex.dsl.ReplacementDSL
import khelp.utilities.stateCheck
import khelp.utilities.text.CharactersInterval
import khelp.utilities.text.EmptyCharactersInterval
import khelp.utilities.text.SimpleCharactersInterval
import khelp.utilities.text.UnionCharactersInterval
import khelp.utilities.thread.Mutex
import java.util.Stack
import java.util.concurrent.atomic.AtomicBoolean
import java.util.regex.Pattern

/**
 * Regular expression
 *
 * The regular expression can be combined only if not resolved. After a resolution, try to combine it, will lead to an exception.
 *
 * Regular expression is resolve after a call to [matcher], [matches], [replacement] or [split]
 */
class RegularExpression internal constructor(private val format : String,
                                             private val regularExpressionParameter : RegularExpressionElement? = null,
                                             private val regularExpressionSecondParameter : RegularExpressionElement? = null)
    : RegularExpressionElement()
{
    companion object
    {
        /**
         * Create regular expression for any character inside an interval of characters
         */
        fun interval(charactersInterval : CharactersInterval) : RegularExpression
        {
            val format =
                when (charactersInterval)
                {
                    is EmptyCharactersInterval  -> throw IllegalArgumentException(
                        "Empty interval can't be convert to regular expression")
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

        /**
         * Create regular expression for any character except those inside an interval of characters
         */
        fun allCharactersExclude(charactersInterval : CharactersInterval) : RegularExpression
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

        /**
         * Create regular expression that match exactly a text
         */
        fun text(text : String) : RegularExpression =
            RegularExpression(Pattern.quote(text))
    }

    internal val patternComputed = AtomicBoolean(false)
    private lateinit var pattern : Pattern
    private val mutex = Mutex()

    /**
     * Indicates if given text fully match the regular expression.
     *
     * The regular expression is resolved after call this method.
     * This regular expression could not be combined with an other one after that.
     */
    fun matches(text : String) : Boolean =
        this.obtainMatcher(text)
            .matches()

    /**
     * Create a replacement for replace matching text part in texts
     *
     * The regular expression is resolved after call this method.
     * This regular expression could not be combined with an other one after that.
     */
    @ReplacementDSL
    fun replacement(replacementCreator : MatcherReplacement.() -> Unit) : Replacement
    {
        // Pattern must be evaluate before create match, to be sure group IDs have been computed
        val pattern = this.getPattern()
        val matcherReplacement = MatcherReplacement(this)
        replacementCreator(matcherReplacement)
        return Replacement(pattern, matcherReplacement.replacement.toString())
    }

    /**
     * Create a matcher for given text. To manipulate matching and non matching parts
     *
     * The regular expression is resolved after call this method.
     * This regular expression could not be combined with an other one after that.
     */
    fun matcher(text : String) : ResultMatcher =
        ResultMatcher(this, this.obtainMatcher(text), text)

    /**
     * Split a text on sing this regular expression as separator
     *
     * The regular expression is resolved after call this method.
     * This regular expression could not be combined with an other one after that.
     */
    fun split(text : String, limit : Int = Int.MAX_VALUE) : Array<String> =
        this.getPattern()
            .split(text, limit)

    /**
     * Combine this regular expression with an other one to make a regular expression match this follow by given one
     */
    operator fun plus(regularExpression : RegularExpression) : RegularExpression =
        when
        {
            this.patternComputed.get()              ->
                throw  IllegalStateException(
                    "This regular expression already have computed its pattern, so can't be combined")
            regularExpression.patternComputed.get() ->
                throw  IllegalArgumentException(
                    "The given regular expression already have computed its pattern, so can't be combined")
            else                                    ->
                RegularExpression("%s%s", this, regularExpression)
        }

    /**
     * Combine this regular expression with a group to make a regular expression match this follow by given one
     */
    operator fun plus(regularExpressionGroup : RegularExpressionGroup) : RegularExpression
    {
        stateCheck(
            ! this.patternComputed.get()) { "This regular expression already have computed its pattern, so can't be combined" }

        val parent = RegularExpression("%s%s", this, regularExpressionGroup)
        regularExpressionGroup.setParent(parent)
        return parent
    }

    operator fun plus(char : Char) : RegularExpression =
        this + char.regularExpression

    operator fun plus(charArray : CharArray) : RegularExpression =
        this + charArray.regularExpression

    operator fun plus(text : String) : RegularExpression =
        this + text.regularExpression

    operator fun plus(charactersInterval : CharactersInterval) : RegularExpression =
        this + charactersInterval.regularExpression

    infix fun OR(charactersInterval : CharactersInterval) : RegularExpression =
        this OR charactersInterval.regularExpression

    infix fun OR(char : Char) : RegularExpression =
        this OR char.regularExpression

    infix fun OR(charArray : CharArray) : RegularExpression =
        this OR charArray.regularExpression

    infix fun OR(text : String) : RegularExpression =
        this OR text.regularExpression

    /**
     * Combine this regular expression with an other one to make a regular expression match this or the given one
     */
    infix fun OR(regularExpression : RegularExpression) : RegularExpression =
        when
        {
            this.patternComputed.get()              ->
                throw  IllegalStateException(
                    "This regular expression already have computed its pattern, so can't be combined")
            regularExpression.patternComputed.get() ->
                throw  IllegalArgumentException(
                    "The given regular expression already have computed its pattern, so can't be combined")
            else                                    ->
                RegularExpression("(?:(?:%s)|(?:%s))", this, regularExpression)
        }

    /**
     * Combine this regular expression with a group to make a regular expression match this follow by given one
     */
    infix fun OR(regularExpression : RegularExpressionGroup) : RegularExpression =
        when
        {
            this.patternComputed.get() ->
                throw  IllegalStateException(
                    "This regular expression already have computed its pattern, so can't be combined")
            else                       ->
            {
                val parent = RegularExpression("(?:(?:%s)|%s)", this, regularExpression)
                regularExpression.setParent(parent)
                parent
            }
        }

    /**
     * Create a regular expression that can repeat this one zero, one or several times
     */
    fun zeroOrMore() : RegularExpression =
        if (this.patternComputed.get())
        {
            throw  IllegalStateException(
                "This regular expression already have computed its pattern, so can't be combined")
        }
        else
        {
            RegularExpression("(?:%s)*", this)
        }

    /**
     * Create a regular expression that can repeat this one at least one time
     */
    fun oneOrMore() : RegularExpression =
        if (this.patternComputed.get())
        {
            throw  IllegalStateException(
                "This regular expression already have computed its pattern, so can't be combined")
        }
        else
        {
            RegularExpression("(?:%s)+", this)
        }

    /**
     * Create a regular expression that match or not to this one
     */
    fun zeroOrOne() : RegularExpression =
        if (this.patternComputed.get())
        {
            throw  IllegalStateException(
                "This regular expression already have computed its pattern, so can't be combined")
        }
        else
        {
            RegularExpression("(?:%s)?", this)
        }

    /**
     * Create a regular expression that match only if this one is repeat a fix number of times
     */
    fun exactTimes(times : Int) : RegularExpression =
        when
        {
            this.patternComputed.get() ->
                throw  IllegalStateException(
                    "This regular expression already have computed its pattern, so can't be combined")
            times <= 0                 -> throw IllegalArgumentException("times must >0, not $times")
            times == 1                 -> this
            else                       -> RegularExpression("(?:%s){$times}", this)
        }

    /**
     * Create a regular expression that match only if this one is repeat at least a number of times
     */
    fun atLeast(times : Int) : RegularExpression =
        when
        {
            this.patternComputed.get() ->
                throw  IllegalStateException(
                    "This regular expression already have computed its pattern, so can't be combined")
            times <= 0                 -> this.zeroOrMore()
            times == 1                 -> this.oneOrMore()
            else                       -> RegularExpression("(?:%s){$times,}", this)
        }

    /**
     * Create a regular expression that match only if this one is repeat at most a number of times
     */
    fun atMost(times : Int) : RegularExpression =
        when
        {
            this.patternComputed.get() ->
                throw  IllegalStateException(
                    "This regular expression already have computed its pattern, so can't be combined")
            times <= 0                 -> throw IllegalArgumentException("times must >0, not $times")
            times == 1                 -> this.zeroOrOne()
            else                       -> RegularExpression("(?:%s){0,$times}", this)
        }

    /**
     * Create a regular expression that match only if this one is repeat between a minimum and maximum number of times
     */
    fun between(minimum : Int, maximum : Int) : RegularExpression =
        when
        {
            this.patternComputed.get()   ->
                throw  IllegalStateException(
                    "This regular expression already have computed its pattern, so can't be combined")
            minimum > maximum            -> throw IllegalArgumentException(
                "minimum $minimum is not lower or equals to maximum $maximum")
            minimum < 0                  -> throw IllegalArgumentException("minimum must be >=0, not $minimum")
            maximum <= 0                 -> throw IllegalArgumentException("maximum must be >0, not $maximum")
            minimum == 0 && maximum == 1 -> this.zeroOrOne()
            minimum == maximum           -> this.exactTimes(minimum)
            else                         -> RegularExpression("(?:%s){$minimum,$maximum}", this)
        }

    /**
     * Create a capturing group that match to this regular expression
     */
    fun group() : RegularExpressionGroup =
        if (this.patternComputed.get())
        {
            throw  IllegalStateException(
                "This regular expression already have computed its pattern, so can't be combined")
        }
        else
        {
            RegularExpressionGroup(this)
        }

    /**
     * Create regular expression that force this follow by given one.
     *
     * The difference with + is the given regular expression is not consumed
     */
    fun followBy(regularExpression : RegularExpression) : RegularExpression =
        RegularExpression("%s(?=%s)", this, regularExpression)

    /**
     * Create regular expression that force this not follow by given one.
     *
     * The given regular expression is not consumed
     */
    fun notFollowBy(regularExpression : RegularExpression) : RegularExpression =
        RegularExpression("%s(?!%s)", this, regularExpression)


    /**
     * Indicates if a regular expression or a group is inside this regular expression
     */
    operator fun contains(regularExpressionElement : RegularExpressionElement) : Boolean
    {
        val stack = Stack<RegularExpressionElement>()
        stack.push(this)
        var current : RegularExpressionElement

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

    override fun regexString(resolveGroup : Boolean) : String
    {
        if (resolveGroup)
        {
            val stackRegularExpression = Stack<RegularExpressionElement>()
            var current : RegularExpressionElement = this
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
                            groupID ++
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

                            current = current.regularExpressionParameter !!
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

    internal fun insideHierarchy(regularExpressionElement : RegularExpressionElement) : Boolean
    {
        val stack = Stack<RegularExpressionElement>()
        stack.push(this)
        var current : RegularExpressionElement

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


    private fun obtainMatcher(text : String) =
        this.getPattern()
            .matcher(text)

    private fun getPattern() : Pattern
    {
        this.mutex.playInCriticalSection {
            if (this.patternComputed.compareAndSet(false, true))
            {
                this.pattern = Pattern.compile(this.regexString(true))
            }
        }

        return this.pattern
    }
}

