package khelp.utilities.collections

import khelp.utilities.extensions.transform

class IntMap<T> : Iterable<Pair<Int, T>>
{
    private val map = SortedArray<IntMapElement<T>>(true)

    val size : Int get() = this.map.size

    val empty : Boolean get() = this.map.empty

    val notEmpty : Boolean get() = this.map.notEmpty

    operator fun get(key : Int) : T?
    {
        val elementIndex = this.map.indexOf(IntMapElement(key))

        return if (elementIndex >= 0) this.map[elementIndex].value else null
    }

    operator fun set(key : Int, value : T)
    {
        val elementIndex = this.map.indexOf(IntMapElement(key))

        if (elementIndex >= 0)
        {
            this.map[elementIndex].value = value
        }
        else
        {
            this.map += IntMapElement<T>(key, value)
        }
    }

    fun remove(key : Int)
    {
        this.map -= IntMapElement(key)
    }

    override fun iterator() : Iterator<Pair<Int, T>> =
        this.map.iterator()
            .transform { intMapElement -> Pair(intMapElement.key, intMapElement.value !!) }

    fun keys() : Iterable<Int> =
        this.map.transform { intMapElement -> intMapElement.key }

    fun values() : Iterable<T> =
        this.map.transform { intMapElement -> intMapElement.value !! }

    fun clear()
    {
        this.map.clear()
    }

    override fun toString() : String = this.map.toString()
}