package khelp.engine3d.resource

import khelp.io.ClassSource
import khelp.resources.Resources
import khelp.ui.game.GameImage

object Resources3D
{
    val resources = Resources(ClassSource(Resources3D::class.java))

    fun loadImage(path:String) : GameImage =
        GameImage.Companion.load(path, this.resources)
}
