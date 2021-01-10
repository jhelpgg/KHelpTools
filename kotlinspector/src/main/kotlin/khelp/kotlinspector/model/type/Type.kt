package khelp.kotlinspector.model.type

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.fillTypeModifiers
import khelp.kotlinspector.model.delegation.FunctionType

class Type
{
    var parenthesizedType: ParenthesizedType? = null
        private set
    var nullableType: NullableType? = null
        private set
    var typeReference: TypeReference? = null
        private set
    var functionType: FunctionType? = null
        private set

    private val typeModifiers = ArrayList<TypeModifier>()

    fun typeModifiers(): Array<TypeModifier> =
        this.typeModifiers.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.parenthesizedType = null
        this.nullableType = null
        this.typeReference = null
        this.functionType = null
        fillTypeModifiers(grammarNode[0], this.typeModifiers)
        val node = grammarNode[2][0]

        when (node.rule)
        {
            KotlinGrammar.parenthesizedType ->
            {
                this.parenthesizedType = ParenthesizedType()
                this.parenthesizedType?.parse(node)
            }
            KotlinGrammar.nullableType      ->
            {
                this.nullableType = NullableType()
                this.nullableType?.parse(node)
            }
            KotlinGrammar.typeReference     ->
            {
                this.typeReference = TypeReference()
                this.typeReference?.parse(node)
            }
            KotlinGrammar.functionType      ->
            {
                this.functionType = FunctionType()
                this.functionType?.parse(node)
            }
        }
    }
}