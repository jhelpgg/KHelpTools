package khelp.kotlinspector.model.typeparameter

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class TypeParameterModifier
{
    var isReified = false
        private set
    var variance = VarianceModifier.NONE
        private set
    var annotation = ""
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.isReified = grammarNode.searchByFirstHeap(KotlinGrammar.reificationModifier) != null

        this.variance =
            when (grammarNode.searchByFirstHeap(KotlinGrammar.varianceModifier)?.text)
            {
                "in"  -> VarianceModifier.IN
                "out" -> VarianceModifier.OUT
                else  -> VarianceModifier.NONE
            }

        this.annotation = grammarNode.searchByFirstHeap(KotlinGrammar.annotation)?.text ?: ""
    }
}