package khelp.image

import khelp.image.bmp.loadBitmap
import khelp.image.extensions.resize
import khelp.image.pcx.PCX
import khelp.image.pcx.isPCX
import khelp.thread.TaskContext
import khelp.thread.parallel
import khelp.utilities.log.exception
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.Locale
import javax.imageio.ImageIO
import javax.imageio.ImageReader
import javax.imageio.stream.ImageInputStream

/**
 * Dummy image 1x1
 */
val DUMMY_IMAGE = JHelpImage(1, 1)

/**
 * Load an image from file.

 * This method also manage [PCX] image files
 *
 * @param image Image file
 * @return Loaded image
 * @throws IOException On file reading issue
 */
@Throws(IOException::class)
fun loadImage(image : File) : JHelpImage
{
    if (isPCX(image))
    {
        var inputStream : InputStream? = null

        try
        {
            inputStream = FileInputStream(image)
            val pcx = PCX(inputStream)
            return pcx.createImage()
        }
        catch (exception : Exception)
        {
            throw IOException(image.absolutePath + " not PCX well formed", exception)
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close()
                }
                catch (ignored : Exception)
                {
                }

            }
        }
    }

    val bitmap = loadBitmap(image)

    if (bitmap.isPresent)
    {
        return bitmap.get()
            .toJHelpImage()
    }

    val bufferedImage = loadBufferedImage(image)
    val width = bufferedImage.width
    val height = bufferedImage.height

    var pixels = IntArray(width * height)
    pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width)

    val imageLoaded = JHelpImage(width, height, pixels)
    bufferedImage.flush()

    return imageLoaded
}

/**
 * Load an image from a stream
 *
 * @param inputStream Stream to read
 * @return Read image
 * @throws IOException On reading issue
 */
@Throws(IOException::class)
fun loadImage(inputStream : InputStream) : JHelpImage
{
    val bufferedImage = ImageIO.read(inputStream) ?: throw IOException("Failed to load image")

    val width = bufferedImage.width
    val height = bufferedImage.height
    var pixels = IntArray(width * height)

    pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width)

    val image = JHelpImage(width, height, pixels)

    bufferedImage.flush()

    return image
}

/**
 * Load an image and resize it to have specific dimension.
 *
 * This method also manage [PCX] image files
 *
 * @param image  Image file
 * @param width  Final width
 * @param height Final height
 * @return Loaded image resized to corresponds to specified dimension
 * @throws IOException On reading file issue
 */
@Throws(IOException::class)
fun loadImageThumb(image : File, width : Int, height : Int) : JHelpImage
{
    if (isPCX(image))
    {
        var inputStream : InputStream? = null

        try
        {
            inputStream = FileInputStream(image)
            val pcx = PCX(inputStream)
            return pcx.createImage().resize(width, height)
        }
        catch (exception : Exception)
        {
            throw IOException(image.absolutePath + " not PCX well formed", exception)
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close()
                }
                catch (ignored : Exception)
                {
                }

            }
        }
    }

    var bufferedImage : BufferedImage = loadBufferedImage(image)

    val imageWidth = bufferedImage.width
    val imageHeight = bufferedImage.height

    if (imageWidth != width || imageHeight != height)
    {
        val bufferedImageTemp = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        val graphics2d = bufferedImageTemp.createGraphics()

        graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                                    RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY)
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                                    RenderingHints.VALUE_COLOR_RENDER_QUALITY)
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                    RenderingHints.VALUE_INTERPOLATION_BICUBIC)
        graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

        graphics2d.drawImage(bufferedImage, 0, 0, width, height, null)

        bufferedImage.flush()
        bufferedImage = bufferedImageTemp
    }

    var pixels = IntArray(width * height)

    pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width)

    val imageLoaded = JHelpImage(width, height, pixels)

    bufferedImage.flush()

    return imageLoaded
}

/**
 * Load a buffered image
 *
 * @param image Image file
 * @return Buffered image loaded
 * @throws IOException On reading file issue
 */
@Throws(IOException::class)
private fun loadBufferedImage(image : File) : BufferedImage
{
    val name = image.name.lowercase(Locale.getDefault())
    val fileImageInformation = FileImageInformation(image)
    var suffix = fileImageInformation.formatName

    if (suffix == null)
    {
        val index = name.lastIndexOf('.')
        if (index > 0)
        {
            suffix = name.substring(index + 1)
        }
    }

    if (suffix != null)
    {
        var stream : ImageInputStream? = null
        var imageReader : ImageReader? = null
        var bufferedImage : BufferedImage
        val imagesReaders = ImageIO.getImageReadersBySuffix(suffix)

        while (imagesReaders.hasNext())
        {
            try
            {
                stream = ImageIO.createImageInputStream(image)
                imageReader = imagesReaders.next()

                imageReader !!.input = stream
                val future = parallel(TaskContext.MAIN, imageReader) { reader -> reader.read(0) }
                bufferedImage = future.result()
                imageReader.dispose()

                return bufferedImage
            }
            catch (exception : Exception)
            {
                exception(exception)
            }
            finally
            {
                if (stream != null)
                {
                    try
                    {
                        stream.close()
                    }
                    catch (ignored : Exception)
                    {
                    }

                }
                stream = null

                if (imageReader != null)
                {
                    imageReader.dispose()
                }
                imageReader = null
            }
        }
    }

    return ImageIO.read(image)
}

