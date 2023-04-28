package khelp.image.extensions

import khelp.image.JHelpImage
import khelp.utilities.extensions.alpha
import khelp.utilities.extensions.blue
import khelp.utilities.extensions.green
import khelp.utilities.extensions.red
import java.awt.image.BufferedImage
import java.io.IOException
import java.io.OutputStream
import javax.imageio.ImageIO

/**
 * Create a bump image with 0.75 contrast, 12 dark, 1 shift X and 1 shift Y

 * Note : If one of image is not in draw mode, all visible sprite (of this image) will be consider as a part of the
 * image
 *
 * @param source Image source
 * @param bump   Image used for bump
 * @return Bumped image
 */
infix fun JHelpImage.bump(bump : JHelpImage) : JHelpImage =
    this.bump(bump, 0.75, 12, 1, 1)


/**
 * Create a bump image with 0.75 contrast, 12 dark, -1 shift X and -1 shift Y

 * Note : If one of image is not in draw mode, all visible sprite (of this image) will be consider as a part of the
 * image
 *
 * @param source Image source
 * @param bump   Image used for bump
 * @return Bumped image
 */
infix fun JHelpImage.bump2(bump : JHelpImage) : JHelpImage =
    this.bump(bump, 0.75, 12, - 1, - 1)

/**
 * Create a bump image

 * Note : If one of image is not in draw mode, all visible sprite (of this image) will be consider as a part of the
 * image
 *
 * @param bump     Image used for bump
 * @param contrast Contrast to use in [0, 1].
 * @param dark     Dark to use. in [0, 255].
 * @param shiftX   Shift X [-3, 3].
 * @param shiftY   Shift Y [-3, 3].
 * @return Bumped image
 */
fun JHelpImage.bump(bump : JHelpImage,
                    contrast : Double, dark : Int, shiftX : Int, shiftY : Int) : JHelpImage
{
    var contrast = contrast
    val width = this.width
    val height = this.height

    if (width != bump.width || height != bump.height)
    {
        throw IllegalArgumentException("Images must have the same size")
    }

    if (contrast < 0.5)
    {
        contrast *= 2.0
    }
    else
    {
        contrast = contrast * 18 - 8
    }

    this.update()
    bump.update()
    val bumped = JHelpImage(width, height)
    val temp = JHelpImage(width, height)

    bumped.startDrawMode()
    temp.startDrawMode()

    bumped.copy(bump)
    bumped.gray()
    bumped.contrast(contrast)

    temp.copy(bumped)
    temp *= this
    temp.darker(dark)

    bumped.invertColors()
    bumped *= this
    bumped.darker(dark)
    bumped.shift(shiftX, shiftY)
    bumped += temp

    bumped.endDrawMode()
    temp.endDrawMode()

    return bumped
}

/**
 * Create a resized image from a given one in parameter.
 *
 * If the desired size is exactly the same has the given image, the image itself is return.
 *
 * In case of different size, if the given image is not in draw mode, visible sprites on it will be a part of resized
 * image
 *
 * @param width  New width
 * @param height New height
 * @return Resized image
 */
fun JHelpImage.resize(width : Int, height : Int) : JHelpImage
{
    if (width < 1 || height < 1)
    {
        throw IllegalArgumentException(
            "width and height must be > 1, but it is specify : " + width + "x" + height)
    }

    if (this.width == width && this.height == height)
    {
        return this
    }

    val result = JHelpImage(width, height)
    result.startDrawMode()
    result.fillRectangleScaleBetter(0, 0, width, height, this, false)
    result.endDrawMode()

    return result
}

/**
 * Compare 2 images and compute if they "look" the same in compare the image border. That is to say if we obtain
 * border of objects inside the image
 *
 * The precision is to determine the accepted distance in border limit, and percent to know the percent of accepted
 * pixels
 * doesn't match to precision.
 *
 * Note : if images haven't same dimension, the smallest is firstly scale to fit to the biggest
 *
 * Note : if one image is not in draw mode, the visible sprites of this image will be consider like a part of the image
 *
 * @param image                    Second image
 * @param precision                 Difference accepted in border limit
 * @param percentDifferenceAccepted Percent of accepted different pixels (Pixels doesn't match to the precision)
 * @return `true` if images "look" the same
 */
