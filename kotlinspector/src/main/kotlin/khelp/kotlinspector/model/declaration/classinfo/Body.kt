package khelp.kotlinspector.model.declaration.classinfo

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.fillClassMemberDeclarations

sealed class Body
{
    private val classMemberDeclarations = ArrayList<ClassMemberDeclaration>()

    fun classMemberDeclarations(): Array<ClassMemberDeclaration> =
        this.classMemberDeclarations.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.parseSpecific(grammarNode)
        fillClassMemberDeclarations(grammarNode, this.classMemberDeclarations)
    }

    protected open fun parseSpecific(grammarNode: GrammarNode)
    {
        // Nothing to do by default
    }
}

class ClassBody : Body()

class EnumBody : Body()
{
    private val enumEntries = ArrayList<EnumEntry>()

    fun enumEntries() : Array<EnumEntry> =
        this.enumEntries.toTypedArray()

    override fun parseSpecific(grammarNode: GrammarNode)
    {
        this.enumEntries.clear()
        grammarNode[2].forEachDeep(KotlinGrammar.enumEntry) {enumEntryNode->
            val enumEntry = EnumEntry()
            enumEntry.parse(enumEntryNode)
            this.enumEntries.add(enumEntry)
        }
    }
}