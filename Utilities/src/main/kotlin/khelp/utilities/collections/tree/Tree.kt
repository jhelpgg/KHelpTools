package khelp.utilities.collections.tree

import khelp.utilities.collections.queue.Queue
import khelp.utilities.extensions.forEachReversed
import khelp.utilities.optional.Optional
import java.util.Stack

class Tree<V : Any> : Iterable<Tree<V>>
{
    private val branches = ArrayList<Tree<V>>()

    var parent : Tree<V>?
        private set
    val root : Boolean get() = this.parent == null
    var value : V
    val leaf : Boolean get() = this.branches.isEmpty()
    val numberBranches : Int get() = this.branches.size

    private constructor(parent : Tree<V>?, value : V)
    {
        this.parent = parent
        this.value = value
    }

    constructor(value : V) : this(null, value)

    operator fun get(index : Int) : Tree<V> =
        this.branches[index]

    override fun iterator() : Iterator<Tree<V>> =
        this.branches.iterator()

    fun addBranch(value : V) : Tree<V>
    {
        val branch = Tree(this, value)
        this.branches.add(branch)
        return branch
    }

    fun cutBranch(index : Int) : Tree<V>
    {
        val branch = this.branches.removeAt(index)
        branch.parent = null
        return branch
    }

    fun indexOf(child:Tree<V>) : Int =
        this.branches.indexOf(child)

    /**
     * Visit the tree from root left to right in going depth.
     *
     * If tree is
     *
     * ```
     *      A
     *      |
     *    ----------
     *    |    |  |
     *    B    C   D
     *    |    |
     *   ---   G
     *   |  |
     *   E  F
     * ```
     *
     * The order of visit is :
     * `A B E F C G D`
     *
     * @param visitor The visitor that receive the value. It returns a Boolean to indicates is it wants to continue the visit or not (`true` to continue)
     */
    fun visitLeftToRightDepth(visitor : (V) -> Boolean)
    {
        val stack = Stack<Tree<V>>()
        stack.push(this)

        while (stack.isNotEmpty())
        {
            val tree = stack.pop()

            if (! visitor(tree.value))
            {
                return
            }

            this.branches.forEachReversed { branch -> stack.push(branch) }
        }
    }

    /**
     * Visit the tree from root left to right in going high.
     *
     * If tree is
     *
     * ```
     *      A
     *      |
     *    ----------
     *    |    |  |
     *    B    C   D
     *    |    |
     *   ---   G
     *   |  |
     *   E  F
     * ```
     *
     * The order of visit is :
     * `A B C D E F G`
     *
     * @param visitor The visitor that receive the value. It returns a Boolean to indicates is it wants to continue the visit or not (`true` to continue)
     */
    fun visitLeftToRightHigh(visitor : (V) -> Boolean)
    {
        val queue = Queue<Tree<V>>()
        queue.inQueue(this)

        while (queue.isNotEmpty())
        {
            val tree = queue.outQueue()

            if (! visitor(tree.value))
            {
                return
            }

            this.branches.forEach { branch -> queue.inQueue(branch) }
        }
    }

    /**
     * Visit the tree from root right to left in going depth.
     *
     * If tree is
     *
     * ```
     *      A
     *      |
     *    ----------
     *    |    |  |
     *    B    C   D
     *    |    |
     *   ---   G
     *   |  |
     *   E  F
     * ```
     *
     * The order of visit is :
     * `A D C G B F E`
     *
     * @param visitor The visitor that receive the value. It returns a Boolean to indicates is it wants to continue the visit or not (`true` to continue)
     */
    fun visitRightToLeftDepth(visitor : (V) -> Boolean)
    {
        val stack = Stack<Tree<V>>()
        stack.push(this)

        while (stack.isNotEmpty())
        {
            val tree = stack.pop()

            if (! visitor(tree.value))
            {
                return
            }

            this.branches.forEach { branch -> stack.push(branch) }
        }
    }

    /**
     * Visit the tree from root right to left in going high.
     *
     * If tree is
     *
     * ```
     *      A
     *      |
     *    ----------
     *    |    |  |
     *    B    C   D
     *    |    |
     *   ---   G
     *   |  |
     *   E  F
     * ```
     *
     * The order of visit is :
     * `A D C B G F E`
     *
     * @param visitor The visitor that receive the value. It returns a Boolean to indicates is it wants to continue the visit or not (`true` to continue)
     */
    fun visitRightToLeftHigh(visitor : (V) -> Boolean)
    {
        val queue = Queue<Tree<V>>()
        queue.inQueue(this)

        while (queue.isNotEmpty())
        {
            val tree = queue.outQueue()

            if (! visitor(tree.value))
            {
                return
            }

            this.branches.forEachReversed { branch -> queue.inQueue(branch) }
        }
    }

    /**
     * Search first element that match a condition.
     *
     * Thea way of search is left to right in going depth.
     *
     *  If tree is
     *
     * ```
     *      A
     *      |
     *    ----------
     *    |    |  |
     *    B    C   D
     *    |    |
     *   ---   G
     *   |  |
     *   E  F
     * ```
     *
     * The order of visit is :
     * `A B E F C G D`
     */
    fun searchLeftToRightDepth(condition : (V) -> Boolean) : Optional<V>
    {
        var result : Optional<V> = Optional.empty()

        this.visitLeftToRightDepth { value ->
            if (condition(value))
            {
                result = Optional.value(value)
                false
            }
            else
            {
                true
            }
        }

        return result
    }

