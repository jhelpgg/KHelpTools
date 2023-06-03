package khelp.engine3d.resource

import khelp.io.ClassSource
import khelp.resources.Resources
import khelp.resources.ResourcesText
import khelp.ui.game.GameImage

object Resources3D
{
    val resources = Resources(ClassSource(Resources3D::class.java))
    val resourcesText : ResourcesText = this.resources.resourcesText("texts/engineTexts")

    fun loadImage(path : String) : GameImage =
        GameImage.load(path, this.resources)
}
