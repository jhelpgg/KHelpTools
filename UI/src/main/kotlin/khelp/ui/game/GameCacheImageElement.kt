package khelp.ui.game

import khelp.resources.Resources
import khelp.resources.defaultResources
import java.lang.ref.WeakReference
import java.util.Objects

class GameCacheImageElement(private val keyImage : String, private val width : Int, private val height : Int,
                            private val resources : Resources)
    : Comparable<GameCacheImageElement>
{
    companion object
    {
        fun computeKey(keyImage : String,  width : Int,  height : Int, resources : Resources) : String =
            "$keyImage ${width}x$height : $resources"

        private fun load(path : String, resources : Resources, imageWidth : Int, imageHeight : Int) : GameImage
        {
            val inputStream =
                when
                {
                    resources.exists(path)        -> resources.inputStream(path)
                    defaultResources.exists(path) -> defaultResources.inputStream(path)
                    else                          -> return GameImage.DUMMY
                }

            val image =
                if (imageWidth > 0 && imageHeight > 0)
                {
                    GameImage.loadThumbnail(inputStream, imageWidth, imageHeight)
                }
                else
                {
                    GameImage.load(inputStream)
                }

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

    private var cachedImage : WeakReference<GameImage>? = null

    fun image() : GameImage
    {
        var image = this.cachedImage?.get()

        if (image != null)
        {
            return image
        }

        image = GameCacheImageElement.load(this.keyImage, this.resources, this.width, this.height)
        this.cachedImage = WeakReference(image)
        return image
    }

    override fun equals(other : Any?) : Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is GameCacheImageElement)
        {
            return false
        }

        return this.keyImage == other.keyImage && this.width == other.width && this.height == other.height && this.resources == other.resources
    }

    override fun hashCode() : Int = Objects.hash(this.keyImage, this.width, this.height, this.resources)

    override fun compareTo(other : GameCacheImageElement) : Int
    {
        var comparison = this.keyImage.compareTo(other.keyImage)

        if (comparison != 0)
        {
            return comparison
        }

        comparison = this.width - other.width

        if (comparison != 0)
        {
            return comparison
        }

        comparison = this.height - other.height

        if (comparison != 0)
        {
            return comparison
        }

        return this.resources.hashCode() - other.resources.hashCode()
    }
}