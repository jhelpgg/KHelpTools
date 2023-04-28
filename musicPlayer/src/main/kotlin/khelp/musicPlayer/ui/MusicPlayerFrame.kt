package khelp.musicPlayer.ui

import khelp.utilities.math.random
import khelp.io.ClassSource
import khelp.musicPlayer.player.MediaSeeker
import khelp.musicPlayer.player.MusicPlayer
import khelp.resources.Resources
import khelp.thread.TaskContext
import khelp.thread.parallel
import khelp.ui.dsl.borderLayout
import khelp.ui.dsl.frame
import khelp.ui.extensions.drawImage
import khelp.ui.game.GameComponent
import khelp.ui.game.GameImage
import khelp.ui.utilities.CHARACTER_ESCAPE
import khelp.ui.utilities.INVISIBLE_CURSOR
import khelp.ui.utilities.createKeyStroke
import khelp.ui.utilities.screenShot
import khelp.utilities.collections.ThrowSet
import khelp.utilities.math.square
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.min
import kotlin.math.sqrt

object MusicPlayerFrame
{
    private val launched = AtomicBoolean(false)
    private val images = ThrowSet<File>()
    private lateinit var image : GameImage

    fun launchPlayer()
    {
        val screenShot = screenShot()
        val gameComponent = GameComponent(screenShot.width, screenShot.height)
        gameComponent.cursor = INVISIBLE_CURSOR
        this.image = gameComponent.gameImage
        this.image.draw { graphics2D -> graphics2D.drawImage(screenShot, 0, 0, null) }
        val resources = Resources(ClassSource(MusicPlayerFrame::class.java))
        val resourcesText = resources.resourcesText("texts/texts")

        MediaSeeker.imagesFilesFlow.then(TaskContext.INDEPENDENT, this::imageReceived)
        MusicPlayer.play()

        frame("Music player by JHelp", decorated = false, full = true) {
            globalAction("exit", "exit", resourcesText, createKeyStroke(CHARACTER_ESCAPE)) {
                onClick(TaskContext.INDEPENDENT) { close() }
            }

            borderLayout { center(gameComponent) }

            cursor = INVISIBLE_CURSOR
        }
    }

    private fun imageReceived(imageFile : File)
    {
        synchronized(this.images) { this.images.throwIn(imageFile) }

        if (this.launched.compareAndSet(false, true))
        {
            parallel(TaskContext.INDEPENDENT, this::imageRunner)
        }
    }

    private fun imageRunner()
    {
        var hasNext = true
        val width = this.image.width
        val height = this.image.height
        Thread.sleep(16L * sqrt(square(width.toDouble()) + square(height.toDouble())).toLong())
        val imagesSeen = ThrowSet<File>()

        while (hasNext)
        {
            val file = synchronized(this.images) { this.images() }
            var wait = 128L

            try
            {

                val stream = FileInputStream(file)
                var image = GameImage.load(stream)
                stream.close()
                var imageWidth = image.width
                var imageHeight = image.height

                if (imageWidth > width || imageHeight > height)
                {
                    imageWidth = min(width, imageWidth)
                    imageHeight = min(height, imageHeight)
                    image = image.resize(imageWidth, imageHeight)
                }

                val x = random(0, width - imageWidth)
                val y = random(0, height - imageHeight)
                this.image.draw { graphics2D -> graphics2D.drawImage(x, y, image) }

                imagesSeen.throwIn(file)
                wait = sqrt(square(imageWidth.toDouble()) + square(imageHeight.toDouble())).toLong()
            }
            catch (_ : Exception)
            {
            }

            Thread.sleep(wait)

            synchronized(this.images)
            {
                if (this.images.empty)
                {
                    this.images.add(imagesSeen)
                    imagesSeen.clear()
                }

                hasNext = this.images.notEmpty
            }
        }
        this.launched.set(false)
    }
}