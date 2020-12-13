package khelp.utilities.collections

class IterableTransformed<S : Any, D : Any>(private val transformation: (S) -> D, private val iterable: Iterable<S>) :
    Iterable<D>
{
    override fun iterator(): Iterator<D> =
        IteratorTransformed(this.transformation, this.iterable.iterator())
}