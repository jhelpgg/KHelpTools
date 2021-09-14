package khelp.sound.other

import khelp.sound.SoundInterface
import khelp.sound.SoundProgress
import khelp.sound.SoundState
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.thread.parallel
import java.io.File
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.Clip

class SoundOther(file : File) : SoundInterface
{
    override val totalSize : Long
        get() = this.clip.microsecondLength
    override var position : Long
        get() = this.clip.microsecondPosition
        set(value)
        {
            this.clip.microsecondPosition = value
            this.soundProgressObservableData.value(SoundProgress(value, this.totalSize))
        }
    private val soundStateObservableData = ObservableData<SoundState>(SoundState.STOPPED)
    private val soundProgressObservableData : ObservableData<SoundProgress>
    override val soundStateObservable : Observable<SoundState>
        get() = this.soundStateObservableData.observable
    override val soundProgressObservable : Observable<SoundProgress>
        get() = this.soundProgressObservableData.observable

    private val audioInputStream : AudioInputStream
    private val clip : Clip
    private var alive = false
    private var stopWithStop = true

    init
    {
        val (audioInputStream, clip) = createSound(file)
        this.audioInputStream = audioInputStream
        this.clip = clip
        this.soundProgressObservableData = ObservableData<SoundProgress>(SoundProgress(0L, this.totalSize))
    }

    override fun destroy()
    {
        if (this.soundStateObservableData.value() == SoundState.DESTROYED)
        {
            return
        }

        this.soundStateObservableData.value(SoundState.DESTROYED)
        this.alive = false
        this.clip.stop()
        this.clip.close()
        this.audioInputStream.close()
    }

    override fun play()
    {
        if (this.soundStateObservableData.value() == SoundState.DESTROYED)
        {
            return
        }

        if (! this.alive)
        {
            this.alive = true
            this.stopWithStop = true
            this.soundStateObservableData.value(SoundState.PLAYING)
            parallel { this.playTask() }
        }
    }

    override fun stop()
    {
        if (this.soundStateObservableData.value() == SoundState.DESTROYED)
        {
            return
        }

        this.stopWithStop = true
        this.alive = false
    }

    override fun pause()
    {
        if (this.soundStateObservableData.value() == SoundState.DESTROYED)
        {
            return
        }

        this.stopWithStop = false
        this.alive = false
    }

    private fun playTask()
    {
        Thread.sleep(8)
        this.clip.start()
        Thread.sleep(8)

        while (this.alive && this.clip.isRunning)
        {
            this.soundProgressObservableData.value(SoundProgress(this.position, this.totalSize))
            Thread.sleep(128)
        }

        this.clip.stop()


        if (this.alive || this.stopWithStop)
        {
            this.clip.microsecondPosition = 0
        }

        this.alive = false

        if (this.soundStateObservableData.value() != SoundState.DESTROYED)
        {
            if (this.stopWithStop)
            {
                this.soundStateObservableData.value(SoundState.STOPPED)
            }
            else
            {
                this.soundStateObservableData.value(SoundState.PAUSED)
            }
        }
    }
}
