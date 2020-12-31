package khelp.utilities.extensions

/**
 * Do action on all elements followed on reverse way (from end to begin) without create on other list
 */
fun <T> List<T>.forEachReversed(action: (T) -> Unit)
{
    for (index in this.size - 1 downTo 0)
    {
        action(this[index])
    }
}
