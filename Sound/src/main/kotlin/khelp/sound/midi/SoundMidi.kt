package khelp.sound.midi

import khelp.sound.SoundException
import khelp.sound.SoundInterface
import khelp.sound.SoundProgress
import khelp.sound.SoundState
import khelp.thread.delay
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.thread.parallel
import khelp.utilities.extensions.bounds
import java.io.File
import javax.sound.midi.MidiSystem
import javax.sound.midi.Sequencer

class SoundMidi(file : File) : SoundInterface
{
    private val sequencer : Sequencer

    override val totalSize : Long
        get() = this.sequencer.microsecondLength
    override var position : Long
        get() = this.sequencer.microsecondPosition
        set(value)
        {
            val position = value.bounds(0L, this.totalSize)
            this.sequencer.microsecondPosition = position
            this.soundProgressObservableData.value(SoundProgress(position, this.totalSize))
        }

    private val soundStateObservableData = ObservableData<SoundState>(SoundState.NOT_LAUNCHED)
    private val soundProgressObservableData : ObservableData<SoundProgress>

    override val soundStateObservable : Observable<SoundState>
        get() = this.soundStateObservableData.observable
    override val soundProgressObservable : Observable<SoundProgress>
        get() = this.soundProgressObservableData.observable

    private var alive = false
    private val lock = Object()

    init
    {
        try
        {
            this.sequencer = MidiSystem.getSequencer()
            this.sequencer.sequence = MidiSystem.getSequence(file)
            this.sequencer.open()
            this.soundProgressObservableData = ObservableData<SoundProgress>(SoundProgress(0L, this.totalSize))
        }
        catch (exception : Exception)
        {
            throw SoundException("Failed to create sequencer", exception)
        }
    }

    override fun destroy()
    {
        if(this.soundStateObservableData.value() == SoundState.DESTROYED) {
            return
        }

        synchronized(this.lock)
        {
            this.alive = false
        }

        this.soundStateObservableData.value(SoundState.DESTROYED)
        this.sequencer.stop()
        this.sequencer.close()
    }

    override fun play()
    {
        if(this.soundStateObservableData.value() == SoundState.DESTROYED) {
            return
        }

        this.sequencer.start()

        synchronized(this.lock)
        {
            if (! this.alive)
            {
                this.alive = true
                this.soundStateObservableData.value(SoundState.PLAYING)
                parallel { this.waitEndRunningTask() }
            }
        }
    }

    override fun stop()
    {
        if(this.soundStateObservableData.value() == SoundState.DESTROYED) {
            return
        }

        this.sequencer.stop()
        this.position = 0L
    }

    override fun pause()
    {
        if(this.soundStateObservableData.value() == SoundState.DESTROYED) {
            return
        }

        this.soundStateObservableData.value(SoundState.PAUSED)
        this.sequencer.stop()
    }

    private fun waitEndRunningTask()
    {
        this.soundProgressObservableData.value(SoundProgress(this.position, this.totalSize))

        if (this.sequencer.isRunning)
        {
            delay(128) { this.waitEndRunningTask() }
            return
        }

        synchronized(this.lock)
        {
            this.alive = false
        }

        if (this.soundStateObservableData.value() == SoundState.PLAYING)
        {
            this.soundStateObservableData.value(SoundState.STOPPED)
        }
    }
}
