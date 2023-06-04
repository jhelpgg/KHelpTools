package khelp.utilities.extensions

import khelp.utilities.comparators.FunctionComparator

val <T : Any> ((T, T) -> Int).comparator : Comparator<T> get() = FunctionComparator(this)
