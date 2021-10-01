package khelp.engine3d.sound3d

import khelp.engine3d.animation.NodePosition
import khelp.engine3d.geometry.Point3D
import khelp.engine3d.render.Node
import khelp.thread.TaskContext
import khelp.thread.delay
import khelp.thread.future.FutureResult
import khelp.thread.observable.Observer
import khelp.utilities.collections.queue.Queue
import khelp.utilities.log.debug
import org.lwjgl.openal.AL10

/**
 * A source of sound. It is an origin of a sound.
 *
 * A sound source can be place in 3D or attach to a [Node].
 *
 * The 3D effect work only for MONO sound (See OpenAL documentation).
 *
 * STEREO sounds will ignore the 3D position.
 *
 * To create one instance use [SoundManager.createSource].
 */
open class SoundSource internal constructor()
{
    /**Sound queue to play*/
    private val soundQueue = Queue<Sound>()

    /**Open AL source ID*/
    private var source = AL10.alGenSources()

    /**Open AL buffer ID*/
    private var buffer = - 1
    private var internalPosition = Point3D()
    private var linkedNode : Node? = null
    private var linkedNodePositionObserver : Observer<NodePosition>? = null
    private var finishedFuture : FutureResult<Unit>? = null
    private var playing = false

    var position : Point3D
        get() = this.linkedNode?.let { node -> Point3D(node.x, node.y, node.z) } ?: this.internalPosition
        set(value)
        {
            this.internalPosition = value

            if (this.linkedNode == null)
            {
                AL10.alSource3f(this.source, AL10.AL_POSITION,
                                this.internalPosition.x, this.internalPosition.y, this.internalPosition.z)
            }
        }

    init
    {
        AL10.alSource3f(this.source, AL10.AL_POSITION,
                        this.internalPosition.x, this.internalPosition.y, this.internalPosition.z)
    }

    /**
     * Enqueue a sound.
     *
     * If their not sound in waiting queue and no sound is currently playing, the given sound is playing immediately and becomes the current one.
     *
     * In other cases, the sound is just put in queue and wait its turn.
     *
     * @param sound Sound to enqueue.
     */
    fun enqueue(sound : Sound)
    {
        debug("source=",this.source)
        if (this.source < 0)
        {
            return
        }

        debug("playing=", this.playing)

        if (! this.playing)
        {
            this.play(sound)
            return
        }

        synchronized(this.soundQueue)
        {
            this.soundQueue.inQueue(sound)
        }
    }

    /**
     * Play a sound immediately on stopping current one if needed
     *
     * @param sound Sound to play now
     */
    fun playSound(sound : Sound)
    {
        debug("source=",this.source)
        this.play(sound)
    }

    /**
     * Stop current sound (If their one) and clear the sound queue
     */
    fun clearSounds()
    {
        debug("source=",this.source)
        this.finishedFuture?.cancel("Clear sounds")
        this.finishedFuture = null

        synchronized(this.soundQueue)
        {
            this.soundQueue.clear()
        }

        if (this.source < 0)
        {
            return
        }

        if (this.buffer >= 0)
        {
            AL10.alSourceStop(this.source)
            AL10.alDeleteBuffers(this.buffer)
            this.buffer = - 1
        }
    }

    /**
     * Link the source to given node.
     * That is to say the sound will take the position where the node is and each time the node move, the sound will move too.
     *
     * To free the source node call [unLink]
     *
     * @param node Node to follow
     */
    fun link(node : Node)
    {
        this.linkedNodePositionObserver?.stopObserve()
        this.linkedNode = node
        this.linkedNodePositionObserver =
            node.nodePositionObservable.observedBy(TaskContext.INDEPENDENT) { nodeLink ->
                AL10.alSource3f(this.source, AL10.AL_POSITION,
                                nodeLink.x, nodeLink.y, nodeLink.z)
            }
    }

    /**
     * Remove node following constraints
     */
    fun unLink()
    {
        this.linkedNodePositionObserver?.stopObserve()
        this.linkedNode = null
        AL10.alSource3f(this.source, AL10.AL_POSITION,
                        this.internalPosition.x, this.internalPosition.y, this.internalPosition.z)
    }


    /**
     * Play a sound immediately on stopping current one if need
     *
     * @param sound Sound to play now
     */
    internal open fun play(sound : Sound)
    {
        debug("source=",this.source)
        this.playing = true

        this.finishedFuture?.cancel("play")
        this.finishedFuture = null

        if (this.source < 0)
        {
            return
        }

        if (this.buffer >= 0)
        {
            debug("Clear previous")
            AL10.alSourceStop(this.source)
            AL10.alDeleteBuffers(this.buffer)
        }

        debug("New buffer")
        this.buffer = AL10.alGenBuffers()
        sound.transferToBuffer(this.buffer)
        AL10.alSourcei(this.source, AL10.AL_BUFFER, this.buffer)
        AL10.alSourcePlay(this.source)
        this.finishedFuture = delay(sound.duration()
                                        .toInt()) { this.soundFinished() }
    }

    /**
     * Stop all sounds and free memory.
     *
     * Not reuse the sound source after that call
     */
    internal open fun stopAll()
    {
        debug("source=",this.source)
        this.finishedFuture?.cancel("play")
        this.finishedFuture = null

        if (this.source < 0)
        {
            return
        }

        if (this.buffer >= 0)
        {
            AL10.alSourceStop(this.source)
            AL10.alDeleteBuffers(this.buffer)
            this.buffer = - 1
        }

        AL10.alDeleteSources(this.source)
        this.source = - 1
    }

    /**
     * Called when current sound finished
     */
    private fun soundFinished()
    {
        debug("source=",this.source)
        this.playing = false

        synchronized(this.soundQueue)
        {
            if (this.soundQueue.empty)
            {
                if (this.source >= 0 && this.buffer >= 0)
                {
                    debug("buffer clear")
                    AL10.alSourceStop(this.source)
                    AL10.alDeleteBuffers(this.buffer)
                    this.buffer = - 1
                }
            }
            else
            {
                this.play(this.soundQueue.outQueue())
            }
        }
    }
}
