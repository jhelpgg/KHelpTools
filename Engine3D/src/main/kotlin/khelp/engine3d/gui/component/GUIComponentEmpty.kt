package khelp.engine3d.gui.component

import khelp.engine3d.gui.GUIMargin
import java.awt.Dimension
import java.awt.Graphics2D
import kotlin.math.max

class GUIComponentEmpty(preferredWidth : Int = 32, preferredHeight : Int = preferredWidth) : GUIComponent()
{
    private val preferredWidth : Int = max(16, preferredWidth)
    private val preferredHeight : Int = max(16, preferredHeight)

    override fun drawIntern(graphics2D : Graphics2D, margin : GUIMargin) : Unit = Unit

    override fun preferredSize(margin : GUIMargin) : Dimension =
        Dimension(margin.width + this.preferredWidth, margin.height + this.preferredHeight)
}
