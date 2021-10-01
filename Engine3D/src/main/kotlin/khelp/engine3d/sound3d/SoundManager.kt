package khelp.engine3d.sound3d

import khelp.engine3d.geometry.Point3D
import khelp.engine3d.utils.ThreadOpenGL
import khelp.utilities.extensions.bounds
import org.lwjgl.openal.AL
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC10

/**
 * Manager of sounds.
 *
 * To have instance use [Window3D.soundManager].
 */
class SoundManager internal constructor()
{
    companion object
    {
        /**
         * Position in Z for sound level 0
         */
        private val LEVEL_ZERO_FAR = - 10f
    }

    /**Indicates if manager still alive*/
    var alive = true
        private set

    /**Current managed sound sources*/
    private val sources = ArrayList<SoundSource>()

    /**Current sound device ID*/
    private val device = ALC10.alcOpenDevice(null as CharSequence?)

    /**Open AL context to use*/
    private val context = ALC10.alcCreateContext(this.device, null as IntArray?)

    /**Background sound source*/
    private val sourceBackground : SoundSource

    init
    {
        ALC10.alcMakeContextCurrent(this.context)

        val alcCapabilities = ALC.createCapabilities(this.device)
        // The alCapabilities creation is mandatory
        // It should look do:
        //     AL.createCapabilities(alcCapabilities)
        // Is enough, but without the affectation it seems cause trouble to Kotlin in this specific case
        val alCapabilities = AL.createCapabilities(alcCapabilities)

        this.sourceBackground = SoundSource()
    }

    /**
     * Create a sound source
     *
     * @return Created source
     */
    fun createSource() : SoundSource
    {
        if (! this.alive)
        {
            return DummySoundSource
        }

        synchronized(this.sources)
        {
            val soundSource = SoundSource()
            this.sources.add(soundSource)
            return soundSource
        }
    }

    /**
     * Destroy the sound manager
     *
     * Don't call this method manually, it will be called by the system.
     *
     * If this rules is not respected, JVM crashes may occur
     */
    @ThreadOpenGL
    internal fun destroy()
    {
        if (! this.alive)
        {
            return
        }

        this.alive = false
        this.sourceBackground.stopAll()

        synchronized(this.sources)
        {
            this.sources.forEach { it.stopAll() }
            this.sources.clear()
        }

        ALC10.alcMakeContextCurrent(0)
        ALC10.alcDestroyContext(this.context)
        ALC10.alcCloseDevice(this.device)
    }

    /**
     * Destroy a sound source
     */
    fun destroySource(soundSource : SoundSource)
    {
        soundSource.stopAll()

        synchronized(this.sources)
        {
            this.sources.remove(soundSource)
        }
    }

    /**
     * Enqueue sound in background
     *
     * If their not sound in waiting queue and no sound is currently playing, the given sound is playing immediately and becomes the current one.
     *
     * In other cases, the sound is just put in queue and wait its turn.
     * @param sound Sound to enqueue
     */
    fun enqueueBackground(sound : Sound)
    {
        if (! this.alive)
        {
            return
        }

        this.sourceBackground.enqueue(sound)
    }

    /**
     * Play a sound in background immediately on stopping current one if need
     *
     * @param sound Sound to play
     */
    fun playBackground(sound : Sound)
    {
        if (! this.alive)
        {
            return
        }

        this.sourceBackground.playSound(sound)
    }

    /**
     * Background sound level in [0, 1] (0 no sound, 1 maximum level)
     *
     * @return Background sound level
     */
    fun backgroundLevel() : Float
    {
        val position = this.sourceBackground.position
        return 1f - position.z / SoundManager.LEVEL_ZERO_FAR
    }

    /**
     * Change background sound level.
     * The sound level is only applied on MONO sounds, due OpenAL restriction. STEREO sounds will ignore this constraints
     *
     * @param level New sound level in [0, 1] (0 no sound, 1 maximum level)
     */
    fun backgroundLevel(level : Float)
    {
        this.sourceBackground.position = Point3D(0f, 0f, LEVEL_ZERO_FAR * (1f - level.bounds(0f, 1f)))
    }

    /**
     * Trick for ensure sound manager creation
     */
    internal fun init() = Unit
}