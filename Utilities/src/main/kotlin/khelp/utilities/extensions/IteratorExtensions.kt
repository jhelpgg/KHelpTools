package khelp.utilities.extensions

import khelp.utilities.collections.IteratorSelected
import khelp.utilities.collections.IteratorTransformed

/**
 * View an iterator that shows only elements match the given criteria.
 *
 * Faster and take less memory than [Iterable.filter]
 */
fun <T : Any> Iterator<T>.select(criteria: (T) -> Boolean): Iterator<T> =
    if (this is IteratorSelected)
    {
        IteratorSelected({ element -> this.criteria(element) && criteria(element) }, this.iterator)
    }
    else
    {
        IteratorSelected(criteria, this)
    }

/**
 * View iterator that transform all source elements with given [transformation]
 *
 * Faster and take less memory than [Iterable.map]
 */
fun <S : Any, D : Any> Iterator<S>.transform(transformation: (S) -> D): Iterator<D> =
    IteratorTransformed(transformation, this)
