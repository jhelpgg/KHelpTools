package khelp.grammar

import khelp.utilities.argumentCheck
import khelp.utilities.regex.NAME
import khelp.utilities.regex.RegularExpression
import khelp.utilities.regex.WHITE_SPACE

internal fun checkRuleName(ruleName: String): String
{
    argumentCheck(NAME.matches(ruleName)) { "$ruleName is not a valid rule name" }
    return ruleName
}

val WHITE_SPACES = RuleDefinitionRegularExpression(WHITE_SPACE.zeroOrMore())

@GrammarRuleDefinitionDSL
class RuleDefinition(val automaticWhiteSpace: Boolean)
{
    var rule: RuleDefinitionElement? = null

    //

    @GrammarRuleDefinitionDSL
    operator fun String.times(rule: RuleDefinitionElement) =
        +checkRuleName(this) * rule

    @GrammarRuleDefinitionDSL
    operator fun String.times(string: String) =
        +checkRuleName(this) * +checkRuleName(string)

    @GrammarRuleDefinitionDSL
    operator fun String.times(regularExpression: RegularExpression) =
        +checkRuleName(this) * +regularExpression

    @GrammarRuleDefinitionDSL
    operator fun RegularExpression.times(rule: RuleDefinitionElement) =
        +this * rule

    @GrammarRuleDefinitionDSL
    operator fun RegularExpression.times(string: String) =
        +this * +checkRuleName(string)

    @GrammarRuleDefinitionDSL
    operator fun RegularExpression.times(regularExpression: RegularExpression) =
        +this * +regularExpression

    @GrammarRuleDefinitionDSL
    infix fun String.I(rule: RuleDefinitionElement) =
        +checkRuleName(this) I rule

    @GrammarRuleDefinitionDSL
    infix fun String.I(string: String) =
        +checkRuleName(this) I +checkRuleName(string)

    @GrammarRuleDefinitionDSL
    infix fun String.I(regularExpression: RegularExpression) =
        +checkRuleName(this) I +regularExpression

    @GrammarRuleDefinitionDSL
    infix fun RegularExpression.I(rule: RuleDefinitionElement) =
        +this I rule

    @GrammarRuleDefinitionDSL
    infix fun RegularExpression.I(string: String) =
        +this I +checkRuleName(string)

    @GrammarRuleDefinitionDSL
    operator fun RuleDefinitionElement.times(reference: String) =
        this * +checkRuleName(reference)

    @GrammarRuleDefinitionDSL
    operator fun RuleDefinitionElement.times(regularExpression: RegularExpression) =
        this * +regularExpression

    @GrammarRuleDefinitionDSL
    infix fun RuleDefinitionElement.I(reference: String) =
        this I +checkRuleName(reference)

    @GrammarRuleDefinitionDSL
    infix fun RuleDefinitionElement.I(regularExpression: RegularExpression) =
        this I +regularExpression

    //

    @GrammarRuleDefinitionDSL
    operator fun String.unaryPlus(): RuleDefinitionElement =
        RuleDefinitionReference(checkRuleName(this))

    @GrammarRuleDefinitionDSL
    operator fun RegularExpression.unaryPlus(): RuleDefinitionElement =
        RuleDefinitionRegularExpression(this)

    @GrammarRuleDefinitionDSL
    fun String.zeroOrMore(): RuleDefinitionElement =
        RuleDefinitionReferenceZeroOrMore(checkRuleName(this))

    @GrammarRuleDefinitionDSL
    fun String.oneOrMore(): RuleDefinitionElement =
        RuleDefinitionReferenceOneOrMore(checkRuleName(this))

    @GrammarRuleDefinitionDSL
    fun String.zeroOrOne(): RuleDefinitionElement =
        RuleDefinitionReferenceZeroOrOne(checkRuleName(this))

