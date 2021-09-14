package khelp.sound

import khelp.thread.observable.Observable

/**
 * A sound.
 *
 * Sound position represents a part of the sound, not a duration.
 *
 * Each part have same duration, so if want reach middle of sound, use middle position, ...
 */
internal interface SoundInterface
{
    val totalSize : Long
    var position : Long
    val soundStateObservable : Observable<SoundState>
    val soundProgressObservable : Observable<SoundProgress>

    /**
     * Destroy properly the sound.
     *
     * Free memory and thread associated to the sound playing.
     *
     * Can't use the sound after this call
     */
    fun destroy()

    /**
     * Play the sound.
     *
     * Launch the playing and return immediately.
     */
    fun play()

    /**
     * Stop the sound
     */
    fun stop()

    fun pause()
}