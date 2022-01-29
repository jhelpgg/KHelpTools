package khelp.sound.mp3

import javazoom.jl.player.Player
import khelp.sound.SoundException
import khelp.sound.SoundInterface
import khelp.sound.SoundProgress
import khelp.sound.SoundState
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.thread.parallel
import khelp.utilities.log.exception
import java.io.File

internal class SoundMP3(file : File) : SoundInterface
{
    private val controlInputStream = ControlInputStream(file)
    private val soundStateObservableData = ObservableData<SoundState>(SoundState.NOT_LAUNCHED)
    private var alive = false
    private val lock = Object()
    private var player : Player? = null
    private var resumePosition = 0L

    override val totalSize : Long = this.controlInputStream.size
    override var position : Long
        get() = this.controlInputStream.position
        set(value)
        {
            this.controlInputStream.position = value
        }
    override val soundStateObservable get() : Observable<SoundState> = this.soundStateObservableData.observable
    override val soundProgressObservable get() : Observable<SoundProgress> = this.controlInputStream.progressObservable

    override fun destroy()
    {
        if (this.soundStateObservableData.value() == SoundState.DESTROYED)
        {
            return
        }

        this.controlInputStream.close()
        this.player?.close()
        this.player = null

        synchronized(this.lock)
        {
            this.alive = false
        }

        this.soundStateObservableData.value(SoundState.DESTROYED)
    }

    override fun play()
    {
        if (this.soundStateObservableData.value() == SoundState.DESTROYED)
        {
            return
        }

        synchronized(this.lock)
        {
            if (this.alive)
            {
                this.controlInputStream.pause = false
                this.soundStateObservableData.value(SoundState.PLAYING)
            }
            else
            {
                this.alive = true

                try
                {
                    this.controlInputStream.resetAt0()
                    this.position = this.resumePosition
                    this.player = Player(this.controlInputStream)
                }
                catch (exception : Exception)
                {
                    throw SoundException("Playing start failed", exception)
                }

                this.soundStateObservableData.value(SoundState.PLAYING)
                parallel { this.taskPlayTheSound() }
            }
        }
    }

    override fun stop()
    {
        if (this.soundStateObservableData.value() == SoundState.DESTROYED)
        {
            return
        }

        this.resumePosition = 0L
        this.playEnd()
    }

    override fun pause()
    {
        if (this.soundStateObservableData.value() == SoundState.DESTROYED)
        {
            return
        }

        this.resumePosition = this.position
        this.controlInputStream.pause = true
        this.soundStateObservableData.value(SoundState.PAUSED)
        this.playEnd()
    }

    private fun taskPlayTheSound()
    {
        try
        {
            this.player?.play()
        }
        catch (exception : Exception)
        {
            this.soundStateObservableData.value(SoundState.ERROR)
            exception(exception)
            this.controlInputStream.pause = true
            this.destroy()
            return
        }

        this.playEnd()
    }

    private fun playEnd()
    {
        synchronized(this.lock)
        {
            this.alive = false
        }

        this.player?.close()
        this.player = null

        if (! this.controlInputStream.pause)
        {
            this.resumePosition = 0L
            this.soundStateObservableData.value(SoundState.STOPPED)
        }
    }
}
