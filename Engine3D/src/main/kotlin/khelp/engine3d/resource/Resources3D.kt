package khelp.engine3d.resource

import khelp.io.ClassSource
import khelp.preferences.Preferences
import khelp.resources.Resources
import khelp.resources.ResourcesText
import khelp.ui.game.GameImage

object Resources3D
{
    val resources = Resources(ClassSource(Resources3D::class.java))
    val resourcesText : ResourcesText = this.resources.resourcesText("texts/engineTexts")
    val preferences : Preferences = Preferences()

    fun loadImage(path : String) : GameImage =
        GameImage.load(path, this.resources)
}
