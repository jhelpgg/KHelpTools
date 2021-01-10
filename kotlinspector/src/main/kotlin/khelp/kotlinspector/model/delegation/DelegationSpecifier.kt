package khelp.kotlinspector.model.delegation

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.fillValueArguments
import khelp.kotlinspector.model.expression.Expression
import khelp.kotlinspector.model.expression.ValueArgument
import khelp.kotlinspector.model.type.ReceiverType
import khelp.kotlinspector.model.type.SimpleUserType
import khelp.kotlinspector.model.type.Type
import khelp.kotlinspector.model.typeparameter.FunctionTypeParameters

sealed class DelegationSpecifier
{
    internal abstract fun parse(grammarNode: GrammarNode)
}

class ConstructorInvocation : DelegationSpecifier()
{
    val userType = UserType()

    private val valueArguments = ArrayList<ValueArgument>()

    fun valueArguments(): Array<ValueArgument> =
        this.valueArguments.toTypedArray()

    override fun parse(grammarNode: GrammarNode)
    {
        this.userType.parse(grammarNode[0])
        fillValueArguments(grammarNode[2], this.valueArguments)
    }
}

class ExplicitDelegation : DelegationSpecifier()
{
    var userType: UserType? = null
        private set
    var functionType: FunctionType? = null
        private set
    val expression = Expression()

    override fun parse(grammarNode: GrammarNode)
    {
        this.userType = null
        this.functionType = null
        val typeNode = grammarNode[0][0]

        when (typeNode.rule)
        {
            KotlinGrammar.userType     ->
            {
                this.userType = UserType()
                this.userType?.parse(typeNode)
            }
            KotlinGrammar.functionType ->
            {
                this.functionType = FunctionType()
                this.functionType?.parse(typeNode)
            }
        }

        this.expression.parse(grammarNode[4])
    }
}

class UserType : DelegationSpecifier()
{
    private val simpleUserTypes = ArrayList<SimpleUserType>()

    fun simpleUserTypes(): Array<SimpleUserType> =
        this.simpleUserTypes.toTypedArray()

    override fun parse(grammarNode: GrammarNode)
    {
        this.simpleUserTypes.clear()
        grammarNode.forEachDeep(KotlinGrammar.simpleUserType) { simpleUserTypeNode ->
            val simpleUserType = SimpleUserType()
            simpleUserType.parse(simpleUserTypeNode)
            this.simpleUserTypes.add(simpleUserType)
        }
    }
}

class FunctionType : DelegationSpecifier()
{
    var receiverType: ReceiverType? = null
        private set
    val functionTypeParameters = FunctionTypeParameters()
    val type = Type()

    override fun parse(grammarNode: GrammarNode)
    {
        this.receiverType = null
        val node = grammarNode[0]

        if (node.numberChildren > 0)
        {
            this.receiverType = ReceiverType()
            this.receiverType?.parse(node[0])
        }

        this.functionTypeParameters.parse(grammarNode[2])
        this.type.parse(grammarNode[6])
    }
}
