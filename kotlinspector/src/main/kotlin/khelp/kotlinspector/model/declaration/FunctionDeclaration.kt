package khelp.kotlinspector.model.declaration

import khelp.grammar.GrammarNode
import khelp.kotlinspector.fillFunctionValueParameters
import khelp.kotlinspector.fillModifiersAndReturnAnnotation
import khelp.kotlinspector.fillTypeConstraints
import khelp.kotlinspector.fillTypeParameters
import khelp.kotlinspector.model.declaration.classinfo.FunctionBody
import khelp.kotlinspector.model.modifier.Modifier
import khelp.kotlinspector.model.type.ReceiverType
import khelp.kotlinspector.model.type.Type
import khelp.kotlinspector.model.type.TypeConstraint
import khelp.kotlinspector.model.typeparameter.TypeParameter

class FunctionDeclaration : Declaration(DeclarationType.FUNCTION)
{
    var annotation = ""
        private set
    var receiverType: ReceiverType? = null
        private set
    var name = ""
        private set
    var type: Type? = null
        private set
    var functionBody: FunctionBody? = null
        private set

    private val modifiers = ArrayList<Modifier>()
    private val typeParameters = ArrayList<TypeParameter>()
    private val functionValueParameters = ArrayList<FunctionValueParameter>()
    private val typeConstraints = ArrayList<TypeConstraint>()

    fun modifiers(): Array<Modifier> =
        this.modifiers.toTypedArray()

    fun typeParameters(): Array<TypeParameter> =
        this.typeParameters.toTypedArray()

    fun functionValueParameters(): Array<FunctionValueParameter> =
        this.functionValueParameters.toTypedArray()

    fun typeConstraints(): Array<TypeConstraint> =
        this.typeConstraints.toTypedArray()

    override fun parse(grammarNode: GrammarNode)
    {
        this.receiverType = null
        this.type = null
        this.functionBody = null
        this.annotation = fillModifiersAndReturnAnnotation(grammarNode[0], this.modifiers)
        fillTypeParameters(grammarNode[4], this.typeParameters)
        val receiverTypeNode = grammarNode[6]

        if (receiverTypeNode.numberChildren > 0)
        {
            this.receiverType = ReceiverType()
            this.receiverType?.parse(receiverTypeNode[0])
        }

        this.name = grammarNode[8].text
        fillFunctionValueParameters(grammarNode[10], this.functionValueParameters)
        val typeNode = grammarNode[12]

        if (typeNode.numberChildren > 0)
        {
            this.type = Type()
            this.type?.parse(typeNode[0][2])
        }

        fillTypeConstraints(grammarNode[14], this.typeConstraints)
        val functionBodyNode = grammarNode[16]

        if (functionBodyNode.numberChildren > 0)
        {
            this.functionBody = FunctionBody()
            this.functionBody?.parse(functionBodyNode[0])
        }
    }
}