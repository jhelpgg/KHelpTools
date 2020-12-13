package khelp.utilities.extensions

import khelp.utilities.collections.IterableSelected
import khelp.utilities.collections.IterableTransformed

fun <T : Any> Iterable<T>.select(criteria: (T) -> Boolean): Iterable<T> =
    if (this is IterableSelected)
    {
        IterableSelected({ element -> this.criteria(element) && criteria(element) }, this.iterable)
    }
    else
    {
        IterableSelected(criteria, this)
    }

fun <S : Any, D : Any> Iterable<S>.transform(transformation: (S) -> D): Iterable<D> =
    IterableTransformed(transformation, this)
