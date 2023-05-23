package khelp.engine3d.gui.component

import khelp.engine3d.gui.GUIMargin
import khelp.ui.extensions.drawImage
import khelp.ui.game.GameImage
import java.awt.Dimension
import java.awt.Graphics2D

class GUIComponentImage(image : GameImage = GameImage.DUMMY) : GUIComponent()
{
    var image : GameImage = image
        set(value)
        {
            field = value
            this.refresh()
        }

    override fun drawIntern(graphics2D : Graphics2D, margin : GUIMargin)
    {
        graphics2D.drawImage(margin.left, margin.top, this.image)
    }

    override fun preferredSize(margin : GUIMargin) : Dimension = Dimension(this.image.width + margin.width,
                                                                           this.image.height + margin.height)
}
