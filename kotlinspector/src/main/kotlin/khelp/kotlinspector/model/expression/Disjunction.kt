package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

// Separate by ||
class Disjunction
{
    private val conjunctions = ArrayList<Conjunction>()

    fun conjunctions(): Array<Conjunction> =
        this.conjunctions.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.conjunctions.clear()
        var conjunction = Conjunction()
        conjunction.parse(grammarNode[0])
        this.conjunctions.add(conjunction)

        grammarNode[2].forEachDeep(KotlinGrammar.conjunction) { conjunctionNode ->
            conjunction = Conjunction()
            conjunction.parse(conjunctionNode)
            this.conjunctions.add(conjunction)
        }
    }
}
