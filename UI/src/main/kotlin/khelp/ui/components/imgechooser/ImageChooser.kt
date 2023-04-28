package khelp.ui.components.imgechooser

import khelp.io.outsideDirectory
import khelp.preferences.Preferences
import khelp.resources.CANCEL
import khelp.resources.OPEN
import khelp.resources.defaultTexts
import khelp.thread.TaskContext
import khelp.thread.future.FutureResult
import khelp.thread.future.Promise
import khelp.thread.parallel
import khelp.ui.dsl.constraintLayout
import khelp.ui.dsl.styledButton
import khelp.ui.extensions.file
import khelp.ui.extensions.get
import khelp.ui.game.GameImage
import khelp.ui.layout.constraints.ConstraintsSize
import khelp.ui.style.shape.StyleShapeSausage
import java.beans.PropertyChangeEvent
import java.io.File
import java.io.FileInputStream
import javax.swing.JFileChooser
import javax.swing.JPanel
import javax.swing.filechooser.FileNameExtensionFilter

class ImageChooser(private val preferenceKey : String, private val preferences : Preferences) : JPanel()
{
    private val fileChooser = JFileChooser(this.preferences[this.preferenceKey, outsideDirectory])
    private val preview = ImagePreview()
    private var promise : Promise<File>? = null

    init
    {
        val openButton = styledButton(OPEN, defaultTexts) {
            style {
                shape = StyleShapeSausage
            }
            onClick {
                val selectedFile = this@ImageChooser.fileChooser.selectedFile

                if (selectedFile != null)
                {
                    this@ImageChooser.preferences.edit { file(this@ImageChooser.preferenceKey, selectedFile) }
                    this@ImageChooser.promise?.result(selectedFile)
                }
                else
                {
                    this@ImageChooser.promise?.fail(Exception("No selection"))
                }

                this@ImageChooser.promise = null
            }
        }

        val cancelButton = styledButton(CANCEL, defaultTexts) {
            style {
                shape = StyleShapeSausage
            }
            onClick {
                this@ImageChooser.promise?.fail(Exception("No selection"))
                this@ImageChooser.promise = null
            }
        }

        this.fileChooser.isMultiSelectionEnabled = false
        this.fileChooser.fileFilter = FileNameExtensionFilter("Image", "jpg", "jpeg", "png")
        this.fileChooser.accessory = this.preview
        this.fileChooser.addPropertyChangeListener { property ->
            parallel(TaskContext.IO, property, this::propertyChanged)
        }
        this.fileChooser.controlButtonsAreShown = false

        constraintLayout {
            this@ImageChooser.fileChooser("fileChooser") {
                horizontalSize = ConstraintsSize.EXPANDED
                verticalSize = ConstraintsSize.EXPANDED
                topAtParent
                bottomAtTop = "openButton"
                leftAtParent
                rightAtParent
            }

            openButton("openButton") {
                marginLeft = 8
                horizontalSize = ConstraintsSize.WRAPPED
                verticalSize = ConstraintsSize.WRAPPED
                topFree
                bottomAtParent
                leftAtParent
                rightFree
            }

            cancelButton("cancelButton") {
                marginLeft = 32
                horizontalSize = ConstraintsSize.WRAPPED
                verticalSize = ConstraintsSize.WRAPPED
                topFree
                bottomAtParent
                leftAtRight = "openButton"
                rightFree
            }
        }
    }

    fun selectImage() : FutureResult<File>
    {
        val promise = Promise<File>()
        this.promise = promise
        return promise.futureResult
    }

    private fun propertyChanged(property : PropertyChangeEvent)
    {
        var update = false
        var file : File? = null
        val name = property.propertyName

        when (name)
        {
            JFileChooser.DIRECTORY_CHANGED_PROPERTY     -> update = true
            JFileChooser.SELECTED_FILE_CHANGED_PROPERTY ->
            {
                update = true
                file = property.newValue as? File
            }
        }

        if (update)
        {
            this.preview.image =
                file?.let { fileImage -> GameImage.load(FileInputStream(fileImage)) } ?: GameImage.DUMMY
        }
    }
}