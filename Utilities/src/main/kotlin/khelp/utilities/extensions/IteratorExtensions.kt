package khelp.utilities.extensions

import khelp.utilities.collections.IteratorSelected
import khelp.utilities.collections.IteratorTransformed

fun <T : Any> Iterator<T>.select(criteria: (T) -> Boolean): Iterator<T> =
    if (this is IteratorSelected)
    {
        IteratorSelected({ element -> this.criteria(element) && criteria(element) }, this.iterator)
    }
    else
    {
        IteratorSelected(criteria, this)
    }

fun <S : Any, D : Any> Iterator<S>.transform(transformation: (S) -> D): Iterator<D> =
    IteratorTransformed(transformation, this)
