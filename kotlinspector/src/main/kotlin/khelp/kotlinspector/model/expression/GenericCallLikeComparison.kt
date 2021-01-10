package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode

class GenericCallLikeComparison
{
    lateinit var infixOperation:InfixOperation
    private val callSuffixArray = ArrayList<CallSuffix>()

    fun callSuffixArray() : Array<CallSuffix> =
        this.callSuffixArray.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.infixOperation = InfixOperation()
        this.infixOperation.parse(grammarNode[0])
        this.callSuffixArray.clear()

        for(callSuffixNode in grammarNode[2])
        {
            val callSuffix = CallSuffix()
            callSuffix.parse(callSuffixNode)
            this.callSuffixArray.add(callSuffix)
        }
    }
}
