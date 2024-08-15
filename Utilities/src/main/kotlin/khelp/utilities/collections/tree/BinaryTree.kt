package khelp.utilities.collections.tree

import java.util.Stack
import khelp.utilities.collections.SortedArray
import khelp.utilities.collections.queue.Queue
import khelp.utilities.comparators.ComparableNaturalOrderComparator

fun <KC : Comparable<KC>, V : Any> BinaryTree() : BinaryTree<KC, V> =
    BinaryTree<KC, V>(ComparableNaturalOrderComparator<KC>())

class BinaryTree<K : Any, V : Any>(comparator : Comparator<K>) : MutableMap<K, V>
{
    private val tree = SortedArray<BinaryTreeEntry<K, V>>(BinaryTreeEntryComparator<K, V>(comparator), unique = true)

    override val entries : MutableSet<MutableMap.MutableEntry<K, V>> =
        this.tree as MutableSet<MutableMap.MutableEntry<K, V>>
    override val keys : MutableSet<K> get() = this.tree.map { (key, _) -> key }.toMutableSet<K>()
    override val size : Int get() = this.tree.size
    val empty : Boolean get() = this.tree.empty
    val notEmpty : Boolean get() = this.tree.notEmpty
    override val values : MutableCollection<V> get() = ArrayList<V>(this.tree.map { (_, value) -> value })

    override fun clear()
    {
        this.tree.clear()
    }

    override fun isEmpty() : Boolean = this.tree.empty

    override fun remove(key : K) : V?
    {
        val (index, oldValue) = this.search(key)

        if (index >= 0)
        {
            this.tree.remove(index)
        }

        return oldValue
    }

    override fun putAll(from : Map<out K, V>)
    {
        for ((key, value) in from)
        {
            this[key] = value
        }
    }

    override fun put(key : K, value : V) : V?
    {
        val (index, oldValue) = this.search(key)

        if (index >= 0)
        {
            this.tree[index].value = value
            return oldValue
        }

        this.tree.add(BinaryTreeEntry<K, V>(key, value))
        return null
    }

    override fun get(key : K) : V? =
        this.search(key).second

    override fun containsValue(value : V) : Boolean =
        this.tree.any { (_, valueExplored) -> value == valueExplored }

    override fun containsKey(key : K) : Boolean =
        this.search(key).first >= 0

    override fun remove(key : K, value : V) : Boolean
    {
        val (index, valueFound) = this.search(key)

        if (index < 0)
        {
            return false
        }

        if (value == valueFound)
        {
            this.tree.remove(index)
            return true
        }

        return false
    }

    override fun getOrDefault(key : K, defaultValue : V) : V =
        this[key] ?: defaultValue

    /**
     * Visit the tree.
     *
     * If the tree is
     *
     * ```
     *      A
     *    /   \
     *   B     C
     *  / \   / \
     * D   E F   G
     * ```
     *
     * The visit order is : A B D E C F G
     *
     * The visitor can stop the visit by return false
     *
     * @param visitor The visitor that receive the entries in specified order
     */
    fun visitLowDeep(visitor : (MutableMap.MutableEntry<K, V>) -> Boolean)
    {
        val stack = Stack<BranchInfo<Unit>>()
        stack.push(BranchInfo(0, this.tree.size - 1))

        while (stack.isNotEmpty())
        {
            val (minimum, maximum) = stack.pop()
            val middle = (minimum + maximum) / 2

            if (visitor(this.tree[middle]).not())
            {
                return
            }

            if (middle + 1 <= maximum)
            {
                stack.push(BranchInfo(middle + 1, maximum))
            }

            if (minimum <= middle - 1)
            {
                stack.push(BranchInfo(minimum, middle - 1))
            }
        }
    }

