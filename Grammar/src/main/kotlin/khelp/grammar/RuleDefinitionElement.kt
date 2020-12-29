package khelp.grammar

import khelp.utilities.regex.RegularExpression

sealed class RuleDefinitionElement

class RuleDefinitionReference(val ruleName: String) : RuleDefinitionElement()

class RuleDefinitionRegularExpression(val regularExpression: RegularExpression) : RuleDefinitionElement()

class RuleDefinitionReferenceZeroOrMore(val ruleName: String) : RuleDefinitionElement()

class RuleDefinitionReferenceOneOrMore(val ruleName: String) : RuleDefinitionElement()

class RuleDefinitionReferenceZeroOrOne(val ruleName: String) : RuleDefinitionElement()

class RuleDefinitionReferenceExactTimes(val ruleName: String, val numberTimes: Int) : RuleDefinitionElement()

class RuleDefinitionReferenceAtLeast(val ruleName: String, val minimumTimes: Int) : RuleDefinitionElement()

class RuleDefinitionReferenceAtMost(val ruleName: String, val maximumTimes: Int) : RuleDefinitionElement()

class RuleDefinitionReferenceBetween(val ruleName: String,
                                     val minimumTimes: Int,
                                     val maximumTimes: Int) : RuleDefinitionElement()

class RuleDefinitionElementZeroOrMore(val ruleDefinitionElement: RuleDefinitionElement) : RuleDefinitionElement()

class RuleDefinitionElementOneOrMore(val ruleDefinitionElement: RuleDefinitionElement) : RuleDefinitionElement()

class RuleDefinitionElementZeroOrOne(val ruleDefinitionElement: RuleDefinitionElement) : RuleDefinitionElement()

class RuleDefinitionElementExactTimes(val ruleDefinitionElement: RuleDefinitionElement,
                                      val numberTimes: Int) : RuleDefinitionElement()

class RuleDefinitionElementAtLeast(val ruleDefinitionElement: RuleDefinitionElement,
                                   val minimumTimes: Int) : RuleDefinitionElement()

class RuleDefinitionElementAtMost(val ruleDefinitionElement: RuleDefinitionElement,
                                  val maximumTimes: Int) : RuleDefinitionElement()

class RuleDefinitionElementBetween(val ruleDefinitionElement: RuleDefinitionElement,
                                   val minimumTimes: Int,
                                   val maximumTimes: Int) : RuleDefinitionElement()

class RuleDefinitionElementConcatenate() : RuleDefinitionElement()
{
    val ruleElements = ArrayList<RuleDefinitionElement>()
}

class RuleDefinitionElementAlternative() : RuleDefinitionElement()
{
    val ruleElements = ArrayList<RuleDefinitionElement>()
}

