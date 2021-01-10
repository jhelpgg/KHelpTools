package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.InOperator
import khelp.kotlinspector.model.expression.operator.IsOperator
import khelp.kotlinspector.model.type.Type

class InfixOperation
{
    val elvisExpression = ElvisExpression()
    var inOperator = InOperator.NONE
        private set
    var inElvisPression: ElvisExpression? = null
        private set
    var isOperator = IsOperator.NONE
        private set
    var isType: Type? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.inOperator = InOperator.NONE
        this.inElvisPression = null
        this.isOperator = IsOperator.NONE
        this.isType = null
        this.elvisExpression.parse(grammarNode[0])
        val nodeInIsParent = grammarNode[2]

        if (nodeInIsParent.numberChildren > 0)
        {
            val nodeInIs = nodeInIsParent[0]

            when (nodeInIs.rule)
            {
                KotlinGrammar.inOperator ->
                {
                    this.inOperator = InOperator.parse(nodeInIs.text)
                    this.inElvisPression = ElvisExpression()
                    this.inElvisPression?.parse(nodeInIsParent[2])
                }
                KotlinGrammar.isOperator ->
                {
                    this.isOperator = IsOperator.parse(nodeInIs.text)
                    this.isType = Type()
                    this.isType?.parse(nodeInIsParent[2])
                }
            }
        }
    }
}
