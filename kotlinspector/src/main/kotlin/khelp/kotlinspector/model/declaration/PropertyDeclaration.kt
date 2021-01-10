package khelp.kotlinspector.model.declaration

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.fillModifiersAndReturnAnnotation
import khelp.kotlinspector.fillTypeConstraints
import khelp.kotlinspector.fillTypeParameters
import khelp.kotlinspector.fillVariableDeclarations
import khelp.kotlinspector.model.ValOrVar
import khelp.kotlinspector.model.expression.Expression
import khelp.kotlinspector.model.modifier.Modifier
import khelp.kotlinspector.model.parseValOrVar
import khelp.kotlinspector.model.type.ReceiverType
import khelp.kotlinspector.model.type.TypeConstraint
import khelp.kotlinspector.model.typeparameter.TypeParameter

class PropertyDeclaration : Declaration(DeclarationType.PROPERTY)
{
    var annotation = ""
        private set
    var valOrVar = ValOrVar.NONE
        private set
    var receiverType: ReceiverType? = null
        private set
    var byDelegation = false
        private set
    var value: Expression? = null
        private set
    var getter: Getter? = null
        private set
    var setter: Setter? = null
        private set

    private val modifiers = ArrayList<Modifier>()
    private val typeParameters = ArrayList<TypeParameter>()
    private val variableDeclarations = ArrayList<VariableDeclaration>()
    private val typeConstraints = ArrayList<TypeConstraint>()

    fun modifiers(): Array<Modifier> =
        this.modifiers.toTypedArray()

    fun typeParameters(): Array<TypeParameter> =
        this.typeParameters.toTypedArray()

    fun variableDeclarations(): Array<VariableDeclaration> =
        this.variableDeclarations.toTypedArray()

    fun typeConstraints(): Array<TypeConstraint> =
        this.typeConstraints.toTypedArray()

    override fun parse(grammarNode: GrammarNode)
    {
        this.receiverType = null
        this.value = null
        this.byDelegation = false
        this.annotation = fillModifiersAndReturnAnnotation(grammarNode[0], this.modifiers)
        this.valOrVar = parseValOrVar(grammarNode[2].text)
        fillTypeParameters(grammarNode[4], this.typeParameters)
        val receiverTypeNode = grammarNode[6]

        if (receiverTypeNode.numberChildren > 0)
        {
            this.receiverType = ReceiverType()
            this.receiverType?.parse(receiverTypeNode[0])
        }

        fillVariableDeclarations(grammarNode[8], this.variableDeclarations)
        fillTypeConstraints(grammarNode[10], this.typeConstraints)
        val valueNode = grammarNode[12]

        if (valueNode.numberChildren > 0)
        {
            if (valueNode[0].rule == KotlinGrammar.propertyDelegate)
            {
                this.byDelegation = true
                this.value = Expression()
                this.value?.parse(valueNode[0][2])
            }
            else
            {
                this.byDelegation = false
                this.value = Expression()
                this.value?.parse(valueNode[0][0][2])
            }
        }

        this.getter = grammarNode.searchByFirstHeap(KotlinGrammar.getter)
            ?.let { getterNode ->
                val getter = Getter()
                getter.parse(getterNode)
                getter
            }

        this.setter = grammarNode.searchByFirstHeap(KotlinGrammar.setter)
            ?.let { setterNode ->
                val setter = Setter()
                setter.parse(setterNode)
                setter
            }
    }
}