package khelp.utilities.comparators

/**
 * [Comparator] that use the "natural order" of a [Comparable]
 */
class ComparableNaturalOrderComparator<C : Comparable<C>> : Comparator<C>
{
    override fun compare(comparable1: C, comparable2: C): Int =
        comparable1.compareTo(comparable2)
}