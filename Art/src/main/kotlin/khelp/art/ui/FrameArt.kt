package khelp.art.ui

import khelp.art.LAST_DIRECTORY_KEY
import khelp.art.preferencesArt
import khelp.image.extensions.border
import khelp.image.loadImageThumb
import khelp.image.ui.JHelpImageComponent
import khelp.thread.delay
import khelp.ui.components.imgechooser.ImageChooser
import khelp.ui.dsl.constraintLayout
import khelp.ui.dsl.frame
import khelp.ui.layout.constraints.ConstraintsSize
import khelp.utilities.log.exception
import javax.swing.JFrame

object FrameArt
{
    private val frame : JFrame by lazy { this.createFrame() }
    private val imageChooser = ImageChooser(LAST_DIRECTORY_KEY, preferencesArt)
    private val imageComponent = JHelpImageComponent(512 + 64, 512 + 64)
    private val image = this.imageComponent.image
    private val backgroundSprite = this.image.createSprite(0, 0, this.image.width, this.image.height)
    private val pathSprite = this.image.createSprite(0, 0, this.image.width, this.image.height)
    private val artSprite = this.image.createSprite(0, 0, this.image.width, this.image.height)
    private val imageComponentEvents = ImageComponentEvents(this.image, this.pathSprite)

    init
    {
        this.imageComponent.isRequestFocusEnabled = true
        this.imageComponent.isFocusable = true
        this.imageComponent.requestFocusInWindow()
        this.imageComponent.requestFocus()
        this.imageComponent.addMouseListener(this.imageComponentEvents)
        this.imageComponent.addMouseMotionListener(this.imageComponentEvents)

        this.backgroundSprite.visible(true)
        this.pathSprite.visible(true)
        this.artSprite.visible(true)
    }

    fun show()
    {
        this.frame.isVisible = true
        this.selectFile()
    }

    private fun selectFile()
    {
        this.imageChooser.selectImage()
            .and { file ->
                try
                {
                    val imageSelected = loadImageThumb(file, this.image.width, this.image.height)
                    val border = imageSelected.border

                    this.backgroundSprite.visible(false)
                    val image = this.backgroundSprite.image()
                    image.startDrawMode()
                    image.clear()
                    image.fillRectangleScaleBetter(0, 0, this.image.width, this.image.height, border,
                                                   doAlphaMix = false)
                    image.endDrawMode()
                    this.backgroundSprite.visible(true)
                    this.image.update()
                }
                catch (exception : Exception)
                {
                    exception(exception)
                }

                delay(1024) { this.selectFile() }
            }
    }

    private fun createFrame() : JFrame =
        frame {
            constraintLayout {
                this@FrameArt.imageComponent("imageComponent") {
                    horizontalSize = ConstraintsSize.WRAPPED
                    verticalSize = ConstraintsSize.WRAPPED
                    topAtParent
                    bottomFree
                    leftAtParent
                    rightAtParent
                }

                this@FrameArt.imageChooser("imageChooser") {
                    horizontalSize = ConstraintsSize.EXPANDED
                    verticalSize = ConstraintsSize.EXPANDED
                    topAtBottom = "imageComponent"
                    bottomAtParent
                    leftAtParent
                    rightAtParent
                }
            }
        }
}
