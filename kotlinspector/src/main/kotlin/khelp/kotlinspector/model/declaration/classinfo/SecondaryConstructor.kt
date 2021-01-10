package khelp.kotlinspector.model.declaration.classinfo

import khelp.grammar.GrammarNode
import khelp.kotlinspector.fillFunctionValueParameters
import khelp.kotlinspector.fillModifiersAndReturnAnnotation
import khelp.kotlinspector.model.declaration.FunctionValueParameter
import khelp.kotlinspector.model.delegation.ConstructorDelegationCall
import khelp.kotlinspector.model.modifier.Modifier
import khelp.kotlinspector.model.statement.Block

class SecondaryConstructor
{
    var annotation = ""
        private set
    var constructorDelegationCall: ConstructorDelegationCall? = null
        private set
    var block: Block? = null
        private set

    private val modifiers = ArrayList<Modifier>()
    private val functionValueParameters = ArrayList<FunctionValueParameter>()

    fun modifiers(): Array<Modifier> =
        this.modifiers.toTypedArray()

    fun functionValueParameters(): Array<FunctionValueParameter> =
        this.functionValueParameters.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.constructorDelegationCall = null
        this.block = null
        this.annotation = fillModifiersAndReturnAnnotation(grammarNode[0], this.modifiers)
        fillFunctionValueParameters(grammarNode[4], this.functionValueParameters)
        val constructorDelegationCallNode = grammarNode[6]

        if (constructorDelegationCallNode.numberChildren > 0)
        {
            this.constructorDelegationCall = ConstructorDelegationCall()
            this.constructorDelegationCall?.parse(constructorDelegationCallNode[2])
        }

        val blockNode = grammarNode[8]

        if (blockNode.numberChildren > 0)
        {
            this.block = Block()
            this.block?.parse(blockNode[0])
        }
    }
}