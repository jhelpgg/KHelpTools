package khelp.kotlinspector.model.declaration.classinfo

import khelp.grammar.GrammarNode
import khelp.kotlinspector.fillModifiersAndReturnAnnotation
import khelp.kotlinspector.fillValueArguments
import khelp.kotlinspector.model.expression.ValueArgument
import khelp.kotlinspector.model.modifier.Modifier

class EnumEntry
{
    var annotation = ""
        private set
    var name = ""
        private set
    var classBody: ClassBody? = null
        private set

    private val modifiers = ArrayList<Modifier>()
    private val valueArguments = ArrayList<ValueArgument>()

    fun modifiers(): Array<Modifier> =
        this.modifiers.toTypedArray()

    fun valueArguments(): Array<ValueArgument> =
        this.valueArguments.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.annotation = fillModifiersAndReturnAnnotation(grammarNode[0], this.modifiers)
        this.name = grammarNode[2].text
        fillValueArguments(grammarNode[4], this.valueArguments)
        val classBodyNode = grammarNode[6]

        if (classBodyNode.numberChildren > 0)
        {
            this.classBody = ClassBody()
            this.classBody?.parse(classBodyNode[0])
        }
    }
}