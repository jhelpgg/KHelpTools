package khelp.kotlinspector.model.type

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.typeparameter.VarianceModifier

class TypeProjectionModifier
{
    var varianceModifier = VarianceModifier.NONE
        private set
    var annotation = ""
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.varianceModifier = VarianceModifier.NONE
        this.annotation = ""
        val node = grammarNode[0]

        when (node.rule)
        {
            KotlinGrammar.varianceModifier -> this.varianceModifier = VarianceModifier.parse(node[0].text)
            KotlinGrammar.annotation       -> this.annotation = node[0].text
        }
    }
}