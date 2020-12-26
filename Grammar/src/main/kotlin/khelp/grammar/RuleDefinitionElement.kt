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

class RuleDefinitionReferenceBetween(val ruleName: String, val minimumTimes: Int, val maximumTimes: Int) :
    RuleDefinitionElement()

class RuleDefinitionElementConcatenate() : RuleDefinitionElement()
{
    val ruleElements = ArrayList<RuleDefinitionElement>()
}

class RuleDefinitionElementAlternative() : RuleDefinitionElement()
{
    val ruleElements = ArrayList<RuleDefinitionElement>()
}

