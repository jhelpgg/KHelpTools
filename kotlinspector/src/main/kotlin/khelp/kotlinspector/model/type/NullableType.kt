package khelp.kotlinspector.model.type

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class NullableType
{
    var typeReference : TypeReference? = null
    private set
    var parenthesizedType : ParenthesizedType? = null
    private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.typeReference = null
        this.parenthesizedType = null
        val node = grammarNode[0][0]

        when(node.rule)
        {
            KotlinGrammar.typeReference ->
            {
                this.typeReference = TypeReference()
                this.typeReference?.parse(node)
            }
            KotlinGrammar.parenthesizedType ->
            {
                this.parenthesizedType = ParenthesizedType()
                this.parenthesizedType?.parse(node)
            }
        }
    }
}