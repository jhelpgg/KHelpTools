package khelp.kotlinspector.model.statement

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.AssignableExpression
import khelp.kotlinspector.model.expression.DirectlyAssignableExpression
import khelp.kotlinspector.model.expression.Expression
import khelp.kotlinspector.model.expression.operator.AssignmentAndOperator

class Assignment
{
    var directlyAssignableExpression: DirectlyAssignableExpression? = null
        private set
    var assignableExpression: AssignableExpression? = null
        private set
    var assignmentAndOperator = AssignmentAndOperator.NONE
    val expression = Expression()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.directlyAssignableExpression = null
        this.assignableExpression = null
        this.assignmentAndOperator = AssignmentAndOperator.NONE
        val node = grammarNode[0]
        val expressionNode = node[0]

        when (expressionNode.rule)
        {
            KotlinGrammar.directlyAssignableExpression ->
            {
                this.directlyAssignableExpression = DirectlyAssignableExpression()
                this.directlyAssignableExpression?.parse(expressionNode)
            }
            KotlinGrammar.assignableExpression         ->
            {
                this.assignableExpression = AssignableExpression()
                this.assignableExpression?.parse(expressionNode)
                this.assignmentAndOperator = AssignmentAndOperator.parse(node[2].text)
            }
        }

        this.expression.parse(node[4])
    }
}