package khelp.neural.propagation

sealed interface Propagation
{
    fun propagation(t:Double):Double

    fun retroPropagation(t:Double):Double
}