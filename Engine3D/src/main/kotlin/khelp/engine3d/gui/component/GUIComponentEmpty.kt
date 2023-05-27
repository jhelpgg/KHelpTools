package khelp.engine3d.gui.component

import khelp.engine3d.gui.GUIMargin
import java.awt.Dimension
import java.awt.Graphics2D

class GUIComponentEmpty : GUIComponent()
{
    override fun drawIntern(graphics2D : Graphics2D, margin : GUIMargin) : Unit = Unit

    override fun preferredSize(margin : GUIMargin) : Dimension =
        Dimension(margin.width + 16, margin.height + 16)
}
