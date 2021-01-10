package khelp.kotlinspector.model.delegation

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class AnnotatedDelegationSpecifier
{
    lateinit var delegationSpecifier: DelegationSpecifier
        private set
    private val annotations = ArrayList<String>()

    fun annotations(): Array<String> =
        this.annotations.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.annotations.clear()
        grammarNode[0].forEachDeep(KotlinGrammar.annotation) { annotationNode -> this.annotations.add(annotationNode.text) }
        val delegationSpecifierNode = grammarNode[2][0]
        this.delegationSpecifier =
            when (delegationSpecifierNode.rule)
            {
                KotlinGrammar.constructorInvocation -> ConstructorInvocation()
                KotlinGrammar.explicitDelegation    -> ExplicitDelegation()
                KotlinGrammar.userType              -> UserType()
                KotlinGrammar.functionType          -> FunctionType()
                else                                -> throw IllegalArgumentException("No delegation specifier found")
            }

        this.delegationSpecifier.parse(delegationSpecifierNode)
    }
}