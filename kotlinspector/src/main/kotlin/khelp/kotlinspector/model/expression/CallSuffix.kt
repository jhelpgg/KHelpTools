package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class CallSuffix
{
    var typeArguments: TypeArguments? = null
        private set
    var annotatedLambda: AnnotatedLambda? = null
        private set

    private val valueArguments = ArrayList<ValueArgument>()

    fun valueArguments(): Array<ValueArgument> =
        this.valueArguments.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.typeArguments = null
        this.annotatedLambda = null
        this.valueArguments.clear()
        val node = grammarNode[0]
        val typeArgumentsNode = node[0]

        if (typeArgumentsNode.numberChildren > 0)
        {
            this.typeArguments = TypeArguments()
            this.typeArguments?.parse(typeArgumentsNode)
        }

        node[2].forEachDeep(KotlinGrammar.valueArgument) { valueArgumentNode ->
            val valueArgument = ValueArgument()
            valueArgument.parse(valueArgumentNode)
            this.valueArguments.add(valueArgument)
        }

        if (node.numberChildren > 3)
        {
            this.annotatedLambda = AnnotatedLambda()
            this.annotatedLambda?.parse(node[4])
        }
    }
}
