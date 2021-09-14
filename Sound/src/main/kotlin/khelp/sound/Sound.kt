package khelp.sound

import khelp.thread.TaskContext
import khelp.thread.observable.Observable

class Sound internal constructor(private val soundInterface : SoundInterface)
{
    val soundStateObservable : Observable<SoundState> = this.soundInterface.soundStateObservable
    val soundProgressObservable : Observable<SoundProgress> = this.soundInterface.soundProgressObservable
    val totalSize : Long = this.soundInterface.totalSize
    var position : Long
        get() = this.soundInterface.position
        set(value)
        {
            this.soundInterface.position = value
        }
    private val observerState = this.soundStateObservable.observedBy(TaskContext.INDEPENDENT, this::soundSateChanged)
    var destroyOnEnd = false
    private var loop = - 1

    fun play()
    {
        this.soundInterface.play()
    }

    fun loop(loop : Int = Int.MAX_VALUE)
    {
        this.loop = loop
        this.play()
    }

    fun pause()
    {
        this.soundInterface.pause()
    }

    fun stop()
    {
        this.loop = - 1
        this.soundInterface.stop()
    }

    fun destroy()
    {
        this.soundInterface.destroy()
        this.observerState.stopObserve()
    }

    private fun soundSateChanged(soundState : SoundState)
    {
        when (soundState)
        {
            SoundState.PAUSED, SoundState.DESTROYED, SoundState.PLAYING -> return
            SoundState.STOPPED                                          -> Unit
        }

        if (this.loop > 0)
        {
            this.loop --
            this.play()
            return
        }

        if (this.destroyOnEnd)
        {
            this.destroy()
        }
    }
}
