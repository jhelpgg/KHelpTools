package khelp.musicPlayer.player

import khelp.sound.Sound
import khelp.sound.SoundState
import khelp.sound.soundFromFile
import khelp.thread.Locker
import khelp.thread.TaskContext
import khelp.thread.observable.Observer
import khelp.thread.parallel
import khelp.utilities.collections.ThrowSet
import khelp.utilities.log.debug
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

object MusicPlayer
{
    private const val MINIMUM_TIME = 1024L
    private val alive = AtomicBoolean(false)
    private val waiting = AtomicBoolean(false)
    private val lock = Object()
    private val soundsToPlay = ThrowSet<File>()
    private val soundsPlayed = ThrowSet<File>()
    private var sound : Sound? = null
    private var soundObserver : Observer<SoundState>? = null

    init
    {
        MediaSeeker.musicFilesFlow.then(TaskContext.INDEPENDENT) { file -> this.fileReceived(file) }
        MediaSeeker.startSeek()
    }

    fun play()
    {
        this.sound?.play()

        if (this.alive.compareAndSet(false, true))
        {
            parallel { this.playNext() }
        }
    }

    fun pause()
    {
        this.sound?.pause()
    }

    private fun fileReceived(file : File)
    {
        synchronized(this.soundsToPlay) {
            this.soundsToPlay.throwIn(file)
        }

        synchronized(this.lock) {
            if (this.waiting.get())
            {
                this.lock.notify()
            }
        }
    }

    private fun playNext()
    {
        while (true)
        {
            val locker = Locker()
            var file : File? = null

            synchronized(this.soundsToPlay) {
                if (this.soundsToPlay.empty)
                {
                    this.soundsToPlay.add(this.soundsPlayed)
                    this.soundsPlayed.clear()
                }

                if (this.soundsToPlay.notEmpty)
                {
                    file = this.soundsToPlay()
                }
            }

            if (file == null)
            {
                synchronized(this.lock) {
                    this.waiting.set(true)
                    this.lock.wait()
                    this.waiting.set(false)
                }

                Thread.sleep(512)
                continue
            }

            val time = System.currentTimeMillis()
            debug(file !!.absolutePath, " | to play=", this.soundsToPlay.size, " | played=", this.soundsPlayed.size)
            var onError = false

            try
            {
                val sound = soundFromFile(file !!)
                this.sound = sound
                sound.destroyOnEnd = true
                this.soundObserver = sound.soundStateObservable
                    .observedBy(TaskContext.INDEPENDENT)
                    { state ->
                        when (state)
                        {
                            SoundState.ERROR     -> onError = true
                            SoundState.DESTROYED -> locker.unlock()
                        }
                    }
                sound.play()
            }
            catch (_ : Exception)
            {
                onError = true
                this.soundObserver?.stopObserve()
                locker.unlock()
            }

            locker.lock()
            this.soundObserver?.stopObserve()
            val toWait = MusicPlayer.MINIMUM_TIME - (System.currentTimeMillis() - time)

            if (toWait > 0L)
            {
                Thread.sleep(toWait)
            }

            this.sound?.destroy()

            if (! onError)
            {
                this.soundsPlayed.throwIn(file !!)
            }
        }
    }
}
