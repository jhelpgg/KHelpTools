package khelp.utilities.extensions

fun <K, V> Map<K, V>.copy() : Map<K, V>
{
    val copy = HashMap<K, V>()

    for ((key, value) in this)
    {
        copy[key] = value
    }

    return copy
}

fun <K, V> Map<K, V>.grabElementIf(condition : (K, V) -> Boolean) : Pair<K, V>?
{
    for ((key, value) in this)
    {
        if (condition(key, value))
        {
            return Pair(key, value)
        }
    }

    return null
}
