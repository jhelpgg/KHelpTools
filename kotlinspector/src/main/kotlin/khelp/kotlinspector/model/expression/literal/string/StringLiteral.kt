package khelp.kotlinspector.model.expression.literal.string

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class StringLiteral
{
    var lineStringLiteral: LineStringLiteral? = null
        private set
    var multiLineStringLiteral: MultiLineStringLiteral? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.lineStringLiteral = null
        this.multiLineStringLiteral = null
        val node = grammarNode[0]

        if (node.rule == KotlinGrammar.lineStringLiteral)
        {
            this.lineStringLiteral = LineStringLiteral()
            this.lineStringLiteral?.parse(node)
        }
        else
        {
            this.multiLineStringLiteral = MultiLineStringLiteral()
            this.multiLineStringLiteral?.parse(node)
        }
    }
}