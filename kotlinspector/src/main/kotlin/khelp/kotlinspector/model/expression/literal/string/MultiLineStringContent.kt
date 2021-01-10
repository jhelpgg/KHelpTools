package khelp.kotlinspector.model.expression.literal.string

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class MultiLineStringContent
{
    var multiLineStrText: MultiLineStrText? = null
        private set
    var isQuote = false
        private set
    var multiLineStrRef: MultiLineStrRef? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.multiLineStrText = null
        this.isQuote = false
        this.multiLineStrRef = null
        val node = grammarNode[0]

        when (node.rule)
        {
            KotlinGrammar.MultiLineStrText ->
            {
                this.multiLineStrText = MultiLineStrText()
                this.multiLineStrText?.parse(node)
            }
            KotlinGrammar.MultiLineStrRef  ->
            {
                this.multiLineStrRef = MultiLineStrRef()
                this.multiLineStrRef?.parse(node)
            }
            else                           -> this.isQuote = true
        }
    }
}