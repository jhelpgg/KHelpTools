package khelp.kotlinspector.model.expression.literal.lambda

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.fillStatements
import khelp.kotlinspector.model.statement.Statement

class LambdaLiteral
{
    private val lambdaParameters = ArrayList<LambdaParameter>()
    private val statements = ArrayList<Statement>()

    fun lambdaParameters(): Array<LambdaParameter> =
        this.lambdaParameters.toTypedArray()

    fun statements(): Array<Statement> =
        this.statements.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        val node = grammarNode[0][0]
        this.lambdaParameters.clear()
        this.statements.clear()

        val statementsIndex =
            if (node.numberChildren > 5)
            {
                node[2].forEachDeep(KotlinGrammar.lambdaParameter) { lambdaParameterNode ->
                    val lambdaParameter = LambdaParameter()
                    lambdaParameter.parse(lambdaParameterNode)
                    this.lambdaParameters.add(lambdaParameter)
                }

                6
            }
            else
            {
                2
            }

        fillStatements(node[statementsIndex], this.statements)
    }
}