    /**
     * Visit the tree.
     *
     * If the tree is
     *
     * ```
     *      A
     *    /   \
     *   B     C
     *  / \   / \
     * D   E F   G
     * ```
     *
     * The visit order is : D E B F G C A
     *
     * The visitor can stop the visit by return false
     *
     * @param visitor The visitor that receive the entries in specified order
     */
    fun visitLowDeepest(visitor : (MutableMap.MutableEntry<K, V>) -> Boolean)
    {
        val stack = Stack<BranchInfo<Boolean>>()
        stack.push(BranchInfo(0, this.tree.size - 1, false))

        while (stack.isNotEmpty())
        {
            val (minimum, maximum, explored) = stack.pop()
            val middle = (minimum + maximum) / 2

            if (explored || minimum == maximum)
            {
                if (visitor(this.tree[middle]).not())
                {
                    return
                }

                continue
            }

            stack.push(BranchInfo(minimum, maximum, true))

            if (middle + 1 <= maximum)
            {
                stack.push(BranchInfo(middle + 1, maximum, false))
            }

            if (minimum <= middle - 1)
            {
                stack.push(BranchInfo(minimum, middle - 1, false))
            }
        }
    }

    /**
     * Visit the tree.
     *
     * If the tree is
     *
     * ```
     *      A
     *    /   \
     *   B     C
     *  / \   / \
     * D   E F   G
     * ```
     *
     * The visit order is : A C G F B E D
     *
     * The visitor can stop the visit by return false
     *
     * @param visitor The visitor that receive the entries in specified order
     */
    fun visitHighDeep(visitor : (MutableMap.MutableEntry<K, V>) -> Boolean)
    {
        val stack = Stack<BranchInfo<Unit>>()
        stack.push(BranchInfo(0, this.tree.size - 1))

        while (stack.isNotEmpty())
        {
            val (minimum, maximum) = stack.pop()
            val middle = (minimum + maximum) / 2

            if (visitor(this.tree[middle]).not())
            {
                return
            }

            if (minimum <= middle - 1)
            {
                stack.push(BranchInfo(minimum, middle - 1))
            }

            if (middle + 1 <= maximum)
            {
                stack.push(BranchInfo(middle + 1, maximum))
            }
        }
    }
    /**
     * Visit the tree.
     *
     * If the tree is
     *
     * ```
     *      A
     *    /   \
     *   B     C
     *  / \   / \
     * D   E F   G
     * ```
     *
     * The visit order is : G F C E D B A
     *
     * The visitor can stop the visit by return false
     *
     * @param visitor The visitor that receive the entries in specified order
     */
    fun visitHighDeepest(visitor : (MutableMap.MutableEntry<K, V>) -> Boolean)
    {
        val stack = Stack<BranchInfo<Boolean>>()
        stack.push(BranchInfo(0, this.tree.size - 1, false))

        while (stack.isNotEmpty())
        {
            val (minimum, maximum, explored) = stack.pop()
            val middle = (minimum + maximum) / 2

            if (explored || minimum == maximum)
            {
                if (visitor(this.tree[middle]).not())
                {
                    return
                }

                continue
            }

            stack.push(BranchInfo(minimum, maximum, true))

            if (minimum <= middle - 1)
            {
                stack.push(BranchInfo(minimum, middle - 1, false))
            }

            if (middle + 1 <= maximum)
            {
                stack.push(BranchInfo(middle + 1, maximum, false))
            }
        }
    }

    /**
     * Visit the tree.
     *
     * If the tree is
     *
     * ```
     *      A
     *    /   \
     *   B     C
     *  / \   / \
     * D   E F   G
     * ```
     *
     * The visit order is : A B C D E F G
     *
     * The visitor can stop the visit by return false
     *
     * @param visitor The visitor that receive the entries in specified order
     */
    fun visitLowFirst(visitor : (MutableMap.MutableEntry<K, V>) -> Boolean)
    {
        val queue = Queue<BranchInfo<Unit>>()
        queue.inQueue(BranchInfo(0, this.tree.size - 1))

        while (queue.isNotEmpty())
        {
            val (minimum, maximum) = queue.outQueue()
            val middle = (minimum + maximum) / 2

            if (visitor(this.tree[middle]).not())
            {
                return
            }

            if (minimum <= middle - 1)
            {
                queue.inQueue(BranchInfo(minimum, middle - 1))
            }

            if (middle + 1 <= maximum)
            {
                queue.inQueue(BranchInfo(middle + 1, maximum))
            }
        }
    }

