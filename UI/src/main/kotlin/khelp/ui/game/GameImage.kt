package khelp.ui.game

import khelp.resources.Resources
import khelp.thread.flow.Flow
import khelp.thread.flow.FlowData
import khelp.ui.extensions.u
import khelp.ui.extensions.v
import khelp.ui.extensions.y
import khelp.ui.utilities.FONT_RENDER_CONTEXT
import khelp.ui.utilities.PercentGraphics
import khelp.ui.utilities.TRANSPARENT
import khelp.ui.utilities.yuvToBlue
import khelp.ui.utilities.yuvToGreen
import khelp.ui.utilities.yuvToRed
import khelp.utilities.extensions.limit_0_255
import java.awt.Color
import java.awt.Component
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Paint
import java.awt.RenderingHints
import java.awt.TexturePaint
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO
import javax.swing.Icon
import kotlin.math.max


class GameImage(val width : Int, val height : Int) : Icon
{
    companion object
    {
        val DUMMY = GameImage(1, 1)
        val DARK_LIGHT : GameImage by lazy {
            val gameImage = GameImage(32, 32)
            gameImage.clear(Color.LIGHT_GRAY)
            gameImage.drawPercent { percentGraphics ->
                percentGraphics.color = Color.DARK_GRAY
                percentGraphics.fillRectangle(0.0, 0.0, 0.5, 0.5)
                percentGraphics.fillRectangle(0.5, 0.5, 0.5, 0.5)
            }
            gameImage
        }
        val DARK_LIGHT_PAINT : Paint by lazy {
            TexturePaint(GameImage.DARK_LIGHT.image,
                         Rectangle2D.Double(0.0, 0.0,
                                            GameImage.DARK_LIGHT.width.toDouble(),
                                            GameImage.DARK_LIGHT.height.toDouble()))
        }

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
            catch (exception : Exception)
            {
                GameImage.DUMMY
            }

        private fun load(path : String, resources : Resources, imageWidth : Int, imageHeight : Int) : GameImage =
            GameImageCache.image(path, imageWidth, imageHeight, resources)
    }

