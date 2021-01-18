package khelp.kotlinspector.model.expression.`when`

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class WhenExpression
{
    var whenSubject: WhenSubject? = null
        private set

    private val whenEntries = ArrayList<WhenEntry>()

    fun whenEntries(): Array<WhenEntry> =
        this.whenEntries.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.whenSubject = null
        this.whenEntries.clear()
        val whenSubjectNode = grammarNode[2]

        if (whenSubjectNode.numberChildren > 0)
        {
            this.whenSubject = WhenSubject()
            this.whenSubject?.parse(whenSubjectNode[0])
        }

        grammarNode[6].forEachDeep(KotlinGrammar.whenEntry) { whenEntryNode ->
            val whenEntry = WhenEntry()
            whenEntry.parse(whenEntryNode)
            this.whenEntries.add(whenEntry)
        }
    }
}