package khelp.utilities.collections

import fr.jhelp.utilities.random
import khelp.utilities.elementExistsCheck

class ThrowSet<T>
{
    private val set = ArrayList<T>()

    val empty : Boolean get() = this.set.isEmpty()
    val notEmpty : Boolean get() = this.set.isNotEmpty()
    val size : Int get() = this.set.size

    fun throwIn(element : T)
    {
        if (this.set.isEmpty())
        {
            this.set.add(element)
        }
        else
        {
            this.set.add(random(0, this.set.size - 1), element)
        }
    }

    operator fun invoke() : T
    {
        val size = this.set.size
        elementExistsCheck(size > 0) { "Set is empty" }
        val index = random(0, size - 1)
        val element = this.set[index]
        this.set.removeAt(index)
        return element
    }

    fun add(elements : ThrowSet<T>)
    {
        this.set.addAll(elements.set)
    }

    fun add(elements : Collection<T>)
    {
        this.set.addAll(elements)
    }

    fun add(elements : Array<T>)
    {
        this.set.addAll(elements)
    }

    fun clear()
    {
        this.set.clear()
    }
}
