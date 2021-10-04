package khelp.engine3d.resource

import khelp.engine3d.render.Texture
import khelp.ui.game.GameImage
import khelp.utilities.log.exception
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

object TextureCache
{
    private val cacheTexture = HashMap<String, Texture>()
    private val cacheFromFile = HashMap<String, Texture>()
    var resources = Resources3D.resources

    operator fun get(path : String) : Texture =
        this.cacheTexture.getOrPut(path) { Texture(GameImage.load(path, this.resources)) }

    operator fun get(file : File) : Texture
    {
        val key = file.absolutePath
        var texture = this.cacheFromFile[key]

        if (texture != null)
        {
            return texture
        }

        var inputStream : InputStream? = null
        var image : GameImage

        try
        {
            inputStream = FileInputStream(file)
            image = GameImage.load(inputStream)
        }
        catch (exception : Exception)
        {
            exception(exception, "Failed to load : ", key)
            image = GameImage.DUMMY
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close()
                }
                catch (_ : Exception)
                {
                }
            }
        }

        texture = Texture(image)
        this.cacheFromFile[key] = texture
        return texture
    }
}