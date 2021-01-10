package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.type.TypeProjection

class TypeArguments
{
    private val typeProjections = ArrayList<TypeProjection>()

    fun typeProjections(): Array<TypeProjection> =
        this.typeProjections.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.typeProjections.clear()
        grammarNode.forEachDeep(KotlinGrammar.typeProjection) { typeProjectionNode ->
            val typeProjection = TypeProjection()
            typeProjection.parse(typeProjectionNode)
            this.typeProjections.add(typeProjection)
        }
    }
}