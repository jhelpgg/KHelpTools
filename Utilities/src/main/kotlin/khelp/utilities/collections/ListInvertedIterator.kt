package khelp.utilities.collections

class ListInvertedIterator<T>(private val list : List<T>) : Iterator<T>
{
    private var index = this.list.size - 1

    override fun hasNext() : Boolean =
        this.index >= 0

    override fun next() : T
    {
        if (this.index < 0)
        {
            throw NoSuchElementException("No more elements to iterates")
        }

        val value = this.list[this.index]
        this.index --
        return value
    }
}
