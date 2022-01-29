package khelp.match3.ui

import khelp.io.ClassSource
import khelp.resources.Resources
import khelp.resources.ResourcesText
import khelp.sound.Sound
import khelp.sound.SoundCache
import khelp.ui.font.JHelpFont
import khelp.ui.game.GameImage

private class GameResources

val gameResources : Resources = Resources(ClassSource(GameResources::class.java))

val gameTexts : ResourcesText = gameResources.resourcesText("texts/texts")

val POINTS_FONT = JHelpFont("Courier", 32, bold = true)

fun obtainImage(imageName : String) : GameImage = GameImage.load("images/$imageName", gameResources)

fun obtainSound(soundName : String) : Sound
{
    var sound = SoundCache.sound(soundName)

    if (sound == null)
    {
        SoundCache.store(soundName, "sounds/$soundName", gameResources)
        sound = SoundCache.sound(soundName)
    }

    return sound !!
}
