package khelp.ui.game

import khelp.resources.Resources
import khelp.resources.defaultResources
import khelp.ui.utilities.PercentGraphics
import khelp.ui.utilities.TRANSPARENT
import java.awt.Color
import java.awt.Component
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO
import javax.swing.Icon


class GameImage(val width : Int, val height : Int) : Icon
{
    companion object
    {
        val DUMMY = GameImage(1, 1)

        fun load(inputStream : InputStream) =
            this.load(inputStream, - 1, - 1)

        fun loadThumbnail(inputStream : InputStream, imageWidth : Int, imageHeight : Int) =
            this.load(inputStream, imageWidth, imageHeight)

        fun load(path : String, resources : Resources) =
            this.load(path, resources, - 1, - 1)

        fun loadThumbnail(path : String, resources : Resources, imageWidth : Int, imageHeight : Int) =
            this.load(path, resources, imageWidth, imageHeight)

        private fun load(inputStream : InputStream, imageWidth : Int, imageHeight : Int) : GameImage =
            try
            {
                val image = ImageIO.read(inputStream)
                val width = image.width
                val height = image.height
                val targetWidth = if (imageWidth > 0) imageWidth else width
                val targetHeight = if (imageHeight > 0) imageHeight else height

                if (width <= 0 || height <= 0)
                {
                    GameImage.DUMMY
                }
                else
                {
                    val gameImage = GameImage(targetWidth, targetHeight)
                    gameImage.clear(TRANSPARENT)
                    gameImage.draw { graphics2D ->
                        graphics2D.drawImage(image,
                                             0, 0, targetWidth, targetHeight,
                                             0, 0, width, height,
                                             null)
                    }
                    gameImage
                }
            }
            catch (_ : Exception)
            {
                GameImage.DUMMY
            }

        private fun load(path : String, resources : Resources, imageWidth : Int, imageHeight : Int) : GameImage
        {
            val inputStream =
                when
                {
                    resources.exists(path)        -> resources.inputStream(path)
                    defaultResources.exists(path) -> defaultResources.inputStream(path)
                    else                          -> return GameImage.DUMMY
                }

            val image = GameImage.load(inputStream, imageWidth, imageHeight)

            try
            {
                inputStream.close()
            }
            catch (_ : Exception)
            {
            }

            return image
        }
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