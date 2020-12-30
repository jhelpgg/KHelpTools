package khelp.utilities.collections

import khelp.utilities.comparators.ComparableNaturalOrderComparator
import khelp.utilities.extensions.bounds
import khelp.utilities.extensions.forEachReversed

/**
 * Create an array of [Comparable] sorted on their "natural order"
 */
fun <C : Comparable<C>> sortedArray(unique: Boolean = false) =
    SortedArray<C>(ComparableNaturalOrderComparator(), unique)

/**
 * Array that sort its elements with given [Comparator]
 *
 * If unique option is activated it is not possible to add two elements declared equivalent by the [Comparator]
 *
 * * Addition, indexOf, contains and remove are on O(log(N))
 */
class SortedArray<T>(private val comparator: Comparator<T>, val unique: Boolean = false) : Collection<T>
{
    private val list = ArrayList<T>()

    override val size get() = this.list.size
    val empty get() = this.list.isEmpty()
    val notEmpty get() = this.list.isNotEmpty()

    /**
     * Iterator in sorted elements order
     */
    override fun iterator(): Iterator<T> =
        this.list.iterator()

    /**
     * Add an element in the array.
     *
     * If array is on unique mode and the [Comparator] indicates that given element is same than an other one already inside the array, the element is not added
     */
    operator fun plusAssign(element: T)
    {
        val indexInsert = this.indexFor(true, element)

        if (indexInsert >= this.list.size)
        {
            this.list.add(element)
        }
        else if (indexInsert >= 0)
        {
            this.list.add(indexInsert, element)
        }
    }

    /**
     * Index of an element.
     *
     * If element is not exists a negative value is returned. Don't assume it is -1
     */
    fun indexOf(element: T): Int =
        this.indexFor(add = false, element)

    override operator fun contains(element: T) =
        this.indexFor(add = false, element) >= 0

    operator fun get(index: Int) =
        this.list[index]

    override fun toString(): String =
        this.list.toString()

    /**
     * Remove, if exists, an element form the array
     */
    operator fun minusAssign(element: T)
    {
        val index = this.indexFor(add = false, element)

        if (index >= 0)
        {
            this.list.removeAt(index)
        }
    }

    /**
     * Add all elements of given [Iterable] in the array
     */
    operator fun plusAssign(iterable: Iterable<T>)
    {
        for (element in iterable)
        {
            this += element
        }
    }

    /**
     * Remove all elements of given [Iterable] from the array
     */
    operator fun minusAssign(iterable: Iterable<T>)
    {
        for (element in iterable)
        {
            this -= element
        }
    }

    /**
     * Keep elements of the list inside the given iterable
     *
     * It will left only the intersection
     */
    operator fun remAssign(iterable: Iterable<T>)
    {
        val contains: (T) -> Boolean =
            when (iterable)
            {
                is Collection ->
                {
                    iterable::contains
                }
                else          ->
                {
                    { element -> iterable.indexOf(element) >= 0 }
                }
            }

        val iterator = this.list.iterator()

        while (iterator.hasNext())
        {
            if (!contains(iterator.next()))
            {
                iterator.remove()
            }
        }
    }

    /**
     * Remove all elements that match given conditon
     *
     * Only case of remove on O(N)
     */
    fun removeIf(condition: (T) -> Boolean)
    {
        val iterator = this.list.iterator()

        while (iterator.hasNext())
        {
            if (condition(iterator.next()))
            {
                iterator.remove()
            }
        }
    }

    /**
     * Make array empty on removing all
     */
    fun clear()
    {
        this.list.clear()
    }

    fun forEachReversed(action: (T) -> Unit)
    {
        this.list.forEachReversed(action)
    }

    /**
     * Indicates if all elements of given [Collection] are inside the array
     */
    override fun containsAll(elements: Collection<T>): Boolean
    {
        for (element in elements)
        {
            if (element !in this)
            {
                return false
            }
        }

        return true
    }

    override fun isEmpty(): Boolean =
        this.list.isEmpty()

    /**
     * Searech an element index
     * @param add Indicates we want use this index for insert in array
     */
    private fun indexFor(add: Boolean, element: T): Int
    {
        if (this.list.isEmpty())
        {
            return if (add)
            {
                0
            }
            else
            {
                -1
            }
        }

        val sameDelta =
            if (add && this.unique)
            {
                Int.MIN_VALUE
            }
            else
            {
                0
            }

        val differentDelta =
            if (add)
            {
                0
            }
            else
            {
                Int.MIN_VALUE
            }

        var comparison = this.comparator.compare(element, this.list[0])

        if (comparison < 0)
        {
            return differentDelta
        }
        else if (comparison == 0)
        {
            return sameDelta
        }

        var minimum = 0
        var maximum = this.list.size - 1
        comparison = this.comparator.compare(element, this.list[maximum])

        if (comparison > 0)
        {
            return differentDelta + maximum + 1
        }
        else if (comparison == 0)
        {
            return sameDelta + maximum
        }

        var middle: Int

        while (minimum + 1 < maximum)
        {
            middle = (minimum + maximum) shr 1
            comparison = this.comparator.compare(element, this.list[middle])

            when
            {
                comparison < 0  -> maximum = middle
                comparison == 0 -> return sameDelta + middle
                comparison > 0  -> minimum = middle
            }
        }

        return differentDelta + maximum
    }

    internal fun listIterator(): ListIterator<T> =
        this.list.listIterator()

    internal fun listIterator(index: Int): ListIterator<T> =
        this.list.listIterator(index)

    fun subList(start: Int, end: Int): SortedArray<T>
    {
        val subArray = SortedArray(this.comparator, this.unique)

        for (index in start.bounds(0, this.size - 1) until end.bounds(0, this.size))
        {
            subArray += this[index]
        }

        return subArray
    }

    fun immutableList() : List<T> =
        SortedArrayImmutable(this)
}
