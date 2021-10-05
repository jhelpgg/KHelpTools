package khelp.editor.ui.components

import khelp.editor.ui.Editor
import khelp.engine3d.render.Texture
import khelp.engine3d.resource.TextureCache
import khelp.thread.TaskContext
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.ui.dsl.borderLayout
import khelp.ui.events.MouseManager
import khelp.ui.events.MouseState
import khelp.ui.extensions.drawImage
import khelp.ui.game.GameComponent
import khelp.ui.game.GameImage
import khelp.ui.utilities.TRANSPARENT
import java.awt.Color
import javax.swing.JPanel

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

    init
    {
        borderLayout { center(gameComponent) }
        this.refreshImage()
        this.mouseManager = MouseManager.attachTo(this.gameComponent)
        this.mouseManager.mouseStateObservable.observedBy(TaskContext.INDEPENDENT, this::mouseState)
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
            Editor.chooseImageFile()
                .and { imageFile ->
                    this.textureObservableData.value(TextureCache[imageFile])
                    this.refreshImage()
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