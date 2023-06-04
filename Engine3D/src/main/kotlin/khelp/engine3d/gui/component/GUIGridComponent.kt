package khelp.engine3d.gui.component

import khelp.engine3d.gui.GUIMargin
import khelp.engine3d.gui.model.GUIGridModel
import khelp.engine3d.gui.model.grid.GUIGridCellInsert
import khelp.engine3d.gui.model.grid.GUIGridCellMoved
import khelp.engine3d.gui.model.grid.GUIGridCellRemoved
import khelp.engine3d.gui.model.grid.GUIGridCellVisibility
import khelp.engine3d.gui.model.grid.GUIGridEvent
import khelp.engine3d.gui.model.grid.GUIGridSeveralEvents
import khelp.engine3d.gui.renderer.GUIGridCellRenderer
import khelp.thread.Mutex
import khelp.thread.TaskContext
import khelp.thread.parallel
import khelp.utilities.collections.queue.Queue
import khelp.utilities.extensions.forEachReversed
import java.awt.Dimension
import java.awt.Graphics2D
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max

class GUIGridComponent<V : Any>(model : GUIGridModel<V>,
                                private val cellRenderer : GUIGridCellRenderer<V>,
                                numberColumn : Int = 1,
                                marginHorizontal : Int = 4,
                                marginVertical : Int = 4)
    : GUIComponent()
{

    private val mutex = Mutex()
    private val queue = Queue<GUIGridEvent<V>>()
    private val cells = ArrayList<GUIComponent>()
    private val eventTreatmentLaunched = AtomicBoolean(false)
    private val numberColumn = max(1, numberColumn)
    private val marginHorizontal : Int = max(0, marginHorizontal)
    private val marginVertical : Int = max(0, marginVertical)

    init
    {
        model.events.then(TaskContext.INDEPENDENT, this::event)

        model.forEachWithVisibility { element, visible ->
            val component = this.cellRenderer.component(element)
            component.visible = visible
            this.cells.add(component)
        }
    }

    override fun drawIntern(graphics2D : Graphics2D, margin : GUIMargin)
    {
        this.mutex {
            val parentWidth = this.width - margin.width - this.marginHorizontal
            val parentHeight = this.height - margin.height - this.marginVertical
            val number = this.cells.count { component -> component.visible }
            val numberLine = (number + this.numberColumn - 1) / this.numberColumn
            val cellWidth = (parentWidth / this.numberColumn) - this.marginHorizontal
            val cellHeight = (parentHeight / numberLine) - this.marginVertical
            val xStart = margin.left + this.marginHorizontal
            val xStep = cellWidth + this.marginHorizontal
            val yStep = cellHeight + this.marginVertical
            var x = xStart
            var y = margin.top + this.marginVertical
            var column = 0

            for (component in this.cells)
            {
                if (component.visible)
                {
                    component.x = x
                    component.y = y
                    component.width = cellWidth
                    component.height = cellHeight
                    val clip = graphics2D.clip
                    val transform = graphics2D.transform
                    graphics2D.clipRect(component.x, component.y, component.width, component.height)
                    graphics2D.translate(component.x, component.y)
                    component.draw(graphics2D)
                    graphics2D.transform = transform
                    graphics2D.clip = clip

                    x += xStep
                    column = (column + 1) % this.numberColumn

                    if (column == 0)
                    {
                        x = xStart
                        y += yStep
                    }
                }
            }
        }
    }

    override fun preferredSize(margin : GUIMargin) : Dimension =
        this.mutex {
            var numberLine = 0
            var width = 0
            var height = 0
            var x = 0

            for (component in this.cells)
            {
                if (component.visible)
                {
                    if (x == 0)
                    {
                        numberLine ++
                    }

                    val size = component.preferredSize()
                    width = max(width, size.width)
                    height = max(height, size.height)

                    x = (x + 1) % this.numberColumn
                }
            }

            Dimension((width + this.marginHorizontal) * this.numberColumn + this.marginHorizontal + margin.width,
                      (height + this.marginVertical) * numberLine + this.marginVertical + margin.height)
        }


    private fun event(guiGridEvent : GUIGridEvent<V>)
    {
        this.mutex { this.queue.inQueue(guiGridEvent) }

        if (this.eventTreatmentLaunched.compareAndSet(false, true))
        {
            parallel { this.eventTreatment() }
        }
    }

    private fun eventTreatment()
    {
        while (this.eventTreatmentLaunched.get())
        {
            var eventNullable : GUIGridEvent<V>? = null


            this.mutex {
                eventNullable = if (this.queue.notEmpty) this.queue.outQueue() else null
            }

            if (eventNullable == null)
            {
                this.eventTreatmentLaunched.set(false)
                return
            }

            val event = eventNullable !!

            this.mutex {
                when (event)
                {
                    is GUIGridSeveralEvents  -> event.events.forEachReversed(this.queue::ahead)
                    is GUIGridCellInsert     -> this.insert(event)
                    is GUIGridCellRemoved    -> this.remove(event)
                    is GUIGridCellVisibility -> this.visibility(event)
                    is GUIGridCellMoved      -> this.moved(event)
                }
            }
        }
    }

    private fun insert(event : GUIGridCellInsert<V>)
    {
        val component = this.cellRenderer.component(event.element)
        component.visible = event.visible

        if (event.index >= this.cells.size)
        {
            this.cells.add(component)
        }
        else
        {
            this.cells.add(event.index, component)
        }
    }

    private fun remove(event : GUIGridCellRemoved<V>)
    {
        this.cells.removeAt(event.index)
    }

    private fun visibility(event : GUIGridCellVisibility<V>)
    {
        this.cells[event.index].visible = event.visible
    }

    private fun moved(event : GUIGridCellMoved<V>)
    {
        val component = this.cellRenderer.component(event.element)
        component.visible = event.visible
        this.cells[event.newIndex] = component
    }
}
