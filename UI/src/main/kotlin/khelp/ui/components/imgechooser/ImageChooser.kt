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
import khelp.ui.GenericAction
import khelp.ui.dsl.borderLayout
import khelp.ui.extensions.file
import khelp.ui.extensions.get
import khelp.ui.game.GameImage
import java.awt.FlowLayout
import java.beans.PropertyChangeEvent
import java.io.File
import java.io.FileInputStream
import javax.swing.JButton
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
        val actionOpen = GenericAction(defaultTexts, OPEN, null, OPEN, TaskContext.INDEPENDENT) {
            val selectedFile = this.fileChooser.selectedFile

            if (selectedFile != null)
            {
                this.preferences.edit { file(this@ImageChooser.preferenceKey, selectedFile) }
                this.promise?.result(selectedFile)
            }
            else
            {
                this.promise?.fail(Exception("No selection"))
            }

            this.promise = null
        }

        val actionCancel = GenericAction(defaultTexts, CANCEL, null, CANCEL, TaskContext.INDEPENDENT) {
            this.promise?.fail(Exception("No selection"))
            this.promise = null
        }

        this.fileChooser.isMultiSelectionEnabled = false
        this.fileChooser.fileFilter = FileNameExtensionFilter("Image", "jpg", "jpeg", "png")
        this.fileChooser.accessory = this.preview
        this.fileChooser.addPropertyChangeListener { property ->
            parallel(TaskContext.IO, property, this::propertyChanged)
        }
        this.fileChooser.controlButtonsAreShown = false

        borderLayout {
            center(this@ImageChooser.fileChooser)
            pageEnd {
                layout = FlowLayout()
                add(JButton(actionOpen))
                add(JButton(actionCancel))
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