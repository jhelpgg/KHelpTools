package khelp.ui.style

import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.ui.style.background.StyleBackground
import khelp.ui.style.background.StyleBackgroundTransparent
import khelp.ui.style.shape.StyleShape
import khelp.ui.style.shape.StyleShapeRectangle
import java.awt.Color

open class Style
{
    private val changeObservableData : ObservableData<Style> by lazy { ObservableData<Style>(this) }
    val changeStyleObservable : Observable<Style> by lazy { this.changeObservableData.observable }

    var borderColor : Color = Color.BLACK
        set(value)
        {
            val changed = field != value
            field = value

            if (changed)
            {
                this.signalChange()
            }
        }

    var shape : StyleShape = StyleShapeRectangle
        set(value)
        {
            val changed = field != value
            field = value

            if (changed)
            {
                this.signalChange()
            }
        }

    var background : StyleBackground = StyleBackgroundTransparent
        set(value)
        {
            val changed = field != value
            field = value

            if (changed)
            {
                this.signalChange()
            }
        }

    var componentHighLevel : ComponentHighLevel = ComponentHighLevel.AT_GROUND
        set(value)
        {
            val changed = field != value
            field = value

            if (changed)
            {
                this.signalChange()
            }
        }

    protected fun signalChange()
    {
        this.changeObservableData.value(this)
    }
}
