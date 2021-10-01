package khelp.engine3d.resource

import khelp.engine3d.sound3d.Sound
import khelp.engine3d.sound3d.createSoundMP3
import khelp.engine3d.sound3d.createSoundWav
import khelp.resources.Resources

enum class Sounds(private val path : String, private val soundCreator : (String, Resources) -> Sound)
{
    ALYA("sounds/Alya.mp3", ::createSoundMP3),
    KUMA("sounds/Kuma.mp3", ::createSoundMP3),
    SUCCEED("sounds/succeed.wav", ::createSoundWav)
    ;

    val sound : Sound by lazy { this.soundCreator(this.path, Resources3D.resources) }
}