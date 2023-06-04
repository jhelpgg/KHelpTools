package khelp.utilities.extensions

import khelp.utilities.collections.ListInvertedIterable

/**
 * Do action on all elements followed on reverse way (from end to begin) without create on other list
 */
inline fun <T> List<T>.forEachReversed(action : (T) -> Unit)
{
    for (index in this.size - 1 downTo 0)
    {
        action(this[index])
    }
}

inline fun <T> List<T>.forEachReversedWithIndex(action : (Int, T) -> Unit)
{
    for (index in this.size - 1 downTo 0)
    {
        action(index, this[index])
    }
}


fun <T> List<T>.inverted() : Iterable<T> = ListInvertedIterable(this)
