package khelp.game.resources

import khelp.sound.Sound
import khelp.sound.SoundState
import khelp.thread.TaskContext
import khelp.thread.observable.Observer
import khelp.utilities.collections.queue.Queue

object SoundManager
{
    private const val PATH_HEADER = "sounds"
    private val backgroundQueue = Queue<BackgroundSound>()
    private var backgroundSound : Sound? = null
    private var backgroundObserver : Observer<SoundState>? = null

    fun playBackground(backgroundSound : BackgroundSound)
    {
        synchronized(this.backgroundQueue)
        {
            this.backgroundObserver?.stopObserve()
            this.backgroundQueue.clear()
            this.backgroundQueue.inQueue(backgroundSound)
            this.backgroundSound?.stop()
            this.dequeBackground()
        }
    }

    fun enqueueBackground(backgroundSound : BackgroundSound)
    {
        synchronized(this.backgroundQueue)
        {
            this.backgroundQueue.inQueue(backgroundSound)

            if (this.backgroundQueue.size == 1)
            {
                this.dequeBackground()
            }
        }
    }

    fun stopBackground()
    {
        synchronized(this.backgroundQueue)
        {
            this.backgroundObserver?.stopObserve()
            this.backgroundObserver = null
            this.backgroundQueue.clear()
            this.backgroundSound?.stop()
            this.backgroundSound = null
        }
    }

    fun playEffect(effectSound : EffectSound)
    {
        effectSound.sound.play()
    }

    private fun dequeBackground()
    {
        this.backgroundObserver?.stopObserve()

        synchronized(this.backgroundQueue)
        {
            this.backgroundSound =
                if (this.backgroundQueue.size > 1)
                {
                    this.backgroundQueue.outQueue().sound
                }
                else
                {
                    this.backgroundQueue.peek().sound
                }
        }

        this.backgroundSound?.let { sound ->
            sound.play()
            this.backgroundObserver = sound.soundStateObservable.observedBy(TaskContext.INDEPENDENT, this::soundState)
        }
    }

    private fun soundState(soundState : SoundState)
    {
        if (soundState == SoundState.STOPPED)
        {
            this.dequeBackground()
        }
    }
}
