package khelp.kotlinspector.model.typeparameter

import khelp.grammar.GrammarNode

class FunctionTypeParameters
{
    private val typeOrParameters = ArrayList<TypeOrParameter>()

    fun typeOrParameters(): Array<TypeOrParameter> =
        this.typeOrParameters.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.typeOrParameters.clear()
        val node = grammarNode[2]

        if (node.numberChildren > 0)
        {
            val typeOrParameter = TypeOrParameter()
            typeOrParameter.parse(node[0])
            this.typeOrParameters.add(typeOrParameter)
        }

        for (child in grammarNode[4])
        {
            val typeOrParameter = TypeOrParameter()
            typeOrParameter.parse(child[2][0])
            this.typeOrParameters.add(typeOrParameter)
        }
    }
}