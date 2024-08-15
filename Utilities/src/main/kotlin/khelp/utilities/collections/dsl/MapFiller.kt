package khelp.utilities.collections.dsl

class MapFiller<K : Any, V : Any>(val map : MutableMap<K, V>)
{
    infix fun K.IS(value : V)
    {
        this@MapFiller.map[this] = value
    }
}