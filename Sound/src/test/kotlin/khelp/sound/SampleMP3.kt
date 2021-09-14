package khelp.sound

import khelp.io.ClassSource
import khelp.resources.Resources
import khelp.thread.Locker
import khelp.thread.TaskContext
import khelp.thread.delay
import khelp.utilities.log.debug


class SampleMP3

fun main(args : Array<String>)
{
    var paused = false
    val locker = Locker()
    val resources = Resources(ClassSource(SampleMP3::class.java))
    val sound = soundFromResource("Aly.mp3", resources)
    sound.soundStateObservable.observedBy(TaskContext.INDEPENDENT) { soundState ->
        debug("State=", soundState)

        if (soundState == SoundState.DESTROYED)
        {
            locker.unlock()
        }
    }
    sound.soundProgressObservable
        .observedBy(TaskContext.INDEPENDENT)
        { (progress, total) ->
            debug(progress, "/", total)

            if(!paused && progress>total/2) {
                paused = true
                sound.pause()
                Thread.sleep(1024)
                sound.play()
            }
        }
    sound.destroyOnEnd = true
    sound.loop(3)
    locker.lock()
}