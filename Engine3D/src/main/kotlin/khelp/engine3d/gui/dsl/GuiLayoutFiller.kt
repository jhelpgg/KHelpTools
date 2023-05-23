package khelp.engine3d.gui.dsl

import khelp.engine3d.gui.component.GUIComponent
import khelp.engine3d.gui.layout.GUIConstraints
import khelp.engine3d.gui.layout.GUILayout

class GuiLayoutFiller<C : GUIConstraints, L : GUILayout<C>> internal constructor(private val layout : L)
{
    infix fun GUIComponent.with(constraint : C)
    {
        this@GuiLayoutFiller.layout.add(this, constraint)
    }
}
