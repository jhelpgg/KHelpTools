package khelp.engine3d.gui.frames

import khelp.engine3d.resource.Resources3D
import khelp.resources.CANCEL
import khelp.resources.OK
import khelp.resources.ResourcesText
import khelp.resources.defaultTexts
import khelp.resources.standardText
import khelp.thread.TaskContext
import khelp.thread.future.FutureResult
import khelp.thread.future.Promise
import khelp.ui.components.JHelpFrame
import khelp.ui.components.style.StyledButton
import khelp.ui.components.style.StyledLabel
import khelp.ui.dsl.constraintLayout
import khelp.ui.dsl.frame
import khelp.ui.layout.constraints.ConstraintsSize
import khelp.ui.style.StyleImageWithTextClickable
import khelp.ui.style.title
import khelp.ui.utilities.DEFAULT_FONT
import khelp.ui.utilities.centerOnScreen
import java.awt.Dimension
import javax.swing.JScrollPane
import javax.swing.JTextArea

object FrameInputText
{
    private var wasShown = false
    private var promiseText = Promise<String>()
    private val title = StyledLabel("-*-".standardText, defaultTexts, title())
    private val text = JTextArea(80, 40)

    init
    {
        this.text.font = DEFAULT_FONT.font
    }

    private val frameInputText : JHelpFrame by lazy {
        val ok = StyledButton(OK, defaultTexts, StyleImageWithTextClickable())
        val cancel = StyledButton(CANCEL, defaultTexts, StyleImageWithTextClickable())

        val frame = frame(Resources3D.resourcesText["inputTextTitle"], decorated = false) {
            this.canCloseNow = { false }
            this.isAlwaysOnTop = true

            this.constraintLayout {
                this@FrameInputText.title("Title") {
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

                JScrollPane(this@FrameInputText.text)("Text") {
                    this.horizontalSize = ConstraintsSize.EXPANDED
                    this.verticalSize = ConstraintsSize.EXPANDED

                    this.marginTop = 4
                    this.marginLeft = 4
                    this.marginRight = 4
                    this.marginBottom = 4

                    this.topAtBottom = "Title"
                    this.bottomAtTop = "OK"
                    this.leftAtParent
                    this.rightAtParent
                }

                ok("OK") {
                    this.horizontalSize = ConstraintsSize.WRAPPED
                    this.verticalSize = ConstraintsSize.WRAPPED

                    this.marginLeft = 4
                    this.marginBottom = 4

                    this.topFree
                    this.bottomAtParent
                    this.leftAtParent
                    this.rightFree
                }

                cancel("Cancel") {
                    this.horizontalSize = ConstraintsSize.WRAPPED
                    this.verticalSize = ConstraintsSize.WRAPPED

                    this.marginLeft = 4
                    this.marginRight = 4
                    this.marginBottom = 4

                    this.topFree
                    this.bottomAtParent
                    this.leftAtRight = "OK"
                    this.rightAtParent
                }
            }
        }

        ok.onClick(TaskContext.INDEPENDENT) {
            this.promiseText.result(this.text.text)
            frame.isVisible = false
        }

        cancel.onClick(TaskContext.INDEPENDENT) {
            this.promiseText.futureResult.cancel("Canceled")
            frame.isVisible = false
        }

        frame
    }

    fun show(initialText : String = "", titleKeyText : String = "inputTextTitle",
             resourcesText : ResourcesText = Resources3D.resourcesText) : FutureResult<String>
    {
        this.wasShown = true
        this.title.text = resourcesText[titleKeyText]
        this.text.text = initialText
        this.promiseText = Promise<String>()
        this.frameInputText.isVisible = true
        this.frameInputText.size = Dimension(800, 600)
        centerOnScreen(this.frameInputText)
        return this.promiseText.futureResult
    }

    internal fun close()
    {
        if (this.wasShown)
        {
            this.frameInputText.dispose()
        }
    }
}