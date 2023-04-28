package khelp.image.extensions

import khelp.image.JHelpImage
import java.awt.RenderingHints
import java.awt.image.BufferedImage

fun BufferedImage.toJHelpImage() : JHelpImage
{
    val width = this.width
    val height = this.height
    var pixels = IntArray(width * height)
    pixels = this.getRGB(0, 0, width, height, pixels, 0, width)
    return JHelpImage(width, height, pixels)
}

fun BufferedImage.thumbImage(width : Int, height : Int) : JHelpImage
{
    val thumb = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val graphics2d = thumb.createGraphics()
    graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
    graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    graphics2d.drawImage(this, 0, 0, width, height, null)
    val image = thumb.toJHelpImage()
    graphics2d.dispose()
    thumb.flush()
    return image
}
