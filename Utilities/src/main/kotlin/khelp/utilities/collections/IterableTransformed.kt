package khelp.utilities.collections

/**
 * [Iterable] that transform, "on the fly",elements inside given [Iterable] via the [transformation]
 */
class IterableTransformed<S : Any, D : Any>(private val transformation: (S) -> D, private val iterable: Iterable<S>) :
    Iterable<D>
{
    override fun iterator(): Iterator<D> =
        IteratorTransformed(this.transformation, this.iterable.iterator())
}