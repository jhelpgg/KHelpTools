package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.PostfixUnaryOperator

class PostFixUnarySuffix
{
    var postfixUnaryOperator = PostfixUnaryOperator.NONE
    private set
    var typeArguments : TypeArguments? = null
    private set
    var callSuffix : CallSuffix? = null
    private set
    var indexingSuffix : IndexingSuffix? = null
    private set
    var navigationSuffix : NavigationSuffix? = null
    private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.postfixUnaryOperator = PostfixUnaryOperator.NONE
        this.typeArguments=null
        this.callSuffix=null
        this.indexingSuffix=null
        this.navigationSuffix=null
        val node = grammarNode[0]

        when(node.rule)
        {
            KotlinGrammar.postfixUnaryOperator -> this.postfixUnaryOperator = PostfixUnaryOperator.parse(node.text)
            KotlinGrammar.typeArguments ->
            {
                this.typeArguments = TypeArguments()
                this.typeArguments?.parse(node)
            }
            KotlinGrammar.callSuffix ->
            {
                this.callSuffix = CallSuffix()
                this.callSuffix?.parse(node)
            }
            KotlinGrammar.indexingSuffix ->
            {
                this.indexingSuffix = IndexingSuffix()
                this.indexingSuffix?.parse(node)
            }
            KotlinGrammar.navigationSuffix ->
            {
                this.navigationSuffix = NavigationSuffix()
                this.navigationSuffix?.parse(node)
            }
        }
    }
}
