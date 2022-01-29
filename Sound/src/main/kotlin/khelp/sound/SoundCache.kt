package khelp.sound

import khelp.resources.Resources
import khelp.thread.Mutex
import khelp.thread.TaskContext
import java.io.File
import java.net.URL

object SoundCache
{
    private val cache = HashMap<String, SoundCacheElement>()
    private val mutex = Mutex()

    fun store(key : String, file : File)
    {
        this.store(key, SoundFromFile(file))
    }

    fun store(key : String, name : String, resources : Resources)
    {
        this.store(key, SoundFromResources(name, resources))
    }

    fun store(key : String, url : URL)
    {
        this.store(key, SoundFromUrl(url))
    }

    fun store(key : String, soundSource : SoundSource)
    {
        this.mutex {
            this.removeInternal(key)
            this.cache[key] = SoundCacheElement(soundSource)
        }
    }

    fun sound(key : String) : Sound?
    {
        var sound : Sound? = null

        this.mutex {
            val soundCacheElement = this.cache[key] ?: return@mutex
            sound = soundCacheElement.sound

            if (sound == null)
            {
                sound = soundCacheElement.soundSource.sound
                soundCacheElement.sound = sound
                soundCacheElement.stateObserver =
                    sound?.soundStateObservable
                        ?.observedBy(TaskContext.INDEPENDENT)
                        { soundState ->
                            if (soundState == SoundState.DESTROYED)
                            {
                                this.remove(key)
                            }
                        }
            }
        }

        return sound
    }


    fun remove(key : String)
    {
        this.mutex { this.removeInternal(key) }
    }

    /**
     * Must be called inside mutex critical section
     */
    private fun removeInternal(key : String)
    {
        this.cache.remove(key)
            ?.let { soundCacheElement ->
                soundCacheElement.stateObserver?.stopObserve()
                soundCacheElement.sound?.destroy()
                soundCacheElement.stateObserver = null
                soundCacheElement.sound = null
            }
    }
}
