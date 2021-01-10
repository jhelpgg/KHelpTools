package khelp.kotlinspector.model.declaration.classinfo

import khelp.grammar.GrammarNode
import khelp.kotlinspector.fillModifiersAndReturnAnnotation
import khelp.kotlinspector.model.ValOrVar
import khelp.kotlinspector.model.expression.Expression
import khelp.kotlinspector.model.modifier.Modifier
import khelp.kotlinspector.model.parseValOrVar
import khelp.kotlinspector.model.type.Type

class ClassParameter
{
    var annotation = ""
        private set
    var valOrVar = ValOrVar.NONE
        private set
    var name = ""
        private set
    val type = Type()
    var defaultValue: Expression? = null
        private set
    private val modifiers = ArrayList<Modifier>()
    fun modifiers(): Array<Modifier> =
        this.modifiers.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.annotation = fillModifiersAndReturnAnnotation(grammarNode, this.modifiers)
        this.valOrVar = parseValOrVar(grammarNode[2].text)
        this.name = grammarNode[4].text
        this.type.parse(grammarNode[8])
        val defaultValueNode = grammarNode[10]

        if (defaultValueNode.numberChildren > 0)
        {
            this.defaultValue = Expression()
            this.defaultValue?.parse(defaultValueNode[2])
        }
        else
        {
            this.defaultValue = null
        }
    }
}