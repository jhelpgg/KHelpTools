package khelp.utilities.weak

inline fun <reified T> weak(value:T?=null) = Weak<T>(value)
