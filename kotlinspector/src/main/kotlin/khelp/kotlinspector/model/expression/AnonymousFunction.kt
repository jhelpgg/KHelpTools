package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.fillParameterWithOptionalTypes
import khelp.kotlinspector.fillTypeConstraints
import khelp.kotlinspector.model.declaration.ParameterWithOptionalType
import khelp.kotlinspector.model.declaration.classinfo.FunctionBody
import khelp.kotlinspector.model.type.Type
import khelp.kotlinspector.model.type.TypeConstraint

class AnonymousFunction
{
    var returnType: Type? = null
        private set
    var functionBody: FunctionBody? = null
        private set

    private val parametersTypes = ArrayList<Type>()
    private val parametersWithOptionalType = ArrayList<ParameterWithOptionalType>()
    private val typeConstraints = ArrayList<TypeConstraint>()

    fun parametersTypes(): Array<Type> =
        parametersTypes.toTypedArray()

    fun parametersWithOptionalType(): Array<ParameterWithOptionalType> =
        this.parametersWithOptionalType.toTypedArray()

    fun typeConstraints(): Array<TypeConstraint> =
        this.typeConstraints.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.returnType = null
        this.functionBody = null
        this.parametersTypes.clear()
        this.parametersWithOptionalType.clear()
        this.typeConstraints.clear()

        grammarNode[2].forEachDeep(KotlinGrammar.type) { typeNode ->
            val type = Type()
            type.parse(typeNode)
            this.parametersTypes.add(type)
        }

        fillParameterWithOptionalTypes(grammarNode[4], this.parametersWithOptionalType)
        val returnTypeNode = grammarNode[6]

        if (returnTypeNode.numberChildren > 0)
        {
            this.returnType = Type()
            this.returnType?.parse(returnTypeNode[0][2])
        }

        fillTypeConstraints(grammarNode[8], this.typeConstraints)
        val functionBodyNode = grammarNode[10]

        if (functionBodyNode.numberChildren > 0)
        {
            this.functionBody = FunctionBody()
            this.functionBody?.parse(functionBodyNode[0])
        }
    }
}