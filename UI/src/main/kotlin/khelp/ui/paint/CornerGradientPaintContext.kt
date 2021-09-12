package khelp.ui.paint

import java.awt.Color
import java.awt.PaintContext
import java.awt.Rectangle
import java.awt.Transparency
import java.awt.image.ColorModel
import java.awt.image.DirectColorModel
import java.awt.image.Raster
import java.awt.image.WritableRaster
import java.lang.ref.WeakReference

class CornerGradientPaintContext(colorModel : ColorModel, deviceBounds : Rectangle,
                                 colorLeftUp : Color, colorRightUp : Color,
                                 colorLeftBottom : Color, colorRightBottom : Color) : PaintContext
{
    companion object
    {
        private val modelRGB = DirectColorModel(24, 0x00ff0000, 0x0000ff00, 0x000000ff)
        private val modelBGR = DirectColorModel(24, 0x000000ff, 0x0000ff00, 0x00ff0000)
        private var cachedModel : ColorModel? = null
        private var cachedRaster : WeakReference<Raster>? = null

        private fun obtainOrCreateRaster(colorModel : ColorModel, width : Int, height : Int) : Raster
        {
            if (colorModel == CornerGradientPaintContext.cachedModel)
            {
                val raster = CornerGradientPaintContext.cachedRaster?.get()

                if (raster != null && raster.width >= width && raster.height >= height)
                {
                    CornerGradientPaintContext.cachedRaster = null
                    return raster
                }
            }

            return colorModel.createCompatibleWritableRaster(width, height)
        }

        private fun cacheRaster(colorModel : ColorModel, raster : Raster)
        {
            val cachedRaster = CornerGradientPaintContext.cachedRaster?.get()

            if (cachedRaster != null)
            {
                val rasterWidth = raster.width
                val rasterHeight = raster.height
                val cachedRasterWidth = cachedRaster.width
                val cachedRasterHeight = cachedRaster.height

                if (cachedRasterWidth >= rasterWidth && cachedRasterHeight >= rasterHeight)
                {
                    return
                }

                if (cachedRasterWidth * cachedRasterHeight >= rasterWidth * rasterHeight)
                {
                    return
                }
            }

            CornerGradientPaintContext.cachedModel = colorModel
            CornerGradientPaintContext.cachedRaster = WeakReference<Raster>(raster)
        }
    }

    val transparency : Int

    private val colorLeftUpAlpha : Int
    private val colorLeftUpRed : Int
    private val colorLeftUpGreen : Int
    private val colorLeftUpBlue : Int

    private val colorRightUpAlpha : Int
    private val colorRightUpRed : Int
    private val colorRightUpGreen : Int
    private val colorRightUpBlue : Int

    private val colorLeftBottomAlpha : Int
    private val colorLeftBottomRed : Int
    private val colorLeftBottomGreen : Int
    private val colorLeftBottomBlue : Int

    private val colorRightBottomAlpha : Int
    private val colorRightBottomRed : Int
    private val colorRightBottomGreen : Int
    private val colorRightBottomBlue : Int

    private val xMin = deviceBounds.x
    private val yMin = deviceBounds.y
    private val totalWidth = deviceBounds.width
    private val totalHeight = deviceBounds.height
    private val totalSize = deviceBounds.width * deviceBounds.height

    private var raster : Raster? = null
    private var model : ColorModel

    init
    {
        val leftUpARGB = colorLeftUp.rgb
        val rightUpARGB = colorRightUp.rgb
        val leftBottomARGB = colorLeftBottom.rgb
        val rightBottomARGB = colorRightBottom.rgb

        this.colorLeftUpAlpha = (leftUpARGB shr 24) and 0xFF
        this.colorRightUpAlpha = (rightUpARGB shr 24) and 0xFF
        this.colorLeftBottomAlpha = (leftBottomARGB shr 24) and 0xFF
        this.colorRightBottomAlpha = (rightBottomARGB shr 24) and 0xFF

        var redShift = 16
        var blueShift = 0

        if (this.colorLeftUpAlpha == 0xFF && this.colorRightUpAlpha == 0xFF &&
            this.colorLeftBottomAlpha == 0xFF && this.colorRightBottomAlpha == 0xFF)
        {
            this.model = CornerGradientPaintContext.modelRGB

            if (colorModel is DirectColorModel)
            {
                val alphaMask = colorModel.alphaMask

                if ((alphaMask == 0 || alphaMask == 0xFF) &&
                    colorModel.redMask == 0xFF &&
                    colorModel.greenMask == 0xFF000 &&
                    colorModel.blueMask == 0xFF0000)
                {
                    this.model = CornerGradientPaintContext.modelBGR
                    redShift = 0
                    blueShift = 16
                }
            }
        }
        else
        {
            this.model = ColorModel.getRGBdefault()
        }

        this.colorLeftUpRed = (leftUpARGB shr redShift) and 0xFF
        this.colorRightUpRed = (rightUpARGB shr redShift) and 0xFF
        this.colorLeftBottomRed = (leftBottomARGB shr redShift) and 0xFF
        this.colorRightBottomRed = (rightBottomARGB shr redShift) and 0xFF

        this.colorLeftUpGreen = (leftUpARGB shr 8) and 0xFF
        this.colorRightUpGreen = (rightUpARGB shr 8) and 0xFF
        this.colorLeftBottomGreen = (leftBottomARGB shr 8) and 0xFF
        this.colorRightBottomGreen = (rightBottomARGB shr 8) and 0xFF

        this.colorLeftUpBlue = (leftUpARGB shr blueShift) and 0xFF
        this.colorRightUpBlue = (rightUpARGB shr blueShift) and 0xFF
        this.colorLeftBottomBlue = (leftBottomARGB shr blueShift) and 0xFF
        this.colorRightBottomBlue = (rightBottomARGB shr blueShift) and 0xFF

        if (this.colorLeftUpAlpha == 0xFF &&
            this.colorRightUpAlpha == 0xFF &&
            this.colorLeftBottomAlpha == 0xFF &&
            this.colorRightBottomAlpha == 0xFF)
        {
            this.transparency = Transparency.OPAQUE
        }
        else
        {
            this.transparency = Transparency.TRANSLUCENT
        }
    }

    override fun dispose()
    {
        this.raster?.let { raster ->
            CornerGradientPaintContext.cacheRaster(this.model, raster)
            this.raster = null
        }
    }

    override fun getColorModel() : ColorModel = this.model

    override fun getRaster(x : Int, y : Int, width : Int, height : Int) : Raster
    {
        var raster = this.raster

        if (raster == null || raster.width < width || raster.height < height)
        {
            raster = CornerGradientPaintContext.obtainOrCreateRaster(this.model, width, height)
            this.raster = raster
        }

        if (raster is WritableRaster)
        {
            val rasterWidth = raster.width
            val rasterHeight = raster.height
            val pixels = IntArray(rasterWidth * rasterHeight)
            val size = this.totalSize
            var line = 0
            var pixel : Int
            var antiX : Int
            var antiY : Int
            val totalWidth = this.totalWidth
            val totalHeight = this.totalHeight
            val xStart = x - this.xMin
            var xxx : Int
            var yyy = y - this.yMin

            for (yy in 0 until height)
            {
                pixel = line
                antiY = totalHeight - yyy
                xxx = xStart

                for (xx in 0 until width)
                {
                    antiX = totalWidth - xxx
                    pixels[pixel] =
                        ((((this.colorLeftUpAlpha * antiX + this.colorRightUpAlpha * xxx) * antiY +
                           (this.colorLeftBottomAlpha * antiX + this.colorRightBottomAlpha * xxx) * yyy) / size) shl 24) or
                                ((((this.colorLeftUpRed * antiX + this.colorRightUpRed * xxx) * antiY +
                                   (this.colorLeftBottomRed * antiX + this.colorRightBottomRed * xxx) * yyy) / size) shl 16) or
                                ((((this.colorLeftUpGreen * antiX + this.colorRightUpGreen * xxx) * antiY +
                                   (this.colorLeftBottomGreen * antiX + this.colorRightBottomGreen * xxx) * yyy) / size) shl 8) or
                                (((this.colorLeftUpBlue * antiX + this.colorRightUpBlue * xxx) * antiY +
                                  (this.colorLeftBottomBlue * antiX + this.colorRightBottomBlue * xxx) * yyy) / size)

                    xxx ++
                    pixel ++
                }

                yyy ++
                line += rasterWidth
            }

            raster.setDataElements(0, 0, width, height, pixels)
        }

        return raster
    }
}
