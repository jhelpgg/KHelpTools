package khelp.utilities.collections

import khelp.utilities.comparators.ComparableNaturalOrderComparator
import khelp.utilities.extensions.bounds
import khelp.utilities.extensions.forEachReversed

/**
 * Create an array of [Comparable] sorted on their "natural order"
 */
fun <C : Comparable<C>> SortedArray(unique : Boolean = false) =
    SortedArray<C>(ComparableNaturalOrderComparator(), unique)

/**
 * Array that sort its elements with given [Comparator]
 *
 * If unique option is activated it is not possible to add two elements declared equivalent by the [Comparator]
 *
 * * Addition, indexOf, contains and remove are on O(log(N))
 */
class SortedArray<T>(private val comparator : Comparator<T>, val unique : Boolean = false) : MutableSet<T>
{
    private val list = ArrayList<T>()

    override val size get() = this.list.size
    val empty get() = this.list.isEmpty()
    val notEmpty get() = this.list.isNotEmpty()

    /**
     * Iterator in sorted elements order
     */
    override fun iterator() : MutableIterator<T> =
        this.list.iterator()

    /**
     * Add an element in the array.
     *
     * If array is on unique mode and the [Comparator] indicates that given element is same than another one already inside the array, the element is not added
     */
    operator fun plusAssign(element : T)
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
    fun indexOf(element : T) : Int =
        this.indexFor(add = false, element)

    override operator fun contains(element : T) =
        this.indexFor(add = false, element) >= 0

    operator fun get(index : Int) =
        this.list[index]

    override fun toString() : String =
        this.list.toString()

    /**
     * Remove, if exists, an element form the array
     */
    operator fun minusAssign(element : T)
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
    operator fun plusAssign(iterable : Iterable<T>)
    {
        for (element in iterable)
        {
            this += element
        }
    }

    /**
     * Remove all elements of given [Iterable] from the array
     */
    operator fun minusAssign(iterable : Iterable<T>)
    {
        for (element in iterable)
        {
            this -= element
        }
    }

    /**
     * Keep elements of the list inside the given iterable
     *
     * It will leave only the intersection
     */
    operator fun remAssign(iterable : Iterable<T>)
    {
        val contains : (T) -> Boolean =
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
     * Remove all elements that match given condition
     *
     * Only case of remove on O(N)
     */
    fun removeIf(condition : (T) -> Boolean)
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

    fun remove(index : Int) : T = this.list.removeAt(index)

    /**
     * Make array empty on removing all
     */
    override fun clear()
    {
        this.list.clear()
    }

    fun forEachReversed(action : (T) -> Unit)
    {
        this.list.forEachReversed(action)
    }

    /**
     * Indicates if all elements of given [Collection] are inside the array
     */
    override fun containsAll(elements : Collection<T>) : Boolean
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

    override fun isEmpty() : Boolean =
        this.list.isEmpty()

    override fun add(element : T) : Boolean
    {
        val indexInsert = this.indexFor(true, element)

        if (indexInsert >= this.list.size)
        {
            this.list.add(element)
            return true
        }

        if (indexInsert >= 0)
        {
            this.list.add(indexInsert, element)
            return true
        }

        return false
    }

    override fun remove(element : T) : Boolean =
        this.list.remove(element)

    override fun addAll(elements : Collection<T>) : Boolean
    {
        var added = false

        for (element in elements)
        {
            added = this.add(element) || added
        }

        return added
    }

    override fun removeAll(elements : Collection<T>) : Boolean =
        this.list.removeAll(elements.toSet())

    override fun retainAll(elements : Collection<T>) : Boolean =
        this.list.retainAll(elements.toSet())

    /**
     * Search an element index
     * @param add Indicates we want to use this index for insert in array
     */
    private fun indexFor(add : Boolean, element : T) : Int
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

        var middle : Int

        while (minimum + 1 < maximum)
        {
            middle = (minimum + maximum) shr 1
            comparison = this.comparator.compare(element, this.list[middle])

            when
            {
                comparison < 0  -> maximum = middle
                comparison == 0 -> return sameDelta + middle
                else            -> minimum = middle
            }
        }

        return differentDelta + maximum
    }

    internal fun listIterator() : ListIterator<T> =
        this.list.listIterator()

    internal fun listIterator(index : Int) : ListIterator<T> =
        this.list.listIterator(index)

    fun subList(start : Int, end : Int) : SortedArray<T>
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

    /**
     * Copy a part of this array inside the given ony
     * @param index Index where start the copy
     * @param length Number of elements to copy
     * @param subPart Array where add elements
     * @param L Array result type
     * @return The array where the copy done (**subPart**)
     */
    fun <L : SortedArray<T>> subPart(index : Int = 0, length : Int = this.size - index, subPart : L) : L
    {
        var offset = index
        var size = length

        if (offset < 0)
        {
            size += offset
            offset = 0
        }

        if (offset + size > this.size)
        {
            size = this.size - offset
        }

        (offset until offset + size).forEach { subPart += this[it] }
        return subPart
    }

    /**
     * Compute interval index where should be inserted a given element.
     *
     * The couple **(min, max)** returned can be interpreted like that (where `size` is the size of the list) :
     * * **(-1, 0)** means that the element is before the first element of the list
     * * **(size, -1)** means that the element is after the last element of the list
     * * **(index, index)** in other word **min==max**, means that the element is at exactly the index **min**
     * * **Other case (min ,max), min < max** means that the element is after the element at **min** index and
     * before the element at **max** index
     *
     * @param element Element search
     * @return Couple (min, max)
     */
    fun intervalOf(element : T) : Pair<Int, Int>
    {
        if (this.list.isEmpty())
        {
            return Pair<Int, Int>(0, -1)
        }

        var comparison = this.comparator.compare(element, this.list[0])

        if (comparison < 0)
        {
            return Pair<Int, Int>(-1, 0)
        }

        if (comparison == 0)
        {
            return Pair<Int, Int>(0, 0)
        }

        val max = this.list.size - 1
        comparison = this.comparator.compare(element, this.list[max])

        if (comparison > 0)
        {
            return Pair<Int, Int>(this.list.size, -1)
        }

        if (comparison == 0)
        {
            return Pair<Int, Int>(max, max)
        }

        var index = this.indexFor(false, element)

        if (index >= 0)
        {
            return Pair<Int, Int>(index, index)
        }

        index = this.indexFor(true, element)
        return Pair<Int, Int>(index - 1, index)
    }


}
