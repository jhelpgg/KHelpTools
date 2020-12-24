package khelp.utilities.extensions

fun <K, V> Map<K, V>.copy(): Map<K, V>
{
    val copy = HashMap<K, V>()

    for ((key, value) in this)
    {
        copy[key] = value
    }

    return copy
}
