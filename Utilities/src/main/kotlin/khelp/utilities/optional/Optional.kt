package khelp.utilities.optional

sealed interface Optional<T : Any>
{
    companion object
    {
        fun <T : Any> empty() : Optional<T> = OptionalEmpty as Optional<T>
        fun <T : Any> value(value : T) : Optional<T> = OptionalValue(value)
    }

    val present : Boolean
    val absent : Boolean
    val value : T
    fun ifPresent(action : (T) -> Unit)
    fun ifAbsent(action : () -> Unit)
    fun <R> ifPresentElse(presentAction : (T) -> R, absentAction : () -> R) : R
}
