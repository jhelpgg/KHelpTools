package khelp.engine3d.gui.component

import khelp.engine3d.gui.GUI
import khelp.engine3d.gui.GUIMargin
import khelp.engine3d.gui.layout.GUIConstraints
import khelp.engine3d.gui.layout.GUILayout
import khelp.thread.observable.ObservableData
import khelp.ui.extensions.color
import khelp.ui.extensions.semiVisible
import khelp.ui.style.ComponentHighLevel
import khelp.ui.style.background.StyleBackground
import khelp.ui.style.background.StyleBackgroundColor
import khelp.ui.style.shape.StyleShape
import khelp.ui.style.shape.StyleShapeRoundRectangle
import khelp.ui.utilities.colors.Green
import java.awt.Color

class GUIDialog<C : GUIConstraints, L : GUILayout<C>> internal constructor(internal val panel : GUIComponentPanel<C, L>,
                                                                           private val gui : GUI)
{
    private val showObservableData = ObservableData<Boolean>(false)
    val showing = this.showObservableData.observable
    var margin : GUIMargin
        get() = this.panel.margin
        set(value)
        {
            this.panel.margin = value
        }
    var borderColor : Color
        get() = this.panel.borderColor
        set(value)
        {
            this.panel.borderColor = value
        }
    var shape : StyleShape
        get() = this.panel.shape
        set(value)
        {
            this.panel.shape = value
        }
    var background : StyleBackground
        get() = this.panel.background
        set(value)
        {
            this.panel.background = value
        }

    var marginLeft : Int
        get() = this.panel.margin.left
        set(value)
        {
            this.panel.margin = this.panel.margin.left(value)
        }

    var marginRight : Int
        get() = this.panel.margin.right
        set(value)
        {
            this.panel.margin = this.panel.margin.right(value)
        }

    var marginTop : Int
        get() = this.panel.margin.top
        set(value)
        {
            this.panel.margin = this.panel.margin.top(value)
        }

    var marginBottom : Int
        get() = this.panel.margin.bottom
        set(value)
        {
            this.panel.margin = this.panel.margin.bottom(value)
        }

    init
    {
        this.panel.shape = StyleShapeRoundRectangle
        this.panel.background = StyleBackgroundColor(Green.GREEN_0300.color.semiVisible)
        this.panel.borderColor = Green.GREEN_0050.color
        this.panel.margin = GUIMargin(8, 8, 8, 8)
        this.panel.componentHighLevel = ComponentHighLevel.NEAR_GROUND
    }

    fun show()
    {
        if (this.showObservableData.valueIf(value = true) { value -> ! value })
        {
            this.gui.show(this)
        }
    }

    fun close()
    {
        if (this.showObservableData.valueIf(value = false) { value -> value })
        {
            this.gui.hide(this)
        }
    }

}
