package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.expression.literal.lambda.LambdaLiteral

class AnnotatedLambda
{
    var annotation = ""
        private set
    var label = ""
        private set
    val lambdaLiteral = LambdaLiteral()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.annotation = grammarNode[0].text
        this.label = grammarNode[2].text
        this.lambdaLiteral.parse(grammarNode[4])
    }
}