package khelp.utilities.collections

class ImmutableList<T>(collection:Collection<T>) : List<T>
{
    private val list = ArrayList<T>(collection)

    override val size : Int        get() = this.list.size

    override fun contains(element : T) : Boolean = element in this.list

    override fun containsAll(elements : Collection<T>) : Boolean = this.list.containsAll(elements)

    override fun get(index : Int) : T=this.list[index]

    override fun indexOf(element : T) : Int=this.list.indexOf(element)

    override fun isEmpty() : Boolean=this.list.isEmpty()

    override fun iterator() : Iterator<T> = this.list.iterator()

    override fun lastIndexOf(element : T) : Int = this.list.lastIndexOf(element)

    override fun listIterator() : ListIterator<T> = this.list.listIterator()

    override fun listIterator(index : Int) : ListIterator<T> = this.list.listIterator(index)

    override fun subList(fromIndex : Int, toIndex : Int) : List<T> = ImmutableList(this.list.subList(fromIndex, toIndex))
}
