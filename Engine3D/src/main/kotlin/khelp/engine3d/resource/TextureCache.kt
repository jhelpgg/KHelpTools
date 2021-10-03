package khelp.engine3d.resource

import khelp.engine3d.render.Texture
import khelp.ui.game.GameImage

object TextureCache
{
    private val cacheTexture = HashMap<String, Texture>()
    var resources = Resources3D.resources

    operator fun get(path : String) : Texture =
        this.cacheTexture.getOrPut(path) { Texture(GameImage.load(path, this.resources)) }
}