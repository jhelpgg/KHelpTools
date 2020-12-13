package khelp.utilities.collections

class IteratorTransformed<S : Any, D : Any>(private val transformation: (S) -> D, private val iterator: Iterator<S>) :
    Iterator<D>
{
    override fun hasNext(): Boolean =
        this.iterator.hasNext()

    override fun next(): D =
        this.transformation(this.iterator.next())
}
