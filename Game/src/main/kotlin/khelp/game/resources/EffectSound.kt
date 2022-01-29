package khelp.game.resources

import khelp.sound.Sound
import khelp.sound.SoundCache

enum class EffectSound(private val path : String)
{
    ELECTRICITY_SPARK_SWORD_ATTACK_01("sounds/effect/ElectricitySparkSword_Attack_01.wav")
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