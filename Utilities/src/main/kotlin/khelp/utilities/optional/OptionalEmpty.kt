package khelp.utilities.optional

internal object OptionalEmpty : Optional<Any>
{
    override val present = false
    override val absent = true
    override val value : Any get() = throw IllegalStateException("Value is absent")
    override fun ifPresent(action : (Any) -> Unit) = Unit
    override fun ifAbsent(action : () -> Unit) = action()
    override fun <R> ifPresentElse(presentAction : (Any) -> R, absentAction : () -> R) : R = absentAction()
}