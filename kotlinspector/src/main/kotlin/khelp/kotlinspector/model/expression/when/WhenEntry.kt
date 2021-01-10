package khelp.kotlinspector.model.expression.`when`

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.statement.ControlStructureBody

class WhenEntry
{
    val controlStructureBody = ControlStructureBody()
    private val whenConditions = ArrayList<WhenCondition>()

    /**
     * Empty means a else condition
     */
    fun whenConditions(): Array<WhenCondition> =
        this.whenConditions.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.whenConditions.clear()
        val node = grammarNode[0]
        val controlStructureBodyIndex =
            if (node.numberChildren > 7)
            {
                var whenCondition = WhenCondition()
                whenCondition.parse(node[0])
                this.whenConditions.add(whenCondition)

                node[2].forEachDeep(KotlinGrammar.whenCondition) { whenConditionNode ->
                    whenCondition = WhenCondition()
                    whenCondition.parse(whenConditionNode)
                    this.whenConditions.add(whenCondition)
                }

                8
            }
            else
            {
                4
            }
        this.controlStructureBody.parse(node[controlStructureBodyIndex])
    }
}