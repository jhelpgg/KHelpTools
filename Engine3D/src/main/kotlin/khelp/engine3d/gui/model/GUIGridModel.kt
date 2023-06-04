package khelp.engine3d.gui.model

import khelp.engine3d.gui.model.grid.GUIGridCellInsert
import khelp.engine3d.gui.model.grid.GUIGridCellMoved
import khelp.engine3d.gui.model.grid.GUIGridCellRemoved
import khelp.engine3d.gui.model.grid.GUIGridCellVisibility
import khelp.engine3d.gui.model.grid.GUIGridElementIndexed
import khelp.engine3d.gui.model.grid.GUIGridElementIndexedComparator
import khelp.engine3d.gui.model.grid.GUIGridEvent
import khelp.engine3d.gui.model.grid.GUIGridSeveralEvents
import khelp.thread.Mutex
import khelp.thread.flow.Flow
import khelp.thread.flow.FlowData
import khelp.utilities.collections.queue.Queue
import khelp.utilities.extensions.comparator
import khelp.utilities.extensions.forEachReversedWithIndex
import khelp.utilities.extensions.inverted
import khelp.utilities.optional.Optional
import kotlin.math.max

class GUIGridModel<V : Any>
{
    private val mutex = Mutex()
    private val data = ArrayList<GUIGridElementIndexed<V>>()
    private val eventsFlowData = FlowData<GUIGridEvent<V>>()
    var filter : (V) -> Boolean = { true }
        set(value)
        {
            field = value
            this.applyFilter()
        }
    val events : Flow<GUIGridEvent<V>> = this.eventsFlowData.flow

    val size : Int get() = this.mutex { this.data.size }
    val empty : Boolean get() = this.mutex { this.data.isEmpty() }
    val notEmpty : Boolean get() = this.mutex { this.data.isNotEmpty() }

    operator fun get(index : Int) : V = this.mutex { this.data[index].element }

    fun forEach(action : (V) -> Unit)
    {
        val queue = Queue<V>()

        this.mutex {
            for (element in this.data)
            {
                queue.inQueue(element.element)
            }
        }

        while (queue.isNotEmpty())
        {
            action(queue.outQueue())
        }
    }

    fun collect(condition : (V) -> Boolean, action : (V) -> Unit)
    {
        val queue = Queue<V>()

        this.mutex {
            for (element in this.data)
            {
                if (condition(element.element))
                {
                    queue.inQueue(element.element)
                }
            }
        }

        while (queue.isNotEmpty())
        {
            action(queue.outQueue())
        }
    }

    fun add(element : V)
    {
        this.mutex {
            this.data.add(GUIGridElementIndexed(element))
            this.eventsFlowData.publish(GUIGridCellInsert(this.data.size - 1, this.filter(element), element))
        }
    }

    fun addAll(iterable : Iterable<V>)
    {
        this.mutex {
            val events = ArrayList<GUIGridEvent<V>>()
            var index = this.data.size

            for (element in iterable)
            {
                this.data.add(GUIGridElementIndexed(element))
                events.add(GUIGridCellInsert(index, this.filter(element), element))
                index ++
            }

            if (events.isNotEmpty())
            {
                this.eventsFlowData.publish(GUIGridSeveralEvents(events))
            }
        }
    }

    fun insert(index : Int, element : V)
    {
        val indexPositive = max(0, index)

        this.mutex {
            if (indexPositive >= this.data.size)
            {
                this.data.add(GUIGridElementIndexed(element))
                this.eventsFlowData.publish(
                    GUIGridCellInsert(this.data.size - 1, this.filter(element), element))
            }
            else
            {
                this.data.add(indexPositive, GUIGridElementIndexed(element))
                this.eventsFlowData.publish(GUIGridCellInsert(index, this.filter(element), element))
            }
        }
    }

    fun insertAll(index : Int, iterable : Iterable<V>)
    {
        val indexPositive = max(0, index)

        this.mutex {
            val events = ArrayList<GUIGridEvent<V>>()

            if (indexPositive >= this.data.size)
            {
                var indexInserted = this.data.size

                for (element in iterable)
                {
                    this.data.add(GUIGridElementIndexed(element))
                    events.add(GUIGridCellInsert(indexInserted, this.filter(element), element))
                    indexInserted ++
                }
            }
            else
            {
                var indexInserted = indexPositive

                for (element in iterable)
                {
                    this.data.add(indexInserted, GUIGridElementIndexed(element))
                    events.add(GUIGridCellInsert(indexInserted, this.filter(element), element))
                    indexInserted ++
                }
            }


            if (events.isNotEmpty())
            {
                this.eventsFlowData.publish(GUIGridSeveralEvents(events))
            }
        }

    }