fun JHelpImage.lookSamePerBorder(image : JHelpImage,
                                 precision : Int, percentDifferenceAccepted : Int) : Boolean
{
    var image1 = this
    var image2 = image
    val width1 = image1.width
    val height1 = image1.height
    val width2 = image2.width
    val height2 = image2.height

    if (width1 != width2 || height1 != height2)
    {
        if (width1 * height1 >= width2 * height2)
        {
            val imageTemp = JHelpImage(width1, height1)
            imageTemp.startDrawMode()
            imageTemp.fillRectangleScaleBetter(0, 0, width1, height1, image2)
            imageTemp.endDrawMode()

            image2 = imageTemp
        }
        else
        {
            val imageTemp = JHelpImage(width2, height2)
            imageTemp.startDrawMode()
            imageTemp.fillRectangleScaleBetter(0, 0, width2, height2, image1)
            imageTemp.endDrawMode()

            image1 = imageTemp
        }
    }

    val img1 = image1.border(3, 1)
    val img2 = image2.border(3, 1)

    return img1.lookSamePerPixel(img2, precision, percentDifferenceAccepted)
}

/**
 * Compare 2 images and compute if they "look" the same in compare the image luminosity

 * The precision is to determine the accepted distance in luminosity part, and percent to know the percent of
 * accepted pixels
 * doesn't match to precision.

 * Note : if images haven't same dimension, the smallest is firstly scale to fit to the biggest

 * Note : if one image is not in draw mode, the visible sprites of this image will be consider like a part of the image
 *
 * @param image                    Second image
 * @param precision                 Difference accepted in luminosity
 * @param percentDifferenceAccepted Percent of accepted different pixels (Pixels doesn't match to the precision)
 * @return `true` if images "look" the same
 */
fun JHelpImage.lookSamePerLuminosity(image : JHelpImage,
                                     precision : Int, percentDifferenceAccepted : Int) : Boolean
{
    var image1 = this
    var image2 = image
    val width1 = image1.width
    val height1 = image1.height
    val width2 = image2.width
    val height2 = image2.height

    if (width1 != width2 || height1 != height2)
    {
        if (width1 * height1 >= width2 * height2)
        {
            val imageTemp = JHelpImage(width1, height1)
            imageTemp.startDrawMode()
            imageTemp.fillRectangleScaleBetter(0, 0, width1, height1, image2)
            imageTemp.endDrawMode()

            image2 = imageTemp
        }
        else
        {
            val imageTemp = JHelpImage(width2, height2)
            imageTemp.startDrawMode()
            imageTemp.fillRectangleScaleBetter(0, 0, width2, height2, image1)
            imageTemp.endDrawMode()

            image1 = imageTemp
        }
    }

    val img1 = image1.copy()
    img1.startDrawMode()
    img1.gray()
    img1.endDrawMode()

    val img2 = image2.copy()
    img2.startDrawMode()
    img2.gray()
    img2.endDrawMode()

    return img1.lookSamePerPixel(img2, precision, percentDifferenceAccepted)
}


/**
 * Compare 2 images and compute if they "look" the same in compare the image pixels.

 * The precision is to determine the accepted distance in alpha, red, green and blue part, and percent to know the
 * percent of
 * accepted pixels doesn't match to precision.

 * Note : if images haven't same dimension, the smallest is firstly scale to fit to the biggest

 * Note : if one image is not in draw mode, the visible sprites of this image will be consider like a part of the image
 *
 * @param image                    Second image
 * @param colorPartPrecision        Difference accepted in pixel parts
 * @param percentDifferenceAccepted Percent of accepted different pixels (Pixels doesn't match to the precision)
 * @return `true` if images "look" the same
 */
