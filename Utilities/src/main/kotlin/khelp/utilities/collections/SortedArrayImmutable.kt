package khelp.utilities.collections

internal class SortedArrayImmutable<T>(private val sortedArray: SortedArray<T>) : List<T>
{
    override val size: Int get() = this.sortedArray.size

    override fun contains(element: T): Boolean =
        element in this.sortedArray

    override fun containsAll(elements: Collection<T>): Boolean =
        this.sortedArray.containsAll(elements)

    override fun get(index: Int): T =
        this.sortedArray[index]

    override fun indexOf(element: T): Int =
        this.sortedArray.indexOf(element)

    override fun isEmpty(): Boolean =
        this.sortedArray.empty

    override fun iterator(): Iterator<T> =
        this.sortedArray.iterator()

    override fun lastIndexOf(element: T): Int =
        this.sortedArray.indexOf(element)

    override fun listIterator(): ListIterator<T> =
        this.sortedArray.listIterator()

    override fun listIterator(index: Int): ListIterator<T> =
        this.sortedArray.listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int): List<T> =
        this.sortedArray.subList(fromIndex, toIndex)
            .immutableList()
}