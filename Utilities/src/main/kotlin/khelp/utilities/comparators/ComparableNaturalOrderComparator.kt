package khelp.utilities.comparators

class ComparableNaturalOrderComparator<C : Comparable<C>> : Comparator<C>
{
    override fun compare(comparable1: C, comparable2: C): Int =
        comparable1.compareTo(comparable2)
}