package khelp.kotlinspector.model.declaration

import khelp.grammar.GrammarNode
import khelp.kotlinspector.fillModifiersAndReturnAnnotation
import khelp.kotlinspector.fillTypeParameters
import khelp.kotlinspector.model.modifier.Modifier
import khelp.kotlinspector.model.type.Type
import khelp.kotlinspector.model.typeparameter.TypeParameter

class TypeAliasDeclaration : Declaration(DeclarationType.TYPE_ALIAS)
{
    var annotation = ""
        private set
    var simpleIdentifier = ""
        private set
    val type = Type()

    private val modifiers = ArrayList<Modifier>()
    private val typeParameters = ArrayList<TypeParameter>()

    fun modifiers(): Array<Modifier> =
        this.modifiers.toTypedArray()

    fun typeParameters(): Array<TypeParameter> =
        this.typeParameters.toTypedArray()

    override fun parse(grammarNode: GrammarNode)
    {
        this.annotation = fillModifiersAndReturnAnnotation(grammarNode[0], this.modifiers)
        this.simpleIdentifier = grammarNode[4].text
        fillTypeParameters(grammarNode[6], this.typeParameters)
        this.type.parse(grammarNode[10])
    }
}