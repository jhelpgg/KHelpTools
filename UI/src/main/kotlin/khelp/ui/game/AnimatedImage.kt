package khelp.ui.game

import khelp.thread.parallel
import khelp.ui.extensions.drawImage
import khelp.ui.utilities.TRANSPARENT
import khelp.utilities.extensions.bounds
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max

class AnimatedImage(width : Int, height : Int, firstImage : GameImage)
{
    private val images = ArrayList<Pair<Long, GameImage>>()
    private val alive = AtomicBoolean(false)
    private val waiting = AtomicBoolean(false)
    private val lock = Object()
    private var loop = false

    val width : Int = width.bounds(16, 1024)
    val height : Int = height.bounds(16, 1024)
    val imageAnimated : GameImage = GameImage(this.width, this.height)
    var timeBeforeLoopMilliseconds : Long = 40L
        set(value)
        {
            field = max(1L, value)
        }
    val playing : Boolean get() = this.alive.get()
    var actionAtEnd : () -> Unit = {}

    init
    {
        this.images.add(Pair(0L, firstImage.resize(this.width, this.height)))
        this.imageAnimated.draw { graphics2D -> graphics2D.drawImage(0, 0, firstImage) }
    }

    fun appendImage(timeLapsMilliseconds : Long, image : GameImage)
    {
        synchronized(this.images)
        {
            val time = this.images[this.images.size - 1].first + max(1L, timeLapsMilliseconds)
            this.images.add(Pair(time, image.resize(this.width, this.height)))

            synchronized(this.lock)
            {
                if (this.waiting.get())
                {
                    this.lock.notify()
                }
            }
        }
    }

    fun play(loop : Boolean = true)
    {
        this.loop = loop

        synchronized(this.lock)
        {
            if (this.waiting.get())
            {
                this.alive.set(true)
            }
            else if (this.alive.compareAndSet(false, true))
            {
                parallel { this.playing() }
            }
        }
    }

    fun stop()
    {
        synchronized(this.lock)
        {
            this.alive.set(false)

            if (this.waiting.get())
            {
                this.lock.notify()
            }
        }
    }

    private fun playing()
    {
        var startTime = System.currentTimeMillis()
        var lastIndex = 0

        while (this.alive.get())
        {
            val time = System.currentTimeMillis() - startTime
            var image = this.images[0].second
            var lastTime = 0L
            var last = false
            var currentIndex = 0

            synchronized(this.images)
            {
                for (index in 1 until this.images.size)
                {
                    val (imageTime, imageTested) = this.images[index]

                    if (imageTime <= time)
                    {
                        image = imageTested
                        lastTime = imageTime
                        currentIndex = index
                        last = index == this.images.size - 1
                    }
                    else
                    {
                        break
                    }
                }
            }

            if (this.loop && last && time >= lastTime + this.timeBeforeLoopMilliseconds)
            {
                currentIndex = 0
                image = this.images[0].second
                startTime = System.currentTimeMillis()
            }

            if (lastIndex != currentIndex)
            {
                lastIndex = currentIndex
                this.imageAnimated.clear(TRANSPARENT)
                this.imageAnimated.draw { graphics2D -> graphics2D.drawImage(0, 0, image) }
            }

            if (last && ! this.loop)
            {
                this.alive.set(false)
            }

            if (this.alive.get())
            {
                synchronized(this.lock)
                {
                    this.waiting.set(true)
                    var timeToWait = 16384L

                    synchronized(this.images)
                    {
                        if (this.images.size > 1)
                        {
                            if (currentIndex == this.images.size - 1)
                            {
                                timeToWait = this.images[currentIndex].first + this.timeBeforeLoopMilliseconds - time
                            }
                            else
                            {
                                timeToWait = this.images[currentIndex ++].first - time
                            }
                        }
                    }

                    this.lock.wait(max(1L, timeToWait))
                    this.waiting.set(false)
                }
            }
        }

        this.actionAtEnd()
    }
}