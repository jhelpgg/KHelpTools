package khelp.utilities.optional

internal class OptionalValue<T : Any>(override val value : T) : Optional<T>
{
    override val present = true
    override val absent = false
    override fun ifPresent(action : (T) -> Unit) = action(this.value)
    override fun ifAbsent(action : () -> Unit) = Unit
    override fun <R> ifPresentElse(presentAction : (T) -> R, absentAction : () -> R) : R = presentAction(this.value)
}