package khelp.utilities.collections.queue

internal class QueueIterator<T>(private var current: QueueElement<T>?) : Iterator<T>
{
    override fun hasNext(): Boolean =
        this.current != null

    override fun next(): T
    {
        val value = this.current?.element ?: throw NoSuchElementException("No more element !")
        this.current = this.current!!.next
        return value
    }
}