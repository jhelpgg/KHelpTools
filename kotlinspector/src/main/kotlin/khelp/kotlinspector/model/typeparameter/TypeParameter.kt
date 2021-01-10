package khelp.kotlinspector.model.typeparameter

import khelp.grammar.GrammarNode
import khelp.kotlinspector.fillTypeParameterModifiers
import khelp.kotlinspector.model.type.Type

class TypeParameter
{
    var name = ""
        private set
    var type: Type? = null
        private set
    private val modifiers = ArrayList<TypeParameterModifier>()

    fun modifiers(): Array<TypeParameterModifier> =
        this.modifiers.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        fillTypeParameterModifiers(grammarNode, this.modifiers)
        this.name = grammarNode[2].text
        val typeNode = grammarNode[4]

        if (typeNode.numberChildren==0)
        {
            this.type = null
        }
        else
        {
            this.type = Type()
            this.type?.parse(typeNode[2])
        }
    }
}