    /**
     * Visit the tree.
     *
     * If the tree is
     *
     * ```
     *      A
     *    /   \
     *   B     C
     *  / \   / \
     * D   E F   G
     * ```
     *
     * The visit order is : A C B G F E D
     *
     * The visitor can stop the visit by return false
     *
     * @param visitor The visitor that receive the entries in specified order
     */
    fun visitHighFirst(visitor : (MutableMap.MutableEntry<K, V>) -> Boolean)
    {
        val queue = Queue<BranchInfo<Unit>>()
        queue.inQueue(BranchInfo(0, this.tree.size - 1))

        while (queue.isNotEmpty())
        {
            val (minimum, maximum) = queue.outQueue()
            val middle = (minimum + maximum) / 2

            if (visitor(this.tree[middle]).not())
            {
                return
            }

            if (middle + 1 <= maximum)
            {
                queue.inQueue(BranchInfo(middle + 1, maximum))
            }

            if (minimum <= middle - 1)
            {
                queue.inQueue(BranchInfo(minimum, middle - 1))
            }
        }
    }

    /**
     * Returns a string representation of the binary tree.
     *
     * The string representation contains the elements of the tree in a specific order.
     * If the tree is empty, the method returns "{}".
     * The order of elements is determined by the inorder traversal of the tree.
     * A tree is traversed in the following order:
     *
     * For example
     *
     * ```kotlin
     * import khelp.utilities.collections.dsl.BinaryTree
     *
     * fun main()
     * {
     *     val binaryTree = BinaryTree<Int, String> {
     *         0 IS "Zero"
     *         1 IS "One"
     *         2 IS "Two"
     *         3 IS "Three"
     *         4 IS "Four"
     *         5 IS "Five"
     *         6 IS "Six"
     *     }
     *
     *     println(binaryTree)
     * }
     * ```
     *
     * prints
     *
     * ```
     * {3 : Three}
     *    {1 : One}
     *       {0 : Zero}
     *       {2 : Two}
     *    {5 : Five}
     *       {4 : Four}
     *       {6 : Six}
     * ```
     */
    override fun toString() : String
    {
        if (this.tree.empty)
        {
            return "{}"
        }

        val stack = Stack<BranchInfo<String>>()
        stack.push(BranchInfo(0, this.tree.size - 1, ""))
        val stringBuilder = StringBuilder()

        while (stack.isNotEmpty())
        {
            val (minimum, maximum, header) = stack.pop()

            stringBuilder.append(header)
            val index = (minimum + maximum) / 2
            val item = this.tree[index]
            stringBuilder.append("{${item.key} : ${item.value}}\n")

            if (index + 1 <= maximum)
            {
                stack.push(BranchInfo(index + 1, maximum, "$header   "))
            }

            if (minimum <= index - 1)
            {
                stack.push(BranchInfo(minimum, index - 1, "$header   "))
            }
        }

        stringBuilder.deleteCharAt(stringBuilder.length - 1)
        return stringBuilder.toString()
    }

    private fun search(key : K) : Pair<Int, V?>
    {
        if (this.empty)
        {
            return Pair(-1, null)
        }

        val searched = BinaryTreeEntry<K, V>(key, this.tree[0].value)
        val index = this.tree.indexOf(searched)

        if (index < 0)
        {
            return Pair(index, null)
        }

        return Pair(index, this.tree[index].value)
    }
}
