package khelp.grammar

import khelp.utilities.argumentCheck

@GrammarRulesDSL
class Rules(val automaticWhiteSpaces: Boolean)
{
    internal var mainRule: String = ""
    internal val rules = HashMap<String, RuleDefinitionElement>()

    @GrammarRulesDSL
    infix fun String.IS(definitionCreator: RuleDefinition.() -> Unit)
    {
        argumentCheck(this.isNotEmpty()) { "Rule definition must not be empty" }
        checkRuleName(this)
        argumentCheck(!this@Rules.rules.containsKey(this)) { "A rule '$this' already defined" }

        if (this@Rules.mainRule.isEmpty())
        {
            this@Rules.mainRule = this
        }

        val ruleDefinition = RuleDefinition(automaticWhiteSpaces)
        definitionCreator(ruleDefinition)
        this@Rules.rules[this] = ruleDefinition.rule
                                 ?: throw IllegalArgumentException("Rule $this have no definition. Specify it with 'rule = '")
    }
}