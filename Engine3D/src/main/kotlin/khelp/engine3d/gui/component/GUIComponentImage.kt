package khelp.engine3d.gui.component

import khelp.engine3d.gui.GUIMargin
import khelp.ui.game.GameImage
import java.awt.Dimension
import java.awt.Graphics2D
import kotlin.math.min

class GUIComponentImage(var image : GameImage = GameImage.DARK_LIGHT,
                        var imageConstraint : ImageConstraint = ImageConstraint.FIT_PROPORTION) : GUIComponent()
{
    override fun drawIntern(graphics2D : Graphics2D, margin : GUIMargin)
    {
        val parentWidth = this.width - margin.width
        val parentHeight = this.height - margin.height
        var x = margin.left
        var y = margin.top
        var width = this.image.width
        var height = this.image.height

        when (this.imageConstraint)
        {
            ImageConstraint.CUT_CENTER     ->
            {
                x += (parentWidth - width) / 2
                y += (parentHeight - height) / 2
            }

            ImageConstraint.FIT            ->
            {
                width = parentWidth
                height = parentHeight
            }

            ImageConstraint.FIT_PROPORTION ->
            {
                val widthFactor = parentWidth.toDouble() / width.toDouble()
                val heightFactor = parentHeight.toDouble() / height.toDouble()
                val factor = min(widthFactor, heightFactor)

                width = (width * factor).toInt()
                height = (height * factor).toInt()

                x += (parentWidth - width) / 2
                y += (parentHeight - height) / 2
            }
        }

        this.image.drawOn(graphics2D, x, y, width, height)
    }

    override fun preferredSize(margin : GUIMargin) : Dimension = Dimension(this.image.width + margin.width,
                                                                           this.image.height + margin.height)
}
