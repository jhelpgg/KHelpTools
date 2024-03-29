package khelp.image

import khelp.image.extensions.computeTextLinesAlpha
import khelp.image.path.Path
import khelp.image.path.Segment
import khelp.image.raster.RasterImage
import khelp.image.raster.RasterImageType
import khelp.image.shape.ring
import khelp.image.transformation.Transformation
import khelp.image.transformation.Vector
import khelp.thread.Mutex
import khelp.ui.TextAlignment
import khelp.ui.font.JHelpFont
import khelp.ui.utilities.AFFINE_TRANSFORM
import khelp.ui.utilities.FLATNESS
import khelp.utilities.collections.queue.Queue
import khelp.utilities.collections.sortedArray
import khelp.utilities.extensions.alpha
import khelp.utilities.extensions.blue
import khelp.utilities.extensions.bounds
import khelp.utilities.extensions.green
import khelp.utilities.extensions.limit_0_255
import khelp.utilities.extensions.red
import khelp.utilities.math.isNul
import khelp.utilities.math.sign
import java.awt.Component
import java.awt.Graphics
import java.awt.Image
import java.awt.Point
import java.awt.Polygon
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.Shape
import java.awt.Toolkit
import java.awt.geom.Arc2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Path2D
import java.awt.geom.PathIterator
import java.awt.geom.RoundRectangle2D
import java.awt.image.BufferedImage
import java.awt.image.MemoryImageSource
import java.util.Arrays
import java.util.Stack
import javax.swing.Icon
import kotlin.math.PI
import kotlin.math.abs

/**Convert long to int, short name to be able have 5 colors per lines in PALETTE*/
private fun Long.int() = this.toInt()

/**
 * Represents an image.
 *
 * Can draw on image only this image if it is on draw mode, see [JHelpImage.startDrawMode], [JHelpImage.endDrawMode]
 * and [JHelpImage.isDrawMode]
 *
 * Can also create [JHelpSprite], that are small image that can be easy animated
 *
 * The image is refresh at screen , only if exit of draw mode [JHelpImage.endDrawMode] or call [JHelpImage.update]
 * @param width Image width
 * @param height Image height
 * @param pixels Image pixels in ARGB format
 * @throws IllegalArgumentException If width or height not at least 1 or pixels array size is not width*height
 */
