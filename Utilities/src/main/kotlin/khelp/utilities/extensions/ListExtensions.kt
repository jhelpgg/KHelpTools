package khelp.utilities.extensions

fun <T> List<T>.forEachReversed(action: (T) -> Unit)
{
    for (index in this.size - 1 downTo 0)
    {
        action(this[index])
    }
}
