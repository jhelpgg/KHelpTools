package khelp.utilities.thread

inline fun <reified T> atomic(initialValue : T) = ReferenceAtomic<T>(initialValue)

fun atomic(initialValue : Boolean) = BooleanAtomic(initialValue)

fun atomic(initialValue : Int) = IntAtomic(initialValue)
