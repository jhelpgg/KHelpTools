package khelp.ui.style.background

import khelp.resources.Resources
import khelp.ui.extensions.drawImage
import khelp.ui.game.GameImage
import java.awt.Graphics2D
import java.awt.Shape
import java.util.Objects

class StyleBackgroundImage(private val keyImage : String, private val resources : Resources,
                           private val repeat : Boolean = false) : StyleBackground
{
    override fun applyOnShape(graphics2D : Graphics2D, shape : Shape)
    {
        val clip = graphics2D.clip
        graphics2D.clip(shape)
        val bounds = shape.bounds
        val image = GameImage.load(this.keyImage, this.resources)

        if (this.repeat)
        {
            val imageWidth = image.width
            val imageHeight = image.height
            val xMax = bounds.x + bounds.width
            val yMax = bounds.y + bounds.height

            for (yy in bounds.y .. yMax step imageHeight)
            {
                for (xx in bounds.x .. xMax step imageWidth)
                {
                    graphics2D.drawImage(xx, yy, image)
                }
            }
        }
        else
        {
            graphics2D.drawImage(bounds.x, bounds.y, bounds.width, bounds.height, image)
        }

        graphics2D.clip = clip
    }

    override fun hashCode() : Int = Objects.hash(this.keyImage, this.repeat)

    override fun equals(other : Any?) : Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is StyleBackgroundImage)
        {
            return false
        }

        return this.keyImage == other.keyImage && this.repeat == other.repeat
    }
}
