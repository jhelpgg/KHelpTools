package khelp.utilities.collections

import java.util.concurrent.atomic.AtomicBoolean

class IteratorSelected<T : Any>(internal val criteria: (T) -> Boolean, internal val iterator: Iterator<T>) : Iterator<T>
{
    private val hasNext = AtomicBoolean(false)
    private lateinit var currentValue: T

    init
    {
        this.searchNext()
    }

    override fun hasNext(): Boolean =
        this.hasNext.get()

    override fun next(): T
    {
        if (this.hasNext.get())
        {
            val value = this.currentValue
            this.searchNext()
            return value
        }

        throw NoSuchElementException("No more elements to iterates")
    }

    private fun searchNext()
    {
        var current: T

        while (this.iterator.hasNext())
        {
            current = this.iterator.next()

            if (this.criteria(current))
            {
                this.currentValue = current
                this.hasNext.set(true)
                return
            }
        }

        this.hasNext.set(false)
    }
}