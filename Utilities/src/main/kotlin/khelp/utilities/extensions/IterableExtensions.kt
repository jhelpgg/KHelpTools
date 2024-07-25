package khelp.utilities.extensions

import khelp.utilities.collections.IterableSelected
import khelp.utilities.collections.IterableTransformed

/**
 * View an iterable that shows only elements match the given criteria.
 *
 * Faster and take less memory than [Iterable.filter]
 */
fun <T : Any> Iterable<T>.select(criteria: (T) -> Boolean): Iterable<T> =
    if (this is IterableSelected)
    {
        IterableSelected({ element -> this.criteria(element) && criteria(element) }, this.iterable)
    }
    else
    {
        IterableSelected(criteria, this)
    }

/**
 * View iterable that transform all source elements with given [transformation]
 *
 * Faster and take less memory than [Iterable.map]
 */
fun <S : Any, D : Any> Iterable<S>.transform(transformation: (S) -> D): Iterable<D> =
    IterableTransformed(transformation, this)

inline fun <T : Any, reified D : T> Iterable<T>.selectInstance(): Iterable<D> = this.select { element -> element is D }.transform { element -> element as D }