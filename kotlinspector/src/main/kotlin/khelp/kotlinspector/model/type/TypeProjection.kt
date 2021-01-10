package khelp.kotlinspector.model.type

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class TypeProjection
{
    var isStar = false
    private set
    var type : Type?=null
    private set

    private val typeProjectionModifiers = ArrayList<TypeProjectionModifier>()

    fun typeProjectionModifiers() : Array<TypeProjectionModifier> =
        this.typeProjectionModifiers.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.isStar = false
        this.type = null
        this.typeProjectionModifiers.clear()
        val node = grammarNode[0]

        if(node.text=="*")
        {
            this.isStar = true
        }
        else
        {
           node[0].forEachDeep(KotlinGrammar.typeProjectionModifier) { typeProjectionModifierNode->
               val typeProjectionModifier = TypeProjectionModifier()
               typeProjectionModifier.parse(typeProjectionModifierNode)
               this.typeProjectionModifiers.add(typeProjectionModifier)
           }

            this.type = Type()
            this.type?.parse(node[2])
        }
    }
}