    /**
     * Image for draw in a swing component
     */
    internal val image : BufferedImage = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB)
    private val refreshFlowData = FlowData<Unit>()
    val refreshFlow : Flow<Unit> = this.refreshFlowData.flow

    fun clear(color : Color, refresh : Boolean = true)
    {
        val col = color.rgb
        val length = this.width * this.height
        val pixels = IntArray(length) { col }
        this.image.setRGB(0, 0, this.width, this.height, pixels, 0, this.width)

        if (refresh)
        {
            this.refresh()
        }
    }

    fun draw(drawer : (Graphics2D) -> Unit)
    {
        val graphics = this.image.createGraphics()
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                                  RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY)

        if (FONT_RENDER_CONTEXT.isAntiAliased)
        {
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        }
        else
        {
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF)
        }

        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY)
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

        if (FONT_RENDER_CONTEXT.usesFractionalMetrics())
        {
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                                      RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        }
        else
        {
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                                      RenderingHints.VALUE_FRACTIONALMETRICS_OFF)
        }

        drawer(graphics)
        graphics.dispose()
        this.refresh()
    }

    fun drawPercent(drawer : (PercentGraphics) -> Unit)
    {
        this.draw { graphics -> drawer(PercentGraphics(graphics, this.width, this.height)) }
    }

    fun drawOn(graphics : Graphics, x : Int, y : Int)
    {
        graphics.drawImage(this.image, x, y, null)
    }

    fun drawOn(graphics : Graphics, x : Int, y : Int, width : Int, height : Int)
    {
        graphics.drawImage(this.image, x, y, width, height, null)
    }

    fun drawOnPart(graphics : Graphics, x : Int, y : Int, imageX : Int, imageY : Int, width : Int, height : Int)
    {
        graphics.drawImage(this.image,
                           x, y, x + width, y + height,
                           imageX, imageY, imageX + width, imageY + height,
                           null)
    }

    override fun paintIcon(ignored : Component?, graphics : Graphics, x : Int, y : Int)
    {
        graphics.drawImage(this.image, x, y, null)
    }

    override fun getIconWidth() : Int = this.width

    override fun getIconHeight() : Int = this.height

    fun grabPixels(x : Int = 0, y : Int = 0, width : Int = this.width - x, height : Int = this.height - y) : IntArray
    {
        if (x < 0 || width <= 0 || y < 0 || height <= 0 || x + width > this.width || y + height > this.height)
        {
            throw IllegalArgumentException(
                "The image is ${this.width}x${this.height} but its required : ($x, $y) ${width}x$height that is out of bounds")
        }

        val pixels = IntArray(width * height)
        this.image.getRGB(x, y, width, height, pixels, 0, width)
        return pixels
    }

    fun putPixels(x : Int, y : Int, width : Int, height : Int, pixels : IntArray)
    {
        if (x < 0 || width <= 0 || y < 0 || height <= 0 || x + width > this.width || y + height > this.height)
        {
            throw IllegalArgumentException(
                "The image is ${this.width}x${this.height} but its required : ($x, $y) ${width}x$height that is out of bounds")
        }

        if (pixels.size != width * height)
        {
            throw IllegalArgumentException(
                "Pixels array must have size $width*$height=${width * height}, but it is ${pixels.size}")
        }

        this.image.setRGB(x, y, width, height, pixels, 0, width)
        this.refresh()
    }

    fun resize(targetWidth : Int, targetHeight : Int) : GameImage
    {
        val width = max(1, targetWidth)
        val height = max(1, targetHeight)

        if (this.width == width && this.height == height)
        {
            return this
        }

        val resized = GameImage(width, height)
        resized.clear(TRANSPARENT)
        resized.drawPercent { percentGraphics -> percentGraphics.drawImageFit(0.0, 0.0, this.image, 1.0, 1.0) }
        return resized
    }

    fun copy() : GameImage
    {
        val copy = GameImage(this.width, this.height)
        copy.putPixels(0, 0, this.width, this.height, this.grabPixels())
        return copy
    }

    fun gray()
    {
        this.manipulatePixels { pixels ->
            for (index in pixels.indices)
            {
                val color = pixels[index]
                val y = color.y.toInt()
                    .limit_0_255
                pixels[index] = (color and 0xFF000000.toInt()) or (y shl 16) or (y shl 8) or y
            }
        }
    }

    fun tint(color : Color)
    {
        val red = color.red
        val green = color.green
        val blue = color.blue

        this.manipulatePixels { pixels ->
            for (index in pixels.indices)
            {
                val col = pixels[index]
                val y = col.y.toInt()
                    .limit_0_255
                pixels[index] =
                    (col and 0xFF000000.toInt()) or (((red * y) shr 8) shl 16) or (((green * y) shr 8) shl 8) or ((blue * y) shr 8)
            }
        }
    }

    fun contrast(contrast : Double)
    {
        this.manipulatePixels { pixels ->
            for (index in pixels.indices)
            {
                val color = pixels[index]
                val y = color.y * contrast
                val u = color.u
                val v = color.v
                val red = yuvToRed(y, u, v)
                val green = yuvToGreen(y, u, v)
                val blue = yuvToBlue(y, u, v)
                pixels[index] = (color and 0xFF000000.toInt()) or (red shl 16) or (green shl 8) or blue
            }
        }
    }

    /**
     * Compute the image rotated from 180 degree
     *
     * @return Rotated image
     */
    fun rotate180() : GameImage
    {
        val source = this.grabPixels()
        val width = this.width
        val height = this.height
        val length = width * height
        val rotated = IntArray(length)

        var pix = 0
        var pixR = length - 1
        while (pixR >= 0)
        {
            rotated[pixR] = source[pix]
            pix ++
            pixR --
        }

        val result = GameImage(width, height)
        result.putPixels(0, 0, width, height, rotated)
        return result
    }

    /**
     * Compute the image rotated from 270 degree
     *
     * @return Rotated image
     */
    fun rotate270() : GameImage
    {
        val source = this.grabPixels()
        val width = this.height
        val height = this.width
        val rotated = IntArray(width * height)

        var xr = width - 1
        val yr = 0
        val startR = yr * width
        var pixR = startR + xr

        var pix = 0

        for (y in 0 until this.height)
        {
            for (x in 0 until this.width)
            {
                rotated[pixR] = source[pix]

                pix ++
                pixR += width
            }

            xr --
            pixR = startR + xr
        }

        val result = GameImage(width, height)
        result.putPixels(0, 0, width, height, rotated)
        return result
    }

    /**
     * Compute the image rotated from 90 degree
     *
     * @return Rotated image
     */
    fun rotate90() : GameImage
    {
        val source = this.grabPixels()
        val width = this.height
        val height = this.width
        val rotated = IntArray(width * height)

        var xr = 0
        val yr = height - 1
        val stepR = - width
        val startR = yr * width
        var pixR = startR + xr

        var pix = 0

        for (y in 0 until this.height)
        {
            for (x in 0 until this.width)
            {
                rotated[pixR] = source[pix]

                pix ++
                pixR += stepR
            }

            xr ++
            pixR = startR + xr
        }

        val result = GameImage(width, height)
        result.putPixels(0, 0, width, height, rotated)
        return result
    }

    /**
     * Flip the image horizontally and vertically in same time.
     *
     * Visually its same result as :
     *
     * ```kotlin
     *     image.flipHorizontal()
     *     image.flipVertical()
     * ```
     *
     * But it's done faster
     */
    fun flipBoth()
    {
        this.manipulatePixels { pixels ->
            val length = pixels.size
            val middlePixel = length shr 1
            var color : Int

            var pixelStart = 0
            var pixelEnd = length - 1

            while (pixelStart < middlePixel)
            {
                color = pixels[pixelStart]
                pixels[pixelStart] = pixels[pixelEnd]
                pixels[pixelEnd] = color
                pixelStart ++
                pixelEnd --
            }
        }
    }

    /**
     * Flip the image horizontally
     */
    fun flipHorizontal()
    {
        this.manipulatePixels { pixels ->
            val mx = this.width shr 1
            var line = 0
            var pixL : Int
            var pixR : Int
            var color : Int

            for (y in 0 until this.height)
            {
                pixL = line
                pixR = line + this.width - 1

                for (x in 0 until mx)
                {
                    color = pixels[pixL]
                    pixels[pixL] = pixels[pixR]
                    pixels[pixR] = color

                    pixL ++
                    pixR --
                }

                line += this.width
            }
        }
    }

    /**
     * Flip the image vertically
     */
    fun flipVertical()
    {
        this.manipulatePixels { pixels ->
            val my = this.height shr 1
            var lineU = 0
            var lineB = (this.height - 1) * this.width
            val line = IntArray(this.width)

            for (y in 0 until my)
            {
                System.arraycopy(pixels, lineU, line, 0, this.width)
                System.arraycopy(pixels, lineB, pixels, lineU, this.width)
                System.arraycopy(line, 0, pixels, lineB, this.width)

                lineU += this.width
                lineB -= this.width
            }
        }
    }


    fun toBufferedImage() : BufferedImage
    {
        val image = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB)
        val pixels = this.grabPixels()
        image.setRGB(0, 0, this.width, this.height, pixels, 0, this.width)
        return image
    }

    private fun manipulatePixels(pixelsModifier : (IntArray) -> Unit)
    {
        val pixels = this.grabPixels()
        pixelsModifier(pixels)
        this.putPixels(0, 0, this.width, this.height, pixels)
    }

    private fun refresh()
    {
        this.image.flush()
        this.refreshFlowData.publish(Unit)
    }
}