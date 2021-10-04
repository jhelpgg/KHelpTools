package khelp.ui.components

import khelp.resources.ResourcesText
import khelp.thread.TaskContext
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.thread.parallel
import khelp.ui.events.MouseManager
import khelp.ui.events.MouseState
import khelp.ui.game.GameImage
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JColorChooser
import javax.swing.JComponent

class ColorComponent(color : Color,
                     private val keyTitle : String, private val resourcesText : ResourcesText,
                     private val withTransparency : Boolean = false) : JComponent()
{
    private val colorObservableData = ObservableData<Color>(color)
    val colorObservable : Observable<Color> = this.colorObservableData.observable
    var color : Color
        get() = this.colorObservableData.value()
        set(value)
        {
            this.colorObservableData.value(value)
            this.repaint()
        }

    init
    {
        this.minimumSize = Dimension(16, 16)
        this.preferredSize = Dimension(64, 64)
        MouseManager.attachTo(this).mouseStateObservable.observedBy(TaskContext.INDEPENDENT, this::mouseState)
    }

    override fun paintComponent(graphics : Graphics)
    {
        val graphics2D = graphics as Graphics2D
        val margin = this.insets
        val x = margin.left
        val y = margin.top
        val width = this.width - margin.left - margin.right
        val height = this.height - margin.top - margin.bottom
        val paint = graphics2D.paint
        graphics2D.paint = GameImage.DARK_LIGHT_PAINT
        graphics.fillRect(x, y, width, height)
        graphics2D.paint = paint

        graphics2D.color = this.color
        graphics2D.fillRect(x, y, width, height)
    }

    private fun mouseState(mouseState : MouseState)
    {
        if (mouseState.clicked)
        {
            parallel {
                this.color =
                    JColorChooser.showDialog(this, this.resourcesText[this.keyTitle], this.color, this.withTransparency)
                    ?: this.color
            }
        }
    }
}
