package khelp.grammar

import java.util.Stack
import khelp.utilities.collections.queue.Queue
import khelp.utilities.extensions.forEachReversed

class GrammarNode internal constructor(val rule: String, val text: String) : Iterable<GrammarNode>
{
    internal val children = ArrayList<GrammarNode>()

    val empty: Boolean get() = this.text.isEmpty() && this.children.isEmpty()
    val numberChildren: Int get() = this.children.size

    operator fun get(index: Int): GrammarNode =
        this.children[index]

    override fun iterator(): Iterator<GrammarNode> =
        this.children.iterator()

    /**
     * Search a grammar node by name
     *
     * The search order is first deep. For exemple if tree like :
     *
     *            A
     *        ____|_____
     *       |    |    |
     *       B    C    D
     *       |    |    |
     *      / \   |   / \
     *     E   F  G  H   I
     *
     * The test order will be : A, B, E, F, C, G, D, H, I
     *
     * The method stops to first found
     */
    fun searchByFirstDeep(rule: String): GrammarNode? =
        this.searchByFirstDeep { grammarNode -> grammarNode.rule == rule }

    /**
     * Search a grammar node that match criteria
     *
     * The search order is first deep. For exemple if tree like :
     *
     *            A
     *        ____|_____
     *       |    |    |
     *       B    C    D
     *       |    |    |
     *      / \   |   / \
     *     E   F  G  H   I
     *
     * The test order will be : A, B, E, F, C, G, D, H, I
     *
     * The method stops to first found
     */
    fun searchByFirstDeep(criteria: (GrammarNode) -> Boolean): GrammarNode?
    {
        val stack = Stack<GrammarNode>()
        stack.push(this)
        var current: GrammarNode

        while (stack.isNotEmpty())
        {
            current = stack.pop()

            if (criteria(current))
            {
                return current
            }

            current.children.forEachReversed(stack::push)
        }

        return null
    }

    fun forEachDeep(criteria: (GrammarNode) -> Boolean = { true },
                action: (GrammarNode) -> Unit)
    {
        val stack = Stack<GrammarNode>()
        stack.push(this)
        var current: GrammarNode

        while (stack.isNotEmpty())
        {
            current = stack.pop()

            if (criteria(current))
            {
                action(current)
            }

            current.children.forEachReversed(stack::push)
        }
    }

    /**
     * Search a grammar node by name
     *
     * The search order is last deep. For exemple if tree like :
     *
     *            A
     *        ____|_____
     *       |    |    |
     *       B    C    D
     *       |    |    |
     *      / \   |   / \
     *     E   F  G  H   I
     *
     * The test order will be : A, D, I, H, C, G, B, F, E
     *
     * The method stops to first found
     */
    fun searchByLastDeep(rule: String): GrammarNode? =
        this.searchByLastDeep { grammarNode -> grammarNode.rule == rule }

    /**
     * Search a grammar node that match criteria
     *
     * The search order is last deep. For exemple if tree like :
     *
     *            A
     *        ____|_____
     *       |    |    |
     *       B    C    D
     *       |    |    |
     *      / \   |   / \
     *     E   F  G  H   I
     *
     * The test order will be : A, D, I, H, C, G, B, F, E
     *
     * The method stops to first found
     */
    fun searchByLastDeep(criteria: (GrammarNode) -> Boolean): GrammarNode?
    {
        val stack = Stack<GrammarNode>()
        stack.push(this)
        var current: GrammarNode

        while (stack.isNotEmpty())
        {
            current = stack.pop()

            if (criteria(current))
            {
                return current
            }

            current.children.forEach(stack::push)
        }

        return null
    }

    /**
     * Search a grammar node by name
     *
     * The search order is first heap. For exemple if tree like :
     *
     *            A
     *        ____|_____
     *       |    |    |
     *       B    C    D
     *       |    |    |
     *      / \   |   / \
     *     E   F  G  H   I
     *
     * The test order will be : A, B, C, D, E, F, G, H, I
     *
     * The method stops to first found
     */
    fun searchByFirstHeap(rule: String): GrammarNode? =
        this.searchByFirstHeap { grammarNode -> grammarNode.rule == rule }

    /**
     * Search a grammar node that match criteria
     *
     * The search order is first heap. For exemple if tree like :
     *
     *            A
     *        ____|_____
     *       |    |    |
     *       B    C    D
     *       |    |    |
     *      / \   |   / \
     *     E   F  G  H   I
     *
     * The test order will be : A, B, C, D, E, F, G, H, I
     *
     * The method stops to first found
     */
    fun searchByFirstHeap(criteria: (GrammarNode) -> Boolean): GrammarNode?
    {
        val queue = Queue<GrammarNode>()
        queue.inQueue(this)
        var current: GrammarNode

        while (queue.notEmpty)
        {
            current = queue.outQueue()

            if (criteria(current))
            {
                return current
            }

            current.children.forEach(queue::inQueue)
        }

        return null
    }

    /**
     * Search a grammar node by name
     *
     * The search order is last heap. For exemple if tree like :
     *
     *            A
     *        ____|_____
     *       |    |    |
     *       B    C    D
     *       |    |    |
     *      / \   |   / \
     *     E   F  G  H   I
     *
     * The test order will be : A, D, C, B, I, H, G, F, E
     *
     * The method stops to first found
     */
    fun searchByLastHeap(rule: String): GrammarNode? =
        this.searchByLastHeap { grammarNode -> grammarNode.rule == rule }

    /**
     * Search a grammar node that match criteria
     *
     * The search order is last heap. For exemple if tree like :
     *
     *            A
     *        ____|_____
     *       |    |    |
     *       B    C    D
     *       |    |    |
     *      / \   |   / \
     *     E   F  G  H   I
     *
     * The test order will be : A, D, C, B, I, H, G, F, E
     *
     * The method stops to first found
     */
    fun searchByLastHeap(criteria: (GrammarNode) -> Boolean): GrammarNode?
    {
        val queue = Queue<GrammarNode>()
        queue.inQueue(this)
        var current: GrammarNode

        while (queue.notEmpty)
        {
            current = queue.outQueue()

            if (criteria(current))
            {
                return current
            }

            current.children.forEachReversed(queue::inQueue)
        }

        return null
    }

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

    fun removeEmptyNodes()
    {
        val stack = Stack<GrammarNode>()
        var iterator: MutableIterator<GrammarNode>
        var child: GrammarNode
        stack.push(this)

        while (stack.isNotEmpty())
        {
            iterator = stack.pop().children.iterator()

            while (iterator.hasNext())
            {
                child = iterator.next()

                if (child.empty)
                {
                    iterator.remove()
                }
                else
                {
                    stack.push(child)
                }
            }
        }
    }
}