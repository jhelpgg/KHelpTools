package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class AssignableSuffix
{
    var typeArguments: TypeArguments? = null
        private set
    var indexingSuffix: IndexingSuffix? = null
        private set
    var navigationSuffix: NavigationSuffix? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.typeArguments = null
        this.indexingSuffix = null
        this.navigationSuffix = null
        val node = grammarNode[0]

        when (node.rule)
        {
            KotlinGrammar.typeArguments    ->
            {
                this.typeArguments = TypeArguments()
                this.typeArguments?.parse(node)
            }
            KotlinGrammar.indexingSuffix   ->
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