class JHelpImage(
    /**Image width*/
    val width : Int,
    /**Image height*/
    val height : Int,
    private val pixels : IntArray = IntArray(width * height)) : RasterImage,
                                                                Icon
{
    companion object
    {
        /**
         * Palette to use
         */
        private val PALETTE =
            intArrayOf(0xFFFFFFFF.int(), 0xFFFFFFC0.int(), 0xFFFFFF80.int(), 0xFFFFFF40.int(), 0xFFFFFF00.int(),
                       0xFFFFC0FF.int(), 0xFFFFC0C0.int(), 0xFFFC0F80.int(), 0xFFFFC040.int(), 0xFFFFC000.int(),
                       0xFFFF80FF.int(), 0xFFFF80C0.int(), 0xFFFF8080.int(), 0xFFFF8040.int(), 0xFFFF8000.int(),
                       0xFFFF40FF.int(), 0xFFFF40C0.int(), 0xFFFF4080.int(), 0xFFFF4040.int(), 0xFFFF4000.int(),
                       0xFFFF00FF.int(), 0xFFFF00C0.int(), 0xFFFF0080.int(), 0xFFFF0040.int(), 0xFFFF0000.int(),

                       0xFFC0FFFF.int(), 0xFFC0FFC0.int(), 0xFFC0FF80.int(), 0xFFC0FF40.int(), 0xFFC0FF00.int(),
                       0xFFC0C0FF.int(), 0xFFC0C0C0.int(), 0xFFFC0F80.int(), 0xFFC0C040.int(), 0xFFC0C000.int(),
                       0xFFC080FF.int(), 0xFFC080C0.int(), 0xFFC08080.int(), 0xFFC08040.int(), 0xFFC08000.int(),
                       0xFFC040FF.int(), 0xFFC040C0.int(), 0xFFC04080.int(), 0xFFC04040.int(), 0xFFC04000.int(),
                       0xFFC000FF.int(), 0xFFC000C0.int(), 0xFFC00080.int(), 0xFFC00040.int(), 0xFFC00000.int(),

                       0xFF80FFFF.int(), 0xFF80FFC0.int(), 0xFF80FF80.int(), 0xFF80FF40.int(), 0xFF80FF00.int(),
                       0xFF80C0FF.int(), 0xFF80C0C0.int(), 0xFFFC0F80.int(), 0xFF80C040.int(), 0xFF80C000.int(),
                       0xFF8080FF.int(), 0xFF8080C0.int(), 0xFF808080.int(), 0xFF808040.int(), 0xFF808000.int(),
                       0xFF8040FF.int(), 0xFF8040C0.int(), 0xFF804080.int(), 0xFF804040.int(), 0xFF804000.int(),
                       0xFF8000FF.int(), 0xFF8000C0.int(), 0xFF800080.int(), 0xFF800040.int(), 0xFF800000.int(),

                       0xFF40FFFF.int(), 0xFF40FFC0.int(), 0xFF40FF80.int(), 0xFF40FF40.int(), 0xFF40FF00.int(),
                       0xFF40C0FF.int(), 0xFF40C0C0.int(), 0xFFFC0F80.int(), 0xFF40C040.int(), 0xFF40C000.int(),
                       0xFF4080FF.int(), 0xFF4080C0.int(), 0xFF408080.int(), 0xFF408040.int(), 0xFF408000.int(),
                       0xFF4040FF.int(), 0xFF4040C0.int(), 0xFF404080.int(), 0xFF404040.int(), 0xFF404000.int(),
                       0xFF4000FF.int(), 0xFF4000C0.int(), 0xFF400080.int(), 0xFF400040.int(), 0xFF400000.int(),

                       0xFF00FFFF.int(), 0xFF00FFC0.int(), 0xFF00FF80.int(), 0xFF00FF40.int(), 0xFF00FF00.int(),
                       0xFF00C0FF.int(), 0xFF00C0C0.int(), 0xFFFC0F80.int(), 0xFF00C040.int(), 0xFF00C000.int(),
                       0xFF0080FF.int(), 0xFF0080C0.int(), 0xFF008080.int(), 0xFF008040.int(), 0xFF008000.int(),
                       0xFF0040FF.int(), 0xFF0040C0.int(), 0xFF004080.int(), 0xFF004040.int(), 0xFF004000.int(),
                       0xFF0000FF.int(), 0xFF0000C0.int(), 0xFF000080.int(), 0xFF000040.int(), 0xFF000000.int())

        /**
         * Palette size
         */
        val PALETTE_SIZE = JHelpImage.PALETTE.size
    }

    /**
     * Actual clip to apply
     */
    private val clip : Clip

    /**
     * Clips stack
     */
    private val clips : Stack<Clip>

    /**
     * List of registered components to alert if image update
     */
    private val componentsListeners = ArrayList<Component>()

    /**
     * Actual draw mode
     */
    private var drawMode : Boolean = false

    /**
     * Indicates that draw mode status can change or not
     */
    private var drawModeLocked : Boolean = false

    /**
     * Image for draw in a swing component
     */
    val image : Image

    /**
     * Image source
     */
    private val memoryImageSource : MemoryImageSource

    /**
     * For synchronize
     */
    private val mutex = Mutex()

    /**
     * Image name
     */
    var name : String? = null

    /**
     * Tasks to play when image enter in draw mode
     */
    private val playInDrawMode = Queue<(JHelpImage) -> Unit>()

    /**
     * Tasks to play when image exit from draw mode
     */
    private val playOutDrawMode = Queue<(JHelpImage) -> Unit>()

    /**
     * List of sprite
     */
    private val sprites = ArrayList<JHelpSprite>()

    /**
     * Last sprite visibility information collected on [JHelpImage.startDrawMode] to draw sprite in good state when
     * [JHelpImage.endDrawMode] is call
     */
    private var visibilities : BooleanArray? = null
    private val mutexVisibilities = Mutex()

    init
    {
        if (this.width < 1 || this.height < 1)
        {
            throw IllegalArgumentException(
                "width and height must be >= 1, but it is specify : ${this.width}x${this.height}")
        }

        if (this.width * this.height != this.pixels.size)
        {
            throw IllegalArgumentException(
                "The pixels array size must be width*height, but it is specify width=${this.width} height=${this.height} pixels.length=${this.pixels.size}")
        }

        this.memoryImageSource = MemoryImageSource(this.width, this.height, this.pixels, 0, this.width)
        this.memoryImageSource.setAnimated(true)
        this.memoryImageSource.setFullBufferUpdates(true)

        this.image = Toolkit.getDefaultToolkit()
            .createImage(this.memoryImageSource)

        this.clip = Clip(0, this.width - 1, 0, this.height - 1)
        this.clips = Stack<Clip>()
        this.clips.push(this.clip)
    }

    /**
     * Create image fill with one color
     * @param width Image width
     * @param height Image height
     * @param color Color to fill the image
     */
    constructor(width : Int, height : Int, color : Int) : this(width, height)
    {
        this.drawMode = true
        this.clear(color)
        this.drawMode = false
    }

    /**
     * Create a new instance of JHelpImage fill with a pixels array scales to fill all the image
     *
     * @param width       Width of image inside pixels array
     * @param height      Height of image inside pixels array
     * @param pixels      Pixels array
     * @param imageWidth  Image created width
     * @param imageHeight Image created height
     */
    constructor(width : Int, height : Int, pixels : IntArray, imageWidth : Int, imageHeight : Int) : this(imageWidth,
                                                                                                          imageHeight)
    {
        this.fillRectangleScale(0, 0, imageWidth, imageHeight, pixels, width, height)
    }

    /**
     * Create Path for a thick line
     *
     * @param x1        Line start X
     * @param y1        Line start Y
     * @param x2        Line end X
     * @param y2        Line end Y
     * @param thickness Line thickness
     * @return Created path
     */
    private fun createThickLine(x1 : Int, y1 : Int, x2 : Int, y2 : Int, thickness : Int) : Path2D
    {
        val path = Path2D.Double()
        var vx = (x2 - x1).toDouble()
        var vy = (y2 - y1).toDouble()
        val length = Math.sqrt(vx * vx + vy * vy)

        if (isNul(length))
        {
            return path
        }

        val theta = Math.atan2(vy, vx)
        val thick = thickness * 0.5
        vx = vx * thick / length
        vy = vy * thick / length
        var angle = theta + PI / 2.0
        val x = x1 + thick * Math.cos(angle)
        val y = y1 + thick * Math.sin(angle)
        path.moveTo(x, y)
        path.lineTo(x2 + thick * Math.cos(angle), y2 + thick * Math.sin(angle))
        angle = theta - PI / 2.0
        path.quadTo(x2 + vx, y2 + vy, x2 + thick * Math.cos(angle), y2 + thick * Math.sin(angle))
        path.lineTo(x1 + thick * Math.cos(angle), y1 + thick * Math.sin(angle))
        path.quadTo(x1 - vx, y1 - vy, x, y)
        return path
    }

    /**
     * Draw a shape on center it
     *
     * MUST be in draw mode
     *
     * @param shape      Shape to draw
     * @param color      Color to use
     * @param doAlphaMix Indicates if alpha mix is on
     */
    private fun drawShapeCenter(shape : Shape, color : Int, doAlphaMix : Boolean)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val pathIterator = shape.getPathIterator(AFFINE_TRANSFORM, FLATNESS)

        val info = DoubleArray(6)
        var x = 0
        var y = 0
        var xStart = 0
        var yStart = 0
        var xx : Int
        var yy : Int

        val bounds = shape.bounds
        val vx = bounds.width shr 1
        val vy = bounds.height shr 1

        while (! pathIterator.isDone)
        {
            when (pathIterator.currentSegment(info))
            {
                PathIterator.SEG_MOVETO ->
                {
                    x = Math.round(info[0])
                        .toInt()
                    xStart = x
                    y = Math.round(info[1])
                        .toInt()
                    yStart = y
                }
                PathIterator.SEG_LINETO ->
                {
                    xx = Math.round(info[0])
                        .toInt()
                    yy = Math.round(info[1])
                        .toInt()

                    this.drawLine(x - vx, y - vy, xx - vx, yy - vy, color, doAlphaMix)

                    x = xx
                    y = yy
                }
                PathIterator.SEG_CLOSE  ->
                {
                    this.drawLine(x - vx, y - vy, xStart - vx, yStart - vy, color, doAlphaMix)

                    x = xStart
                    y = yStart
                }
            }

            pathIterator.next()
        }
    }

    /**
     * Fill a rectangle with an array of pixels
     *
     * @param x            X of up-left corner
     * @param y            Y of up-left corner
     * @param width        Rectangle width
     * @param height       Rectangle height
     * @param pixels       Pixels array
     * @param pixelsWidth  Image width inside pixels array
     * @param pixelsHeight Image height inside pixels array
     */
    private fun fillRectangleScale(
        x : Int, y : Int, width : Int, height : Int,
        pixels : IntArray, pixelsWidth : Int, pixelsHeight : Int)
    {
        if (width <= 0 || height <= 0)
        {
            return
        }

        val x2 = x + width - 1
        val y2 = y + height - 1

        val startX = Math.max(this.clip.xMin, x)
        val endX = Math.min(this.clip.xMax, x2)
        val startY = Math.max(this.clip.yMin, y)
        val endY = Math.min(this.clip.yMax, y2)

        if (startX > endX || startY > endY)
        {
            return
        }

        var line = startX + startY * this.width
        var pix : Int
        var yTexture = 0
        var pixTexture : Int
        val w = endX - startX + 1
        val h = endY - startY + 1

        var yy = startY
        var yt = 0
        while (yy <= endY)
        {
            pixTexture = yTexture * pixelsWidth
            pix = line

            var xx = startX
            var xt = 0
            var xTexture = 0
            while (xx < endX)
            {
                this.pixels[pix] = pixels[pixTexture + xTexture]
                xx ++
                xt ++
                pix ++
                xTexture = xt * pixelsWidth / w
            }

            line += this.width
            yy ++
            yt ++
            yTexture = yt * pixelsHeight / h
        }
    }

    /**
     * Change a sprite visibility
     *
     * @param index   Sprite index
     * @param visible New visibility state
     */
    internal fun changeSpriteVisibility(index : Int, visible : Boolean)
    {
        this.mutexVisibilities {
            if (this.drawMode)
            {
                this.visibilities !![index] = visible
                return@mutexVisibilities
            }

            val length = this.sprites.size
            var sprite : JHelpSprite

            val visibilities = BooleanArray(length)
            for (i in 0 until length)
            {
                visibilities[i] = false
            }

            var isVisible : Boolean

            for (i in length - 1 downTo index + 1)
            {
                sprite = this.sprites[i]
                visibilities[i] = sprite.visible()
                isVisible = visibilities[i]

                if (isVisible)
                {
                    sprite.changeVisible(false)
                }
            }

            this.sprites[index]
                .changeVisible(visible)

            for (i in index + 1 until length)
            {
                if (visibilities[i])
                {
                    this.sprites[i]
                        .changeVisible(true)
                }
            }
        }
    }

    /**
     * Mix an image pixel with a color
     * @param pixel Pixel index
     * @param alpha Alpha to do the mix
     * @param color Color to mix with
     */
    private fun mixColor(pixel : Int, alpha : Int, color : Int) = this.mixColor(pixel,
                                                                                alpha,
                                                                                color.red,
                                                                                color.green,
                                                                                color.blue)

    /**
     * Mix an image pixel with a color
     * @param pixel Pixel index
     * @param alpha Alpha color part
     * @param red Red color part
     * @param green Green color part
     * @param blue Blue color part
     */
    private fun mixColor(pixel : Int, alpha : Int, red : Int, green : Int, blue : Int)
    {
        val colorThis = this.pixels[pixel]
        val ahpla = 256 - alpha
        this.pixels[pixel] = (Math.min(255, alpha + colorThis.alpha) shl 24) or
                (((red * alpha + colorThis.red * ahpla) shr 8) shl 16) or
                (((green * alpha + colorThis.green * ahpla) shr 8) shl 8) or
                ((blue * alpha + colorThis.blue * ahpla) shr 8)

    }

    /**
     * Draw a part of an image on this image
     *
     * MUST be in draw mode
     *
     * @param x          X on this image
     * @param y          Y on this image
     * @param image      Image to draw
     * @param xImage     X on given image
     * @param yImage     Y on given image
     * @param width      Part width
     * @param height     Part height
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    internal fun drawImageInternal(x : Int, y : Int, image : JHelpImage,
                                   xImage : Int, yImage : Int, width : Int, height : Int, doAlphaMix : Boolean)
    {
        var x = x
        var y = y
        var xImage = xImage
        var yImage = yImage
        var width = width
        var height = height
        if (! doAlphaMix)
        {
            this.drawImageOver(x, y, image, xImage, yImage, width, height)
            return
        }

        if (xImage < 0)
        {
            x -= xImage
            width += xImage
            xImage = 0
        }

        if (x < this.clip.xMin)
        {
            xImage -= x - this.clip.xMin
            width += x - this.clip.xMin
            x = this.clip.xMin
        }

        if (yImage < 0)
        {
            y -= yImage
            height += yImage
            yImage = 0
        }

        if (y < this.clip.yMin)
        {
            yImage -= y - this.clip.yMin
            height += y - this.clip.yMin
            y = this.clip.yMin
        }

        val w = minOf(this.clip.xMax + 1 - x, image.width - xImage, width, this.width - x)
        val h = minOf(this.clip.yMax + 1 - y, image.height - yImage, height, this.height - y)

        if (w <= 0 || h <= 0)
        {
            return
        }

        var lineThis = x + y * this.width
        var pixThis : Int

        var lineImage = xImage + yImage * image.width
        var pixImage : Int

        var colorImage : Int
        var alpha : Int

        for (yy in 0 until h)
        {
            pixThis = lineThis
            pixImage = lineImage

            for (xx in 0 until w)
            {
                colorImage = image.pixels[pixImage]
                alpha = colorImage.alpha

                if (alpha == 255)
                {
                    this.pixels[pixThis] = colorImage
                }
                else if (alpha > 0)
                {
                    this.mixColor(pixThis, alpha, colorImage)
                }

                pixThis ++
                pixImage ++
            }

            lineThis += this.width
            lineImage += image.width
        }
    }

    /**
     * Draw a part of image on using a specific alpha value

     * MUST be in draw mode
     *
     * @param x      X to draw image
     * @param y      Y to draw image
     * @param image  Image to draw
     * @param xImage Start X of image part
     * @param yImage Start Y of image part
     * @param width  Width of image part
     * @param height Height of image part
     * @param alpha  Alpha to use
     */
    internal fun drawImageInternal(x : Int, y : Int, image : JHelpImage,
                                   xImage : Int, yImage : Int, width : Int, height : Int, alpha : Int)
    {
        var x = x
        var y = y
        var xImage = xImage
        var yImage = yImage
        var width = width
        var height = height
        if (alpha == 255)
        {
            this.drawImageOver(x, y, image, xImage, yImage, width, height)
            return
        }

        if (alpha == 0)
        {
            return
        }

        if (xImage < 0)
        {
            x -= xImage
            width += xImage
            xImage = 0
        }

        if (x < this.clip.xMin)
        {
            xImage -= x - this.clip.xMin
            width += x - this.clip.xMin
            x = this.clip.xMin
        }

        if (yImage < 0)
        {
            y -= yImage
            height += yImage
            yImage = 0
        }

        if (y < this.clip.yMin)
        {
            yImage -= y - this.clip.yMin
            height += y - this.clip.yMin
            y = this.clip.yMin
        }

        val w = minOf(this.clip.xMax + 1 - x, image.width - xImage, width)
        val h = minOf(this.clip.yMax + 1 - y, image.height - yImage, height)

        if (w <= 0 || h <= 0)
        {
            return
        }

        var lineThis = x + y * this.width
        var pixThis : Int

        var lineImage = xImage + yImage * image.width
        var pixImage : Int

        for (yy in 0 until h)
        {
            pixThis = lineThis
            pixImage = lineImage

            for (xx in 0 until w)
            {
                this.mixColor(pixThis, alpha, image.pixels[pixImage])
                pixThis ++
                pixImage ++
            }

            lineThis += this.width
            lineImage += image.width
        }
    }

    /**
     * Draw apart of image over this image (just override)
     *
     * MUST be in draw mode
     *
     * @param x      X on this image
     * @param y      Y on this image
     * @param image  Image to draw
     * @param xImage X on image
     * @param yImage Y on image
     * @param width  Part width
     * @param height Part height
     */
    internal fun drawImageOver(x : Int, y : Int, image : JHelpImage, xImage : Int, yImage : Int, width : Int,
                               height : Int)
    {
        var x = x
        var y = y
        var xImage = xImage
        var yImage = yImage
        var width = width
        var height = height
        if (xImage < 0)
        {
            x -= xImage
            width += xImage
            xImage = 0
        }

        if (x < this.clip.xMin)
        {
            xImage -= x - this.clip.xMin
            width += x - this.clip.xMin
            x = this.clip.xMin
        }

        if (yImage < 0)
        {
            y -= yImage
            height += yImage
            yImage = 0
        }

        if (y < this.clip.yMin)
        {
            yImage -= y - this.clip.yMin
            height += y - this.clip.yMin
            y = this.clip.yMin
        }

        val w = minOf(this.clip.xMax + 1 - x, image.width - xImage, width)
        val h = minOf(this.clip.yMax + 1 - y, image.height - yImage, height)

        if (w <= 0 || h <= 0)
        {
            return
        }

        var lineThis = x + y * this.width
        var lineImage = xImage + yImage * image.width

        for (yy in 0 until h)
        {
            System.arraycopy(image.pixels, lineImage, this.pixels, lineThis, w)

            lineThis += this.width
            lineImage += image.width
        }
    }

    /**
     * Refresh the image
     */
    internal fun refresh() = this.memoryImageSource.newPixels()

    /**
     * Add an other image
     *
     * This image and the given one MUST have same dimension
     *
     * Note : if this image or given one not in draw mode, all visible sprites (of the image) are consider like a part
     * of the image
     *
     * @param image Image to add
     */
    fun addition(image : JHelpImage)
    {
        if (this.width != image.width || this.height != image.height)
        {
            throw IllegalArgumentException("We can only add with an image of same size")
        }

        var colorThis : Int
        var colorImage : Int

        for (pix in 0 until this.pixels.size)
        {
            colorThis = this.pixels[pix]
            colorImage = image.pixels[pix]

            this.pixels[pix] = colorThis and BLACK_ALPHA_MASK or
                    ((colorThis.red + colorImage.red).limit_0_255 shl 16) or
                    ((colorThis.green + colorImage.green).limit_0_255 shl 8) or
                    (colorThis.blue + colorImage.blue).limit_0_255
        }
    }

    /**
     * Add an other image
     * @param image Image to add
     */
    operator fun plusAssign(image : JHelpImage) = this.addition(image)

    /**
     * Ad an image with this image an return the result
     * @param image Image to add
     * @return Image result
     */
    operator fun plus(image : JHelpImage) : JHelpImage
    {
        val copy = this.copy()
        copy.startDrawMode()
        copy += image
        copy.endDrawMode()
        return copy
    }

    private fun gauss(c00 : Int, c10 : Int, c20 : Int, c01 : Int, c11 : Int, c21 : Int, c02 : Int, c12 : Int,
                      c22 : Int) =
        (c00 + (c10 shl 1) + c20 + (c01 shl 1) + (c11 shl 2) + (c21 shl 1) + c02 + (c12 shl 1) + c22) shr 4

    /**
     * Apply Gauss filter 3x3 in the image.
     *
     * MUST be in draw mode
     *
     * Note filter is
     *
     *     +-+-+-+
     *     |1|2|1|
     *     +-+-+-+
     *     |2|4|2|
     *     +-+-+-+
     *     |1|2|1|
     *     +-+-+-+
     *
     * When apply the filter to a pixel. It considers the pixel as the center of above table.
     * Other cells are pixel neighbor, the number represents the influence of each pixel
     */
    fun applyGauss3x3()
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val w = this.width + 2
        val h = this.height + 2
        val pix = IntArray(w * h)

        var lineThis = 0
        var linePix = 1 + w
        for (y in 0 until this.height)
        {
            pix[linePix - 1] = this.pixels[lineThis]
            System.arraycopy(this.pixels, lineThis, pix, linePix, this.width)
            lineThis += this.width
            linePix += w
            pix[linePix - 2] = this.pixels[lineThis - 1]
        }

        System.arraycopy(this.pixels, 0, pix, 1, this.width)
        System.arraycopy(this.pixels, this.width * this.height - this.width, pix, w * h - w + 1, this.width)

        var l0 = 0
        var l1 = w
        var l2 = w + w
        var p20 : Int
        var p21 : Int
        var p22 : Int
        var c00 : Int
        var c10 : Int
        var c20 : Int
        var c01 : Int
        var c11 : Int
        var c21 : Int
        var c02 : Int
        var c12 : Int
        var c22 : Int
        var p = 0

        for (y in 0 until this.height)
        {
            p20 = l0 + 2
            p21 = l1 + 2
            p22 = l2 + 2

            c00 = pix[p20 - 2]
            c10 = pix[p20 - 1]

            c01 = pix[p21 - 2]
            c11 = pix[p21 - 1]

            c02 = pix[p22 - 2]
            c12 = pix[p22 - 1]

            for (x in 0 until this.width)
            {
                c20 = pix[p20]
                c21 = pix[p21]
                c22 = pix[p22]

                this.pixels[p] =
                        // Alpha
                    (gauss(c00.alpha, c10.alpha, c20.alpha,
                           c01.alpha, c11.alpha, c21.alpha,
                           c02.alpha, c12.alpha, c22.alpha) shl 24) or
                            // Red
                            (gauss(c00.red, c10.red, c20.red,
                                   c01.red, c11.red, c21.red,
                                   c02.red, c12.red, c22.red) shl 16) or
                            // Green
                            (gauss(c00.green, c10.green, c20.green,
                                   c01.green, c11.green, c21.green,
                                   c02.green, c12.green, c22.green) shl 8) or
                            // Blue
                            gauss(c00.blue, c10.blue, c20.blue,
                                  c01.blue, c11.blue, c21.blue,
                                  c02.blue, c12.blue, c22.blue)

                c00 = c10
                c10 = c20

                c01 = c11
                c11 = c21

                c02 = c12
                c12 = c22

                p20 ++
                p21 ++
                p22 ++

                p ++
            }

            l0 += w
            l1 += w
            l2 += w
        }
    }

    /**
     * Fill with the palette different area
     *
     * MUST be in draw mode
     *
     * @param precision Precision to use for distinguish 2 area
     */
    fun applyPalette(precision : Int)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val colors = sortedArray<Color>()
        val size = this.pixels.size - 1
        Color.precision = precision
        var index : Int
        var col : Int
        var color : Color

        for (i in size downTo 0)
        {
            color = Color(this.pixels[i])
            index = colors.indexOf(color)

            if (index < 0)
            {
                color.info = colors.size
                col = color.info
                colors += (color)
            }
            else
            {
                col = colors.get(index).info
            }

            this.pixels[i] = JHelpImage.PALETTE[col % JHelpImage.PALETTE_SIZE]
        }
    }

    /**
     * Put the image brighter
     *
     * MUST be in draw mode
     *
     * @param factor Factor of bright
     */
    fun brighter(factor : Int)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var color : Int

        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]

            this.pixels[pix] = color and BLACK_ALPHA_MASK or
                    ((color.red + factor).limit_0_255 shl 16) or
                    ((color.green + factor).limit_0_255 shl 8) or
                    (color.blue + factor).limit_0_255
        }
    }

    /**
     * Change image brightness
     *
     * MUST be in draw mode
     *
     * @param factor Brightness factor
     */
    fun brightness(factor : Double)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var color : Int
        var red : Int
        var green : Int
        var blue : Int
        var y : Double
        var u : Double
        var v : Double

        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]

            red = color.red
            green = color.green
            blue = color.blue

            y = y(red, green, blue) * factor
            u = u(red, green, blue)
            v = v(red, green, blue)

            this.pixels[pix] = color and BLACK_ALPHA_MASK or
                    (red(y, u, v) shl 16) or
                    (green(y, u, v) shl 8) or
                    blue(y, u, v)
        }
    }

    /**
     * Colorize all near color with same color
     *
     * MUST be in draw mode
     *
     * @param precision Precision to use
     */
    fun categorizeByColor(precision : Int)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val colors = sortedArray<Color>()
        val size = this.pixels.size - 1
        Color.precision = precision
        var color : Color
        var index : Int

        for (i in size downTo 0)
        {
            color = Color(this.pixels[i])

            index = colors.indexOf(color)

            if (index < 0)
            {
                colors += color
                this.pixels[i] = color.color
            }
            else
            {
                this.pixels[i] = colors.get(index).color
            }
        }
    }

    /**
     * Colorize with 3 colors, one used for "dark" colors, one for "gray" colors and last for "white" colors
     *
     * MUST be in draw mode
     *
     * @param colorLow    Color for dark
     * @param colorMiddle Color for gray
     * @param colorHigh   Color for white
     * @param precision   Precision for decide witch are gray
     */
    fun categorizeByY(colorLow : Int, colorMiddle : Int, colorHigh : Int, precision : Double)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var y : Double

        var index = this.pixels.size - 1
        var color = this.pixels[index]
        var red = color.red
        var green = color.green
        var blue = color.blue

        var yAverage = y(red, green, blue)

        index --
        while (index >= 0)
        {
            color = this.pixels[index]
            red = color.red
            green = color.green
            blue = color.blue

            yAverage += y(red, green, blue)

            index --
        }

        val ymil = yAverage / this.pixels.size

        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]
            red = color.red
            green = color.green
            blue = color.blue

            y = y(red, green, blue)

            if (Math.abs(y - ymil) <= precision)
            {
                this.pixels[pix] = colorMiddle
            }
            else if (y < ymil)
            {
                this.pixels[pix] = colorLow
            }
            else
            {
                this.pixels[pix] = colorHigh
            }
        }
    }

    /**
     * Fill the entire image with same color

     * MUST be in draw mode
     *
     * @param color Color to use
     */
    fun clear(color : Int)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        for (pix in 0 until this.pixels.size)
        {
            this.pixels[pix] = color
        }
    }

    /**
     * Clear the image to be totally transparent
     */
    override fun clear()
    {
        val mode = this.drawMode

        if (! mode)
        {
            this.startDrawMode()
        }

        this.clear(0)

        if (! mode)
        {
            this.endDrawMode()
        }
    }

    /**
     * Create a sprite
     *
     * MUST NOT be in draw mode
     *
     * @param x      Start X of sprite
     * @param y      Start Y of sprite
     * @param width  Sprite width
     * @param height Sprite height
     * @return Created sprite
     */
    fun createSprite(x : Int, y : Int, width : Int, height : Int) : JHelpSprite
    {
        if (this.drawMode)
        {
            throw IllegalStateException("MUST NOT be in draw mode !")
        }

        val index = this.sprites.size
        val sprite = JHelpSprite(x, y, width, height, this, index)
        this.sprites.add(sprite)
        return sprite
    }

    /**
     * Draw a line
     *
     * MUST be in draw mode
     *
     * @param x1         X of first point
     * @param y1         Y first point
     * @param x2         X second point
     * @param y2         Y second point
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun drawLine(
        x1 : Int, y1 : Int, x2 : Int, y2 : Int, color : Int, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (y1 == y2)
        {
            this.drawHorizontalLine(x1, x2, y1, color, doAlphaMix)

            return
        }

        if (x1 == x2)
        {
            this.drawVerticalLine(x1, y1, y2, color, doAlphaMix)

            return
        }

        val alpha = color.alpha

        if (alpha == 0 && doAlphaMix)
        {
            return
        }

        var error = 0
        val dx = abs(x2 - x1)
        val sx = sign(x2 - x1)
        val dy = abs(y2 - y1)
        val sy = sign(y2 - y1)
        var x = x1
        var y = y1

        if (dx >= dy)
        {
            while ((x < this.clip.xMin || x > this.clip.xMax || y < this.clip.yMin || y > this.clip.yMax) && (x != x2 || y != y2))
            {
                x += sx

                error += dy
                if (error >= dx)
                {
                    y += sy

                    error -= dx
                }
            }
        }
        else
        {
            while ((x < this.clip.xMin || x > this.clip.xMax || y < this.clip.yMin || y > this.clip.yMax) && (x != x2 || y != y2))
            {
                y += sy

                error += dx
                if (error >= dy)
                {
                    x += sx

                    error -= dy
                }
            }
        }

        if ((x < this.clip.xMin || x > this.clip.xMax || y < this.clip.yMin || y > this.clip.yMax) && x == x2
            && y == y2)
        {
            return
        }

        var pix = x + y * this.width
        val moreY = sy * this.width

        if (alpha == 255 || ! doAlphaMix)
        {
            if (dx >= dy)
            {
                while (x >= this.clip.xMin && x <= this.clip.xMax && x != x2 && y >= this.clip.yMin && y <= this.clip.yMax &&
                       y != y2)
                {
                    this.pixels[pix] = color

                    pix += sx
                    x += sx

                    error += dy
                    if (error >= dx)
                    {
                        pix += moreY
                        y += sy

                        error -= dx
                    }
                }
            }
            else
            {
                while (x >= this.clip.xMin && x <= this.clip.xMax && x != x2 && y >= this.clip.yMin && y <= this.clip.yMax &&
                       y != y2)
                {
                    this.pixels[pix] = color

                    pix += moreY
                    y += sy

                    error += dx
                    if (error >= dy)
                    {
                        pix += sx
                        x += sx

                        error -= dy
                    }
                }
            }

            return
        }

        val red = (color.red) * alpha
        val green = (color.green) * alpha
        val blue = (color.blue) * alpha

        if (dx >= dy)
        {
            while (x >= this.clip.xMin && x <= this.clip.xMax && x != x2 && y >= this.clip.yMin && y <= this
                    .clip.yMax && (x != x2 || y != y2))
            {
                this.mixColor(pix, alpha, red, green, blue)

                pix += sx
                x += sx

                error += dy
                if (error >= dx)
                {
                    pix += moreY
                    y += sy

                    error -= dx
                }
            }
        }
        else
        {
            while (x >= this.clip.xMin && x <= this.clip.xMax && x != x2 && y >= this.clip.yMin && y <= this
                    .clip.yMax && (x != x2 || y != y2))
            {
                this.mixColor(pix, alpha, red, green, blue)

                pix += moreY
                y += sy

                error += dx
                if (error >= dy)
                {
                    pix += sx
                    x += sx

                    error -= dy
                }
            }
        }
    }

    /**
     * Colorize with automatic palette
     *
     * MUST be in draw mode
     *
     * @param precision Precision to use
     * @return Number of different color
     */
    fun colorizeWithPalette(precision : Int) : Int
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val size = this.pixels.size
        val result = IntArray(size)
        var indexPalette = 0
        var color : Int
        var reference : Int
        var red : Int
        var green : Int
        var blue : Int
        var p : Int
        val stack = Stack<Point>()
        var point : Point
        var x = this.width - 1
        var y = this.height - 1

        for (pix in size - 1 downTo 0)
        {
            if (result[pix] == 0)
            {
                color = JHelpImage.PALETTE[indexPalette % JHelpImage.PALETTE_SIZE]
                indexPalette ++

                reference = this.pixels[pix]
                red = reference.red
                green = reference.green
                blue = reference.blue

                stack.push(Point(x, y))

                while (! stack.isEmpty())
                {
                    point = stack.pop()
                    p = point.x + point.y * this.width

                    result[p] = color

                    if (point.x > 0 && result[p - 1] == 0
                        && Color.isNear(red, green, blue, this.pixels[p - 1], precision))
                    {
                        stack.push(Point(point.x - 1, point.y))
                    }

                    if (point.y > 0 && result[p - this.width] == 0
                        && Color.isNear(red, green, blue, this.pixels[p - this.width], precision))
                    {
                        stack.push(Point(point.x, point.y - 1))
                    }

                    if (point.x < this.width - 1 && result[p + 1] == 0
                        && Color.isNear(red, green, blue, this.pixels[p + 1], precision))
                    {
                        stack.push(Point(point.x + 1, point.y))
                    }

                    if (point.y < this.height - 1 && result[p + this.width] == 0
                        && Color.isNear(red, green, blue, this.pixels[p + this.width], precision))
                    {
                        stack.push(Point(point.x, point.y + 1))
                    }
                }
            }

            x --
            if (x < 0)
            {
                x = this.width - 1
                y --
            }
        }

        System.arraycopy(result, 0, this.pixels, 0, size)

        return indexPalette
    }

    /**
     * Change image contrast by using the middle of the minimum and maximum
     *
     * MUST be in draw mode
     *
     * @param factor Factor to apply to the contrast
     */
    fun contrast(factor : Double)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var y : Double

        var index = this.pixels.size - 1
        var color = this.pixels[index]
        var red = color.red
        var green = color.green
        var blue = color.blue

        var yMax = y(red, green, blue)
        var yMin = yMax

        index --
        while (index >= 0)
        {
            color = this.pixels[index]
            red = color.red
            green = color.green
            blue = color.blue

            y = y(red, green, blue)

            yMin = Math.min(yMin, y)
            yMax = Math.max(yMax, y)

            index --
        }

        val yMil = (yMin + yMax) / 2
        var u : Double
        var v : Double

        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]
            red = color.red
            green = color.green
            blue = color.blue

            y = y(red, green, blue)
            u = u(red, green, blue)
            v = v(red, green, blue)

            y = yMil + factor * (y - yMil)

            this.pixels[pix] = (color and BLACK_ALPHA_MASK
                    or (red(y, u, v) shl 16)
                    or (green(y, u, v) shl 8)
                    or blue(y, u, v))
        }
    }

    /**
     * Change image contrast by using the average of all values

     * MUST be in draw mode
     *
     * @param factor Factor to apply to the contrast
     */
    fun contrastAverage(factor : Double)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var y : Double

        var index = this.pixels.size - 1
        var color = this.pixels[index]
        var red = color.red
        var green = color.green
        var blue = color.blue

        var yAverage = y(red, green, blue)

        index --
        while (index >= 0)
        {
            color = this.pixels[index]
            red = color.red
            green = color.green
            blue = color.blue

            yAverage += y(red, green, blue)

            index --
        }

        val ymil = yAverage / this.pixels.size
        var u : Double
        var v : Double

        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]
            red = color.red
            green = color.green
            blue = color.blue

            y = y(red, green, blue)
            u = u(red, green, blue)
            v = v(red, green, blue)

            y = ymil + factor * (y - ymil)

            this.pixels[pix] = (color and BLACK_ALPHA_MASK
                    or (red(y, u, v) shl 16)
                    or (green(y, u, v) shl 8)
                    or blue(y, u, v))
        }
    }

    /**
     * Copy the image is this one
     *
     * This image and the given one MUST have same dimension
     *
     * Note : if this image or given one not in draw mode, all visible sprites (of the image) are consider like a part
     * of the
     * image
     *
     * @param image Image to copy
     */
    fun copy(image : JHelpImage)
    {
        if (this.width != image.width || this.height != image.height)
        {
            throw IllegalArgumentException("We can only multiply with an image of same size")
        }

        System.arraycopy(image.pixels, 0, this.pixels, 0, this.pixels.size)
    }

    /**
     * Indicates if draw mode is locked.
     *
     * If the draw mode is locked, it is impossible to change the draw mode status
     *
     * @return `true` if draw mode is locked.
     */
    fun drawModeLocked() = this.drawModeLocked

    /**
     * Create an image copy
     *
     * Note : if this image is not in draw mode, all visible sprites will be consider like a part of this image
     *
     * @return The copy
     */
    fun copy() : JHelpImage
    {
        val copy = JHelpImage(this.width, this.height)

        copy.startDrawMode()
        copy.copy(this)
        copy.endDrawMode()

        return copy
    }

    /**
     * Create a mask from the image
     *
     * @param positiveColor Color that consider as light on (other colors are consider as light off)
     * @param precision     Precision or distance minimum for consider colors equals
     * @return Created mask
     */
    fun createMask(positiveColor : Int, precision : Int) : JHelpMask
    {
        var precision = precision
        precision = Math.max(0, precision)
        val mask = JHelpMask(this.width, this.height)
        val alpha = positiveColor.alpha
        val red = positiveColor.red
        val green = positiveColor.green
        val blue = positiveColor.blue
        var pix = 0
        var color : Int
        var a : Int
        var r : Int
        var g : Int
        var b : Int

        for (y in 0 until this.height)
        {
            for (x in 0 until this.width)
            {
                color = this.pixels[pix]
                a = color.alpha
                r = color.red
                g = color.green
                b = color.blue

                if (Math.abs(alpha - a) <= precision && Math.abs(red - r) <= precision
                    && Math.abs(green - g) <= precision && Math.abs(blue - b) <= precision)
                {
                    mask[x, y] = true
                }

                pix ++
            }
        }

        return mask
    }

    /**
     * Draw an empty shape
     *
     * MUST be in draw mode
     *
     * @param shape      Shape to draw
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun drawShape(shape : Shape, color : Int, doAlphaMix : Boolean)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val pathIterator = shape.getPathIterator(AFFINE_TRANSFORM, FLATNESS)

        val info = DoubleArray(6)
        var x = 0
        var y = 0
        var xStart = 0
        var yStart = 0
        var xx : Int
        var yy : Int

        while (! pathIterator.isDone)
        {
            when (pathIterator.currentSegment(info))
            {
                PathIterator.SEG_MOVETO ->
                {
                    x = Math.round(info[0])
                        .toInt()
                    xStart = x
                    y = Math.round(info[1])
                        .toInt()
                    yStart = y
                }
                PathIterator.SEG_LINETO ->
                {
                    xx = Math.round(info[0])
                        .toInt()
                    yy = Math.round(info[1])
                        .toInt()

                    this.drawLine(x, y, xx, yy, color, doAlphaMix)

                    x = xx
                    y = yy
                }
                PathIterator.SEG_CLOSE  ->
                {
                    this.drawLine(x, y, xStart, yStart, color, doAlphaMix)

                    x = xStart
                    y = yStart
                }
            }

            pathIterator.next()
        }
    }

    /**
     * Create sprite with initial image inside
     *
     * MUST NOT be in draw mode
     *
     * @param x      X
     * @param y      Y
     * @param source Initial image
     * @return Created sprite
     */
    fun createSprite(x : Int, y : Int, source : JHelpImage) : JHelpSprite
    {
        if (this.drawMode)
        {
            throw IllegalStateException("MUST NOT be in draw mode !")
        }

        val index = this.sprites.size
        val sprite = JHelpSprite(x, y, source, this, index)
        this.sprites.add(sprite)
        return sprite
    }

    /**
     * Make image darker
     *
     * MUST be in draw mode
     *
     * @param factor Darker factor in [0, 255]
     */
    fun darker(factor : Int)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var color : Int

        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]
            this.pixels[pix] = color and BLACK_ALPHA_MASK or
                    ((color.red - factor).limit_0_255 shl 16) or
                    ((color.green - factor).limit_0_255 shl 8) or
                    (color.blue - factor).limit_0_255
        }
    }

    /**
     * Divide an other image
     *
     * This image and the given one MUST have same dimension
     *
     * Note : if this image or given one not in draw mode, all visible sprites (of the image) are consider like a part
     * of the
     * image
     *
     * @param image Image to divide with
     */
    fun divide(image : JHelpImage)
    {
        if (this.width != image.width || this.height != image.height)
        {
            throw IllegalArgumentException("We can only multiply with an image of same size")
        }

        var colorThis : Int
        var colorImage : Int

        for (pix in 0 until this.pixels.size)
        {
            colorThis = this.pixels[pix]
            colorImage = image.pixels[pix]

            this.pixels[pix] = colorThis and BLACK_ALPHA_MASK or
                    ((colorThis.red) * 256 / ((colorImage.red) + 1) shl 16) or
                    ((colorThis.green) * 256 / ((colorImage.green) + 1) shl 8) or
                    (colorThis.blue) * 256 / ((colorImage.blue) + 1)
        }
    }

    /**
     * Divide with an other image
     * @param image Image to divide with
     */
    operator fun divAssign(image : JHelpImage) = this.divide(image)

    /**
     * Divide this image with an other and return the result
     * @param image Image to divide with
     * @return Division result
     */
    operator fun div(image : JHelpImage) : JHelpImage
    {
        val copy = this.copy()
        copy.startDrawMode()
        copy /= image
        copy.endDrawMode()
        return copy
    }

    /**
     * Draw an ellipse
     *
     * MUST be in draw mode
     *
     * @param x          X of upper left corner
     * @param y          Y of upper left corner
     * @param width      Width
     * @param height     Height
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun drawEllipse(x : Int, y : Int, width : Int, height : Int, color : Int, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.drawShape(Ellipse2D.Double(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble()), color,
                       doAlphaMix)
    }

    fun drawArc(x : Int, y : Int, width : Int, height : Int, color : Int, angleStart : Double, totalAngle : Double,
                arcType : ArcType = ArcType.OPEN, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.drawShape(
            Arc2D.Double(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble(), angleStart, totalAngle,
                         arcType.arcTypeCode), color,
            doAlphaMix)
    }

    fun drawCircle(x : Int, y : Int, radius : Int, color : Int, doAlphaMix : Boolean = true) =
        this.drawEllipse(x - radius, y - radius, radius shl 1, radius shl 1, color, doAlphaMix)

    fun drawRing(x : Int, y : Int, inRadius : Int, outRadius : Int, color : Int, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.drawCircle(x, y, inRadius, color, doAlphaMix)

        if (inRadius != outRadius)
        {
            this.drawCircle(x, y, outRadius, color, doAlphaMix)
        }
    }

    /**
     * Draw horizontal line
     *
     * MUST be in draw mode
     *
     * @param x1         X start
     * @param x2         End X
     * @param y          Y
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun drawHorizontalLine(x1 : Int, x2 : Int, y : Int, color : Int, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (y < this.clip.yMin || y > this.clip.yMax)
        {
            return
        }

        val xMin = Math.max(this.clip.xMin, Math.min(x1, x2))
        val xMax = Math.min(this.clip.xMax, Math.max(x1, x2))

        if (xMin > xMax || xMin > this.clip.xMax || xMax < this.clip.xMin)
        {
            return
        }

        var start = xMin
        var end = xMax

        val alpha = color.alpha

        if (alpha == 0 && doAlphaMix)
        {
            return
        }

        val yy = y * this.width
        start += yy
        end += yy

        if (alpha == 255 || ! doAlphaMix)
        {
            for (pix in start .. end)
            {
                this.pixels[pix] = color
            }

            return
        }

        val red = (color.red) * alpha
        val green = (color.green) * alpha
        val blue = (color.blue) * alpha

        for (pix in start .. end)
        {
            this.mixColor(pix, alpha, red, green, blue)
        }
    }

    /**
     * Draw a part off image
     *
     * MUST be in draw mode
     *
     * @param x          X on this
     * @param y          Y on this
     * @param image      Image to draw
     * @param xImage     X on image
     * @param yImage     Y on image
     * @param width      Part width
     * @param height     Part height
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun drawImage(x : Int, y : Int, image : JHelpImage,
                  xImage : Int = 0, yImage : Int = 0,
                  width : Int = image.width, height : Int = image.height,
                  doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.drawImageInternal(x, y, image, xImage, yImage, width, height, doAlphaMix)
    }

    /**
     * Draw a part of image with a specific alpha
     *
     * MUST be in draw mode
     *
     * @param x      X position
     * @param y      Y position
     * @param image  Image to draw
     * @param xImage X of image part
     * @param yImage Y of image part
     * @param width  Image part width
     * @param height Image part height
     * @param alpha  Alpha to use
     */
    fun drawImage(x : Int, y : Int, image : JHelpImage,
                  xImage : Int = 0, yImage : Int = 0,
                  width : Int = image.width, height : Int = image.height,
                  alpha : Int)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.drawImageInternal(x, y, image, xImage, yImage, width, height, alpha)
    }

    /**
     * Draw a part of image or using a pixel combination
     *
     * @param x                X where locate up-left corner of image to draw
     * @param y                Y where locate up-left corner of image to draw
     * @param image            Image to draw
     * @param xImage           X of up-left corner of image part
     * @param yImage           Y of up-left corner of image part
     * @param width            Part width
     * @param height           Part height
     * @param pixelCombination Pixel combination to use. Can use one of PixelCombination.kt function
     */
    fun drawImage(x : Int, y : Int, image : JHelpImage,
                  xImage : Int = 0, yImage : Int = 0,
                  width : Int = image.width, height : Int = image.height,
                  pixelCombination : (Int, Int) -> Int)
    {
        var x = x
        var y = y
        var xImage = xImage
        var yImage = yImage
        var width = width
        var height = height
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (xImage < 0)
        {
            x -= xImage
            width += xImage
            xImage = 0
        }

        if (x < this.clip.xMin)
        {
            xImage -= x - this.clip.xMin
            width += x - this.clip.xMin
            x = this.clip.xMin
        }

        if (yImage < 0)
        {
            y -= yImage
            height += yImage
            yImage = 0
        }

        if (y < this.clip.yMin)
        {
            yImage -= y - this.clip.yMin
            height += y - this.clip.yMin
            y = this.clip.yMin
        }

        val w = minOf(this.clip.xMax + 1 - x, image.width - xImage, width, this.width - x)
        val h = minOf(this.clip.yMax + 1 - y, image.height - yImage, height, this.height - y)

        if (w <= 0 || h <= 0)
        {
            return
        }

        var lineThis = x + y * this.width
        var pixThis : Int

        var lineImage = xImage + yImage * image.width
        var pixImage : Int

        for (yy in 0 until h)
        {
            pixThis = lineThis
            pixImage = lineImage

            for (xx in 0 until w)
            {
                this.pixels[pixThis] = pixelCombination(this.pixels[pixThis], image.pixels[pixImage])
                pixThis ++
                pixImage ++
            }

            lineThis += this.width
            lineImage += image.width
        }
    }

    /**
     * Draw an image with a given transformation.
     *
     * Image MUST be in draw mode.
     *
     * Given image and transformation MUST have same sizes
     *
     * @param x              X
     * @param y              Y
     * @param image          Image to draw
     * @param transformation Transformation to apply
     * @param doAlphaMix     Indicates if we do the mixing `true`, or we just override `false`
     */
    fun drawImage(x : Int, y : Int, image : JHelpImage, transformation : Transformation, doAlphaMix : Boolean = true)
    {
        var x = x
        var y = y
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var width = image.width
        var height = image.height

        if (width != transformation.width || height != transformation.height)
        {
            throw IllegalArgumentException("Image and transformation MUST have same size")
        }

        var xImage = 0

        if (x < this.clip.xMin)
        {
            xImage -= x - this.clip.xMin
            width += x - this.clip.xMin
            x = this.clip.xMin
        }

        var yImage = 0

        if (y < this.clip.yMin)
        {
            yImage -= y - this.clip.yMin
            height += y - this.clip.yMin
            y = this.clip.yMin
        }

        val w = minOf(this.clip.xMax + 1 - x, image.width - xImage, width, this.width - x)
        val h = minOf(this.clip.yMax + 1 - y, image.height - yImage, height, this.height - y)

        if (w <= 0 || h <= 0)
        {
            return
        }

        var lineImage = xImage + yImage * image.width
        var pixImage : Int
        var pixThis : Int
        var colorImage : Int
        var alpha : Int
        var vector : Vector
        var tx : Int
        var ty : Int

        var yy = 0
        var yyy = y
        while (yy < h)
        {
            pixImage = lineImage

            var xx = 0
            var xxx = x
            while (xx < w)
            {
                vector = transformation[xImage + xx, yImage + yy]
                tx = xxx + vector.vx
                ty = yyy + vector.vy

                if (tx >= this.clip.xMin && tx <= this.clip.xMax && ty >= this.clip.yMin &&
                    ty <= this.clip.yMax)
                {
                    pixThis = tx + ty * this.width
                    colorImage = image.pixels[pixImage]

                    alpha = colorImage.alpha

                    if (alpha == 255 || ! doAlphaMix)
                    {
                        this.pixels[pixThis] = colorImage
                    }
                    else if (alpha > 0)
                    {
                        this.mixColor(pixThis, alpha, colorImage)
                    }
                }

                pixImage ++
                xx ++
                xxx ++
            }

            lineImage += image.width
            yy ++
            yyy ++
        }
    }

    /**
     * Draw a thick line
     *
     * @param x1        First point X
     * @param y1        First point Y
     * @param x2        Second point X
     * @param y2        Second point Y
     * @param thickness Thick of the line
     * @param color     Color to use on line
     */
    fun drawThickLine(x1 : Int, y1 : Int, x2 : Int, y2 : Int, thickness : Int, color : Int)
    {
        if (thickness < 2)
        {
            this.drawLine(x1, y1, x2, y2, color)
            return
        }

        this.fillShape(this.createThickLine(x1, y1, x2, y2, thickness), color)
    }

    /**
     * Draw a thick line
     *
     * @param x1        First point X
     * @param y1        First point Y
     * @param x2        Second point X
     * @param y2        Second point Y
     * @param thickness Thick of the line
     * @param texture   Texture to use on line
     */
    fun drawThickLine(x1 : Int, y1 : Int, x2 : Int, y2 : Int, thickness : Int, texture : JHelpImage) =
        this.fillShape(this.createThickLine(x1, y1, x2, y2, thickness), texture)

    /**
     * Draw shape with thick border
     *
     * @param shape     Shape to draw
     * @param thickness Border thick
     * @param color     Color to use on border
     */
    fun drawThickShape(shape : Shape, thickness : Int, color : Int)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (thickness < 1)
        {
            return
        }

        val pathIterator = shape.getPathIterator(AFFINE_TRANSFORM, FLATNESS)

        val info = DoubleArray(6)
        var x = 0
        var y = 0
        var xStart = 0
        var yStart = 0
        var xx : Int
        var yy : Int

        while (! pathIterator.isDone)
        {
            when (pathIterator.currentSegment(info))
            {
                PathIterator.SEG_MOVETO ->
                {
                    x = Math.round(info[0])
                        .toInt()
                    xStart = x
                    y = Math.round(info[1])
                        .toInt()
                    yStart = y
                }
                PathIterator.SEG_LINETO ->
                {
                    xx = Math.round(info[0])
                        .toInt()
                    yy = Math.round(info[1])
                        .toInt()

                    this.drawThickLine(x, y, xx, yy, thickness, color)

                    x = xx
                    y = yy
                }
                PathIterator.SEG_CLOSE  ->
                {
                    this.drawThickLine(x, y, xStart, yStart, thickness, color)

                    x = xStart
                    y = yStart
                }
            }

            pathIterator.next()
        }
    }

    /**
     * Draw a neon path.
     *
     * Image MUST be in draw mode
     *
     * @param path         Path to draw
     * @param thin         Neon thick
     * @param color        Color to use
     * @param percentStart Path percent to start drawing in [0, 1]
     * @param percentEnd   Path percent to stop drawing in [0, 1]
     */
    fun drawNeon(path : Path, thin : Int, color : Int, percentStart : Double = 0.0, percentEnd : Double = 1.0)
    {
        var thin = thin
        var color = color
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val size = path.size

        if (size <= 0)
        {
            return
        }

        val alpha = color and BLACK_ALPHA_MASK
        var red = color.red
        var green = color.green
        var blue = color.blue
        var y = y(red, green, blue)
        val u = u(red, green, blue)
        val v = v(red, green, blue)
        val start = (size * Math.min(percentStart, percentEnd)).toInt()
            .bounds(0, size)
        val limit = (size * Math.max(percentStart, percentEnd)).toInt()
            .bounds(0, size)
        var segment : Segment

        do
        {
            for (index in start until limit)
            {
                segment = path[index]
                this.drawThickLine(Math.round(segment.x1)
                                       .toInt(), Math.round(segment.y1)
                                       .toInt(),
                                   Math.round(segment.x2)
                                       .toInt(), Math.round(segment.y2)
                                       .toInt(),
                                   thin, color)
            }

            y *= 2.0
            red = red(y, u, v)
            green = green(y, u, v)
            blue = blue(y, u, v)
            color = alpha or (red shl 16) or (green shl 8) or blue
            thin = thin shr 1
        }
        while (thin > 1)
    }

    /**
     * Repeat an image along a path.
     *
     * Image MUST be in draw mode
     *
     * @param path         Path to follow
     * @param elementDraw  Image to repeat
     * @param percentStart Path percent to start drawing in [0, 1]
     * @param percentEnd   Path percent to stop drawing in [0, 1]
     * @param doAlphaMix   Indicates if we do the mixing `true`, or we just override `false`
     */
    fun drawPath(path : Path, elementDraw : JHelpImage, percentStart : Double, percentEnd : Double,
                 doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        path.drawPath(this, elementDraw, doAlphaMix, percentStart, percentEnd)
    }

    /**
     * Draw a polygon
     *
     * MUST be in draw mode
     *
     * @param xs         Polygon X list
     * @param offsetX    Where start read the X list
     * @param ys         Polygon Y list
     * @param offsetY    Where start read the Y list
     * @param length     Number of point
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun drawPolygon(xs : IntArray, offsetX : Int = 0,
                    ys : IntArray, offsetY : Int = 0,
                    length : Int = Math.min(xs.size, ys.size),
                    color : Int, doAlphaMix : Boolean = true)
    {
        var offsetX = offsetX
        var offsetY = offsetY
        var length = length
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (offsetX < 0)
        {
            length += offsetX

            offsetX = 0
        }

        if (offsetY < 0)
        {
            length += offsetY

            offsetY = 0
        }

        length = minOf(length, xs.size - offsetX, ys.size - offsetY)

        if (length < 3)
        {
            return
        }

        var x = xs[offsetX]
        val xStart = x
        var y = ys[offsetY]
        val yStart = y
        var xx : Int
        var yy : Int

        for (i in 1 until length)
        {
            offsetX ++
            offsetY ++

            xx = xs[offsetX]
            yy = ys[offsetY]

            this.drawLine(x, y, xx, yy, color, doAlphaMix)

            x = xx
            y = yy
        }

        this.drawLine(x, y, xStart, yStart, color, doAlphaMix)
    }

    /**
     * Draw an empty rectangle
     *
     * MUST be in draw mode
     *
     * @param x          X of top-left
     * @param y          Y of top-left
     * @param width      Rectangle width
     * @param height     Rectangle height
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun drawRectangle(x : Int, y : Int, width : Int, height : Int, color : Int, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (width <= 0 || height <= 0)
        {
            return
        }

        val x2 = x + width - 1
        val y2 = y + height - 1

        this.drawHorizontalLine(x, x2, y, color, doAlphaMix)
        this.drawHorizontalLine(x, x2, y2, color, doAlphaMix)
        this.drawVerticalLine(x, y, y2, color, doAlphaMix)
        this.drawVerticalLine(x2, y, y2, color, doAlphaMix)
    }

    /**
     * Draw round corner rectangle

     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param width      Width
     * @param height     Height
     * @param arcWidth   Arc width
     * @param arcHeight  Arc height
     * @param color      Color to use
     * @param doAlphaMix Indicates if do alpha mixing or just overwrite
     */
    fun drawRoundRectangle(x : Int, y : Int, width : Int, height : Int, arcWidth : Int, arcHeight : Int, color : Int,
                           doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.drawShape(RoundRectangle2D.Double(x.toDouble(), y.toDouble(),
                                               width.toDouble(), height.toDouble(),
                                               arcWidth.toDouble(), arcHeight.toDouble()),
                       color, doAlphaMix)
    }

    /**
     * Draw shape with thick border
     *
     * @param shape     Shape to draw
     * @param thickness Border thick
     * @param texture   Texture to use on border
     */
    fun drawThickShape(shape : Shape, thickness : Int, texture : JHelpImage)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (thickness < 1)
        {
            return
        }

        val pathIterator = shape.getPathIterator(AFFINE_TRANSFORM, FLATNESS)

        val info = DoubleArray(6)
        var x = 0
        var y = 0
        var xStart = 0
        var yStart = 0
        var xx : Int
        var yy : Int

        while (! pathIterator.isDone)
        {
            when (pathIterator.currentSegment(info))
            {
                PathIterator.SEG_MOVETO ->
                {
                    x = Math.round(info[0])
                        .toInt()
                    xStart = x
                    y = Math.round(info[1])
                        .toInt()
                    yStart = y
                }
                PathIterator.SEG_LINETO ->
                {
                    xx = Math.round(info[0])
                        .toInt()
                    yy = Math.round(info[1])
                        .toInt()

                    this.drawThickLine(x, y, xx, yy, thickness, texture)

                    x = xx
                    y = yy
                }
                PathIterator.SEG_CLOSE  ->
                {
                    this.drawThickLine(x, y, xStart, yStart, thickness, texture)

                    x = xStart
                    y = yStart
                }
            }

            pathIterator.next()
        }
    }

    /**
     * Draw a string
     *
     * MUST be in draw mode
     *
     * @param x          X of top-left
     * @param y          Y of top-left
     * @param string     String to draw
     * @param font       Font to use
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun drawString(x : Int, y : Int, string : String, font : JHelpFont, color : Int, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val shape = font.shape(string, x, y)
        val bounds = shape.getBounds()

        this.drawShape(shape, color, doAlphaMix)

        if (font.underline)
        {
            this.drawHorizontalLine(x, x + bounds.width, font.underlinePosition(string, y), color, doAlphaMix)
        }
    }

    /**
     * Draw a string center on given point
     *
     * MUST be in draw mode
     *
     * @param x          String center X
     * @param y          String center Y
     * @param string     String to draw
     * @param font       Font to use
     * @param color      Color to use
     * @param doAlphaMix Indicates if use alpha mix
     */
    fun drawStringCenter(x : Int, y : Int, string : String, font : JHelpFont, color : Int, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val shape = font.shape(string, x, y)
        val bounds = shape.getBounds()

        this.drawShapeCenter(shape, color, doAlphaMix)

        if (font.underline)
        {
            this.drawHorizontalLine(x - (bounds.width shr 1), x + (bounds.width shr 1),
                                    font.underlinePosition(string, y - (bounds.height shr 1)), color, doAlphaMix)
        }
    }

    /**
     * Draw an ellipse with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param thickness Thick of the border
     * @param color     Color to use on border
     */
    fun drawThickEllipse(x : Int, y : Int, width : Int, height : Int, thickness : Int, color : Int)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.drawThickShape(Ellipse2D.Double(x.toDouble(), y.toDouble(),
                                             width.toDouble(), height.toDouble()),
                            thickness, color)
    }

    /**
     * Draw an ellipse with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param thickness Thick of the border
     * @param texture   Texture to use on border
     */
    fun drawThickEllipse(x : Int, y : Int, width : Int, height : Int, thickness : Int, texture : JHelpImage)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.drawThickShape(Ellipse2D.Double(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble()),
                            thickness, texture)
    }

    /**
     * Draw an ellipse with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param thickness Thick of the border
     * @param paint     Paint to use on border
     */
    fun drawThickEllipse(x : Int, y : Int, width : Int, height : Int, thickness : Int, paint : JHelpPaint)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.drawThickShape(Ellipse2D.Double(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble()),
                            thickness, paint)
    }

    /**
     * Draw a vertical line
     *
     * MUST be in draw mode
     *
     * @param x          X
     * @param y1         Start Y
     * @param y2         End Y
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun drawVerticalLine(x : Int, y1 : Int, y2 : Int, color : Int, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (x < this.clip.xMin || x > this.clip.xMax)
        {
            return
        }

        val yMin = Math.max(this.clip.yMin, Math.min(y1, y2))
        val yMax = Math.min(this.clip.yMax, Math.max(y1, y2))

        if (yMin > yMax || yMin > this.clip.yMax || yMax < this.clip.yMin)
        {
            return
        }

        val start = yMin * this.width + x
        val end = yMax * this.width + x

        if (start > end)
        {
            return
        }

        val alpha = color.alpha

        if (alpha == 0 && doAlphaMix)
        {
            return
        }

        if (alpha == 255 || ! doAlphaMix)
        {
            var pix = start
            while (pix <= end)
            {
                this.pixels[pix] = color
                pix += this.width
            }

            return
        }

        val red = (color.red) * alpha
        val green = (color.green) * alpha
        val blue = (color.blue) * alpha

        var pix = start
        while (pix <= end)
        {
            this.mixColor(pix, alpha, red, green, blue)
            pix += this.width
        }
    }

    /**
     * Stop the draw mode and refresh the image
     *
     * Don't call this method if image is locked. Use [drawModeLocked] to know.
     *
     * The image is locked if we are inside a task launch by [JHelpImage.playInDrawMode] or [JHelpImage.playOutDrawMode]
     *
     * @throws IllegalStateException If draw mode is locked
     */
    fun endDrawMode()
    {
        if (this.drawModeLocked)
        {
            throw IllegalStateException("Draw mode is locked")
        }

        if (this.drawMode)
        {
            this.drawMode = false

            this.mutexVisibilities {
                val length = this.sprites.size

                for (index in 0 until length)
                {
                    if (this.visibilities !![index])
                    {
                        this.sprites[index]
                            .changeVisible(true)
                    }
                }
            }

            synchronized(this.playOutDrawMode) {
                this.drawModeLocked = true

                while (! this.playOutDrawMode.empty)
                {
                    this.playOutDrawMode.outQueue()(this)
                }

                this.drawModeLocked = false
            }
        }

        this.update()
    }

    /**
     * Draw a thick line
     *
     * @param x1        First point X
     * @param y1        First point Y
     * @param x2        Second point X
     * @param y2        Second point Y
     * @param thickness Thick of the line
     * @param paint     Paint to use on line
     */
    fun drawThickLine(
        x1 : Int, y1 : Int, x2 : Int, y2 : Int,
        thickness : Int, paint : JHelpPaint)
    {
        this.fillShape(this.createThickLine(x1, y1, x2, y2, thickness), paint)
    }

    /**
     * Draw a thick polygon
     *
     * MUST be in draw mode
     *
     * @param xs        Polygon X list
     * @param offsetX   Where start read the X list
     * @param ys        Polygon Y list
     * @param offsetY   Where start read the Y list
     * @param length    Number of point
     * @param thickness Thickness
     * @param color     Color to use
     */
    fun drawThickPolygon(xs : IntArray, offsetX : Int = 0,
                         ys : IntArray, offsetY : Int = 0,
                         length : Int = Math.min(xs.size, ys.size),
                         thickness : Int, color : Int)
    {
        var offsetX = offsetX
        var offsetY = offsetY
        var length = length
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (offsetX < 0)
        {
            length += offsetX

            offsetX = 0
        }

        if (offsetY < 0)
        {
            length += offsetY

            offsetY = 0
        }

        length = minOf(length, xs.size - offsetX, ys.size - offsetY)

        if (length < 3 || thickness < 1)
        {
            return
        }

        var x = xs[offsetX]
        val xStart = x
        var y = ys[offsetY]
        val yStart = y
        var xx : Int
        var yy : Int

        for (i in 1 until length)
        {
            offsetX ++
            offsetY ++

            xx = xs[offsetX]
            yy = ys[offsetY]

            this.drawThickLine(x, y, xx, yy, thickness, color)

            x = xx
            y = yy
        }

        this.drawThickLine(x, y, xStart, yStart, thickness, color)
    }

    /**
     * Draw a polygon with thick border
     *
     * @param xs        Xs of polygon points
     * @param offsetX   Offset where start read the Xs
     * @param ys        Ys of polygon points
     * @param offsetY   Offset where start read Ys
     * @param length    Number of polygon point
     * @param thickness Polygon border thick
     * @param texture   Texture to use on polygon
     */
    fun drawThickPolygon(xs : IntArray, offsetX : Int = 0,
                         ys : IntArray, offsetY : Int = 0,
                         length : Int = Math.min(xs.size, ys.size),
                         thickness : Int, texture : JHelpImage)
    {
        var offsetX = offsetX
        var offsetY = offsetY
        var length = length
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (offsetX < 0)
        {
            length += offsetX

            offsetX = 0
        }

        if (offsetY < 0)
        {
            length += offsetY

            offsetY = 0
        }

        length = minOf(length, xs.size - offsetX, ys.size - offsetY)

        if (length < 3 || thickness < 1)
        {
            return
        }

        var x = xs[offsetX]
        val xStart = x
        var y = ys[offsetY]
        val yStart = y
        var xx : Int
        var yy : Int

        for (i in 1 until length)
        {
            offsetX ++
            offsetY ++

            xx = xs[offsetX]
            yy = ys[offsetY]

            this.drawThickLine(x, y, xx, yy, thickness, texture)

            x = xx
            y = yy
        }

        this.drawThickLine(x, y, xStart, yStart, thickness, texture)
    }

    /**
     * Draw a polygon with thick border
     *
     * @param xs        Xs of polygon points
     * @param offsetX   Offset where start read the Xs
     * @param ys        Ys of polygon points
     * @param offsetY   Offset where start read Ys
     * @param length    Number of polygon point
     * @param thickness Polygon border thick
     * @param paint     Paint to use on polygon
     */
    fun drawThickPolygon(xs : IntArray, offsetX : Int = 0,
                         ys : IntArray, offsetY : Int = 0,
                         length : Int = Math.min(xs.size, ys.size),
                         thickness : Int, paint : JHelpPaint)
    {
        var offsetX = offsetX
        var offsetY = offsetY
        var length = length
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (offsetX < 0)
        {
            length += offsetX

            offsetX = 0
        }

        if (offsetY < 0)
        {
            length += offsetY

            offsetY = 0
        }

        length = minOf(length, xs.size - offsetX, ys.size - offsetY)

        if (length < 3 || thickness < 1)
        {
            return
        }

        var x = xs[offsetX]
        val xStart = x
        var y = ys[offsetY]
        val yStart = y
        var xx : Int
        var yy : Int

        for (i in 1 until length)
        {
            offsetX ++
            offsetY ++

            xx = xs[offsetX]
            yy = ys[offsetY]

            this.drawThickLine(x, y, xx, yy, thickness, paint)

            x = xx
            y = yy
        }

        this.drawThickLine(x, y, xStart, yStart, thickness, paint)
    }

    /**
     * Draw rectangle with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param thickness Border thick
     * @param color     Color to use on border
     */
    fun drawThickRectangle(x : Int, y : Int, width : Int, height : Int, thickness : Int, color : Int)
    {
        val x2 = x + width
        val y2 = y + height
        this.drawThickLine(x, y, x2, y, thickness, color)
        this.drawThickLine(x2, y, x2, y2, thickness, color)
        this.drawThickLine(x2, y2, x, y2, thickness, color)
        this.drawThickLine(x, y2, x, y, thickness, color)
    }

    /**
     * Draw rectangle with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param thickness Border thick
     * @param texture   texture to use on border
     */
    fun drawThickRectangle(x : Int, y : Int, width : Int, height : Int, thickness : Int, texture : JHelpImage)
    {
        val x2 = x + width
        val y2 = y + height
        this.drawThickLine(x, y, x2, y, thickness, texture)
        this.drawThickLine(x2, y, x2, y2, thickness, texture)
        this.drawThickLine(x2, y2, x, y2, thickness, texture)
        this.drawThickLine(x, y2, x, y, thickness, texture)
    }

    /**
     * Draw rectangle with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param thickness Border thick
     * @param paint     texture to use on border
     */
    fun drawThickRectangle(x : Int, y : Int, width : Int, height : Int, thickness : Int, paint : JHelpPaint)
    {
        val x2 = x + width
        val y2 = y + height
        this.drawThickLine(x, y, x2, y, thickness, paint)
        this.drawThickLine(x2, y, x2, y2, thickness, paint)
        this.drawThickLine(x2, y2, x, y2, thickness, paint)
        this.drawThickLine(x, y2, x, y, thickness, paint)
    }

    /**
     * Draw round rectangle rectangle with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param arcWidth  Arc width
     * @param arcHeight Arc height
     * @param thickness Border thick
     * @param color     Color to use on border
     */
    fun drawThickRoundRectangle(x : Int, y : Int, width : Int, height : Int,
                                arcWidth : Int, arcHeight : Int,
                                thickness : Int, color : Int)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.drawThickShape(RoundRectangle2D.Double(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble(),
                                                    arcWidth.toDouble(), arcHeight.toDouble()), thickness, color)
    }

    /**
     * Draw round rectangle rectangle with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param arcWidth  Arc width
     * @param arcHeight Arc height
     * @param thickness Border thick
     * @param texture   Texture to use on border
     */
    fun drawThickRoundRectangle(x : Int, y : Int, width : Int, height : Int,
                                arcWidth : Int, arcHeight : Int,
                                thickness : Int, texture : JHelpImage)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.drawThickShape(RoundRectangle2D.Double(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble(),
                                                    arcWidth.toDouble(), arcHeight.toDouble()), thickness, texture)
    }

    /**
     * Draw round rectangle rectangle with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param arcWidth  Arc width
     * @param arcHeight Arc height
     * @param thickness Border thick
     * @param paint     Paint to use on border
     */
    fun drawThickRoundRectangle(x : Int, y : Int, width : Int, height : Int,
                                arcWidth : Int, arcHeight : Int,
                                thickness : Int, paint : JHelpPaint)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.drawThickShape(RoundRectangle2D.Double(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble(),
                                                    arcWidth.toDouble(), arcHeight.toDouble()), thickness, paint)
    }

    /**
     * Extract a sub image from the image
     *
     * Note : If the image is not in draw mode, all visible sprite will be consider as a part of the image
     *
     * @param x      X of upper left corner of the area to extract
     * @param y      Y of upper left corner of the area to extract
     * @param width  Area to extract width
     * @param height Area to extract height
     * @return Extracted image
     */
    fun extractSubImage(x : Int, y : Int, width : Int, height : Int) : JHelpImage
    {
        var x = x
        var y = y
        var width = width
        var height = height
        if (x < 0)
        {
            width += x
            x = 0
        }

        if (y < 0)
        {
            height += y
            y = 0
        }

        if (x + width > this.width)
        {
            width = this.width - x
        }

        if (y + height > this.height)
        {
            height = this.height - y
        }

        if (width < 1 || height < 1)
        {
            return DUMMY_IMAGE
        }

        val part = JHelpImage(width, height)

        part.startDrawMode()
        part.drawImageOver(0, 0, this, x, y, width, height)
        part.endDrawMode()

        return part
    }

    /**
     * Fill image with texture on take count original alpha, but replace other colors part
     *
     * @param texture Texture to fill
     */
    fun fillRespectAlpha(texture : JHelpImage)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val textureWidth = texture.width
        var lineTexture : Int
        var pix = 0
        var color : Int

        var y = 0
        var yTexture = 0
        while (y < this.height)
        {
            lineTexture = yTexture * textureWidth

            var x = 0
            var xTexture = 0
            while (x < this.width)
            {
                color = texture.pixels[lineTexture + xTexture]
                this.pixels[pix] = this.pixels[pix].ushr(24) * color.ushr(24) shr 8 shl 24 or (color and 0x00FFFFFF)
                pix ++
                x ++
                xTexture = (xTexture + 1) % textureWidth
            }
            y ++
            yTexture = (yTexture + 1) % texture.height
        }
    }

    /**
     * Draw shape with thick border
     *
     * @param shape     Shape to draw
     * @param thickness Border thick
     * @param paint     Paint to use on border
     */
    fun drawThickShape(shape : Shape, thickness : Int, paint : JHelpPaint)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (thickness < 1)
        {
            return
        }

        val pathIterator = shape.getPathIterator(AFFINE_TRANSFORM, FLATNESS)

        val info = DoubleArray(6)
        var x = 0
        var y = 0
        var xStart = 0
        var yStart = 0
        var xx : Int
        var yy : Int

        while (! pathIterator.isDone)
        {
            when (pathIterator.currentSegment(info))
            {
                PathIterator.SEG_MOVETO ->
                {
                    x = Math.round(info[0])
                        .toInt()
                    xStart = x
                    y = Math.round(info[1])
                        .toInt()
                    yStart = y
                }
                PathIterator.SEG_LINETO ->
                {
                    xx = Math.round(info[0])
                        .toInt()
                    yy = Math.round(info[1])
                        .toInt()

                    this.drawThickLine(x, y, xx, yy, thickness, paint)

                    x = xx
                    y = yy
                }
                PathIterator.SEG_CLOSE  ->
                {
                    this.drawThickLine(x, y, xStart, yStart, thickness, paint)

                    x = xStart
                    y = yStart
                }
            }

            pathIterator.next()
        }
    }

    /**
     * Fill a shape
     *
     * MUST be in draw mode
     *
     * @param shape      Shape to fill
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun fillShape(shape : Shape, color : Int, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val rectangle = shape.bounds

        val x = rectangle.x
        val y = rectangle.y
        val width = rectangle.width
        val height = rectangle.height

        if (width <= 0 || height <= 0)
        {
            return
        }

        val x2 = x + width - 1
        val y2 = y + height - 1

        val startX = Math.max(this.clip.xMin, x)
        val endX = Math.min(this.clip.xMax, x2)
        val startY = Math.max(this.clip.yMin, y)
        val endY = Math.min(this.clip.yMax, y2)

        if (startX > endX || startY > endY)
        {
            return
        }

        val alpha = color.alpha

        if (alpha == 0 && doAlphaMix)
        {
            return
        }

        var line = startX + startY * this.width
        var pix : Int

        if (alpha == 255 || ! doAlphaMix)
        {
            for (yy in startY .. endY)
            {
                pix = line

                for (xx in startX .. endX)
                {
                    if (shape.contains(xx.toDouble(), yy.toDouble()))
                    {
                        this.pixels[pix] = color
                    }

                    pix ++
                }

                line += this.width
            }

            return
        }

        val red = (color.red) * alpha
        val green = (color.green) * alpha
        val blue = (color.blue) * alpha

        for (yy in startY .. endY)
        {
            pix = line

            for (xx in startX .. endX)
            {
                if (shape.contains(xx.toDouble(), yy.toDouble()))
                {
                    this.mixColor(pix, alpha, red, green, blue)
                }

                pix ++
            }

            line += this.width
        }
    }

    fun fillFunction(contains : (Double, Double) -> Boolean, x : Int, y : Int, width : Int, height : Int, color : Int,
                     doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (width <= 0 || height <= 0)
        {
            return
        }

        val x2 = x + width - 1
        val y2 = y + height - 1

        val startX = Math.max(this.clip.xMin, x)
        val endX = Math.min(this.clip.xMax, x2)
        val startY = Math.max(this.clip.yMin, y)
        val endY = Math.min(this.clip.yMax, y2)

        if (startX > endX || startY > endY)
        {
            return
        }

        val alpha = color.alpha

        if (alpha == 0 && doAlphaMix)
        {
            return
        }

        var line = startX + startY * this.width
        var pix : Int

        if (alpha == 255 || ! doAlphaMix)
        {
            for (yy in startY .. endY)
            {
                pix = line

                for (xx in startX .. endX)
                {
                    if (contains(xx.toDouble(), yy.toDouble()))
                    {
                        this.pixels[pix] = color
                    }

                    pix ++
                }

                line += this.width
            }

            return
        }

        val red = (color.red) * alpha
        val green = (color.green) * alpha
        val blue = (color.blue) * alpha

        for (yy in startY .. endY)
        {
            pix = line

            for (xx in startX .. endX)
            {
                if (contains(xx.toDouble(), yy.toDouble()))
                {
                    this.mixColor(pix, alpha, red, green, blue)
                }

                pix ++
            }

            line += this.width
        }
    }

    fun fillMask(mask : BooleanArray, x : Int, y : Int, width : Int, height : Int, color : Int,
                 doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (width <= 0 || height <= 0)
        {
            return
        }

        val x2 = x + width - 1
        val y2 = y + height - 1

        val startX = Math.max(this.clip.xMin, x)
        val endX = Math.min(this.clip.xMax, x2)
        val startY = Math.max(this.clip.yMin, y)
        val endY = Math.min(this.clip.yMax, y2)

        if (startX > endX || startY > endY)
        {
            return
        }

        val alpha = color.alpha

        if (alpha == 0 && doAlphaMix)
        {
            return
        }

        var line = startX + startY * this.width
        var pix : Int
        var maskLine = startX - x
        var maskPix : Int

        if (alpha == 255 || ! doAlphaMix)
        {
            for (yy in startY .. endY)
            {
                pix = line
                maskPix = maskLine

                for (xx in startX .. endX)
                {
                    if (mask[maskPix])
                    {
                        this.pixels[pix] = color
                    }

                    pix ++
                    maskPix ++
                }

                line += this.width
                maskLine += width
            }

            return
        }

        val red = (color.red) * alpha
        val green = (color.green) * alpha
        val blue = (color.blue) * alpha

        for (yy in startY .. endY)
        {
            pix = line
            maskPix = maskLine

            for (xx in startX .. endX)
            {
                if (mask[maskPix])
                {
                    this.mixColor(pix, alpha, red, green, blue)
                }

                pix ++
                maskPix ++
            }

            line += this.width
            maskLine += width
        }
    }

    /**
     * Filter on using a palette color
     *
     * MUST be on draw mode
     *
     * @param index   Palette color indes
     * @param colorOK Color if match
     * @param colorKO Color if not match
     */
    fun filterPalette(index : Int, colorOK : Int, colorKO : Int)
    {
        this.filterOn(JHelpImage.PALETTE[index % JHelpImage.PALETTE_SIZE], 0x10, colorOK, colorKO)
    }

    /**
     * Fill pixels of image withc color.
     *
     * The start point indicates the color to fill, and all neighboards pixels with color distance of precision will be
     * colored
     *
     *
     * Must be in draw mode
     *
     * @param x         Start X
     * @param y         Start Y
     * @param color     Color to use
     * @param precision Precision for color difference
     * @param alphaMix  Indicates if alpha mix or replace
     */
    fun fillColor(x : Int, y : Int, color : Int, precision : Int, alphaMix : Boolean = true)
    {
        var precision = precision
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (x < 0 || x > this.width || y < 0 || y >= this.height)
        {
            return
        }

        val alpha = color.alpha
        if (alpha == 0 && alphaMix)
        {
            return
        }

        precision = Math.max(0, precision)
        val start = this.pixels[x + y * this.width]
        if (distanceColor(start, color) <= precision)
        {
            return
        }

        if (alpha == 255 || ! alphaMix)
        {
            val stack = Stack<Point>()
            stack.push(Point(x, y))
            var point : Point

            while (! stack.isEmpty())
            {
                point = stack.pop()
                this.pixels[point.x + point.y * this.width] = color

                if (point.x > 0 && distanceColor(start,
                                                 this.pixels[point.x - 1 + point.y * this.width]) <= precision)
                {
                    stack.push(Point(point.x - 1, point.y))
                }

                if (point.x < this.width - 1 && distanceColor(start,
                                                              this.pixels[point.x + 1 + point.y * this.width]) <= precision)
                {
                    stack.push(Point(point.x + 1, point.y))
                }

                if (point.y > 0 && distanceColor(start,
                                                 this.pixels[point.x + (point.y - 1) * this.width]) <= precision)
                {
                    stack.push(Point(point.x, point.y - 1))
                }

                if (point.y < this.height - 1 && distanceColor(start,
                                                               this.pixels[point.x + (point.y + 1) * this.width]) <= precision)
                {
                    stack.push(Point(point.x, point.y + 1))
                }
            }

            return
        }

        val stack = Stack<Point>()
        stack.push(Point(x, y))
        var point : Point
        val red = (color.red) * alpha
        val green = (color.green) * alpha
        val blue = (color.blue) * alpha
        var pix : Int

        while (! stack.isEmpty())
        {
            point = stack.pop()

            pix = point.x + point.y * this.width
            this.mixColor(pix, alpha, red, green, blue)

            if (point.x > 0 && distanceColor(start,
                                             this.pixels[point.x - 1 + point.y * this.width]) <= precision)
            {
                stack.push(Point(point.x - 1, point.y))
            }

            if (point.x < this.width - 1 && distanceColor(start,
                                                          this.pixels[point.x + 1 + point.y * this.width]) <= precision)
            {
                stack.push(Point(point.x + 1, point.y))
            }

            if (point.y > 0 && distanceColor(start,
                                             this.pixels[point.x + (point.y - 1) * this.width]) <= precision)
            {
                stack.push(Point(point.x, point.y - 1))
            }

            if (point.y < this.height - 1 && distanceColor(start,
                                                           this.pixels[point.x + (point.y + 1) * this.width]) <= precision)
            {
                stack.push(Point(point.x, point.y + 1))
            }
        }
    }

    /**
     * Fill an ellipse
     *
     * MUST be in draw mode
     *
     * @param x          X of bounds top-left
     * @param y          Y of bounds top-left
     * @param width      Ellipse width
     * @param height     Ellipse height
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun fillEllipse(x : Int, y : Int, width : Int, height : Int, color : Int, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.fillShape(Ellipse2D.Double(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble()), color,
                       doAlphaMix)
    }

    fun fillCircle(x : Int, y : Int, radius : Int, color : Int, doAlphaMix : Boolean = true) =
        this.fillEllipse(x - radius, y - radius, radius shl 1, radius shl 1, color, doAlphaMix)

    fun fillRing(x : Int, y : Int, inRadius : Int, outRadius : Int, color : Int, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (inRadius == outRadius)
        {
            this.drawCircle(x, y, inRadius, color, doAlphaMix)
            return
        }

        this.fillShape(ring(x.toDouble(), y.toDouble(), inRadius.toDouble(), outRadius.toDouble()), color,
                       doAlphaMix)
    }

    /**
     * Fill ellipse with a texture
     *
     * Note : if the texture is not in draw moe, all of it's visible sprite will be consider like a part of he texture
     *
     * MUST be in draw mode
     *
     * @param x          X of bounds top-left
     * @param y          Y of bounds top-left
     * @param width      Ellipse width
     * @param height     Ellipse height
     * @param texture    Texture to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun fillEllipse(x : Int, y : Int, width : Int, height : Int, texture : JHelpImage, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.fillShape(Ellipse2D.Double(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble()), texture,
                       doAlphaMix)
    }

    fun fillCircle(x : Int, y : Int, radius : Int, texture : JHelpImage, doAlphaMix : Boolean = true) =
        this.fillEllipse(x - radius, y - radius, radius shl 1, radius shl 1, texture, doAlphaMix)

    fun fillRing(x : Int, y : Int, inRadius : Int, outRadius : Int, texture : JHelpImage, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.fillShape(ring(x.toDouble(), y.toDouble(), inRadius.toDouble(), outRadius.toDouble()), texture,
                       doAlphaMix)
    }

    /**
     * Fill an ellipse
     *
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param width      Width
     * @param height     Height
     * @param paint      Paint to use
     * @param doAlphaMix Indicates if do alpha mixing or just overwrite
     */
    fun fillEllipse(x : Int, y : Int, width : Int, height : Int, paint : JHelpPaint, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.fillShape(Ellipse2D.Double(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble()), paint,
                       doAlphaMix)
    }

    fun fillCircle(x : Int, y : Int, radius : Int, paint : JHelpPaint, doAlphaMix : Boolean = true) =
        this.fillEllipse(x - radius, y - radius, radius shl 1, radius shl 1, paint, doAlphaMix)

    fun fillRing(x : Int, y : Int, inRadius : Int, outRadius : Int, paint : JHelpPaint, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.fillShape(ring(x.toDouble(), y.toDouble(), inRadius.toDouble(), outRadius.toDouble()), paint,
                       doAlphaMix)
    }

    /**
     * Fill a polygon
     *
     * MUST be in draw mode
     *
     * @param xs         X list
     * @param offsetX    X list start offset
     * @param ys         Y list
     * @param offsetY    Y list start offset
     * @param length     Number of points
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun fillPolygon(xs : IntArray, offsetX : Int = 0,
                    ys : IntArray, offsetY : Int = 0,
                    length : Int = Math.min(xs.size - offsetX, ys.size - offsetY),
                    color : Int, doAlphaMix : Boolean = true)
    {
        var offsetX = offsetX
        var offsetY = offsetY
        var length = length
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (offsetX < 0)
        {
            length += offsetX

            offsetX = 0
        }

        if (offsetY < 0)
        {
            length += offsetY

            offsetY = 0
        }

        length = minOf(length, xs.size - offsetX, ys.size - offsetY)

        if (length < 3)
        {
            return
        }

        val polygon = Polygon(Arrays.copyOfRange(xs, offsetX, offsetX + length), //
                              Arrays.copyOfRange(ys, offsetY, offsetY + length), length)

        this.fillShape(polygon, color, doAlphaMix)
    }

    /**
     * Fill a polygon
     *
     * Note : if the texture is not in draw moe, all of it's visible sprte will be condider like a part of he texture
     *
     * MUST be in draw mode
     *
     * @param xs         X list
     * @param offsetX    X list start offset
     * @param ys         Y list
     * @param offsetY    Y list offset
     * @param length     Number of points
     * @param texture    Texture to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun fillPolygon(xs : IntArray, offsetX : Int = 0,
                    ys : IntArray, offsetY : Int = 0,
                    length : Int = Math.min(xs.size - offsetX, ys.size - offsetY),
                    texture : JHelpImage, doAlphaMix : Boolean = true)
    {
        var offsetX = offsetX
        var offsetY = offsetY
        var length = length
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (offsetX < 0)
        {
            length += offsetX

            offsetX = 0
        }

        if (offsetY < 0)
        {
            length += offsetY

            offsetY = 0
        }

        length = minOf(length, xs.size - offsetX, ys.size - offsetY)

        if (length < 3)
        {
            return
        }

        val polygon = Polygon(Arrays.copyOfRange(xs, offsetX, offsetX + length), //
                              Arrays.copyOfRange(ys, offsetY, offsetY + length), length)

        this.fillShape(polygon, texture, doAlphaMix)
    }

    /**
     * Fill a polygon
     *
     * MUST be in draw mode
     *
     * @param xs         X coordinates
     * @param offsetX    Start read offset of xs
     * @param ys         Y coordinates
     * @param offsetY    Start read offset of ys
     * @param length     Number of point
     * @param paint      Paint to use
     * @param doAlphaMix Indicates if do alpha mixing or just overwrite
     */
    fun fillPolygon(xs : IntArray, offsetX : Int = 0, ys : IntArray, offsetY : Int = 0,
                    length : Int = Math.min(xs.size - offsetX, ys.size - offsetY),
                    paint : JHelpPaint, doAlphaMix : Boolean = true)
    {
        var offsetX = offsetX
        var offsetY = offsetY
        var length = length
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (offsetX < 0)
        {
            length += offsetX

            offsetX = 0
        }

        if (offsetY < 0)
        {
            length += offsetY

            offsetY = 0
        }

        length = minOf(length, xs.size - offsetX, ys.size - offsetY)

        if (length < 3)
        {
            return
        }

        val polygon = Polygon(Arrays.copyOfRange(xs, offsetX, offsetX + length), //
                              Arrays.copyOfRange(ys, offsetY, offsetY + length), length)

        this.fillShape(polygon, paint, doAlphaMix)
    }

    /**
     * Fill a rectangle
     *
     * MUST be in draw mode
     *
     * @param x          X top-left
     * @param y          U top-left
     * @param width      Rectangle width
     * @param height     Rectangle height
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun fillRectangle(x : Int, y : Int, width : Int, height : Int, color : Int, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (width <= 0 || height <= 0)
        {
            return
        }

        val x2 = x + width - 1
        val y2 = y + height - 1

        val startX = maxOf(this.clip.xMin, x, 0)
        val endX = minOf(this.clip.xMax, x2, this.width - 1)
        val startY = maxOf(this.clip.yMin, y, 0)
        val endY = minOf(this.clip.yMax, y2, this.height - 1)

        if (startX > endX || startY > endY)
        {
            return
        }

        val alpha = color.alpha

        if (alpha == 0 && doAlphaMix)
        {
            return
        }

        var line : Int = startX + startY * this.width
        var pix : Int

        if (alpha == 255 || ! doAlphaMix)
        {
            for (yy in startY .. endY)
            {
                pix = line

                for (xx in startX .. endX)
                {
                    this.pixels[pix] = color

                    pix ++
                }

                line += this.width
            }

            return
        }

        val red = (color.red) * alpha
        val green = (color.green) * alpha
        val blue = (color.blue) * alpha

        for (yy in startY .. endY)
        {
            pix = line

            for (xx in startX until endX)
            {
                this.mixColor(pix, alpha, red, green, blue)
                pix ++
            }

            line += this.width
        }
    }

    /**
     * Fill a rectangle

     * Note : if the texture is not in draw moe, all of it's visible sprte will be condider like a part of he texture

     * MUST be in draw mode
     *
     * @param x          X top-left
     * @param y          Y top-left
     * @param width      Rectangle width
     * @param height     Rectangle height
     * @param texture    Texture to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun fillRectangle(x : Int, y : Int, width : Int, height : Int, texture : JHelpImage, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (width <= 0 || height <= 0)
        {
            return
        }

        val x2 = x + width - 1
        val y2 = y + height - 1

        val startX = Math.max(this.clip.xMin, x)
        val endX = Math.min(this.clip.xMax, x2)
        val startY = Math.max(this.clip.yMin, y)
        val endY = Math.min(this.clip.yMax, y2)

        if (startX > endX || startY > endY)
        {
            return
        }

        var line = startX + startY * this.width
        var pix : Int

        val startXTexture = (startX - x) % texture.width
        var yTexture = (startY - y) % texture.height
        var pixTexture : Int
        var colorTexture : Int

        var alpha : Int

        var yy = startY
        while (yy <= endY)
        {
            pixTexture = yTexture * texture.width
            pix = line

            var xx = startX
            var xTexture = startXTexture
            while (xx < endX)
            {
                colorTexture = texture.pixels[pixTexture + xTexture]

                alpha = colorTexture.alpha

                if (alpha == 255 || ! doAlphaMix)
                {
                    this.pixels[pix] = colorTexture
                }
                else if (alpha > 0)
                {
                    this.mixColor(pix, alpha, colorTexture)
                }

                pix ++
                xx ++
                xTexture = (xTexture + 1) % texture.width
            }

            line += this.width
            yy ++
            yTexture = (yTexture + 1) % texture.height
        }
    }

    /**
     * Fill a rectangle
     *
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param width      Width
     * @param height     Height
     * @param paint      Paint to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun fillRectangle(x : Int, y : Int, width : Int, height : Int, paint : JHelpPaint, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (width <= 0 || height <= 0)
        {
            return
        }

        val x2 = x + width - 1
        val y2 = y + height - 1

        val startX = Math.max(this.clip.xMin, x)
        val endX = Math.min(this.clip.xMax, x2)
        val startY = Math.max(this.clip.yMin, y)
        val endY = Math.min(this.clip.yMax, y2)

        if (startX > endX || startY > endY)
        {
            return
        }

        paint.initializePaint(width, height)

        var line = startX + startY * this.width
        var pix : Int

        val startXPaint = startX - x
        var yPaint = startY - y
        var colorPaint : Int

        var alpha : Int

        var yy = startY
        while (yy <= endY)
        {
            pix = line

            var xx = startX
            var xPaint = startXPaint
            while (xx <= endX)
            {
                colorPaint = paint.obtainColor(xPaint, yPaint)

                alpha = colorPaint.alpha

                if (alpha == 255 || ! doAlphaMix)
                {
                    this.pixels[pix] = colorPaint
                }
                else if (alpha > 0)
                {
                    this.mixColor(pix, alpha, colorPaint)
                }

                pix ++
                xx ++
                xPaint ++
            }

            line += this.width
            yy ++
            yPaint ++
        }
    }

    /**
     * Fill rectangle and invert colors
     *
     * @param x      Up left corner X
     * @param y      Up left corner Y
     * @param width  Rectangle width
     * @param height Rectangle height
     */
    fun fillRectangleInverseColor(x : Int, y : Int, width : Int, height : Int)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (width <= 0 || height <= 0)
        {
            return
        }

        val x2 = x + width - 1
        val y2 = y + height - 1

        val startX = maxOf(this.clip.xMin, x, 0)
        val endX = minOf(this.clip.xMax, x2, this.width - 1)
        val startY = maxOf(this.clip.yMin, y, 0)
        val endY = minOf(this.clip.yMax, y2, this.height - 1)

        if (startX > endX || startY > endY)
        {
            return
        }

        var line : Int = startX + startY * this.width
        var pix : Int
        var color : Int

        for (yy in startY .. endY)
        {
            pix = line

            for (xx in startX .. endX)
            {
                color = this.pixels[pix]
                this.pixels[pix] = color and BLACK_ALPHA_MASK or (color.inv() and COLOR_MASK)

                pix ++
            }

            line += this.width
        }
    }

    /**
     * Fill a rectangle with an image.
     *
     * The image is scaled to fit rectangle size
     *
     * Note : if the texture is not in draw mode, all of it's visible sprite will be consider like a part of he texture
     *
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param width      Width
     * @param height     Height
     * @param texture    Image to draw
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun fillRectangleScale(x : Int, y : Int, width : Int, height : Int, texture : JHelpImage,
                           doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (width <= 0 || height <= 0)
        {
            return
        }

        val x2 = x + width - 1
        val y2 = y + height - 1

        val startX = Math.max(this.clip.xMin, x)
        val endX = Math.min(this.clip.xMax, x2)
        val startY = Math.max(this.clip.yMin, y)
        val endY = Math.min(this.clip.yMax, y2)

        if (startX > endX || startY > endY)
        {
            return
        }

        var line = startX + startY * this.width
        val startXT = startX - x
        var yt = startY - y
        var yTexture = yt * texture.height / height
        var yy = startY

        while (yy <= endY)
        {
            val pixTexture = yTexture * texture.width
            var pix = line
            var xx = startX
            var xt = startXT
            var xTexture = 0

            while (xx < endX)
            {
                val colorTexture = texture.pixels[pixTexture + xTexture]
                val alpha = colorTexture.alpha

                if (alpha == 255 || ! doAlphaMix)
                {
                    this.pixels[pix] = colorTexture
                }
                else if (alpha > 0)
                {
                    this.mixColor(pix, alpha, colorTexture)
                }

                pix ++
                xx ++
                xt ++
                xTexture = xt * texture.width / width
            }

            line += this.width
            yy ++
            yt ++
            yTexture = yt * texture.height / height
        }
    }

    /**
     * Fill a rectangle with an image.
     *
     * The image is scaled to fit rectangle size.
     *
     * The result is nicer than [.fillRectangleScale] but it is slower
     * and take temporary more memory
     *
     * Note : if the texture is not in draw mode, all of it's visible sprite will be consider like a part of the texture
     *
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param width      Width
     * @param height     Height
     * @param texture    Image to draw
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun fillRectangleScaleBetter(x : Int, y : Int, width : Int, height : Int, texture : JHelpImage,
                                 doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (width <= 0 || height <= 0)
        {
            return
        }

        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics2d = bufferedImage.createGraphics()

        graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                                    RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY)
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY)
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
        graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

        graphics2d.drawImage(texture.image, 0, 0, width, height, null)

        var pixels = IntArray(width * height)
        pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width)

        val image = JHelpImage(width, height, pixels)

        bufferedImage.flush()

        this.fillRectangle(x, y, width, height, image, doAlphaMix)
    }

    /**
     * Image height
     *
     * @return Image height
     */
    override fun height() = this.height

    /**
     * Fill the image with a color on respect the alpha.
     *
     * That is to say the given color alpha is no use, but original image alpha for given a pixel
     *
     * @param color Color for fill
     */
    fun fillRespectAlpha(color : Int)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val pure = color and COLOR_MASK
        for (pix in 0 until this.pixels.size)
        {
            this.pixels[pix] = (this.pixels[pix] and BLACK_ALPHA_MASK) or pure
        }
    }

    /**
     * Fill image with pain on respect original alpha, but replace other color parts
     *
     * @param paint Paint to fill with
     */
    fun fillRespectAlpha(paint : JHelpPaint)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        paint.initializePaint(this.width, this.height)
        var pix = 0
        var color : Int

        for (y in 0 until this.height)
        {
            for (x in 0 until this.width)
            {
                color = paint.obtainColor(x, y)
                this.pixels[pix] = this.pixels[pix].ushr(24) * color.ushr(24) shr 8 shl 24 or (color and 0x00FFFFFF)
                pix ++
            }
        }
    }

    /**
     * Fill a round rectangle
     *
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param width      Width
     * @param height     Height
     * @param arcWidth   Arc width
     * @param arcHeight  Arc height
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun fillRoundRectangle(x : Int, y : Int, width : Int, height : Int, arcWidth : Int, arcHeight : Int, color : Int,
                           doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.fillShape(RoundRectangle2D.Double(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble(),
                                               arcWidth.toDouble(), arcHeight.toDouble()), color, doAlphaMix)
    }

    /**
     * Fill a round rectangle
     *
     * Note : if the texture is not in draw mode, all of it's visible sprite will be consider like a part of he texture
     *
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param width      Width
     * @param height     Height
     * @param arcWidth   Arc width
     * @param arcHeight  Arc height
     * @param texture    Texture to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun fillRoundRectangle(
        x : Int, y : Int, width : Int, height : Int,
        arcWidth : Int, arcHeight : Int,
        texture : JHelpImage, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.fillShape(RoundRectangle2D.Double(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble(),
                                               arcWidth.toDouble(), arcHeight.toDouble()), texture, doAlphaMix)
    }

    /**
     * Fill a round rectangle
     *
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param width      Width
     * @param height     Height
     * @param arcWidth   Arc width
     * @param arcHeight  Arc height
     * @param paint      Paint to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun fillRoundRectangle(
        x : Int, y : Int, width : Int, height : Int,
        arcWidth : Int, arcHeight : Int, paint : JHelpPaint,
        doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        this.fillShape(RoundRectangle2D.Double(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble(),
                                               arcWidth.toDouble(), arcHeight.toDouble()), paint, doAlphaMix)
    }

    /**
     * Image type
     */
    override fun imageType() = RasterImageType.JHELP_IMAGE

    /**
     * Image width
     */
    override fun width() = this.width

    /**
     * Fill a shape
     *
     * Note : if the texture is not in draw mode, all of it's visible sprite will be consider like a part of he texture
     *
     * MUST be in draw mode
     *
     * @param shape      Shape to fill
     * @param texture    Texture to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun fillShape(shape : Shape, texture : JHelpImage, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val rectangle = shape.bounds

        val x = rectangle.x
        val y = rectangle.y
        val width = rectangle.width
        val height = rectangle.height

        if (width <= 0 || height <= 0)
        {
            return
        }

        val x2 = x + width - 1
        val y2 = y + height - 1

        val startX = Math.max(this.clip.xMin, x)
        val endX = Math.min(this.clip.xMax, x2)
        val startY = Math.max(this.clip.yMin, y)
        val endY = Math.min(this.clip.yMax, y2)

        if (startX > endX || startY > endY)
        {
            return
        }

        var line = startX + startY * this.width
        var pix : Int

        val startTextureX = (startX - x) % texture.width
        var yTexture = (startY - y) % texture.height
        var pixTexture : Int
        var colorTexture : Int

        var alpha : Int

        var yy = startY
        while (yy <= endY)
        {
            pixTexture = yTexture * texture.width
            pix = line

            var xx = startX
            var xTexture = startTextureX
            while (xx <= endX)
            {
                if (shape.contains(xx.toDouble(), yy.toDouble()))
                {
                    colorTexture = texture.pixels[pixTexture + xTexture]

                    alpha = colorTexture.alpha

                    if (alpha == 255 || ! doAlphaMix)
                    {
                        this.pixels[pix] = colorTexture
                    }
                    else if (alpha > 0)
                    {
                        this.mixColor(pix, alpha, colorTexture)
                    }
                }

                pix ++
                xx ++
                xTexture = (xTexture + 1) % texture.width
            }

            line += this.width
            yy ++
            yTexture = (yTexture + 1) % texture.height
        }
    }

    fun fillMask(mask : BooleanArray, x : Int, y : Int, width : Int, height : Int,
                 texture : JHelpImage, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (width <= 0 || height <= 0)
        {
            return
        }

        val x2 = x + width - 1
        val y2 = y + height - 1

        val startX = Math.max(this.clip.xMin, x)
        val endX = Math.min(this.clip.xMax, x2)
        val startY = Math.max(this.clip.yMin, y)
        val endY = Math.min(this.clip.yMax, y2)

        if (startX > endX || startY > endY)
        {
            return
        }

        var line = startX + startY * this.width
        var pix : Int
        var lineMask = startX - x
        var pixMask : Int

        val startTextureX = (startX - x) % texture.width
        var yTexture = (startY - y) % texture.height
        var pixTexture : Int
        var colorTexture : Int

        var alpha : Int

        var yy = startY
        while (yy <= endY)
        {
            pixTexture = yTexture * texture.width
            pix = line
            pixMask = lineMask

            var xx = startX
            var xTexture = startTextureX
            while (xx <= endX)
            {
                if (mask[pixMask])
                {
                    colorTexture = texture.pixels[pixTexture + xTexture]

                    alpha = colorTexture.alpha

                    if (alpha == 255 || ! doAlphaMix)
                    {
                        this.pixels[pix] = colorTexture
                    }
                    else if (alpha > 0)
                    {
                        this.mixColor(pix, alpha, colorTexture)
                    }
                }

                pix ++
                pixMask ++
                xx ++
                xTexture = (xTexture + 1) % texture.width
            }

            line += this.width
            lineMask += width
            yy ++
            yTexture = (yTexture + 1) % texture.height
        }
    }

    fun fillFunction(contains : (Double, Double) -> Boolean, x : Int, y : Int, width : Int, height : Int,
                     texture : JHelpImage, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (width <= 0 || height <= 0)
        {
            return
        }

        val x2 = x + width - 1
        val y2 = y + height - 1

        val startX = Math.max(this.clip.xMin, x)
        val endX = Math.min(this.clip.xMax, x2)
        val startY = Math.max(this.clip.yMin, y)
        val endY = Math.min(this.clip.yMax, y2)

        if (startX > endX || startY > endY)
        {
            return
        }

        var line = startX + startY * this.width
        var pix : Int

        val startTextureX = (startX - x) % texture.width
        var yTexture = (startY - y) % texture.height
        var pixTexture : Int
        var colorTexture : Int

        var alpha : Int

        var yy = startY
        while (yy <= endY)
        {
            pixTexture = yTexture * texture.width
            pix = line

            var xx = startX
            var xTexture = startTextureX
            while (xx <= endX)
            {
                if (contains(xx.toDouble(), yy.toDouble()))
                {
                    colorTexture = texture.pixels[pixTexture + xTexture]

                    alpha = colorTexture.alpha

                    if (alpha == 255 || ! doAlphaMix)
                    {
                        this.pixels[pix] = colorTexture
                    }
                    else if (alpha > 0)
                    {
                        this.mixColor(pix, alpha, colorTexture)
                    }
                }

                pix ++
                xx ++
                xTexture = (xTexture + 1) % texture.width
            }

            line += this.width
            yy ++
            yTexture = (yTexture + 1) % texture.height
        }
    }

    /**
     * Fill a shape
     *
     * MUST be in draw mode
     *
     * @param shape      Shape to fill
     * @param paint      Paint to use
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     */
    fun fillShape(shape : Shape, paint : JHelpPaint, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val rectangle = shape.bounds

        val x = rectangle.x
        val y = rectangle.y
        val width = rectangle.width
        val height = rectangle.height

        if (width <= 0 || height <= 0)
        {
            return
        }

        val x2 = x + width - 1
        val y2 = y + height - 1

        val startX = Math.max(this.clip.xMin, x)
        val endX = Math.min(this.clip.xMax, x2)
        val startY = Math.max(this.clip.yMin, y)
        val endY = Math.min(this.clip.yMax, y2)

        if (startX > endX || startY > endY)
        {
            return
        }

        paint.initializePaint(width, height)

        var line = startX + startY * this.width
        var pix : Int

        val startXPaint = startX - x
        var yPaint = startY - y
        var colorPaint : Int

        var alpha : Int

        var yy = startY
        while (yy <= endY)
        {
            pix = line

            var xx = startX
            var xPaint = startXPaint
            while (xx <= endX)
            {
                if (shape.contains(xx.toDouble(), yy.toDouble()))
                {
                    colorPaint = paint.obtainColor(xPaint, yPaint)

                    alpha = colorPaint.alpha

                    if (alpha == 255 || ! doAlphaMix)
                    {
                        this.pixels[pix] = colorPaint
                    }
                    else if (alpha > 0)
                    {
                        this.mixColor(pix, alpha, colorPaint)
                    }
                }

                pix ++
                xx ++
                xPaint ++
            }

            line += this.width
            yy ++
            yPaint ++
        }
    }

    fun fillFunction(contains : (Double, Double) -> Boolean, x : Int, y : Int, width : Int, height : Int,
                     paint : JHelpPaint,
                     doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (width <= 0 || height <= 0)
        {
            return
        }

        val x2 = x + width - 1
        val y2 = y + height - 1

        val startX = Math.max(this.clip.xMin, x)
        val endX = Math.min(this.clip.xMax, x2)
        val startY = Math.max(this.clip.yMin, y)
        val endY = Math.min(this.clip.yMax, y2)

        if (startX > endX || startY > endY)
        {
            return
        }

        paint.initializePaint(width, height)

        var line = startX + startY * this.width
        var pix : Int

        val startXPaint = startX - x
        var yPaint = startY - y
        var colorPaint : Int

        var alpha : Int

        var yy = startY
        while (yy <= endY)
        {
            pix = line

            var xx = startX
            var xPaint = startXPaint
            while (xx <= endX)
            {
                if (contains(xx.toDouble(), yy.toDouble()))
                {
                    colorPaint = paint.obtainColor(xPaint, yPaint)

                    alpha = colorPaint.alpha

                    if (alpha == 255 || ! doAlphaMix)
                    {
                        this.pixels[pix] = colorPaint
                    }
                    else if (alpha > 0)
                    {
                        this.mixColor(pix, alpha, colorPaint)
                    }
                }

                pix ++
                xx ++
                xPaint ++
            }

            line += this.width
            yy ++
            yPaint ++
        }
    }

    fun fillMask(mask : BooleanArray, x : Int, y : Int, width : Int, height : Int, paint : JHelpPaint,
                 doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (width <= 0 || height <= 0)
        {
            return
        }

        val x2 = x + width - 1
        val y2 = y + height - 1

        val startX = Math.max(this.clip.xMin, x)
        val endX = Math.min(this.clip.xMax, x2)
        val startY = Math.max(this.clip.yMin, y)
        val endY = Math.min(this.clip.yMax, y2)

        if (startX > endX || startY > endY)
        {
            return
        }

        paint.initializePaint(width, height)

        var line = startX + startY * this.width
        var pix : Int
        var lineMask = startX - x
        var pixMask : Int

        val startXPaint = startX - x
        var yPaint = startY - y
        var colorPaint : Int

        var alpha : Int

        var yy = startY
        while (yy <= endY)
        {
            pix = line
            pixMask = lineMask

            var xx = startX
            var xPaint = startXPaint
            while (xx <= endX)
            {
                if (mask[pixMask])
                {
                    colorPaint = paint.obtainColor(xPaint, yPaint)

                    alpha = colorPaint.alpha

                    if (alpha == 255 || ! doAlphaMix)
                    {
                        this.pixels[pix] = colorPaint
                    }
                    else if (alpha > 0)
                    {
                        this.mixColor(pix, alpha, colorPaint)
                    }
                }

                pix ++
                pixMask ++
                xx ++
                xPaint ++
            }

            line += this.width
            lineMask += width
            yy ++
            yPaint ++
        }
    }

    /**
     * Fill a string
     *
     * MUST be in draw mode
     *
     * @param x          X top-left
     * @param y          Y top-left
     * @param string     String to draw
     * @param font       Font to use
     * @param color      Color for fill
     * @param textAlign  Text alignment if several lines (\n)
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     * @return Bounds where string just draw
     */
    fun fillString(x : Int, y : Int, string : String, font : JHelpFont, color : Int,
                   textAlign : TextAlignment = TextAlignment.LEFT,
                   doAlphaMix : Boolean = true) : Rectangle
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val lines = font.computeTextLinesAlpha(string, textAlign,
                                               this.width - x, this.height - y, true)
        var mask : JHelpImage

        for (textLineAlpha in lines.first)
        {
            mask = textLineAlpha.mask
            mask.startDrawMode()
            mask.fillRespectAlpha(color)
            mask.endDrawMode()
            this.drawImage(x = x + textLineAlpha.x, y = y + textLineAlpha.y, image = mask, doAlphaMix = doAlphaMix)
        }

        return Rectangle(x, y, lines.second.width, lines.second.height)
    }

    fun fillStringCenter(x : Int, y : Int, string : String, font : JHelpFont, color : Int,
                         textAlign : TextAlignment = TextAlignment.LEFT,
                         doAlphaMix : Boolean = true) : Rectangle
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val lines = font.computeTextLinesAlpha(string, textAlign,
                                               this.width - x, this.height - y, true)
        var mask : JHelpImage
        val xx = - (lines.second.width shr 1)
        val yy = - (lines.second.height shr 1)

        for (textLineAlpha in lines.first)
        {
            mask = textLineAlpha.mask
            mask.startDrawMode()
            mask.fillRespectAlpha(color)
            mask.endDrawMode()
            this.drawImage(x = x + textLineAlpha.x + xx, y = y + textLineAlpha.y + yy, image = mask,
                           doAlphaMix = doAlphaMix)
        }

        return Rectangle(x + xx, y + yy, lines.second.width, lines.second.height)
    }

    /**
     * Fill a string
     *
     * Note : if the texture is not in draw mode, all of it's visible sprite will be consider like a part of he texture
     *
     * MUST be in draw mode
     *
     * @param x          X top-left
     * @param y          Y top-left
     * @param string     String to fill
     * @param font       Font to use
     * @param texture    Texture to use
     * @param color      Color if underline
     * @param textAlign  Text alignment if several lines (\n)
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     * @return Bounds where string just draw
     */
    fun fillString(x : Int, y : Int, string : String, font : JHelpFont, texture : JHelpImage, color : Int,
                   textAlign : TextAlignment = TextAlignment.LEFT, doAlphaMix : Boolean = true) : Rectangle
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val lines = font.computeTextLinesAlpha(string, textAlign,
                                               this.width - x,
                                               this.height - y,
                                               true)
        var mask : JHelpImage

        for (textLineAlpha in lines.first)
        {
            mask = textLineAlpha.mask
            mask.startDrawMode()
            mask.fillRespectAlpha(texture)
            mask.endDrawMode()
            this.drawImage(x = x + textLineAlpha.x, y = y + textLineAlpha.y, image = mask, doAlphaMix = doAlphaMix)
        }

        if (font.underline)
        {
            this.drawHorizontalLine(x, x + lines.second.width, font.underlinePosition(string, y), color, doAlphaMix)
        }

        return Rectangle(x, y, lines.second.width, lines.second.height)
    }

    /**
     * Fill a string
     *
     * MUST be on draw mode
     *
     * @param x          X
     * @param y          Y
     * @param string     String to fill
     * @param font       Font to use
     * @param paint      Paint to use
     * @param color      Color for underline
     * @param textAlign  Text alignment if several lines (\n)
     * @param doAlphaMix Indicates if we do the mixing `true`, or we just override `false`
     * @return Bounds where string just draw
     */
    fun fillString(x : Int, y : Int, string : String, font : JHelpFont, paint : JHelpPaint, color : Int,
                   textAlign : TextAlignment = TextAlignment.LEFT, doAlphaMix : Boolean = true) : Rectangle
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val lines = font.computeTextLinesAlpha(string, textAlign,
                                               this.width - x, this.height - y, true)
        var mask : JHelpImage

        for (textLineAlpha in lines.first)
        {
            mask = textLineAlpha.mask
            mask.startDrawMode()
            mask.fillRespectAlpha(paint)
            mask.endDrawMode()
            this.drawImage(x + textLineAlpha.x, y + textLineAlpha.y, mask, doAlphaMix = doAlphaMix)
        }

        if (font.underline)
        {
            this.drawHorizontalLine(x, x + lines.second.width, font.underlinePosition(string, y), color, doAlphaMix)
        }

        return Rectangle(x, y, lines.second.width, lines.second.height)
    }

    /**
     * Filter image on blue channel
     *
     * MUST be on draw mode
     */
    fun filterBlue()
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var color : Int
        var blue : Int

        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]
            blue = color.blue

            this.pixels[pix] = color and 0xFF0000FF.toInt() or (blue shl 16) or (blue shl 8)
        }
    }

    /**
     * Filter image on green channel
     *
     * MUST be on draw mode
     */
    fun filterGreen()
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var color : Int
        var green : Int

        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]
            green = color.green

            this.pixels[pix] = color and 0xFF00FF00.toInt() or (green shl 16) or green
        }
    }

    /**
     * Filter image on a specific color
     *
     * MUST be on draw mode
     *
     * @param color   Color search
     * @param colorOK Color to use if corresponds
     * @param colorKO Colo to use if failed
     */
    fun filterOn(color : Int, colorOK : Int, colorKO : Int)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        for (pix in 0 until this.pixels.size)
        {
            if (color == this.pixels[pix])
            {
                this.pixels[pix] = colorOK
            }
            else
            {
                this.pixels[pix] = colorKO
            }
        }
    }

    /**
     * filter image on a specific color
     *
     * MUST be on draw mode
     *
     * @param color     Color search
     * @param precision Precision to use
     * @param colorOK   Color if corresponds
     * @param colorKO   Color if not corresponds
     */
    fun filterOn(color : Int, precision : Int, colorOK : Int, colorKO : Int)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val reference = Color(color)

        for (pix in 0 until this.pixels.size)
        {
            if (reference.near(Color(this.pixels[pix]), precision))
            {
                this.pixels[pix] = colorOK
            }
            else
            {
                this.pixels[pix] = colorKO
            }
        }
    }

    /**
     * Convert to itself
     */
    override fun toJHelpImage() = this

    /**
     * Filter image on red channel
     *
     * MUST be on draw mode
     */
    fun filterRed()
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var color : Int
        var red : Int

        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]
            red = color.red
            this.pixels[pix] = color and 0xFFFF0000.toInt() or (red shl 8) or red
        }
    }

    /**
     * Filter image on U part
     *
     * MUST be on draw mode
     */
    fun filterU()
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var color : Int
        var u : Int

        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]
            u = u(color.red, color.green, color.blue).toInt()
                .limit_0_255
            this.pixels[pix] = color and BLACK_ALPHA_MASK or (u shl 16) or (u shl 8) or u
        }
    }

    /**
     * Filter image on V part
     *
     * MUST be on draw mode
     */
    fun filterV()
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var color : Int
        var v : Int

        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]
            v = v(color.red, color.green, color.blue).toInt()
                .limit_0_255
            this.pixels[pix] = color and BLACK_ALPHA_MASK or (v shl 16) or (v shl 8) or v
        }
    }

    /**
     * Filter image on Y part
     *
     * MUST be on draw mode
     */
    fun filterY()
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var color : Int
        var y : Int

        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]
            y = y(color.red, color.green, color.blue).toInt()
                .limit_0_255
            this.pixels[pix] = color and BLACK_ALPHA_MASK or (y shl 16) or (y shl 8) or y
        }
    }

    /**
     * Flip the image horizontally and vertically in same time.
     *
     * Visually its same result as :
     *
     *     image.flipHorizontal();
     *     image.flipVertical();
     *
     * But it's done faster
     *
     * MUST be on draw mode
     */
    fun flipBoth()
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val length = this.pixels.size
        val middlePixel = length shr 1
        var color : Int

        var pixelStart = 0
        var pixelEnd = length - 1

        while (pixelStart < middlePixel)
        {
            color = this.pixels[pixelStart]
            this.pixels[pixelStart] = this.pixels[pixelEnd]
            this.pixels[pixelEnd] = color
            pixelStart ++
            pixelEnd --
        }
    }

    /**
     * Flip the image horizontally
     *
     * MUST be on draw mode
     */
    fun flipHorizontal()
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

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
                color = this.pixels[pixL]
                this.pixels[pixL] = this.pixels[pixR]
                this.pixels[pixR] = color

                pixL ++
                pixR --
            }

            line += this.width
        }
    }

    /**
     * Flip the image vertically
     *
     * MUST be on draw mode
     */
    fun flipVertical()
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val my = this.height shr 1
        var lineU = 0
        var lineB = (this.height - 1) * this.width
        val line = IntArray(this.width)

        for (y in 0 until my)
        {
            System.arraycopy(this.pixels, lineU, line, 0, this.width)
            System.arraycopy(this.pixels, lineB, this.pixels, lineU, this.width)
            System.arraycopy(line, 0, this.pixels, lineB, this.width)

            lineU += this.width
            lineB -= this.width
        }
    }

    /**
     * Current clip
     *
     * @return Current clip
     */
    fun clip() = this.clip.copy()

    /**
     * Extract an array of pixels from the image.
     *
     * The returned array will have some additional free integer at start, the number depends on the given offset.
     *
     * If the image is no in draw mode, sprites will be considered as part of image
     *
     * @param x      X up-left corner
     * @param y      Y up-left corner
     * @param width  Rectangle width
     * @param height Rectangle height
     * @param offset Offset where start copy the pixels, so before integers are "free", so it could be see also as the
     * number of free integers
     * @return Extracted pixels
     */
    fun pixels(x : Int, y : Int, width : Int, height : Int, offset : Int = 0) : IntArray
    {
        var x = x
        var y = y
        var width = width
        var height = height
        if (offset < 0)
        {
            throw IllegalArgumentException("offset must be >=0 not $offset")
        }

        if (x < 0)
        {
            width += x
            x = 0
        }

        if (x + width > this.width)
        {
            width = this.width - x
        }

        if (x > this.width || width < 1)
        {
            return IntArray(0)
        }

        if (y < 0)
        {
            height += y
            y = 0
        }

        if (y + height > this.height)
        {
            height = this.height - y
        }

        if (y > this.height || height < 1)
        {
            return IntArray(0)
        }

        val size = width * height
        val result = IntArray(size + offset)
        var pix = x + y * this.width
        var pixImg = offset

        for (yy in 0 until height)
        {
            System.arraycopy(this.pixels, pix, result, pixImg, width)

            pix += this.width
            pixImg += width
        }

        return result
    }

    /**
     * Convert image in gray version
     *
     * MUST be on draw mode
     */
    fun gray()
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var color : Int
        var y : Int

        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]
            y = y(color.red, color.green, color.blue)
                .toInt()
                .limit_0_255
            this.pixels[pix] = (color and BLACK_ALPHA_MASK) or (y shl 16) or (y shl 8) or y
        }
    }

    /**
     * Convert image in gray invert version
     *
     * MUST be on draw mode
     */
    fun grayInvert()
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var color : Int
        var y : Int
        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]

            y = 255 - y(color.red, color.green, color.blue).toInt()
                .limit_0_255

            this.pixels[pix] = color and BLACK_ALPHA_MASK or (y shl 16) or (y shl 8) or y
        }
    }

    /**
     * Invert image colors
     *
     * MUST be on draw mode
     */
    fun invertColors()
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var color : Int
        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]

            this.pixels[pix] = color and BLACK_ALPHA_MASK or
                    (255 - (color.red) shl 16) or
                    (255 - (color.green) shl 8) or
                    255 - (color.blue)
        }
    }

    /**
     * Invert colors and return image result
     * @return Inverted colors image
     */
    operator fun unaryMinus() : JHelpImage
    {
        val copy = this.copy()
        copy.startDrawMode()
        copy.invertColors()
        copy.endDrawMode()
        return copy
    }

    /**
     * Invert U and V parts

     * MUST be on draw mode
     */
    fun invertUV()
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var color : Int
        var red : Int
        var green : Int
        var blue : Int
        var y : Double
        var u : Double
        var v : Double

        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]
            red = color.red
            green = color.green
            blue = color.blue

            y = y(red, green, blue)
            u = u(red, green, blue)
            v = v(red, green, blue)

            this.pixels[pix] = (color and BLACK_ALPHA_MASK
                    or (red(y, v, u) shl 16)
                    or (green(y, v, u) shl 8)
                    or blue(y, v, u))
        }
    }

    /**
     * Indicates if we are in draw mode
     *
     * @return Draw mode status
     */
    fun drawMode() = this.drawMode

    /**
     * Remove all color part except blue
     *
     * MUST be on draw mode
     */
    fun keepBlue()
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        for (pix in 0 until this.pixels.size)
        {
            this.pixels[pix] = this.pixels[pix] and 0xFF0000FF.toInt()
        }
    }

    /**
     * Remove all color part except green
     *
     * MUST be on draw mode
     */
    fun keepGreen()
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        for (pix in 0 until this.pixels.size)
        {
            this.pixels[pix] = this.pixels[pix] and 0xFF00FF00.toInt()
        }
    }

    /**
     * Remove all color part except red
     *
     * MUST be on draw mode
     */
    fun keepRed()
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        for (pix in 0 until this.pixels.size)
        {
            this.pixels[pix] = this.pixels[pix] and 0xFFFF0000.toInt()
        }
    }

    /**
     * Take the maximum between this image and given one
     *
     * Note : if the given image is not in draw mode, all of it's visible sprite will be consider like a part of the
     * given image
     *
     *
     * Given image MUST have same dimension of this
     *
     * MUST be in draw mode
     *
     * @param image Image reference
     */
    fun maximum(image : JHelpImage)
    {
        if (this.width != image.width || this.height != image.height)
        {
            throw IllegalArgumentException("We can only maximize with an image of same size")
        }

        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var colorThis : Int
        var colorImage : Int

        for (pix in 0 until this.pixels.size)
        {
            colorThis = this.pixels[pix]
            colorImage = image.pixels[pix]

            this.pixels[pix] = colorThis and BLACK_ALPHA_MASK or
                    (Math.max(colorThis.red, colorImage.red) shl 16) or //
                    (Math.max(colorThis.green, colorImage.green) shl 8) or //
                    Math.max(colorThis.blue, colorImage.blue)
        }
    }

    /**
     * Maximum between this and given image
     * @param image to maximum with
     * @return Maximum image
     */
    infix fun MAX(image : JHelpImage) : JHelpImage
    {
        val copy = this.copy()
        copy.startDrawMode()
        copy.maximum(image)
        copy.endDrawMode()
        return copy
    }

    /**
     * Take the middle between this image and given one
     *
     * Note : if the given image is not in draw mode, all of it's visible sprite will be consider like a part of the
     * given image
     *
     *
     * Given image MUST have same dimension of this
     *
     * MUST be in draw mode
     *
     * @param image Image reference
     */
    fun middle(image : JHelpImage)
    {
        if (this.width != image.width || this.height != image.height)
        {
            throw IllegalArgumentException("We can only take middle with an image of same size")
        }

        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var colorThis : Int
        var colorImage : Int

        for (pix in 0 until this.pixels.size)
        {
            colorThis = this.pixels[pix]
            colorImage = image.pixels[pix]

            this.pixels[pix] = colorThis and BLACK_ALPHA_MASK or
                    ((colorThis.red) + (colorImage.red) shr 1 shl 16) or
                    ((colorThis.green) + (colorImage.green) shr 1 shl 8) or
                    ((colorThis.blue) + (colorImage.blue) shr 1)
        }
    }

    /**
     * Middle of this image and given one
     * @param image Image to middle with
     * @return Middle image result
     */
    infix fun MID(image : JHelpImage) : JHelpImage
    {
        val copy = this.copy()
        copy.startDrawMode()
        copy.middle(image)
        copy.endDrawMode()
        return copy
    }

    /**
     * Take the minimum between this image and given one
     *
     * Note : if the given image is not in draw mode, all of it's visible sprite will be consider like a part of the
     * given image
     *
     *
     * Given image MUST have same dimension of this
     *
     * MUST be in draw mode
     *
     * @param image Image reference
     */
    fun minimum(image : JHelpImage)
    {
        if (this.width != image.width || this.height != image.height)
        {
            throw IllegalArgumentException("We can only minimize with an image of same size")
        }

        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var colorThis : Int
        var colorImage : Int

        for (pix in 0 until this.pixels.size)
        {
            colorThis = this.pixels[pix]
            colorImage = image.pixels[pix]

            this.pixels[pix] = colorThis and BLACK_ALPHA_MASK or //
                    (Math.min(colorThis.red, colorImage.red) shl 16) or //
                    (Math.min(colorThis.green, colorImage.green) shl 8) or //
                    Math.min(colorThis.blue, colorImage.blue)
        }
    }

    /**
     * Take minimum of this and given image
     * @param image Image to minimum with
     * @return Image with minimum result
     */
    infix fun MIN(image : JHelpImage) : JHelpImage
    {
        val copy = this.copy()
        copy.startDrawMode()
        copy.minimum(image)
        copy.endDrawMode()
        return copy
    }

    /**
     * Multiply the image with an other one
     *
     * Note : if the given image is not in draw mode, all of it's visible sprite will be consider like a part of the
     * given image
     *
     *
     * Given image MUST have same dimension of this
     *
     * MUST be in draw mode
     *
     * @param image Image to multiply
     */
    fun multiply(image : JHelpImage)
    {
        if (this.width != image.width || this.height != image.height)
        {
            throw IllegalArgumentException("We can only multiply with an image of same size")
        }

        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var colorThis : Int
        var colorImage : Int

        for (pix in 0 until this.pixels.size)
        {
            colorThis = this.pixels[pix]
            colorImage = image.pixels[pix]

            this.pixels[pix] = colorThis and BLACK_ALPHA_MASK or
                    ((colorThis.red) * (colorImage.red) / 255 shl 16) or
                    ((colorThis.green) * (colorImage.green) / 255 shl 8) or
                    (colorThis.blue) * (colorImage.blue) / 255
        }
    }

    /**
     * Multiply with given image
     * @param image Image to multiply
     */
    operator fun timesAssign(image : JHelpImage) = this.multiply(image)

    /**
     * Multiply with an other image and return the result
     * @param image Image to multiply
     * @return Multiplication result
     */
    operator fun times(image : JHelpImage) : JHelpImage
    {
        val copy = this.copy()
        copy.startDrawMode()
        copy *= image
        copy.endDrawMode()
        return copy
    }

    /**
     * Paint the image on using an other as alpha mask
     *
     * Alpha mask image can be imagine like a paper with holes that we put on the main image, we paint, and remove the
     * image,
     * only holes are paint on final image.
     *
     * Holes are here pixels with alpha more than 0x80
     *
     * @param x          Where put the left corner X of alpha mask
     * @param y          Where put the left corner Y of alpha mask
     * @param alphaMask  Alpha mask to use
     * @param color      Color to fill holes
     * @param background Color for fill background
     * @param doAlphaMix Indicates if do alpha mixing (`true`) or just overwrite (`false`)
     */
    fun paintAlphaMask(x : Int, y : Int, alphaMask : JHelpImage, color : Int, background : Int,
                       doAlphaMix : Boolean = true)
    {
        this.fillRectangle(x, y, alphaMask.width, alphaMask.height, background, doAlphaMix)
        this.paintAlphaMask(x, y, alphaMask, color)
    }

    /**
     * Paint the image on using an other as alpha mask
     *
     * Alpha mask image can be imagine like a paper with holes that we put on the main image, we paint, and remove the
     * image,
     * only holes are paint on final image.
     *
     * Holes are here pixels with alpha more than 0x80
     *
     * @param x         Where put the left corner X of alpha mask
     * @param y         Where put the left corner Y of alpha mask
     * @param alphaMask Alpha mask to use
     * @param color     Color to fill holes
     */
    fun paintAlphaMask(x : Int, y : Int, alphaMask : JHelpImage, color : Int)
    {
        var x = x
        var y = y
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var w : Int = this.clip.xMax + 1
        var xx = 0
        if (x < this.clip.xMin)
        {
            xx = - x + this.clip.xMin
            w += x - this.clip.xMin
            x = this.clip.xMin
        }

        var h : Int = this.clip.yMax + 1
        var yy = 0
        if (y < this.clip.yMin)
        {
            yy = - y + this.clip.yMin
            h += y - this.clip.yMin
            y = this.clip.yMin
        }

        val widthAlpha = alphaMask.width
        val width = minOf(w - x, widthAlpha, this.width - x)
        val height = minOf(h - y, alphaMask.height, this.height - y)

        if (width < 1 || height < 1)
        {
            return
        }

        var line = x + y * this.width
        var lineAlpha = xx + yy * widthAlpha
        var pix : Int
        var pixAlpha : Int
        var alphaAlpha : Int

        for (yyy in yy until height)
        {
            pix = line
            pixAlpha = lineAlpha

            for (xxx in xx until width)
            {
                alphaAlpha = alphaMask.pixels[pixAlpha].alpha

                if (alphaAlpha > 0x80)
                {
                    this.pixels[pix] = color
                }

                pix ++
                pixAlpha ++
            }

            line += this.width
            lineAlpha += widthAlpha
        }
    }

    /**
     * Paint the image on using an other as alpha mask
     *
     * Alpha mask image can be imagine like a paper with holes that we put on the main image, we paint, and remove the
     * image,
     * only holes are paint on final image.
     *
     * Holes are here pixels with alpha more than 0x80
     *
     * @param x         Where put the left corner X of alpha mask
     * @param y         Where put the left corner Y of alpha mask
     * @param alphaMask Alpha mask to use
     * @param texture   Texture to fill holes
     */
    fun paintAlphaMask(x : Int, y : Int, alphaMask : JHelpImage, texture : JHelpImage)
    {
        var x = x
        var y = y
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var w : Int = this.clip.xMax + 1
        var xx = 0
        if (x < this.clip.xMin)
        {
            xx = - x + this.clip.xMin
            w += x - this.clip.xMin
            x = this.clip.xMin
        }

        var h : Int = this.clip.yMax + 1
        var yy = 0
        if (y < this.clip.yMin)
        {
            yy = - y + this.clip.yMin
            h += y - this.clip.yMin
            y = this.clip.yMin
        }

        val widthAlpha = alphaMask.width
        val heightAlpha = alphaMask.height
        val width = minOf(w - x, widthAlpha, this.width - x)
        val height = minOf(h - y, heightAlpha, this.height - y)

        if (width < 1 || height < 1)
        {
            return
        }

        val widthTexture = texture.width
        val heightTexture = texture.height
        val xStart = xx % widthTexture
        var line = x + y * this.width
        var lineAlpha : Int = xx + yy * widthAlpha
        var pix : Int
        var pixAlpha : Int
        var alphaAlpha : Int
        var lineTexture : Int

        for (yyy in yy until height)
        {
            lineTexture = xStart + yyy % heightTexture * widthTexture
            pix = line
            pixAlpha = lineAlpha

            for (xxx in xx until width)
            {
                alphaAlpha = alphaMask.pixels[pixAlpha].alpha

                if (alphaAlpha > 0x80)
                {
                    this.pixels[pix] = texture.pixels[lineTexture + xxx % widthTexture]
                }

                pix ++
                pixAlpha ++
            }

            line += this.width
            lineAlpha += widthAlpha
        }
    }

    /**
     * Paint the image on using an other as alpha mask
     *
     * Alpha mask image can be imagine like a paper with holes that we put on the main image, we paint, and remove the
     * image,
     * only holes are paint on final image.
     *
     * Holes are here pixels with alpha more than 0x80
     *
     * @param x         Where put the left corner X of alpha mask
     * @param y         Where put the left corner Y of alpha mask
     * @param alphaMask Alpha mask to use
     * @param paint     Paint to fill holes
     */
    fun paintAlphaMask(x : Int, y : Int, alphaMask : JHelpImage, paint : JHelpPaint)
    {
        var x = x
        var y = y
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var w : Int = this.clip.xMax + 1
        var xx = 0
        if (x < this.clip.xMin)
        {
            xx = - x + this.clip.xMin
            w += x - this.clip.xMin
            x = this.clip.xMin
        }

        var h : Int = this.clip.yMax + 1
        var yy = 0
        if (y < this.clip.yMin)
        {
            yy = - y + this.clip.yMin
            h += y - this.clip.yMin
            y = this.clip.yMin
        }

        val widthAlpha = alphaMask.width
        val heightAlpha = alphaMask.height
        val width = minOf(w - x, widthAlpha, this.width - x)
        val height = minOf(h - y, heightAlpha, this.height - y)

        if (width < 1 || height < 1)
        {
            return
        }

        paint.initializePaint(widthAlpha, heightAlpha)
        var line = x + y * this.width
        var lineAlpha : Int = xx + yy * widthAlpha
        var pix : Int
        var pixAlpha : Int
        var alphaAlpha : Int

        for (yyy in yy until height)
        {
            pix = line
            pixAlpha = lineAlpha

            for (xxx in xx until width)
            {
                alphaAlpha = alphaMask.pixels[pixAlpha].alpha

                if (alphaAlpha > 0x80)
                {
                    this.pixels[pix] = paint.obtainColor(xxx, yyy)
                }

                pix ++
                pixAlpha ++
            }

            line += this.width
            lineAlpha += widthAlpha
        }
    }

    /**
     * Draw the image like an [Icon]

     * @param component Reference component
     * @param graphics  Graphics where paint
     * @param x         X position
     * @param y         Y position
     * @see Icon.paintIcon
     */
    override fun paintIcon(component : Component, graphics : Graphics, x : Int, y : Int)
    {
        this.update()
        graphics.drawImage(this.image, x, y, null)
    }

    /**
     * Image width
     * @return Image width
     * @see Icon.getIconWidth
     */
    override fun getIconWidth() = this.width

    /**
     * Image height
     * @return Image height
     * @see Icon.getIconHeight
     */
    override fun getIconHeight() = this.height

    /**
     * Paint a mask
     *
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param mask       Mask to paint
     * @param foreground Foreground color
     * @param background Background color
     * @param doAlphaMix Indicates if do alpha mixing (`true`) or just overwrite (`false`)
     */
    fun paintMask(x : Int, y : Int, mask : JHelpMask, foreground : Int, background : Int,
                  doAlphaMix : Boolean = true)
    {
        var x = x
        var y = y
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var w : Int = this.clip.xMax + 1
        var xx = 0
        if (x < this.clip.xMin)
        {
            xx = - x + this.clip.xMin
            w += x - this.clip.xMin
            x = this.clip.xMin
        }

        var h : Int = this.clip.yMax + 1
        var yy = 0
        if (y < this.clip.yMin)
        {
            yy = - y + this.clip.yMin
            h += y - this.clip.yMin
            y = this.clip.yMin
        }

        val width = minOf(w - x, mask.width, this.width - x)
        val height = minOf(h - y, mask.height, this.height - y)

        if (width < 1 || height < 1)
        {
            return
        }

        var line = x + y * this.width
        var pix : Int
        var color : Int
        var alpha : Int
        var red : Int
        var blue : Int
        var green : Int

        val alphaFore = foreground.alpha
        val redFore = foreground.red * alphaFore
        val greenFore = foreground.green * alphaFore
        val blueFore = foreground.blue * alphaFore

        val alphaBack = background.alpha
        val redBack = background.red * alphaBack
        val greenBack = background.green * alphaBack
        val blueBack = background.blue * alphaBack

        for (yyy in yy until height)
        {
            pix = line

            for (xxx in xx until width)
            {
                if (mask[xxx, yyy])
                {
                    color = foreground
                    alpha = alphaFore
                    red = redFore
                    green = greenFore
                    blue = blueFore
                }
                else
                {
                    color = background
                    alpha = alphaBack
                    red = redBack
                    green = greenBack
                    blue = blueBack
                }

                if (alpha == 255 || ! doAlphaMix)
                {
                    this.pixels[pix] = color
                }
                else if (alpha > 0)
                {
                    this.mixColor(pix, alpha, red, green, blue)
                }

                pix ++
            }

            line += this.width
        }
    }

    /**
     * Paint a mask with unify foreground color and part of image in background
     *
     * Note : if the background is not in draw mode, all of it's visible sprite will be consider like a part of the
     * background
     *
     *
     * MUST be in draw mode
     *
     * @param x           X
     * @param y           Y
     * @param mask        Mask to paint
     * @param foreground  Foreground color
     * @param background  Background image
     * @param backgroundX X start in background image
     * @param backgroundY Y start in background image
     * @param doAlphaMix  Indicates if do alpha mixing (`true`) or just overwrite (`false`)
     */
    fun paintMask(x : Int, y : Int, mask : JHelpMask, foreground : Int,
                  background : JHelpImage, backgroundX : Int, backgroundY : Int,
                  doAlphaMix : Boolean = true)
    {
        var x = x
        var y = y
        var backgroundX = backgroundX
        var backgroundY = backgroundY
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var bw = background.width
        if (backgroundX < 0)
        {
            bw += backgroundX
            backgroundX = 0
        }

        var bh = background.height
        if (backgroundY < 0)
        {
            bh += backgroundY
            backgroundY = 0
        }

        var w : Int = this.clip.xMax + 1
        var xx = 0
        if (x < this.clip.xMin)
        {
            xx = - x + this.clip.xMin
            w += x - this.clip.xMin
            x = this.clip.xMin
        }

        var h : Int = this.clip.yMax + 1
        var yy = 0
        if (y < this.clip.yMin)
        {
            yy = - y + this.clip.yMin
            h += y - this.clip.yMin
            y = this.clip.yMin
        }

        val width = minOf(w - x, mask.width, bw - backgroundX)
        val height = minOf(h - y, mask.height, bh - backgroundY)

        if (width < 1 || height < 1)
        {
            return
        }

        var lineBack = backgroundX + backgroundY * background.width
        var line = x + y * this.width
        var pixBack : Int
        var pix : Int
        var color : Int
        var alpha : Int

        for (yyy in yy until height)
        {
            pixBack = lineBack
            pix = line

            for (xxx in xx until width)
            {
                color = if (mask[xxx, yyy])
                    foreground
                else
                    background.pixels[pixBack]
                alpha = color.alpha

                if (alpha == 255 || ! doAlphaMix)
                {
                    this.pixels[pix] = color
                }
                else if (alpha > 0)
                {
                    this.mixColor(pix, alpha, color)
                }

                pixBack ++
                pix ++
            }

            lineBack += background.width
            line += this.width
        }
    }

    /**
     * Paint a mask with unify color as foreground and paint as background
     *
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param mask       Mask to paint
     * @param foreground Foreground color
     * @param background Background paint
     * @param doAlphaMix Indicates if do alpha mixing (`true`) or just overwrite (`false`)
     */
    fun paintMask(x : Int, y : Int, mask : JHelpMask, foreground : Int, background : JHelpPaint,
                  doAlphaMix : Boolean = true)
    {
        var x = x
        var y = y
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var w : Int = this.clip.xMax + 1
        var xx = 0
        if (x < this.clip.xMin)
        {
            xx = - x + this.clip.xMin
            w += x - this.clip.xMin
            x = this.clip.xMin
        }

        var h : Int = this.clip.yMax + 1
        var yy = 0
        if (y < this.clip.yMin)
        {
            yy = - y + this.clip.yMin
            h += y - this.clip.yMin
            y = this.clip.yMin
        }

        val width = minOf(w - x, mask.width)
        val height = minOf(h - y, mask.height)

        if (width < 1 || height < 1)
        {
            return
        }

        background.initializePaint(width, height)

        var line = x + y * this.width
        var pix : Int
        var color : Int
        var alpha : Int

        for (yyy in yy until height)
        {
            pix = line

            for (xxx in xx until width)
            {
                color = if (mask[xxx, yyy])
                    foreground
                else
                    background.obtainColor(xxx, yyy)
                alpha = color.alpha

                if (alpha == 255 || ! doAlphaMix)
                {
                    this.pixels[pix] = color
                }
                else if (alpha > 0)
                {
                    this.mixColor(pix, alpha, color)
                }

                pix ++
            }

            line += this.width
        }
    }

    /**
     * Paint a mask with part of image as foreground and unify color as background
     *
     * Note : if the foreground is not in draw mode, all of it's visible sprite will be consider like a part of the
     * foreground
     *
     *
     * MUST be in draw mode
     *
     * @param x           X
     * @param y           Y
     * @param mask        Mask to paint
     * @param foreground  Foreground image
     * @param foregroundX X start on foreground image
     * @param foregroundY Y start on foreground image
     * @param background  Background color
     * @param doAlphaMix  Indicates if do alpha mixing (`true`) or just overwrite (`false`)
     */
    fun paintMask(x : Int, y : Int, mask : JHelpMask,
                  foreground : JHelpImage, foregroundX : Int, foregroundY : Int,
                  background : Int, doAlphaMix : Boolean = true)
    {
        var x = x
        var y = y
        var foregroundX = foregroundX
        var foregroundY = foregroundY
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var fw = foreground.width
        if (foregroundX < 0)
        {
            fw += foregroundX
            foregroundX = 0
        }

        var fh = foreground.height
        if (foregroundY < 0)
        {
            fh += foregroundY
            foregroundY = 0
        }

        var w : Int = this.clip.xMax + 1
        var xx = 0
        if (x < this.clip.xMin)
        {
            xx = - x + this.clip.xMin
            w += x - this.clip.xMin
            x = this.clip.xMin
        }

        var h : Int = this.clip.yMax + 1
        var yy = 0
        if (y < this.clip.yMin)
        {
            yy = - y + this.clip.yMin
            h += y - this.clip.yMin
            y = this.clip.yMin
        }

        val width = minOf(w - x, mask.width, fw - foregroundX)
        val height = minOf(h - y, mask.height, fh - foregroundY)

        if (width < 1 || height < 1)
        {
            return
        }

        var lineFore = foregroundX + foregroundY * foreground.width
        var line = x + y * this.width
        var pixFore : Int
        var pix : Int
        var color : Int
        var alpha : Int

        for (yyy in yy until height)
        {
            pixFore = lineFore
            pix = line

            for (xxx in xx until width)
            {
                color = if (mask[xxx, yyy])
                    foreground.pixels[pixFore]
                else
                    background
                alpha = color.alpha

                if (alpha == 255 || ! doAlphaMix)
                {
                    this.pixels[pix] = color
                }
                else if (alpha > 0)
                {
                    this.mixColor(pix, alpha, color)
                }

                pixFore ++
                pix ++
            }

            lineFore += foreground.width
            line += this.width
        }
    }

    /**
     * Paint a mask with 2 images, one for "foreground" pixels, one for "background" ones
     *
     * Note : if the foreground or background is not in draw mode, all of it's visible sprite will be consider like a
     * part of the
     * foreground or background
     *
     * MUST be in draw mode
     *
     * @param x           X position for the mask
     * @param y           Y position for the mask
     * @param mask        Mask to paint
     * @param foreground  Foreground image
     * @param foregroundX X start position in foreground image
     * @param foregroundY Y start position in foreground image
     * @param background  Background image
     * @param backgroundX X start position in background image
     * @param backgroundY Y start position in background image
     * @param doAlphaMix  Indicates if do alpha mixing (`true`) or just overwrite (`false`)
     */
    fun paintMask(x : Int, y : Int, mask : JHelpMask,
                  foreground : JHelpImage, foregroundX : Int, foregroundY : Int,
                  background : JHelpImage, backgroundX : Int, backgroundY : Int,
                  doAlphaMix : Boolean = true)
    {
        var x = x
        var y = y
        var foregroundX = foregroundX
        var foregroundY = foregroundY
        var backgroundX = backgroundX
        var backgroundY = backgroundY
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var fw = foreground.width
        if (foregroundX < 0)
        {
            fw += foregroundX
            foregroundX = 0
        }

        var fh = foreground.height
        if (foregroundY < 0)
        {
            fh += foregroundY
            foregroundY = 0
        }

        var bw = background.width
        if (backgroundX < 0)
        {
            bw += backgroundX
            backgroundX = 0
        }

        var bh = background.height
        if (backgroundY < 0)
        {
            bh += backgroundY
            backgroundY = 0
        }

        var w : Int = this.clip.xMax + 1
        var xx = 0
        if (x < this.clip.xMin)
        {
            xx = - x + this.clip.xMin
            w += x - this.clip.xMin
            x = this.clip.xMin
        }

        var h : Int = this.clip.yMax + 1
        var yy = 0
        if (y < this.clip.yMin)
        {
            yy = - y + this.clip.yMin
            h += y - this.clip.yMin
            y = this.clip.yMin
        }

        val width = minOf(w - x, mask.width, fw - foregroundX, bw - backgroundX)
        val height = minOf(h - y, mask.height, fh - foregroundY, bh - backgroundY)

        if (width < 1 || height < 1)
        {
            return
        }

        var lineFore = foregroundX + foregroundY * foreground.width
        var lineBack = backgroundX + backgroundY * background.width
        var line = x + y * this.width
        var pixFore : Int
        var pixBack : Int
        var pix : Int
        var color : Int
        var alpha : Int

        for (yyy in yy until height)
        {
            pixFore = lineFore
            pixBack = lineBack
            pix = line

            for (xxx in xx until width)
            {
                color = if (mask[xxx, yyy])
                    foreground.pixels[pixFore]
                else
                    background.pixels[pixBack]
                alpha = color.alpha

                if (alpha == 255 || ! doAlphaMix)
                {
                    this.pixels[pix] = color
                }
                else if (alpha > 0)
                {
                    this.mixColor(pix, alpha, color)
                }

                pixFore ++
                pixBack ++
                pix ++
            }

            lineFore += foreground.width
            lineBack += background.width
            line += this.width
        }
    }

    /**
     * Paint a mask with image in foreground and paint in background
     *
     * Note : if the foreground is not in draw mode, all of it's visible sprite will be consider like a part of the
     * foreground
     *
     *
     * MUST be in draw mode
     *
     * @param x           X where paint the mask
     * @param y           Y where paint the mask
     * @param mask        Mask to paint
     * @param foreground  Image in foreground
     * @param foregroundX X start on foreground image
     * @param foregroundY Y start on foreground image
     * @param background  Background paint
     * @param doAlphaMix  Indicates if do alpha mixing (`true`) or just overwrite (`false`)
     */
    fun paintMask(x : Int, y : Int, mask : JHelpMask,
                  foreground : JHelpImage, foregroundX : Int, foregroundY : Int,
                  background : JHelpPaint, doAlphaMix : Boolean = true)
    {
        var x = x
        var y = y
        var foregroundX = foregroundX
        var foregroundY = foregroundY
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var fw = foreground.width
        if (foregroundX < 0)
        {
            fw += foregroundX
            foregroundX = 0
        }

        var fh = foreground.height
        if (foregroundY < 0)
        {
            fh += foregroundY
            foregroundY = 0
        }

        var w : Int = this.clip.xMax + 1
        var xx = 0
        if (x < this.clip.xMin)
        {
            xx = - x + this.clip.xMin
            w += x - this.clip.xMin
            x = this.clip.xMin
        }

        var h : Int = this.clip.yMax + 1
        var yy = 0
        if (y < this.clip.yMin)
        {
            yy = - y + this.clip.yMin
            h += y - this.clip.yMin
            y = this.clip.yMin
        }

        val width = minOf(w - x, mask.width, fw - foregroundX)
        val height = minOf(h - y, mask.height, fh - foregroundY)

        if (width < 1 || height < 1)
        {
            return
        }

        var lineFore = foregroundX + foregroundY * foreground.width
        background.initializePaint(width, height)
        var line = x + y * this.width
        var pixFore : Int
        var pix : Int
        var color : Int
        var alpha : Int

        for (yyy in yy until height)
        {
            pixFore = lineFore
            pix = line

            for (xxx in xx until width)
            {
                color = if (mask[xxx, yyy])
                    foreground.pixels[pixFore]
                else
                    background.obtainColor(xxx, yyy)
                alpha = color.alpha

                if (alpha == 255 || ! doAlphaMix)
                {
                    this.pixels[pix] = color
                }
                else if (alpha > 0)
                {
                    this.mixColor(pix, alpha, color)
                }

                pixFore ++
                pix ++
            }

            lineFore += foreground.width
            line += this.width
        }
    }

    /**
     * Paint mask with paint in foreground and color in background
     *
     * MUST be in draw mode
     *
     * @param x          X where paint the mask
     * @param y          Y where paint the mask
     * @param mask       Mask to paint
     * @param foreground Foreground paint
     * @param background Background color
     * @param doAlphaMix Indicates if do alpha mixing (`true`) or just overwrite (`false`)
     */
    fun paintMask(x : Int, y : Int, mask : JHelpMask, foreground : JHelpPaint, background : Int,
                  doAlphaMix : Boolean = true)
    {
        var x = x
        var y = y
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var w : Int = this.clip.xMax + 1
        var xx = 0
        if (x < this.clip.xMin)
        {
            xx = - x + this.clip.xMin
            w += x - this.clip.xMin
            x = this.clip.xMin
        }

        var h : Int = this.clip.yMax + 1
        var yy = 0
        if (y < this.clip.yMin)
        {
            yy = - y + this.clip.yMin
            h += y - this.clip.yMin
            y = this.clip.yMin
        }

        val width = minOf(w - x, mask.width)
        val height = minOf(h - y, mask.height)

        if (width < 1 || height < 1)
        {
            return
        }

        foreground.initializePaint(width, height)
        var line = x + y * this.width
        var pix : Int
        var color : Int
        var alpha : Int

        for (yyy in yy until height)
        {
            pix = line

            for (xxx in xx until width)
            {
                color = if (mask[xxx, yyy])
                    foreground.obtainColor(xxx, yyy)
                else
                    background
                alpha = color.alpha

                if (alpha == 255 || ! doAlphaMix)
                {
                    this.pixels[pix] = color
                }
                else if (alpha > 0)
                {
                    this.mixColor(pix, alpha, color)
                }

                pix ++
            }

            line += this.width
        }
    }

    /**
     * Paint a mask with paint in foreground and image in background
     *
     * Note : if the background is not in draw mode, all of it's visible sprite will be consider like a part of the
     * background
     *
     *
     * MUST be in draw mode
     *
     * @param x           X position for mask
     * @param y           Y position for mask
     * @param mask        Mask to paint
     * @param foreground  Foreground paint
     * @param background  Background image
     * @param backgroundX X start in background image
     * @param backgroundY Y start in background image
     * @param doAlphaMix  Indicates if do alpha mixing (`true`) or just overwrite (`false`)
     */
    fun paintMask(x : Int, y : Int, mask : JHelpMask, foreground : JHelpPaint,
                  background : JHelpImage, backgroundX : Int, backgroundY : Int,
                  doAlphaMix : Boolean = true)
    {
        var x = x
        var y = y
        var backgroundX = backgroundX
        var backgroundY = backgroundY
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var bw = background.width
        if (backgroundX < 0)
        {
            bw += backgroundX
            backgroundX = 0
        }

        var bh = background.height
        if (backgroundY < 0)
        {
            bh += backgroundY
            backgroundY = 0
        }

        var w : Int = this.clip.xMax + 1
        var xx = 0
        if (x < this.clip.xMin)
        {
            xx = - x + this.clip.xMin
            w += x - this.clip.xMin
            x = this.clip.xMin
        }

        var h : Int = this.clip.yMax + 1
        var yy = 0
        if (y < this.clip.yMin)
        {
            yy = - y + this.clip.yMin
            h += y - this.clip.yMin
            y = this.clip.yMin
        }

        val width = minOf(w - x, mask.width, bw - backgroundX)
        val height = minOf(h - y, mask.height, bh - backgroundY)

        if (width < 1 || height < 1)
        {
            return
        }

        foreground.initializePaint(width, height)
        var lineBack = backgroundX + backgroundY * background.width
        var line = x + y * this.width
        var pixBack : Int
        var pix : Int
        var color : Int
        var alpha : Int

        for (yyy in yy until height)
        {
            pixBack = lineBack
            pix = line

            for (xxx in xx until width)
            {
                color = if (mask[xxx, yyy])
                    foreground.obtainColor(xxx, yyy)
                else
                    background.pixels[pixBack]
                alpha = color.alpha

                if (alpha == 255 || ! doAlphaMix)
                {
                    this.pixels[pix] = color
                }
                else if (alpha > 0)
                {
                    this.mixColor(pix, alpha, color)
                }

                pixBack ++
                pix ++
            }

            lineBack += background.width
            line += this.width
        }
    }

    /**
     * Paint mask with paint in foreground and background
     *
     * MUST be in draw mode
     *
     * @param x          X position for mask
     * @param y          Y position for mask
     * @param mask       Mask to paint
     * @param foreground Foreground paint
     * @param background Background paint
     * @param doAlphaMix Indicates if do alpha mixing (`true`) or just overwrite (`false`)
     */
    fun paintMask(x : Int, y : Int, mask : JHelpMask, foreground : JHelpPaint, background : JHelpPaint,
                  doAlphaMix : Boolean = true)
    {
        var x = x
        var y = y
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var w : Int = this.clip.xMax + 1
        var xx = 0
        if (x < this.clip.xMin)
        {
            xx = - x + this.clip.xMin
            w += x - this.clip.xMin
            x = this.clip.xMin
        }

        var h : Int = this.clip.yMax + 1
        var yy = 0
        if (y < this.clip.yMin)
        {
            yy = - y + this.clip.yMin
            h += y - this.clip.yMin
            y = this.clip.yMin
        }

        val width = minOf(w - x, mask.width)
        val height = minOf(h - y, mask.height)

        if (width < 1 || height < 1)
        {
            return
        }

        foreground.initializePaint(width, height)
        background.initializePaint(width, height)
        var line = x + y * this.width
        var pix : Int
        var color : Int
        var alpha : Int

        for (yyy in yy until height)
        {
            pix = line

            for (xxx in xx until width)
            {
                color = if (mask[xxx, yyy])
                    foreground.obtainColor(xxx, yyy)
                else
                    background.obtainColor(xxx, yyy)
                alpha = color.alpha

                if (alpha == 255 || ! doAlphaMix)
                {
                    this.pixels[pix] = color
                }
                else if (alpha > 0)
                {
                    this.mixColor(pix, alpha, color)
                }

                pix ++
            }

            line += this.width
        }
    }

    /**
     * Pick a color inside the image
     *
     * Note : if the image is not in draw mode, all visible sprite are consider as a part of image, so may obtain a
     * sprite pixel
     *
     * @param x X position
     * @param y Y position
     * @return Picked color
     */
    fun pickColor(x : Int, y : Int) : Int
    {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height)
        {
            throw IllegalArgumentException(
                "Coordinates of peek point must be in [0, ${this.width}[ x [0, ${this.height}[ not ($x, $y")
        }

        return this.pixels[x + (y * this.width)]
    }

    /**
     * Do a task in draw mode.
     *
     * Don't call this method if image is locked. Use [drawModeLocked] to know.
     *
     * The image is locked if we are inside a task launch by [playInDrawMode] or [playOutDrawMode]
     *
     * @param task Task to do. The parameter will be this image locked in draw mode
     * @throws IllegalStateException If draw mode is locked
     */
    fun playInDrawMode(task : (JHelpImage) -> Unit)
    {
        if (this.drawModeLocked)
        {
            throw IllegalStateException("Draw mode is locked")
        }

        val drawMode = this.drawMode

        if (! drawMode)
        {
            this.startDrawMode()
        }

        this.drawModeLocked = true
        task(this)
        this.drawModeLocked = false

        if (! drawMode)
        {
            this.endDrawMode()
        }
    }

    /**
     * Do a task not in draw mode.
     *
     * Don't call this method if image is locked. Use [drawModeLocked] to know.
     *
     * The image is locked if we are inside a task launch by [playInDrawMode] or [playOutDrawMode]
     *
     * @param task Task to do. The parameter will be this image locked in not draw mode
     * @throws IllegalStateException If draw mode is locked
     */
    fun playOutDrawMode(task : (JHelpImage) -> Unit)
    {
        if (this.drawModeLocked)
        {
            throw IllegalStateException("Draw mode is locked")
        }

        val drawMode = this.drawMode

        if (drawMode)
        {
            this.endDrawMode()
        }

        this.drawModeLocked = true
        task(this)
        this.drawModeLocked = false

        if (drawMode)
        {
            this.startDrawMode()
        }
    }

    /**
     * Play a task when image enter in draw mode.
     *
     * If image already in draw mode, the task is played immediately.
     *
     * If image not in draw mode, task will be played next time someone calls [startDrawMode]
     *
     * @param task Task to play in draw mode
     */
    fun playWhenEnterDrawMode(task : (JHelpImage) -> Unit)
    {
        if (this.drawMode)
        {
            task(this)
        }
        else
        {
            synchronized(this.playInDrawMode)
            {
                this.playInDrawMode.inQueue(task)
            }
        }
    }

    /**
     * Play task when image exit from draw mode.
     *
     * If image already not in draw mode, the task is played immediately.
     *
     * If image in draw mode, task will be played next time someone calls [endDrawMode]
     *
     * @param task Task to play in draw mode
     */
    fun playWhenExitDrawMode(task : (JHelpImage) -> Unit)
    {
        if (this.drawMode)
        {
            synchronized(this.playOutDrawMode)
            {
                this.playOutDrawMode.inQueue(task)
            }
        }
        else
        {
            task(this)
        }
    }

    /**
     * Pop clip from the stack
     */
    fun popClip()
    {
        if (this.clips.size > 1)
        {
            this.clip.set(this.clips.pop())
        }
        else
        {
            this.clip.set(this.clips.peek())
        }
    }

    /**
     * Push clip to stack
     *
     * @param x      X up-left corner
     * @param y      Y up-left corner
     * @param width  Clip width
     * @param height Clip height
     */
    fun pushClip(x : Int, y : Int, width : Int, height : Int) =
        this.pushClip(Clip(Math.max(x, 0), Math.min(x + width - 1, this.width - 1),
                           Math.max(y, 0), Math.min(y + height - 1, this.height - 1)))

    /**
     * Push clip in the stack
     *
     * @param clip Clip to push
     */
    fun pushClip(clip : Clip)
    {
        this.clips.push(this.clip.copy())
        this.clip.set(Clip(Math.max(clip.xMin, 0), Math.min(clip.xMax, this.width - 1), Math.max(clip.yMin, 0),
                           Math.min(clip.yMax, this.height - 1)))
    }

    /**
     * Push intersection of current clip and given one
     *
     * @param x      X up-left corner
     * @param y      Y up-left corner
     * @param width  Clip width
     * @param height Clip height
     */
    fun pushClipIntersect(x : Int, y : Int, width : Int, height : Int) =
        this.pushClipIntersect(Clip(x, x + width - 1, y, y + height - 1))

    /**
     * Push intersection of current clip and given one
     *
     * @param clip Given clip
     */
    fun pushClipIntersect(clip : Clip)
    {
        val intersect = Clip(Math.max(this.clip.xMin, clip.xMin),
                             Math.min(this.clip.xMax, clip.xMax),
                             Math.max(this.clip.yMin, clip.yMin),
                             Math.min(this.clip.yMax, clip.yMax))
        this.clips.push(this.clip.copy())
        this.clip.set(intersect)
    }

    /**
     * Register a component to update on image change
     *
     * @param component Component to register
     */
    fun register(component : Component)
    {
        this.mutex {
            if (! this.componentsListeners.contains(component))
            {
                this.componentsListeners.add(component)
            }
        }
    }

    /**
     * Remove a sprite from linked sprites.

     * The sprite is no more usable

     * MUST NOT be in draw mode
     *
     * @param sprite Sprite to remove
     */
    fun removeSprite(sprite : JHelpSprite)
    {
        if (this.drawMode)
        {
            throw IllegalStateException("MUST NOT be in draw mode !")
        }

        sprite.visible(false)

        if (this.sprites.remove(sprite))
        {
            this.mutexVisibilities {
                val index = sprite.spriteIndex()
                if (index >= 0 && this.visibilities != null && this.visibilities !!.size > index)
                {
                    System.arraycopy(this.visibilities, index + 1, this.visibilities, index,
                                     this.visibilities !!.size - index - 1)
                }

                for (indexSprite in 0 until this.sprites.size)
                {
                    this.sprites[indexSprite].spriteIndex(indexSprite)
                }
            }

            this.update()
        }
    }

    /**
     * Repeat an image on following a line
     *
     * @param x1         First point X
     * @param y1         First point Y
     * @param x2         Second point X
     * @param y2         Second point Y
     * @param image      Image to repeat
     * @param doAlphaMix Indicates if we want do alpha mixing
     */
    fun repeatOnLine(x1 : Int, y1 : Int, x2 : Int, y2 : Int, image : JHelpImage, doAlphaMix : Boolean = true)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (x1 == x2)
        {
            this.repeatOnLineVertical(x1, y1, y2, image, doAlphaMix)
            return
        }

        if (y1 == y2)
        {
            this.repeatOnLineHorizontal(x1, x2, y1, image, doAlphaMix)
            return
        }

        var error = 0
        val dx = abs(x2 - x1)
        val sx = sign(x2 - x1)
        val dy = abs(y2 - y1)
        val sy = sign(y2 - y1)
        var x = x1
        var y = y1
        val xx = - (image.width shr 1)
        val yy = - (image.height shr 1)

        if (dx >= dy)
        {
            while ((x < this.clip.xMin || x > this.clip.xMax || y < this.clip.yMin || y > this.clip.yMax) && (x != x2 || y != y2))
            {
                x += sx

                error += dy
                if (error >= dx)
                {
                    y += sy

                    error -= dx
                }
            }

            while (x >= this.clip.xMin && x <= this.clip.xMax && x != x2
                   && y >= this.clip.yMin && y <= this.clip.yMax && y != y2)
            {
                this.drawImage(xx + x, yy + y, image, doAlphaMix = doAlphaMix)

                x += sx

                error += dy
                if (error >= dx)
                {
                    y += sy

                    error -= dx
                }
            }
        }
        else
        {
            while ((x < this.clip.xMin || x > this.clip.xMax || y < this.clip.yMin || y > this.clip.yMax) && (x != x2 || y != y2))
            {
                y += sy

                error += dx
                if (error >= dy)
                {
                    x += sx

                    error -= dy
                }
            }

            while (x >= this.clip.xMin && x <= this.clip.xMax && x != x2
                   && y >= this.clip.yMin && y <= this.clip.yMax && y != y2)
            {
                this.drawImage(xx + x, yy + y, image, doAlphaMix = doAlphaMix)

                y += sy

                error += dx
                if (error >= dy)
                {
                    x += sx

                    error -= dy
                }
            }
        }
    }

    /**
     * Repeat an image on following a horizontal line
     *
     * @param x1         First point X
     * @param x2         Second point X
     * @param y          of line
     * @param image      Image to repeat
     * @param doAlphaMix Indicates if we do alpha mixing
     */
    fun repeatOnLineHorizontal(x1 : Int, x2 : Int, y : Int, image : JHelpImage, doAlphaMix : Boolean = true)
    {
        val xx = - (image.width shr 1)
        val yy = y - (image.height shr 1)
        val xMin = xx + Math.min(x1, x2)
        val xMax = xx + Math.max(x1, x2)

        for (x in xMin .. xMax)
        {
            this.drawImage(x, yy, image, doAlphaMix = doAlphaMix)
        }
    }

    /**
     * Repeat an image on following a vertical line
     *
     * @param x          Line X
     * @param y1         First point y
     * @param y2         Second point Y
     * @param image      Image to repeat
     * @param doAlphaMix Indicates if we do alpha mixing
     */
    fun repeatOnLineVertical(x : Int, y1 : Int, y2 : Int, image : JHelpImage, doAlphaMix : Boolean = true)
    {
        val xx = x - (image.width shr 1)
        val yy = - (image.height shr 1)
        val yMin = yy + Math.min(y1, y2)
        val yMax = yy + Math.max(y1, y2)

        for (y in yMin .. yMax)
        {
            this.drawImage(xx, y, image, doAlphaMix = doAlphaMix)
        }
    }

    /**
     * Replace all pixels near a color by an other color

     * MUST be in draw mode
     *
     * @param colorToReplace Color searched
     * @param newColor       New color
     * @param near           Distance maximum from color searched to consider to color is near
     */
    fun replaceColor(colorToReplace : Int, newColor : Int, near : Int)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var color : Int

        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]

            if (Math.abs((colorToReplace.alpha) - (color.alpha)) <= near
                && Math.abs((colorToReplace.red) - (color.red)) <= near
                && Math.abs((colorToReplace.green) - (color.green)) <= near
                && Math.abs((colorToReplace.blue) - (color.blue)) <= near)
            {
                this.pixels[pix] = newColor
            }
        }
    }

    /**
     * Compute the image rotated from 180 degree

     * If the image is not in draw mode, visible sprites are consider like a part of image
     *
     * @return Rotated image
     */
    fun rotate180() : JHelpImage
    {
        val width = this.width
        val height = this.height
        val length = width * height
        val pixels = IntArray(length)

        var pix = 0
        var pixR = length - 1
        while (pixR >= 0)
        {
            pixels[pixR] = this.pixels[pix]
            pix ++
            pixR --
        }

        return JHelpImage(width, height, pixels)
    }

    /**
     * Compute the image rotated from 270 degree

     * If the image is not in draw mode, visible sprites are consider like a part of image
     *
     * @return Rotated image
     */
    fun rotate270() : JHelpImage
    {
        val width = this.height
        val height = this.width
        val pixels = IntArray(width * height)

        var xr = width - 1
        val yr = 0
        val startR = yr * width
        var pixR = startR + xr

        var pix = 0

        for (y in 0 until this.height)
        {
            for (x in 0 until this.width)
            {
                pixels[pixR] = this.pixels[pix]

                pix ++
                pixR += width
            }

            xr --
            pixR = startR + xr
        }

        return JHelpImage(width, height, pixels)
    }

    /**
     * Compute the image rotated from 90 degree

     * If the image is not in draw mode, visible sprites are consider like a part of image
     *
     * @return Rotated image
     */
    fun rotate90() : JHelpImage
    {
        val width = this.height
        val height = this.width
        val pixels = IntArray(width * height)

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
                pixels[pixR] = this.pixels[pix]

                pix ++
                pixR += stepR
            }

            xr ++
            pixR = startR + xr
        }

        return JHelpImage(width, height, pixels)
    }

    /**
     * Extract a sub image and then rotate it from 180 degree

     * If the image is not in draw mode, visible sprites are consider like a part of image
     *
     * @param x      Upper left area corner X
     * @param y      Upper left area corner Y
     * @param width  Area to extract width
     * @param height Area to extract height
     * @return Result image
     */
    fun rotatedPart180(x : Int, y : Int, width : Int, height : Int) = this.extractSubImage(x, y, width, height)
        .rotate180()

    /**
     * Extract a sub image and then rotate it from 270 degree

     * If the image is not in draw mode, visible sprites are consider like a part of image
     *
     * @param x      Upper left area corner X
     * @param y      Upper left area corner Y
     * @param width  Area to extract width
     * @param height Area to extract height
     * @return Result image
     */
    fun rotatedPart270(x : Int, y : Int, width : Int, height : Int) = this.extractSubImage(x, y, width, height)
        .rotate270()

    /**
     * Extract a sub image and then rotate it from 90 degree

     * If the image is not in draw mode, visible sprites are consider like a part of image
     *
     * @param x      Upper left area corner X
     * @param y      Upper left area corner Y
     * @param width  Area to extract width
     * @param height Area to extract height
     * @return Result image
     */
    fun rotatedPart90(x : Int, y : Int, width : Int, height : Int) = this.extractSubImage(x, y, width, height)
        .rotate90()

    /**
     * Change one pixel color.

     * Must be in draw mode
     *
     * @param x     X
     * @param y     Y
     * @param color Color
     */
    fun setPixel(x : Int, y : Int, color : Int)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (x < 0 || x >= this.width || y < 0 || y >= this.height)
        {
            return
        }

        this.pixels[x + y * this.width] = color
    }

    /**
     * Change a pixels area.
     *
     * MUST be in draw mode
     *
     * @param x      X up-left corner
     * @param y      Y up-left corner
     * @param width  Width of image in pixels array
     * @param height Height of image in pixels array
     * @param pixels Pixels array
     * @param offset Offset where start read pixels data
     */
    fun pixels(x : Int, y : Int, width : Int, height : Int, pixels : IntArray, offset : Int = 0)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        if (x == 0 && y == 0 && width == this.width && height == this.height && offset == 0)
        {
            System.arraycopy(pixels, 0, this.pixels, 0, this.pixels.size)
            return
        }

        var width = width
        var x = x

        if (x < 0)
        {
            width += x
            x = 0
        }

        var height = height
        var y = y

        if (y < 0)
        {
            height += y
            y = 0
        }

        val w = minOf(this.width - x, width)
        val h = minOf(this.height - y, height)

        if (w <= 0 || h <= 0)
        {
            return
        }

        var lineThis = x + y * this.width
        var lineImage = offset

        for (yy in 0 until h)
        {
            System.arraycopy(pixels, lineImage, this.pixels, lineThis, w)

            lineThis += this.width
            lineImage += width
        }
    }

    /**
     * Change image global transparency

     * MUST be in draw mode
     *
     * @param alpha New global transparency
     */
    fun setTransparency(alpha : Int)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        val alphaPart = alpha.limit_0_255 shl 24
        var color : Int

        for (pix in 0 until this.pixels.size)
        {
            color = this.pixels[pix]
            this.pixels[pix] = alphaPart or (color and COLOR_MASK)
        }
    }

    /**
     * Shift (translate) the image

     * MUST be in draw mode
     *
     * @param x X shift
     * @param y Y shift
     */
    fun shift(x : Int, y : Int)
    {
        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var index = x + y * this.width
        val size = this.pixels.size
        index = index % size

        if (index < 0)
        {
            index += size
        }

        val temp = IntArray(size)
        System.arraycopy(this.pixels, 0, temp, 0, size)

        for (i in 0 until size)
        {
            this.pixels[i] = temp[index]

            index = (index + 1) % size
        }
    }

    /**
     * Force a sprite to be over the others
     *
     * @param sprite Sprite to put at top
     */
    fun spriteAtTop(sprite : JHelpSprite?)
    {
        if (sprite == null)
        {
            return
        }

        val index = this.sprites.indexOf(sprite)

        if (index < 0)
        {
            return
        }

        val oldDrawMode = this.drawMode

        if (oldDrawMode)
        {
            this.endDrawMode()
        }

        val size = this.sprites.size
        val visibles = BooleanArray(size)

        for (i in 0 until size)
        {
            visibles[i] = this.sprites[i].visible()
            this.sprites[i].visible(false)
        }

        val visible = visibles[index]
        System.arraycopy(visibles, index + 1, visibles, index, size - index - 1)
        visibles[size - 1] = visible

        this.sprites.removeAt(index)
        this.sprites.add(sprite)

        for (i in 0 until size)
        {
            this.sprites[i].spriteIndex(i)
            this.sprites[i].visible(visibles[i])
        }

        if (oldDrawMode)
        {
            this.startDrawMode()
        }
    }

    /**
     * Start the draw mode.

     * Don't call this method if image is locked. Use [.drawModeLocked] to know.

     * The image is locked if we are inside a task launch by [.playInDrawMode] or [.playOutDrawMode]
     *
     * @throws IllegalStateException If draw mode is locked
     */
    fun startDrawMode()
    {
        if (this.drawModeLocked)
        {
            throw IllegalStateException("Draw mode is locked")
        }

        if (! this.drawMode)
        {
            this.drawMode = true

            this.mutexVisibilities {
                val length = this.sprites.size

                if (this.visibilities == null || this.visibilities?.size != length)
                {
                    this.visibilities = BooleanArray(length)
                }

                var visible : Boolean
                var sprite : JHelpSprite

                for (index in length - 1 downTo 0)
                {
                    sprite = this.sprites[index]
                    this.visibilities !![index] = sprite.visible()
                    visible = this.visibilities !![index]

                    if (visible)
                    {
                        sprite.changeVisible(false)
                    }
                }
            }

            synchronized(this.playInDrawMode)
            {
                this.drawModeLocked = true

                while (! this.playInDrawMode.empty)
                {
                    this.playInDrawMode.outQueue()(this)
                }

                this.drawModeLocked = false
            }
        }
    }

    /**
     * Subtract the image by an other one

     * Note : if the given image is not in draw mode, all of it's visible sprite will be consider like a part of the
     * given image
     *

     * Given image MUST have same dimension of this

     * MUST be in draw mode
     *
     * @param image Image to subtract
     */
    fun subtract(image : JHelpImage)
    {
        if (this.width != image.width || this.height != image.height)
        {
            throw IllegalArgumentException("We can only subtract with an image of same size")
        }

        if (! this.drawMode)
        {
            throw IllegalStateException("Must be in draw mode !")
        }

        var colorThis : Int
        var colorImage : Int

        for (pix in 0 until this.pixels.size)
        {
            colorThis = this.pixels[pix]
            colorImage = image.pixels[pix]

            this.pixels[pix] = colorThis and BLACK_ALPHA_MASK or
                    ((colorThis.red - colorImage.red).limit_0_255 shl 16) or
                    ((colorThis.green - colorImage.green).limit_0_255 shl 8) or
                    (colorThis.blue - colorImage.blue).limit_0_255
        }
    }

    /**
     * Subtract with an other image
     * @param image To subtract
     */
    operator fun minusAssign(image : JHelpImage) = this.subtract(image)

    /**
     * Subtract with an other image and return the result
     * @param image To subtract
     * @return Subtraction result
     */
    operator fun minus(image : JHelpImage) : JHelpImage
    {
        val copy = this.copy()
        copy.startDrawMode()
        copy -= image
        copy.endDrawMode()
        return copy
    }

    /**
     * Tint image.

     * MUST be in draw mode
     *
     * @param color Color to tint with
     */
    fun tint(color : Int)
    {
        this.gray()
        val red = color.red
        val green = color.green
        val blue = color.blue
        var col : Int
        var gray : Int

        for (pix in 0 until this.pixels.size)
        {
            col = this.pixels[pix]
            gray = col.blue
            this.pixels[pix] =
                col and BLACK_ALPHA_MASK or (red * gray shr 8 shl 16) or (green * gray shr 8 shl 8) or (blue * gray shr 8)
        }
    }

    /**
     * Tint image.

     * MUST be in draw mode
     *
     * @param colorHigh Color for "high" value
     * @param colorLow  Color for "low" value
     */
    fun tint(colorHigh : Int, colorLow : Int)
    {
        this.gray()
        val redHigh = colorHigh.red
        val greenHigh = colorHigh.green
        val blueHigh = colorHigh.blue
        val redLow = colorLow.red
        val greenLow = colorLow.green
        val blueLow = colorLow.blue
        var col : Int
        var gray : Int
        var yarg : Int

        for (pix in 0 until this.pixels.size)
        {
            col = this.pixels[pix]
            gray = col and 0xFF
            yarg = 256 - gray
            this.pixels[pix] = (col and BLACK_ALPHA_MASK
                    or (redHigh * gray + redLow * yarg shr 8 shl 16)
                    or (greenHigh * gray + greenLow * yarg shr 8 shl 8)
                    or (blueHigh * gray + blueLow * yarg shr 8))
        }
    }

    /**
     * Give all sprites of this image to an other image
     *
     * @param image Image will receive this image sprites
     */
    fun transfertSpritesTo(image : JHelpImage)
    {
        val drawMode = image.drawMode
        val draw = this.drawMode

        image.endDrawMode()
        this.endDrawMode()
        var visible : Boolean

        for (sprite in this.sprites)
        {
            visible = sprite.visible()
            sprite.visible(false)
            sprite.parent(image)
            image.sprites.add(sprite)
            sprite.visible(visible)
        }

        this.sprites.clear()

        image.update()

        if (drawMode)
        {
            image.startDrawMode()
        }

        if (draw)
        {
            this.startDrawMode()
        }
    }

    /**
     * Unregister a component
     *
     * @param component Component to unregister
     */
    fun unregister(component : Component) =
        this.mutex { this.componentsListeners.remove(component) }

    /**
     * Update the image, to see last changes
     */
    fun update()
    {
        val onDraw = this.drawMode

        if (onDraw)
        {
            this.endDrawMode()
        }

        this.memoryImageSource.newPixels()

        if (onDraw)
        {
            this.startDrawMode()
        }

        this.mutex {
            for (component in this.componentsListeners)
            {
                component.invalidate()
                component.validate()
                component.repaint()
            }
        }
    }
}
