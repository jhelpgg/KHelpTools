package khelp.engine3d.gui.dsl

import khelp.engine3d.gui.component.GUIComponent
import khelp.engine3d.gui.layout.vertical.GUIVerticalConstraint
import khelp.engine3d.gui.layout.vertical.GUIVerticalLayout

class GUIVerticalFiller internal constructor(private val layout : GUIVerticalLayout)
{
    var margin : Int
        get() = this.layout.margin
        set(value)
        {
            this.layout.margin = value
        }

    val GUIComponent.left : Unit
        get()
        {
            this@GUIVerticalFiller.layout.add(this, GUIVerticalConstraint.LEFT)
        }

    val GUIComponent.center : Unit
        get()
        {
            this@GUIVerticalFiller.layout.add(this, GUIVerticalConstraint.CENTER)
        }

    val GUIComponent.right : Unit
        get()
        {
            this@GUIVerticalFiller.layout.add(this, GUIVerticalConstraint.RIGHT)
        }
}