    @GrammarRuleDefinitionDSL
    fun String.numberTimes(numberTimes: Int): RuleDefinitionElement =
        when
        {
            numberTimes <= 0 ->
                throw IllegalArgumentException("Repeat something at least one time")
            numberTimes == 1 ->
                RuleDefinitionReference(checkRuleName(this))
            else             ->
                RuleDefinitionReferenceExactTimes(checkRuleName(this), numberTimes)
        }

    @GrammarRuleDefinitionDSL
    fun String.atLeast(numberTimes: Int): RuleDefinitionElement =
        when
        {
            numberTimes < 0  ->
                throw IllegalArgumentException("Negative repetition have no meaning")
            numberTimes == 0 ->
                RuleDefinitionReferenceZeroOrMore(checkRuleName(this))
            numberTimes == 1 ->
                RuleDefinitionReferenceOneOrMore(checkRuleName(this))
            else             ->
                RuleDefinitionReferenceAtLeast(checkRuleName(this), numberTimes)
        }

    @GrammarRuleDefinitionDSL
    fun String.atMost(numberTimes: Int): RuleDefinitionElement =
        when
        {
            numberTimes <= 0 ->
                throw IllegalArgumentException("Repeat something at least one time")
            numberTimes == 1 ->
                RuleDefinitionReference(checkRuleName(this))
            else             ->
                RuleDefinitionReferenceAtMost(checkRuleName(this), numberTimes)
        }

    @GrammarRuleDefinitionDSL
    fun String.betweenTimes(minimumTimes: Int, maximumTimes: Int): RuleDefinitionElement =
        when
        {
            minimumTimes < 0                       ->
                throw IllegalArgumentException("Negative repetition have no meaning : minimum=$minimumTimes maximum=$maximumTimes")
            minimumTimes >= maximumTimes           ->
                throw IllegalArgumentException("Minimum must be <= to maximum : minimum=$minimumTimes maximum=$maximumTimes")
            maximumTimes == 0                      ->
                throw IllegalArgumentException("Repeat something at least one time : minimum=$minimumTimes maximum=$maximumTimes")
            minimumTimes == 0 && maximumTimes == 1 ->
                RuleDefinitionReferenceZeroOrOne(checkRuleName(this))
            minimumTimes == 0                      ->
                RuleDefinitionReferenceAtMost(checkRuleName(this), maximumTimes)
            minimumTimes == maximumTimes           ->
                RuleDefinitionReferenceExactTimes(checkRuleName(this), minimumTimes)
            else                                   ->
                RuleDefinitionReferenceBetween(checkRuleName(this), minimumTimes, maximumTimes)
        }

    //

    fun RuleDefinitionElement.zeroOrMore(): RuleDefinitionElement =
        RuleDefinitionElementZeroOrMore(this)

    fun RuleDefinitionElement.oneOrMore(): RuleDefinitionElement =
        RuleDefinitionElementOneOrMore(this)

    fun RuleDefinitionElement.zeroOrOne(): RuleDefinitionElement =
        RuleDefinitionElementZeroOrOne(this)

    fun RuleDefinitionElement.exactTimes(numberTimes: Int): RuleDefinitionElement =
        RuleDefinitionElementExactTimes(this, numberTimes)

    fun RuleDefinitionElement.atLeast(minimumTimes: Int): RuleDefinitionElement =
        RuleDefinitionElementAtLeast(this, minimumTimes)

    fun RuleDefinitionElement.atMost(maximumTimes: Int): RuleDefinitionElement =
        RuleDefinitionElementAtMost(this, maximumTimes)

    fun RuleDefinitionElement.between(minimumTimes: Int, maximumTimes: Int): RuleDefinitionElement =
        RuleDefinitionElementBetween(this, minimumTimes, maximumTimes)

    //

