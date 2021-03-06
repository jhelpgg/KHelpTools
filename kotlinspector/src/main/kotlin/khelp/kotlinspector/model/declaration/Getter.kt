package khelp.kotlinspector.model.declaration

import khelp.grammar.GrammarNode
import khelp.kotlinspector.fillModifiersAndReturnAnnotation
import khelp.kotlinspector.model.declaration.classinfo.FunctionBody
import khelp.kotlinspector.model.modifier.Modifier
import khelp.kotlinspector.model.type.Type

class Getter
{
    var annotation = ""
    private set
    var type : Type?=null
    private set
    var functionBody : FunctionBody?= null
    private set

    private val modifiers = ArrayList<Modifier>()

    fun modifiers() : Array<Modifier> =
        this.modifiers.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.type = null
        this.functionBody = null
        val node = grammarNode[0]
        this.annotation = fillModifiersAndReturnAnnotation(node[0], this.modifiers)

        if(node.numberChildren>3)
        {
            val typeNode = node[8]

            if(typeNode.numberChildren>0)
            {
                this.type = Type()
                this.type?.parse(typeNode[2])
            }

            this.functionBody = FunctionBody()
            this.functionBody?.parse(node[10])
        }
    }
}