package khelp.utilities.collections

import khelp.utilities.comparators.ComparableNaturalOrderComparator

fun <C : Comparable<C>> sortedArray(unique: Boolean = false) =
    SortedArray<C>(ComparableNaturalOrderComparator(), unique)

class SortedArray<T>(private val comparator: Comparator<T>, val unique: Boolean = false) : Collection<T>
{
    private val list = ArrayList<T>()

    override val size get() = this.list.size
    val empty get() = this.list.isEmpty()
    val notEmpty get() = this.list.isNotEmpty()

    override fun iterator(): Iterator<T> =
        this.list.iterator()

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

    fun indexOf(element: T): Int =
        this.indexFor(add = false, element)

    override operator fun contains(element: T) =
        this.indexFor(add = false, element) >= 0

    operator fun get(index: Int) =
        this.list[index]

    override fun toString(): String =
        this.list.toString()

    operator fun minusAssign(element: T)
    {
        val index = this.indexFor(add = false, element)

        if (index >= 0)
        {
            this.list.removeAt(index)
        }
    }

    operator fun plusAssign(iterable: Iterable<T>)
    {
        for (element in iterable)
        {
            this += element
        }
    }

    operator fun minusAssign(iterable: Iterable<T>)
    {
        for (element in iterable)
        {
            this -= element
        }
    }

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

    fun clear()
    {
        this.list.clear()
    }

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
}
