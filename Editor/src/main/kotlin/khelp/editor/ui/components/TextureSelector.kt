package khelp.editor.ui.components

import khelp.editor.ui.Editor
import khelp.engine3d.render.Texture
import khelp.engine3d.resource.TextureCache
import khelp.io.outsideDirectory
import khelp.thread.TaskContext
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.thread.parallel
import khelp.ui.dsl.borderLayout
import khelp.ui.events.MouseManager
import khelp.ui.events.MouseState
import khelp.ui.extensions.drawImage
import khelp.ui.extensions.file
import khelp.ui.extensions.get
import khelp.ui.game.GameComponent
import khelp.ui.game.GameImage
import khelp.ui.utilities.TRANSPARENT
import java.awt.Color
import javax.swing.JFileChooser
import javax.swing.JPanel
import javax.swing.filechooser.FileNameExtensionFilter

class TextureSelector : JPanel()
{
    companion object
    {
        private const val LAST_DIRECTORY_PREFERENCE_KEY = "TextureSelector.lastDirectory"
        private const val IMAGE_SIZE = 128
        private val defaultImage : GameImage by lazy {
            val gameImage = GameImage(TextureSelector.IMAGE_SIZE, TextureSelector.IMAGE_SIZE)
            gameImage.clear(Color.WHITE)
            gameImage.drawPercent { percentGraphics ->
                percentGraphics.color = Color.BLACK
                percentGraphics.fillRectangle(0.0, 0.0, 0.5, 0.5)
                percentGraphics.fillRectangle(0.5, 0.5, 0.5, 0.5)
            }
            gameImage
        }

        val DEFAULT_TEXTURE : Texture by lazy { Texture(TextureSelector.defaultImage) }
    }

    private val textureObservableData = ObservableData(TextureSelector.DEFAULT_TEXTURE)
    val textureObservable : Observable<Texture> = this.textureObservableData.observable

    private val gameComponent = GameComponent(TextureSelector.IMAGE_SIZE, TextureSelector.IMAGE_SIZE)
    private val mouseManager : MouseManager
    private val fileChooser =
        JFileChooser(Editor.preferences[TextureSelector.LAST_DIRECTORY_PREFERENCE_KEY, outsideDirectory])

    init
    {
        borderLayout { center(gameComponent) }
        this.refreshImage()
        this.mouseManager = MouseManager.attachTo(this.gameComponent)
        this.mouseManager.mouseStateObservable.observedBy(TaskContext.INDEPENDENT, this::mouseState)
        this.fileChooser.isMultiSelectionEnabled = false
        this.fileChooser.fileFilter = FileNameExtensionFilter("Image", "jpg", "jpeg", "png")
    }

    fun setTexture(texture : Texture?)
    {
        this.textureObservableData.value(texture ?: TextureSelector.DEFAULT_TEXTURE)
        this.refreshImage()
    }

    private fun mouseState(mouseState : MouseState)
    {
        if (mouseState.clicked)
        {
            parallel {
                if (this.fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                {
                    val imageFile = this.fileChooser.selectedFile
                    Editor.preferences.edit {
                        file(TextureSelector.LAST_DIRECTORY_PREFERENCE_KEY, imageFile.parentFile)
                    }
                    this.textureObservableData.value(TextureCache[imageFile])
                    this.refreshImage()
                }
            }
        }
    }

    private fun refreshImage()
    {
        this.gameComponent.gameImage.clear(TRANSPARENT)
        this.gameComponent.gameImage.draw { graphics2D ->
            graphics2D.drawImage(0, 0,
                                 TextureSelector.IMAGE_SIZE, TextureSelector.IMAGE_SIZE,
                                 this.textureObservableData.value().gameImage)
        }
    }
}