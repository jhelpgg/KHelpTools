package khelp.image.gif

import khelp.image.JHelpImage
import khelp.image.JHelpSprite
import khelp.io.IntegerArrayInputStream
import khelp.utilities.collections.ArrayInt
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * GIF image
 * @param inputStream Stream to read image
 */
class GIF(inputStream : InputStream)
{
    companion object
    {
        /**
         * Compute size of an GIF image.
         *
         * If the given file is not a GIF image file, `null` is return
         *
         * @param file Image GIF file
         * @return GIF image size OR `null` if given file not a valid GIF image file
         */
        fun computeGifSize(file : File) = khelp.image.gif.computeGifSize(file)

        /**
         * Indicates if a file is a GIF image file
         *
         * @param file Tested file
         * @return `true` if the file is a GIF image file
         */
        fun isGIF(file : File) = khelp.image.gif.isGIF(file)
    }

    /**
     * Visitor to collect images
     * @param delays Images delays to fill
     */
    private class InternalVisitor(val delays : ArrayInt) : DataGIFVisitor
    {
        /**Images collected*/
        private val list = ArrayList<JHelpImage>()

        /**Image width*/
        var width = 0

        /**Image height*/
        var height = 0

        /**
         * Transform collected list in array, and empty the list
         *
         * @return Array of images
         */
        fun getArray() : Array<JHelpImage>
        {
            val array = this.list.toTypedArray()
            this.list.clear()
            return array
        }

        /**
         * Called when collection of images is finished          *
         */
        override fun endCollecting() = Unit

        /**
         * Called when next image is computed
         * @param duration Image duration in millisecond
         * @param image    Image
         */
        override fun nextImage(duration : Long, image : JHelpImage)
        {
            this.delays.add(duration.toInt())
            this.list.add(image)
        }

        /**
         * Called when collect image start
         * @param width  Image width
         * @param height Image height
         */
        override fun startCollecting(width : Int, height : Int)
        {
            this.width = width
            this.height = height
        }
    }

    /**Images delays*/
    private val delays = ArrayInt()

    /**Image width*/
    val width : Int

    /**Image height*/
    val height : Int

    /**Images of animated GIF*/
    private val images : Array<JHelpImage>

    /**Previous image index*/
    private var previousIndex = 0

    /**Start animation time*/
    private var startTime = 0L

    /**Total GIF animation time*/
    val totalTime : Int

    init
    {
        var totalTime = 0
        val dataGIF = DataGIF()
        dataGIF.read(inputStream)
        val internalVisitor = InternalVisitor(this.delays)
        dataGIF.collectImages(internalVisitor)
        this.width = internalVisitor.width
        this.height = internalVisitor.height
        this.images = internalVisitor.getArray()
        val size = this.delays.size

        for (i in 0 until size)
        {
            totalTime += this.delays[i]
        }

        this.totalTime = totalTime

        if (this.images.isEmpty())
        {
            throw IOException("Failed to load GIF, no extracted image")
        }
    }

    /**
     * Compute GIF MD5
     *
     * @return GIF MD5
     * @throws NoSuchAlgorithmException If MD5 unknown
     * @throws IOException              On computing problem
     */
    @Throws(NoSuchAlgorithmException::class, IOException::class)
    fun md5() : String
    {
        val md5 = MessageDigest.getInstance("MD5")
        val numberOfImages = this.images.size
        var temp = ByteArray(4096)
        temp[0] = (numberOfImages shr 24 and 0xFF).toByte()
        temp[1] = (numberOfImages shr 16 and 0xFF).toByte()
        temp[2] = (numberOfImages shr 8 and 0xFF).toByte()
        temp[3] = (numberOfImages and 0xFF).toByte()

        md5.update(temp, 0, 4)

        var inputStream : IntegerArrayInputStream
        var bufferedImage : JHelpImage
        var pixels : IntArray
        var read : Int
        var width : Int
        var height : Int

        for (image1 in this.images)
        {
            bufferedImage = image1

            width = bufferedImage.width
            height = bufferedImage.height

            pixels = bufferedImage.pixels(0, 0, width, height, 2)
            pixels[0] = width
            pixels[1] = height

            inputStream = IntegerArrayInputStream(pixels)
            read = inputStream.read(temp)

            while (read >= 0)
            {
                md5.update(temp, 0, read)

                read = inputStream.read(temp)
            }

            inputStream.close()
        }

        temp = md5.digest()
        val stringBuffer = StringBuilder()

        for (b in temp)
        {
            read = b.toInt() and 0xFF
            stringBuffer.append(Integer.toHexString(read shr 4 and 0xF))
            stringBuffer.append(Integer.toHexString(read and 0xF))
        }

        return stringBuffer.toString()
    }

