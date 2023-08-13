package khelp.uno.ui

import khelp.engine3d.extensions.alpha
import khelp.engine3d.extensions.blue
import khelp.engine3d.extensions.green
import khelp.engine3d.extensions.red
import khelp.engine3d.gui.GUIMargin
import khelp.engine3d.gui.component.GUIComponent
import khelp.thread.delay
import khelp.thread.future.FutureResult
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.ui.events.MouseState
import khelp.ui.events.MouseStatus
import khelp.ui.extensions.drawImage
import khelp.uno.model.ColorSelectionStatus
import java.awt.Dimension
import java.awt.Graphics2D
import java.util.concurrent.atomic.AtomicReference

class CardColorSelectorComponent : GUIComponent()
{
    private val status = AtomicReference<ColorSelectionStatus>(ColorSelectionStatus.NO_SELECTION)
    private val colorSelectedObservableData = ObservableData<ColorSelectionStatus>(ColorSelectionStatus.NO_SELECTION)
    private var futureRestore : FutureResult<Unit>? = null
    val colorSelected : Observable<ColorSelectionStatus> = this.colorSelectedObservableData.observable

    override fun preferredSize(margin : GUIMargin) : Dimension
    {
        val image = this.status.get().image
        return Dimension(margin.width + image.width, margin.height + image.height)
    }

    override fun drawIntern(graphics2D : Graphics2D, margin : GUIMargin)
    {
        graphics2D.drawImage(margin.left, margin.height, this.status.get().image)
    }

    override fun mouseState(mouseState : MouseState) : Boolean
    {
        val image = this.status.get().image
        val x = mouseState.x - this.marginLeft
        val y = mouseState.y - this.marginTop

        if (! this.visible || mouseState.mouseStatus == MouseStatus.OUTSIDE || x < 0 || y < 0 || x >= image.width || y >= image.height)
        {
            this.status.set(ColorSelectionStatus.NO_SELECTION)
            this.colorSelectedObservableData.valueIf(ColorSelectionStatus.NO_SELECTION) { colorSelectionStatus ->
                colorSelectionStatus != ColorSelectionStatus.NO_SELECTION
            }

            return false
        }

        val pixel = image.grabPixels(x, y, 1, 1)[0]
        val alpha = pixel.alpha
        val red = pixel.red
        val green = pixel.green
        val blue = pixel.blue

        val newStatus =
            when
            {
                alpha < 200                           -> ColorSelectionStatus.NO_SELECTION
                red - blue > 50 && red - green > 50   -> ColorSelectionStatus.SELECT_RED
                red - blue > 50                       -> ColorSelectionStatus.SELECT_YELLOW
                blue - red > 50 && blue - green > 50  -> ColorSelectionStatus.SELECT_BLUE
                green - blue > 50 && green - red > 50 -> ColorSelectionStatus.SELECT_GREEN
                else                                  -> ColorSelectionStatus.NO_SELECTION
            }

        this.status.set(newStatus)

        if (newStatus == ColorSelectionStatus.NO_SELECTION || mouseState.clicked)
        {
            this.futureRestore?.cancel("something happen")
            this.colorSelectedObservableData.valueIf(newStatus) { colorSelectionStatus ->
                colorSelectionStatus != newStatus
            }

            this.futureRestore = delay(16) {
                this.colorSelectedObservableData.valueIf(ColorSelectionStatus.NO_SELECTION) { colorSelectionStatus ->
                    colorSelectionStatus != ColorSelectionStatus.NO_SELECTION
                }
            }
        }

        return true
    }
}
