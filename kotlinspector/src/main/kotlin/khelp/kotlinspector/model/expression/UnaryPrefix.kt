package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.PrefixUnaryOperator

class UnaryPrefix
{
    var annotation = ""
        private set
    var label = ""
        private set
    var prefixUnaryOperator = PrefixUnaryOperator.NONE
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.annotation = ""
        this.label = ""
        this.prefixUnaryOperator = PrefixUnaryOperator.NONE
        val node = grammarNode[0]

        when (node.rule)
        {
            KotlinGrammar.annotation          -> this.annotation = node.text
            KotlinGrammar.label               -> this.label = node.text
            KotlinGrammar.prefixUnaryOperator -> this.prefixUnaryOperator = PrefixUnaryOperator.parse(node.text)
        }
    }
}
