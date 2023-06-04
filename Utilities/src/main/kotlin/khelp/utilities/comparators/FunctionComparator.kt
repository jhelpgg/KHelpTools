package khelp.utilities.comparators

class FunctionComparator<T : Any>(private val function : (T, T) -> Int) : Comparator<T>
{
    override fun compare(first : T, second : T) : Int = this.function(first, second)
}
