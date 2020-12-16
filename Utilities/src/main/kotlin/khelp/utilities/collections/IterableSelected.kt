package khelp.utilities.collections

/**
 * [Iterable] that filter elements of given [Iterable] via the [criteria]
 */
class IterableSelected<T : Any>(internal val criteria: (T) -> Boolean, internal val iterable: Iterable<T>) : Iterable<T>
{
    override fun iterator(): Iterator<T> =
        IteratorSelected(this.criteria, this.iterable.iterator())
}