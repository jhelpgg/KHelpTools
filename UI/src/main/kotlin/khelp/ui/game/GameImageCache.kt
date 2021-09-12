package khelp.ui.game

import khelp.resources.Resources

internal object GameImageCache
{
    private val cache = HashMap<String, GameCacheImageElement>()

    fun image(keyImage : String, width : Int, height : Int, resources : Resources) : GameImage
    {
        val key = GameCacheImageElement.computeKey(keyImage, width, height, resources)
        return this.cache
            .getOrPut(key) { GameCacheImageElement(keyImage, width, height, resources) }
            .image()
    }
}
