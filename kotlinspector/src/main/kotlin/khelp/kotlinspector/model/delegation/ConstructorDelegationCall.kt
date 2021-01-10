package khelp.kotlinspector.model.delegation

import khelp.grammar.GrammarNode
import khelp.kotlinspector.fillValueArguments
import khelp.kotlinspector.model.ThisOrSuper
import khelp.kotlinspector.model.expression.ValueArgument

class ConstructorDelegationCall
{
    var thisOrSuper = ThisOrSuper.NONE
        private set

    private val valueArguments = ArrayList<ValueArgument>()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.thisOrSuper = ThisOrSuper.parse(grammarNode[0].text)
        fillValueArguments(grammarNode[2], this.valueArguments)
    }
}