    /**
     * Obtain an image delay
     *
     * @param index Image index
     * @return Delay in millisecond
     */
    fun delay(index : Int) = this.delays[index]

    /**
     * Get a image
     *
     * @param index Image index
     * @return Desired image
     */
    fun image(index : Int) = this.images[index]

    /**
     * Get the image index suggest between last time [GIF.startAnimation] was called and time this method is called based on
     * images delays
     *
     * @return Image index since last time [GIF.startAnimation] was called
     */
    fun imageIndexFromStartAnimation() : Int
    {
        val time = System.currentTimeMillis() - this.startTime
        val max = this.images.size - 1
        val relativeTime = (time % this.totalTime).toInt()
        var index = 0
        var actualTime = 0
        var delay : Int

        while (index < max)
        {
            delay = this.delays[index]
            actualTime += delay

            if (actualTime >= relativeTime)
            {
                break
            }

            index ++
        }

        val nextIndex = (this.previousIndex + 1) % this.images.size

        if (index != this.previousIndex)
        {
            index = nextIndex
        }

        this.previousIndex = index
        return index
    }

    /**
     * Get the image suggest between last time [GIF.startAnimation] was called and time this method is called based on
     * images delays
     *
     * @return Image since last time [GIF.startAnimation] was called
     */
    fun imageFromStartAnimation() = this.images[this.imageIndexFromStartAnimation()]

    /**
     * Number of images
     *
     * @return Number of images
     */
    val numberOfImage : Int
        get() = this.images.size

    /**
     * Start/restart animation from beginning, to follow evolution, use [GIF.getImageFromStartAnimation] to have current
     * image of the animation
     */
    fun startAnimation()
    {
        this.startTime = System.currentTimeMillis()
    }
}

/**
 * Dynamic animation based on GIF image
 * @param gif Gif to draw
 * @param x Start position X
 * @param y Start position Y
 */
class DynamicAnimationGIF(val gif : GIF, private var x : Int = 0, private var y : Int = 0)
{
    /** Sprite where gif is draw*/
    private var spriteDrawGif : JHelpSprite? = null

    /**
     * Play the animation.
     *
     * @return `true` if animation continues. `false` if animation finished
     */
    fun animate() : Boolean
    {
        val img = this.spriteDrawGif?.image()

        if (img != null)
        {
            img.startDrawMode()
            img.drawImage(0, 0, this.gif.imageFromStartAnimation(), doAlphaMix = false)
            img.endDrawMode()
        }

        return true
    }

    /**
     * Terminate properly the animation
     *
     * The given image is not in draw mode **DON'T change this !**. It just here to remove properly created sprites for the
     * animation
     *
     * @param image Image parent
     */
    fun endAnimation(image : JHelpImage)
    {
        if (this.spriteDrawGif != null)
        {
            image.removeSprite(this.spriteDrawGif !!)
            this.spriteDrawGif = null
        }
    }

    /**
     * Start the animation.
     *
     * The given image is not in draw mode **DON'T change this !**. It let you opportunity to create sprites for your
     * animation
     *
     * @param image              Image parent
     */
    fun startAnimation(image : JHelpImage)
    {
        this.spriteDrawGif = image.createSprite(this.x, this.y, this.gif.width, this.gif.height)
        val img = this.spriteDrawGif?.image()

        if (img != null)
        {
            img.startDrawMode()
            img.drawImage(0, 0, this.gif.image(0), doAlphaMix = false)
            img.endDrawMode()
        }

        this.spriteDrawGif?.visible(true)
        this.gif.startAnimation()
    }
}