    @GrammarRuleDefinitionDSL
    operator fun RuleDefinitionElement.times(ruleDefinitionElement: RuleDefinitionElement): RuleDefinitionElement =
        when
        {
            this is RuleDefinitionElementConcatenate && ruleDefinitionElement is RuleDefinitionElementConcatenate ->
            {
                val ruleDefinitionElementConcatenate = RuleDefinitionElementConcatenate()
                ruleDefinitionElementConcatenate.ruleElements.addAll(this.ruleElements)

                if (automaticWhiteSpace)
                {
                    ruleDefinitionElementConcatenate.ruleElements.add(WHITE_SPACES)
                }

                ruleDefinitionElementConcatenate.ruleElements.addAll(ruleDefinitionElement.ruleElements)
                ruleDefinitionElementConcatenate
            }
            this is RuleDefinitionElementConcatenate                                                              ->
            {
                val ruleDefinitionElementConcatenate = RuleDefinitionElementConcatenate()
                ruleDefinitionElementConcatenate.ruleElements.addAll(this.ruleElements)

                if (automaticWhiteSpace)
                {
                    ruleDefinitionElementConcatenate.ruleElements.add(WHITE_SPACES)
                }

                ruleDefinitionElementConcatenate.ruleElements.add(ruleDefinitionElement)
                ruleDefinitionElementConcatenate
            }
            ruleDefinitionElement is RuleDefinitionElementConcatenate                                             ->
            {
                val ruleDefinitionElementConcatenate = RuleDefinitionElementConcatenate()
                ruleDefinitionElementConcatenate.ruleElements.add(this)

                if (automaticWhiteSpace)
                {
                    ruleDefinitionElementConcatenate.ruleElements.add(WHITE_SPACES)
                }

                ruleDefinitionElementConcatenate.ruleElements.addAll(ruleDefinitionElement.ruleElements)
                ruleDefinitionElementConcatenate
            }
            else                                                                                                  ->
            {
                val ruleDefinitionElementConcatenate = RuleDefinitionElementConcatenate()
                ruleDefinitionElementConcatenate.ruleElements.add(this)

                if (automaticWhiteSpace)
                {
                    ruleDefinitionElementConcatenate.ruleElements.add(WHITE_SPACES)
                }

                ruleDefinitionElementConcatenate.ruleElements.add(ruleDefinitionElement)
                ruleDefinitionElementConcatenate
            }
        }

    @GrammarRuleDefinitionDSL
    infix fun RuleDefinitionElement.I(ruleDefinitionElement: RuleDefinitionElement): RuleDefinitionElement =
        when
        {
            this is RuleDefinitionElementAlternative && ruleDefinitionElement is RuleDefinitionElementAlternative ->
            {
                val ruleDefinitionElementAlternative = RuleDefinitionElementAlternative()
                ruleDefinitionElementAlternative.ruleElements.addAll(this.ruleElements)
                ruleDefinitionElementAlternative.ruleElements.addAll(ruleDefinitionElement.ruleElements)
                ruleDefinitionElementAlternative
            }
            this is RuleDefinitionElementAlternative                                                              ->
            {
                val ruleDefinitionElementAlternative = RuleDefinitionElementAlternative()
                ruleDefinitionElementAlternative.ruleElements.addAll(this.ruleElements)
                ruleDefinitionElementAlternative.ruleElements.add(ruleDefinitionElement)
                ruleDefinitionElementAlternative
            }
            ruleDefinitionElement is RuleDefinitionElementAlternative                                             ->
            {
                val ruleDefinitionElementAlternative = RuleDefinitionElementAlternative()
                ruleDefinitionElementAlternative.ruleElements.add(this)
                ruleDefinitionElementAlternative.ruleElements.addAll(ruleDefinitionElement.ruleElements)
                ruleDefinitionElementAlternative
            }
            else                                                                                                  ->
            {
                val ruleDefinitionElementAlternative = RuleDefinitionElementAlternative()
                ruleDefinitionElementAlternative.ruleElements.add(this)
                ruleDefinitionElementAlternative.ruleElements.add(ruleDefinitionElement)
                ruleDefinitionElementAlternative
            }
        }
}