fun JHelpImage.lookSamePerPixel(image : JHelpImage,
                                colorPartPrecision : Int, percentDifferenceAccepted : Int) : Boolean
{
    var image1 = this
    var image2 = image
    var colorPartPrecision = colorPartPrecision
    var percentDifferenceAccepted = percentDifferenceAccepted
    colorPartPrecision = Math.max(0, Math.min(255, colorPartPrecision))
    percentDifferenceAccepted = Math.max(0, Math.min(100, percentDifferenceAccepted))

    var width1 = image1.width
    var height1 = image1.height
    val width2 = image2.width
    val height2 = image2.height

    if (width1 != width2 || height1 != height2)
    {
        if (width1 * height1 >= width2 * height2)
        {
            val imageTemp = JHelpImage(width1, height1)
            imageTemp.startDrawMode()
            imageTemp.fillRectangleScaleBetter(0, 0, width1, height1, image2)
            imageTemp.endDrawMode()

            image2 = imageTemp
        }
        else
        {
            val imageTemp = JHelpImage(width2, height2)
            imageTemp.startDrawMode()
            imageTemp.fillRectangleScaleBetter(0, 0, width2, height2, image1)
            imageTemp.endDrawMode()

            image1 = imageTemp
            width1 = width2
            height1 = height2
        }
    }

    val length = width1 * height1
    val pixels1 = image1.pixels(0, 0, image1.width, image1.height)
    val pixels2 = image2.pixels(0, 0, image2.width, image2.height)
    var color1 : Int
    var color2 : Int
    var difference = 0

    for (pix in length - 1 downTo 0)
    {
        color1 = pixels1[pix]
        color2 = pixels2[pix]

        if (Math.abs((color1.alpha) - (color2.alpha)) > colorPartPrecision)
        {
            difference ++
        }

        if (Math.abs((color1.red) - (color2.red)) > colorPartPrecision)
        {
            difference ++
        }

        if (Math.abs((color1.green) - (color2.green)) > colorPartPrecision)
        {
            difference ++
        }

        if (Math.abs((color1.blue) - (color2.blue)) > colorPartPrecision)
        {
            difference ++
        }
    }

    return difference * 100 / (length shl 2) <= percentDifferenceAccepted
}

/**
 * Extract the border of the objects inside the image. Width 2, step 1

 * Note : If the image is not in draw mode, all visible sprite will be consider as a part of the image
 *
 * @return Image border
 */
val JHelpImage.border : JHelpImage
    get() =
        this.border(2)

/**
 * Extract the border of the objects inside the image.

 * Note : If the image is not in draw mode, all visible sprite will be consider as a part of the image
 *
 * @param width  Line width
 * @param step   Step to jump between width : [1, width]
 * @return Image border
 */
fun JHelpImage.border(width : Int = 1, step : Int = 1) : JHelpImage
{
    if (width < 0)
    {
        throw IllegalArgumentException("width can't be negative")
    }

    if (step < 1)
    {
        throw IllegalArgumentException("step must be >=1")
    }

    this.update()

    val result = this.copy()
    result.startDrawMode()
    result.gray()
    val temporary = - result
    val temp = result.copy()
    temp.startDrawMode()
    result.shift(1, 1)
    result += temporary
    val image = temp.copy()
    var y = - width

    while (y <= width)
    {
        var x = - width

        while (x <= width)
        {
            temp.copy(image)
            temp.shift(x, y)
            temp += temporary
            result.minimum(temp)
            x += step
        }

        y += step
    }

    temp.endDrawMode()
    result.endDrawMode()

    return result
}

@Throws(IOException::class)
fun JHelpImage.savePNG(outputStream : OutputStream)
{
    val bufferedImage : BufferedImage = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB)
    val pixels = this.pixels(0, 0, this.width, this.height)
    bufferedImage.setRGB(0, 0, this.width, this.height, pixels, 0, this.width)
    ImageIO.write(bufferedImage, "PNG", outputStream)
    outputStream.flush()
    bufferedImage.flush()
}

@Throws(IOException::class)
fun JHelpImage.saveJPG(outputStream : OutputStream)
{
    val bufferedImage = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB)
    val graphics2d = bufferedImage.createGraphics()
    graphics2d.drawImage(this.image, 0, 0, null)
    ImageIO.write(bufferedImage, "JPG", outputStream)
    outputStream.flush()
    bufferedImage.flush()
    graphics2d.dispose()
}
