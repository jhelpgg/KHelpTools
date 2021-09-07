package khelp.ui.game

import khelp.ui.utilities.PercentGraphics
import java.awt.Color
import java.awt.Component
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import javax.swing.Icon


class GameImage(val width : Int, val height : Int) : Icon
{
    companion object
    {
        val DUMMY = GameImage(1, 1)
    }

    /**
     * Image for draw in a swing component
     */
    internal val image : BufferedImage = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB)
    internal var refreshListener : () -> Unit = {}

    fun clear(color : Color)
    {
        val col = color.rgb
        val length = this.width * this.height
        val pixels = IntArray(length) { col }
        this.image.setRGB(0, 0, this.width, this.height, pixels, 0, this.width)
        this.refresh()
    }

    fun draw(drawer : (Graphics2D) -> Unit)
    {
        val graphics = this.image.createGraphics()
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY)
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE)
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                                  RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY)
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        drawer(graphics)
        graphics.dispose()
        this.refresh()
    }

    fun drawPercent(drawer : (PercentGraphics) -> Unit)
    {
        this.draw { graphics -> drawer(PercentGraphics(graphics, this.width, this.height)) }
    }

    override fun paintIcon(ignored : Component?, graphics : Graphics, x : Int, y : Int)
    {
        graphics.drawImage(this.image, x, y, null)
    }

    override fun getIconWidth() : Int = this.width

    override fun getIconHeight() : Int = this.height

    private fun refresh()
    {
        this.image.flush()
        this.refreshListener()
    }
}