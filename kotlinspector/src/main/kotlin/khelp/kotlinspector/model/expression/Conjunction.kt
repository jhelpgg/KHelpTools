package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

// Separate by &&
class Conjunction
{
    private val equalities = ArrayList<Equality>()

    fun equalities(): Array<Equality> =
        this.equalities.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.equalities.clear()
        var equality = Equality()
        equality.parse(grammarNode[0])
        this.equalities.add(equality)

        grammarNode[2].forEachDeep(KotlinGrammar.equality) { equalityNode ->
            equality = Equality()
            equality.parse(equalityNode)
            this.equalities.add(equality)
        }
    }
}
