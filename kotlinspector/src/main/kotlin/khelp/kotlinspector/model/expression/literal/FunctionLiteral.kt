package khelp.kotlinspector.model.expression.literal

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.AnonymousFunction
import khelp.kotlinspector.model.expression.literal.lambda.LambdaLiteral

class FunctionLiteral
{
    var lambdaLiteral: LambdaLiteral? = null
        private set
    var anonymousFunction: AnonymousFunction? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        val node = grammarNode[0]

        if (node.rule == KotlinGrammar.lambdaLiteral)
        {
            this.lambdaLiteral = LambdaLiteral()
            this.lambdaLiteral?.parse(node)
        }
        else
        {
            this.anonymousFunction = AnonymousFunction()
            this.anonymousFunction?.parse(node)
        }
    }
}