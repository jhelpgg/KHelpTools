package khelp.game.resources

import khelp.io.ClassSource
import khelp.preferences.Preferences
import khelp.resources.Resources
import khelp.resources.ResourcesText

object GameResources
{
    val resources : Resources = Resources(ClassSource(GameResources::class.java))
    val resourcesText : ResourcesText = resources.resourcesText("texts/texts")
    val preferences : Preferences = Preferences()
}