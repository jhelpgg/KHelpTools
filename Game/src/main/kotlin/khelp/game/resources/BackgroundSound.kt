package khelp.game.resources

import khelp.sound.Sound
import khelp.sound.SoundCache

enum class BackgroundSound(private val path : String)
{
    BATTLE_01("sounds/background/Battle01.mid")
    ;

    val sound : Sound
        get()
        {
            val sound = SoundCache.sound(this.path)

            if (sound != null)
            {
                return sound
            }

            SoundCache.store(this.path, this.path, GameResources.resources)
            return SoundCache.sound(this.path) !!
        }
}