package khelp.engine3d.sound3d

/**
 * Dummy sound source that plays nothing
 */
object DummySoundSource : SoundSource()
{
    override fun play(sound : Sound) = Unit

    override fun stopAll() = Unit
}