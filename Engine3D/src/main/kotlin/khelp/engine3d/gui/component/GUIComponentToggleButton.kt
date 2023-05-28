package khelp.engine3d.gui.component

import khelp.engine3d.gui.GUIMargin
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.ui.events.MouseState
import khelp.ui.events.MouseStatus
import java.awt.Dimension
import java.awt.Graphics2D
import kotlin.math.max

class GUIComponentToggleButton(private val normal : GUIComponent,
                               private val over : GUIComponent,
                               private val down : GUIComponent,
                               private val disabled : GUIComponent) : GUIComponent()
{
    private val selectedObservableData = ObservableData<Boolean>(false)
    val selected : Observable<Boolean> = this.selectedObservableData.observable
    var enabled : Boolean = true

    private var isOver = false
    private var mayDeselect = false

    fun select(select : Boolean)
    {
        if (this.enabled)
        {
            this.mayDeselect = false
            this.selectedObservableData.valueIf(select) { value -> value != select }
        }
    }

    override fun drawIntern(graphics2D : Graphics2D, margin : GUIMargin)
    {
        val component =
            when
            {
                ! this.enabled                      -> this.disabled
                this.selectedObservableData.value() -> this.down
                this.isOver                         -> this.over
                else                                -> this.normal
            }

        component.margin = this.margin
        component.x = 0
        component.y = 0
        component.width = this.width
        component.height = this.height
        component.draw(graphics2D)
    }

    override fun preferredSize(margin : GUIMargin) : Dimension
    {
        var width = 16
        var height = 16

        var preferred = this.normal.preferredSize()
        width = max(width, preferred.width)
        height = max(height, preferred.height)

        preferred = this.over.preferredSize()
        width = max(width, preferred.width)
        height = max(height, preferred.height)

        preferred = this.down.preferredSize()
        width = max(width, preferred.width)
        height = max(height, preferred.height)

        preferred = this.disabled.preferredSize()
        width = max(width, preferred.width)
        height = max(height, preferred.height)

        return Dimension(width + margin.width, height + margin.height)
    }

    override fun mouseState(mouseState : MouseState) : Boolean
    {
        if (this.enabled)
        {
            when
            {
                mouseState.mouseStatus == MouseStatus.ENTER ->
                    if (! this.isOver)
                    {
                        this.isOver = true
                        this.mayDeselect = false
                    }

                mouseState.mouseStatus == MouseStatus.EXIT  ->
                    if (this.isOver)
                    {
                        this.isOver = false
                        this.mayDeselect = false
                    }

                mouseState.leftButtonDown                   ->
                    if (this.selectedObservableData.value())
                    {
                        this.mayDeselect = true
                    }
                    else
                    {
                        this.selectedObservableData.value(true)
                    }

                ! mouseState.leftButtonDown                 ->
                    if (this.mayDeselect)
                    {
                        this.mayDeselect = false
                        this.selectedObservableData.value(false)
                    }
            }

            return true
        }

        return false
    }
}
