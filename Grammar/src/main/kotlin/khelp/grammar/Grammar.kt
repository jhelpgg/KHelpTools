package khelp.grammar

import java.util.Stack
import java.util.TreeSet
import khelp.utilities.argumentCheck
import khelp.utilities.extensions.string
import khelp.utilities.log.information
import khelp.utilities.log.mark
import khelp.utilities.stateCheck

class Grammar(val automaticWhiteSpaces: Boolean = false, val stopAtFirstAlternativeMatch: Boolean = true)
{
    private var rules = Rules(this.automaticWhiteSpaces)

    @GrammarRulesDSL
    fun rules(rulesCreator: Rules.() -> Unit)
    {
        this.rules = Rules(this.automaticWhiteSpaces)
        rulesCreator(this.rules)
        argumentCheck(this.rules.rules.isNotEmpty()) { "Must define at least one rule !" }
        argumentCheck(this.rules.mainRule in rules.rules) { "Main rule '${rules.mainRule}' is not defined in given rules" }
        this.checkIfAllReferenceHaveADefinition()
    }

    fun parse(text: String): GrammarNode?
    {
        stateCheck(this.rules.mainRule.isNotEmpty()) { "Rules must be defined before call this method" }

        val ruleMatcher = RuleMatcher(StringPositionReader(text),
                                      this.rules.mainRule,
                                      this.rules,
                                      stopAtFirstAlternativeMatch = this.stopAtFirstAlternativeMatch)
        var grammarNode = ruleMatcher.find()

        while (grammarNode != null && !ruleMatcher.finished)
        {
            mark("FOUND")
            information(grammarNode.toString())

            grammarNode = ruleMatcher.find()
        }

        if (!ruleMatcher.finished)
        {
            return null
        }

        return grammarNode
    }

    fun parseSpecificRule(text: String, ruleName: String): GrammarNode?
    {
        argumentCheck(this.rules.rules.containsKey(ruleName)) { "$ruleName not defined in grammar" }

        val ruleMatcher = RuleMatcher(StringPositionReader(text),
                                      ruleName,
                                      this.rules,
                                      stopAtFirstAlternativeMatch = this.stopAtFirstAlternativeMatch)
        val grammarNode = ruleMatcher.find()

        return if (ruleMatcher.finished)
        {
            grammarNode
        }
        else
        {
            null
        }
    }

    private fun checkIfAllReferenceHaveADefinition()
    {
        val missingDefinitions = TreeSet<String>()
        val stack = Stack<RuleDefinitionElement>()

        for (ruleDefinitionElement in rules.rules.values)
        {
            stack.push(ruleDefinitionElement)
        }

        var ruleDefinitionElement: RuleDefinitionElement

        while (stack.isNotEmpty())
        {
            ruleDefinitionElement = stack.pop()

            when (ruleDefinitionElement)
            {
                is RuleDefinitionReference           ->
                    if (ruleDefinitionElement.ruleName !in this.rules.rules)
                    {
                        missingDefinitions += ruleDefinitionElement.ruleName
                    }
                is RuleDefinitionRegularExpression   -> Unit
                is RuleDefinitionReferenceZeroOrMore ->
                    if (ruleDefinitionElement.ruleName !in this.rules.rules)
                    {
                        missingDefinitions += ruleDefinitionElement.ruleName
                    }
                is RuleDefinitionReferenceOneOrMore  ->
                    if (ruleDefinitionElement.ruleName !in this.rules.rules)
                    {
                        missingDefinitions += ruleDefinitionElement.ruleName
                    }
                is RuleDefinitionReferenceZeroOrOne  ->
                    if (ruleDefinitionElement.ruleName !in this.rules.rules)
                    {
                        missingDefinitions += ruleDefinitionElement.ruleName
                    }
                is RuleDefinitionReferenceExactTimes ->
                    if (ruleDefinitionElement.ruleName !in this.rules.rules)
                    {
                        missingDefinitions += ruleDefinitionElement.ruleName
                    }
                is RuleDefinitionReferenceAtLeast    ->
                    if (ruleDefinitionElement.ruleName !in this.rules.rules)
                    {
                        missingDefinitions += ruleDefinitionElement.ruleName
                    }
                is RuleDefinitionReferenceAtMost     ->
                    if (ruleDefinitionElement.ruleName !in this.rules.rules)
                    {
                        missingDefinitions += ruleDefinitionElement.ruleName
                    }
                is RuleDefinitionReferenceBetween    ->
                    if (ruleDefinitionElement.ruleName !in this.rules.rules)
                    {
                        missingDefinitions += ruleDefinitionElement.ruleName
                    }
                is RuleDefinitionElementConcatenate  ->
                    for (ruleElement in ruleDefinitionElement.ruleElements)
                    {
                        stack.push(ruleElement)
                    }
                is RuleDefinitionElementAlternative  ->
                    for (ruleElement in ruleDefinitionElement.ruleElements)
                    {
                        stack.push(ruleElement)
                    }
                else                                 -> Unit
            }
        }

        argumentCheck(missingDefinitions.isEmpty()) {
            "Missing definition for : ${
                missingDefinitions.toTypedArray()
                    .string()
            }"
        }
    }
}
