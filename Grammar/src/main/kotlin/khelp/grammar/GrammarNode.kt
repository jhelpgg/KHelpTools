package khelp.grammar

class GrammarNode internal constructor(val rule: String, val text: String) : Iterable<GrammarNode>
{
    internal val children = ArrayList<GrammarNode>()

    override fun iterator(): Iterator<GrammarNode> =
        this.children.iterator()

    override fun toString(): String
    {
        val stringBuilder = StringBuilder()
        this.toString(0, stringBuilder)
        return stringBuilder.toString()
    }

    private fun toString(level: Int, stringBuilder: StringBuilder)
    {
        for (times in 0 until level)
        {
            stringBuilder.append(" > ")
        }

        stringBuilder.append(this.rule)
        stringBuilder.append(" = ")
        stringBuilder.append(this.text)
        stringBuilder.append("\n")

        for (child in this.children)
        {
            child.toString(level + 1, stringBuilder)
        }
    }
}