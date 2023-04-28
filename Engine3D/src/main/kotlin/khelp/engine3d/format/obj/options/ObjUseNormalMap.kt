package khelp.engine3d.format.obj.options

import khelp.resources.Resources
import khelp.ui.game.GameImage

class ObjUseNormalMap(private val imagePath : String, private val resources : Resources) : ObjOption
{
    val normalMap : NormalMap by lazy {
        val image = GameImage.load(this.imagePath, this.resources)
        NormalMap(image.width, image.height, image.grabPixels())
    }
}
