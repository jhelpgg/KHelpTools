package khelp.utilities.collections.queue

/**
 * Queue of elements
 */
class Queue<T> : Collection<T>
{
    private var head: QueueElement<T>? = null
    private var tail: QueueElement<T>? = null
    private var count = 0

    val empty get() = this.head == null

    val notEmpty get() = this.head != null

    /**
     * Add element at the the end of the queue
     */
    fun inQueue(element: T)
    {
        if (this.head === null)
        {
            this.count = 1
            this.head = QueueElement(element)
            this.tail = this.head
            return
        }

        this.count++
        this.tail!!.next = QueueElement(element)
        this.tail = this.tail!!.next
    }

    /**
     * Add element at first element of the queue (It becomes priority)
     *
     * Don't abuse call of it, it is for very urgent things, this method break the "queue logic"
     */
    fun ahead(element: T)
    {
        if (this.head === null)
        {
            this.count = 1
            this.head = QueueElement(element)
            this.tail = this.head
            return
        }

        this.count++
        this.head = QueueElement(element, this.head)
    }

    /**
     * Remove and return the first element of the queue
     */
    @Throws
    fun outQueue(): T
    {
        if (this.head == null)
        {
            throw IllegalStateException("The queue is empty")
        }

        this.count--
        val element = this.head!!.element
        this.head = this.head!!.next

        if (this.head == null)
        {
            this.tail = null
        }

        return element
    }

    /**
     * Look the first element of the queue
     */
    @Throws
    fun peek(): T =
        this.head?.element ?: throw IllegalStateException("The queue is empty")

    /**
     * Do action on each queue elements
     */
    fun forEach(action: (T) -> Unit)
    {
        var queueElement = this.head

        while (queueElement != null)
        {
            action(queueElement.element)
            queueElement = queueElement.next
        }
    }

    /**
     * Remove all elements in queue that match given condition
     */
    fun removeIf(condition: (T) -> Boolean)
    {
        var parent: QueueElement<T>? = null
        var current = this.head

        while (current != null)
        {
            if (condition(current.element))
            {
                this.count--

                if (parent == null)
                {
                    this.head = this.head!!.next

                    if (this.head == null)
                    {
                        this.tail = null
                        return
                    }
                }
                else
                {
                    parent.next = current.next
                }
            }
            else
            {
                parent = current
            }

            current = current.next
        }
    }

    /**
     * Remove all queue elements.
     * Queue becomes empty
     */
    fun clear()
    {
        this.count = 0
        this.head = null
        this.tail = null
    }

    override fun iterator(): Iterator<T> =
        QueueIterator(this.head)

    override operator fun contains(element: T): Boolean
    {
        var current = this.head

        while (current != null)
        {
            if (current.element == element)
            {
                return true
            }

            current = current.next
        }

        return false
    }

    override fun containsAll(elements: Collection<T>): Boolean
    {
        for (element in elements)
        {
            if (element !in this)
            {
                return false
            }
        }

        return true
    }

    override fun toString(): String
    {
        val stringBuilder = StringBuilder()
        stringBuilder.append('[')
        var current = this.head

        while (current != null)
        {
            stringBuilder.append(current.element)
            current = current.next

            if (current != null)
            {
                stringBuilder.append(" ; ")
            }
        }

        stringBuilder.append(']')
        return stringBuilder.toString()
    }

    override val size: Int
        get() = this.count

    override fun isEmpty(): Boolean =
        this.head == null
}