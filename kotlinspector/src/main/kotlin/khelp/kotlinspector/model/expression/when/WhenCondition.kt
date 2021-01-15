package khelp.kotlinspector.model.expression.`when`

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.Expression
import khelp.kotlinspector.model.expression.test.RangeTest
import khelp.kotlinspector.model.expression.test.TypeTest

class WhenCondition
{
    var expression : Expression?= null
    private set
    var rangeTest : RangeTest?=null
    private set
    var typeTest : TypeTest?=null
        private set

    internal fun parse(grammarNode: GrammarNode) {
        this.expression = null
        this.rangeTest = null
        this.typeTest = null
        val node = grammarNode[0]

        when(node.rule)
        {
            KotlinGrammar.expression, KotlinGrammar.disjunction->
            {
                this.expression = Expression()
                this.expression?.parse(node)
            }
            KotlinGrammar.rangeTest->
            {
                this.rangeTest = RangeTest()
                this.rangeTest?.parse(node)
            }
            KotlinGrammar.typeTest->
            {
                this.typeTest = TypeTest()
                this.typeTest?.parse(node)
            }
        }
    }
}