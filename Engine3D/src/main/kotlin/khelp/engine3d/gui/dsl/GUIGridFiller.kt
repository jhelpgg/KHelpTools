package khelp.engine3d.gui.dsl

import khelp.engine3d.gui.component.GUIComponent
import khelp.engine3d.gui.layout.grid.GUIGridConstraint
import khelp.engine3d.gui.layout.grid.GUIGridLayout

class GUIGridFiller internal constructor(private val layout : GUIGridLayout)
{
    operator fun GUIComponent.unaryPlus()
    {
        this@GUIGridFiller.layout.add(this, GUIGridConstraint)
    }
}
