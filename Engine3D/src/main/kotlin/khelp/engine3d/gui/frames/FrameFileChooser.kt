package khelp.engine3d.gui.frames

import khelp.engine3d.resource.Resources3D
import khelp.io.outsideDirectory
import khelp.resources.CANCEL
import khelp.resources.OK
import khelp.resources.ResourcesText
import khelp.resources.defaultTexts
import khelp.resources.standardText
import khelp.thread.TaskContext
import khelp.thread.future.FutureResult
import khelp.thread.future.Promise
import khelp.thread.parallel
import khelp.ui.components.JHelpFrame
import khelp.ui.components.style.StyledButton
import khelp.ui.components.style.StyledLabel
import khelp.ui.dsl.constraintLayout
import khelp.ui.dsl.frame
import khelp.ui.extensions.file
import khelp.ui.extensions.get
import khelp.ui.layout.constraints.ConstraintsSize
import khelp.ui.style.button
import khelp.ui.style.title
import khelp.utilities.log.debug
import java.beans.PropertyChangeEvent
import java.io.File
import javax.swing.JFileChooser

object FrameFileChooser
{
    private const val LAST_DIRECTORY = "lastDirectory"
    private val ok = StyledButton(OK, defaultTexts, button())
    private var wasShown = false
    private var promiseFileSelected = Promise<File>()
    private val title = StyledLabel("-*-".standardText, defaultTexts, title())
    var preferences = Resources3D.preferences
    var preferencesKey = FrameFileChooser.LAST_DIRECTORY
    private val fileChooser = JFileChooser(this.preferences[this.preferencesKey, outsideDirectory])
    private var selectedFile : File? = null

    private val frameFileChooser : JHelpFrame by lazy {
        this.ok.onClick(TaskContext.INDEPENDENT) {
            val file = this.selectedFile !!
            this.preferences.edit {
                this.file(this@FrameFileChooser.preferencesKey, file.parentFile)
            }
            this.promiseFileSelected.result(file)
            this.hide()
        }
        this.ok.isEnabled = false

        val cancel = StyledButton(CANCEL, defaultTexts, button())
        cancel.onClick(TaskContext.INDEPENDENT) {
            this.promiseFileSelected.futureResult.cancel("Canceled")
            this.hide()
        }

        this.fileChooser.isMultiSelectionEnabled = false
        this.fileChooser.fileFilter = FileChooseType.IMAGE_AND_OBJ.fileFilter
        this.fileChooser.accessory = PreviewFileChooser
        this.fileChooser.addPropertyChangeListener { property ->
            parallel(TaskContext.IO, property, this::propertyChanged)
        }
        this.fileChooser.controlButtonsAreShown = false

        frame(decorated = false, full = true) {
            this.constraintLayout {
                this@FrameFileChooser.title("Title") {
                    this.horizontalSize = ConstraintsSize.WRAPPED
                    this.verticalSize = ConstraintsSize.WRAPPED

                    this.marginTop = 4
                    this.marginLeft = 4
                    this.marginRight = 4

                    this.topAtParent
                    this.bottomFree
                    this.leftAtParent
                    this.rightAtParent
                }

                this@FrameFileChooser.fileChooser("FileChooser") {
                    this.horizontalSize = ConstraintsSize.EXPANDED
                    this.verticalSize = ConstraintsSize.EXPANDED

                    this.marginTop = 2
                    this.marginBottom = 2
                    this.marginLeft = 2
                    this.marginRight = 2

                    this.topAtBottom = "Title"
                    this.bottomAtTop = "OK"
                    this.leftAtParent
                    this.rightAtParent
                }

                this@FrameFileChooser.ok("OK") {
                    this.horizontalSize = ConstraintsSize.WRAPPED
                    this.verticalSize = ConstraintsSize.WRAPPED

                    this.marginBottom = 4
                    this.marginLeft = 8

                    this.topFree
                    this.bottomAtParent
                    this.leftAtParent
                    this.rightFree
                }

                cancel("Cancel") {
                    this.horizontalSize = ConstraintsSize.WRAPPED
                    this.verticalSize = ConstraintsSize.WRAPPED

                    this.marginBottom = 4
                    this.marginLeft = 32

                    this.topFree
                    this.bottomAtParent
                    this.leftAtRight = "OK"
                    this.rightFree
                }
            }
        }
    }

    fun show(titleKeyText : String = "chooseFile",
             resourcesText : ResourcesText = Resources3D.resourcesText,
             fileType : FileChooseType = FileChooseType.IMAGE_AND_OBJ) : FutureResult<File>
    {
        this.promiseFileSelected = Promise<File>()
        this.fileChooser.currentDirectory = this.preferences[this.preferencesKey, outsideDirectory]
        this.fileChooser.fileFilter = fileType.fileFilter
        debug(this.fileChooser.currentDirectory.absolutePath)
        this.title.text = resourcesText[titleKeyText]

        this.wasShown = true
        this.frameFileChooser.showFrame()
        PreviewFileChooser.changeVisibility(true)
        return this.promiseFileSelected.futureResult
    }

    internal fun close()
    {
        if (this.wasShown)
        {
            PreviewFileChooser.close()
            this.frameFileChooser.dispose()
        }
    }

    private fun hide()
    {
        PreviewFileChooser.changeVisibility(false)
        this.frameFileChooser.isVisible = false
    }

    private fun propertyChanged(property : PropertyChangeEvent)
    {
        val file : File? =
            if (property.propertyName == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)
            {
                property.newValue as? File
            }
            else
            {
                null
            }

        this.selectedFile = file
        this.ok.isEnabled = file != null
        PreviewFileChooser.preview(file)
    }
}