    /**
     * Search first element that match a condition.
     *
     * Thea way of search is left to right in going high.
     *
     * If tree is
     *
     * ```
     *      A
     *      |
     *    ----------
     *    |    |  |
     *    B    C   D
     *    |    |
     *   ---   G
     *   |  |
     *   E  F
     * ```
     *
     * The order of visit is :
     * `A B C D E F G`
     */
    fun searchLeftToRightHigh(condition : (V) -> Boolean) : Optional<V>
    {
        var result : Optional<V> = Optional.empty()

        this.visitLeftToRightHigh { value ->
            if (condition(value))
            {
                result = Optional.value(value)
                false
            }
            else
            {
                true
            }
        }

        return result
    }

    /**
     * Search first element that match a condition.
     *
     * Thea way of search is right to left in going depth.
     *
     * If tree is
     *
     * ```
     *      A
     *      |
     *    ----------
     *    |    |  |
     *    B    C   D
     *    |    |
     *   ---   G
     *   |  |
     *   E  F
     * ```
     *
     * The order of visit is :
     * `A D C G B F E`
     */
    fun searchRightToLeftDepth(condition : (V) -> Boolean) : Optional<V>
    {
        var result : Optional<V> = Optional.empty()

        this.visitRightToLeftDepth { value ->
            if (condition(value))
            {
                result = Optional.value(value)
                false
            }
            else
            {
                true
            }
        }

        return result
    }

    /**
     * Search first element that match a condition.
     *
     * Thea way of search is right to left in going high.
     *
     * If tree is
     *
     * ```
     *      A
     *      |
     *    ----------
     *    |    |  |
     *    B    C   D
     *    |    |
     *   ---   G
     *   |  |
     *   E  F
     * ```
     *
     * The order of visit is :
     * `A D C B G F E`
     */
    fun searchRightToLeftHigh(condition : (V) -> Boolean) : Optional<V>
    {
        var result : Optional<V> = Optional.empty()

        this.visitRightToLeftHigh { value ->
            if (condition(value))
            {
                result = Optional.value(value)
                false
            }
            else
            {
                true
            }
        }

        return result
    }

    /**
     * Collect elements that match a condition.
     *
     * The way of search is left to right in going depth.
     *
     *  If tree is
     *
     * ```
     *      A
     *      |
     *    ----------
     *    |    |  |
     *    B    C   D
     *    |    |
     *   ---   G
     *   |  |
     *   E  F
     * ```
     *
     * The order of visit is :
     * `A B E F C G D`
     *
     * @param condition Condition value should match to be collected
     * @param collector Collect matching values. Returns `true` if collection should continue
     */
    fun collectLeftToRightDepth(condition : (V) -> Boolean, collector : (V) -> Boolean)
    {
        this.visitLeftToRightDepth { value ->
            if (condition(value))
            {
                collector(value)
            }
            else
            {
                true
            }
        }
    }

    /**
     * Collect elements that match a condition.
     *
     * Thea way of search is left to right in going high.
     *
     * If tree is
     *
     * ```
     *      A
     *      |
     *    ----------
     *    |    |  |
     *    B    C   D
     *    |    |
     *   ---   G
     *   |  |
     *   E  F
     * ```
     *
     * The order of visit is :
     * `A B C D E F G`
     *
     * @param condition Condition value should match to be collected
     * @param collector Collect matching values. Returns `true` if collection should continue
     */
    fun collectLeftToRightHigh(condition : (V) -> Boolean, collector : (V) -> Boolean)
    {
        this.visitLeftToRightHigh { value ->
            if (condition(value))
            {
                collector(value)
            }
            else
            {
                true
            }
        }
    }

    /**
     * Collect elements that match a condition.
     *
     * Thea way of search is right to left in going depth.
     *
     * If tree is
     *
     * ```
     *      A
     *      |
     *    ----------
     *    |    |  |
     *    B    C   D
     *    |    |
     *   ---   G
     *   |  |
     *   E  F
     * ```
     *
     * The order of visit is :
     * `A D C G B F E`
     *
     * @param condition Condition value should match to be collected
     * @param collector Collect matching values. Returns `true` if collection should continue
     */
    fun collectRightToLeftDepth(condition : (V) -> Boolean, collector : (V) -> Boolean)
    {
        this.visitRightToLeftDepth { value ->
            if (condition(value))
            {
                collector(value)
            }
            else
            {
                true
            }
        }
    }

    /**
     * Collect elements that match a condition.
     *
     * Thea way of search is right to left in going high.
     *
     * If tree is
     *
     * ```
     *      A
     *      |
     *    ----------
     *    |    |  |
     *    B    C   D
     *    |    |
     *   ---   G
     *   |  |
     *   E  F
     * ```
     *
     * The order of visit is :
     * `A D C B G F E`
     *
     * @param condition Condition value should match to be collected
     * @param collector Collect matching values. Returns `true` if collection should continue
     */
    fun collectRightToLeftHigh(condition : (V) -> Boolean, collector : (V) -> Boolean)
    {
        this.visitRightToLeftHigh { value ->
            if (condition(value))
            {
                collector(value)
            }
            else
            {
                true
            }
        }
    }

    override fun toString() : String = this.value.toString()
}
