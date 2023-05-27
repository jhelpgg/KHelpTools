package khelp.utilities.collections

class ListInvertedIterable<T>(private val list : List<T>) : Iterable<T>
{
    override fun iterator() : Iterator<T> = ListInvertedIterator<T>(this.list)
}
