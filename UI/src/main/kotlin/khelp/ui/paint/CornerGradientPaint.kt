package khelp.ui.paint

import java.awt.Color
import java.awt.Paint
import java.awt.PaintContext
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.Transparency
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.awt.image.ColorModel

class CornerGradientPaint(val colorLeftUp : Color, val colorRightUp : Color,
                          val colorLeftBottom : Color, val colorRightBottom : Color) : Paint
{
    private val transparency =
        if (this.colorLeftUp.alpha == 0xFF && this.colorRightUp.alpha == 0xFF &&
            this.colorLeftBottom.alpha == 0xFF && this.colorRightBottom.alpha == 0xFF)
        {
            Transparency.OPAQUE
        }
        else
        {
            Transparency.TRANSLUCENT
        }

    override fun getTransparency() : Int = this.transparency

    override fun createContext(colorModel : ColorModel,
                               deviceBounds : Rectangle, userBounds : Rectangle2D,
                               affineTransform : AffineTransform, renderingHints : RenderingHints) : PaintContext =
        CornerGradientPaintContext(colorModel, deviceBounds,
                                   this.colorLeftUp, this.colorRightUp,
                                   this.colorLeftBottom, this.colorRightBottom)
}
