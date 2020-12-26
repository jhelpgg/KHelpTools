package khelp.grammar

@GrammarRulesDSL
class Rules
{
    internal var mainRule: String = ""
    internal val rules = HashMap<String, RuleDefinitionElement>()

    @GrammarRulesDSL
    infix fun String.IS(definitionCreator: RuleDefinition.() -> Unit)
    {
        if (this.isEmpty())
        {
            throw IllegalArgumentException("Rule definition must not be empty")
        }

        checkRuleName(this)

        if (this@Rules.rules.containsKey(this))
        {
            throw IllegalArgumentException("A rule '$this' al ready defined")
        }

        if (this@Rules.mainRule.isEmpty())
        {
            this@Rules.mainRule = this
        }

        val ruleDefinition = RuleDefinition()
        definitionCreator(ruleDefinition)
        this@Rules.rules[this] = ruleDefinition.rule
                                 ?: throw IllegalArgumentException("Rule $this have no definition. Specify it with 'rule = '")
    }
}