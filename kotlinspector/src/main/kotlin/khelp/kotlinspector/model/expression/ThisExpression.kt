package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class ThisExpression
{
    var atIdentifier = ""
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.atIdentifier = ""
        val node = grammarNode[0]

        if (node.rule == KotlinGrammar.THIS_AT)
        {
            this.atIdentifier = node[0][2].text
        }
    }
}