    fun remove(index : Int) : V =
        this.mutex {
            if (index < 0 || index >= this.data.size)
            {
                throw IndexOutOfBoundsException("index must be in [0, ${this.data.size} [")
            }

            val value = this.data.removeAt(index)
            this.eventsFlowData.publish(GUIGridCellRemoved(index))
            value.element
        }

    fun removeIf(condition : (V) -> Boolean)
    {
        this.mutex {
            val events = ArrayList<GUIGridEvent<V>>()

            this.data.forEachReversedWithIndex { index, element ->
                if (condition(element.element))
                {
                    this.data.removeAt(index)
                    events.add(GUIGridCellRemoved(index))
                }
            }

            if (events.isNotEmpty())
            {
                this.eventsFlowData.publish(GUIGridSeveralEvents(events))
            }
        }
    }

    fun sort(comparator : (V, V) -> Int)
    {
        this.sort(comparator.comparator)
    }

    fun sort(comparator : Comparator<V>)
    {
        this.mutex {
            // Set the initial indexes
            this.data.forEachIndexed { index, guiGridElementIndexed ->
                guiGridElementIndexed.index = index
            }

            // Sort the data
            this.data.sortWith(GUIGridElementIndexedComparator(comparator))

            // Collect and publish the changes
            val events = ArrayList<GUIGridEvent<V>>()

            this.data.forEachIndexed { index, guiGridElementIndexed ->
                if (guiGridElementIndexed.index != index)
                {
                    events.add(GUIGridCellMoved(guiGridElementIndexed.index, index,
                                                guiGridElementIndexed.element,
                                                this.filter(guiGridElementIndexed.element)))
                }
            }

            if (events.isNotEmpty())
            {
                this.eventsFlowData.publish(GUIGridSeveralEvents(events))
            }
        }
    }

    fun first(condition : (V) -> Boolean) : Optional<V>
    {
        var response : Optional<V> = Optional.empty()

        this.mutex {
            for (element in this.data)
            {
                if (condition(element.element))
                {
                    response = Optional.value(element.element)
                    break
                }
            }
        }

        return response
    }

    fun firstIndex(startIndex : Int, condition : (V) -> Boolean) : Optional<Pair<Int, V>>
    {
        var response : Optional<Pair<Int, V>> = Optional.empty()

        this.mutex {
            for (index in startIndex until this.data.size)
            {
                val element = this.data[index].element

                if (condition(element))
                {
                    response = Optional.value(Pair(index, element))
                    break
                }
            }
        }

        return response
    }

    fun last(condition : (V) -> Boolean) : Optional<V>
    {
        var response : Optional<V> = Optional.empty()

        this.mutex {
            for (element in this.data.inverted())
            {
                if (condition(element.element))
                {
                    response = Optional.value(element.element)
                    break
                }
            }
        }

        return response
    }

    fun lastIndex(startIndex : Int, condition : (V) -> Boolean) : Optional<Pair<Int, V>>
    {
        var response : Optional<Pair<Int, V>> = Optional.empty()

        this.mutex {
            for (index in startIndex downTo 0)
            {
                val element = this.data[index].element

                if (condition(element))
                {
                    response = Optional.value(Pair(index, element))
                    break
                }
            }
        }

        return response
    }

    fun any(condition : (V) -> Boolean) : Boolean = this.first(condition).present

    fun none(condition : (V) -> Boolean) : Boolean = this.first(condition).absent

    internal fun forEachWithVisibility(action : (V, Boolean) -> Unit)
    {
        val queue = Queue<Pair<V, Boolean>>()

        this.mutex {
            for (element in this.data)
            {
                queue.inQueue(Pair(element.element, this.filter(element.element)))
            }
        }

        while (queue.isNotEmpty())
        {
            val (element, visible) = queue.outQueue()
            action(element, visible)
        }
    }


    private fun applyFilter()
    {
        this.mutex {
            val events = ArrayList<GUIGridEvent<V>>()

            this.data.forEachIndexed { index, element ->
                events.add(GUIGridCellVisibility(index, this.filter(element.element)))
            }

            if (events.isNotEmpty())
            {
                this.eventsFlowData.publish(GUIGridSeveralEvents(events))
            }
        }
    }
}
