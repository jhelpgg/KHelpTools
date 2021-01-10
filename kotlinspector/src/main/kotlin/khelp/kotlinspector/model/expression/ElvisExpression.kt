package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class ElvisExpression
{
    private val infixFunctionCalls = ArrayList<InfixFunctionCall>()

    fun infixFunctionCalls(): Array<InfixFunctionCall> =
        this.infixFunctionCalls.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.infixFunctionCalls.clear()
        grammarNode.forEachDeep(KotlinGrammar.infixFunctionCall) { infixFunctionCallNode ->
            val infixFunctionCall = InfixFunctionCall()
            infixFunctionCall.parse(infixFunctionCallNode)
            this.infixFunctionCalls.add(infixFunctionCall)
        }
    }
}
