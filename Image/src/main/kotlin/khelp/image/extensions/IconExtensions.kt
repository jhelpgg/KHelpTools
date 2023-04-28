package khelp.image.extensions

import khelp.image.DUMMY_IMAGE
import khelp.image.JHelpImage
import java.awt.image.BufferedImage
import javax.swing.Icon

/**
 * Convert an [Icon] to a [JHelpImage].

 * If the [Icon] is already a [JHelpImage], it is returned good cast,
 * else a new [JHelpImage] is created and the [Icon] is draw on it
 *
 * @param icon Icon to convert
 * @return Converted image
 */
fun Icon.toJHelpImage() : JHelpImage
{
    if (this is JHelpImage)
    {
        return this
    }

    val width = this.iconWidth
    val height = this.iconHeight

    if (width <= 0 || height <= 0)
    {
        return DUMMY_IMAGE
    }

    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val graphics2d = bufferedImage.createGraphics()
    this.paintIcon(null, graphics2d, 0, 0)
    bufferedImage.flush()
    graphics2d.dispose()
    return bufferedImage.toJHelpImage()
}
