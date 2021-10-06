package khelp.editor.ui.components.color

import khelp.editor.ui.Editor
import khelp.engine3d.render.Color4f
import khelp.engine3d.render.GRAY
import khelp.thread.TaskContext
import khelp.thread.future.FutureResult
import khelp.thread.future.Promise
import khelp.ui.GenericAction
import khelp.ui.dsl.constraintLayout
import khelp.ui.dsl.verticalLayout
import khelp.ui.layout.constraints.ConstraintsSize
import khelp.ui.layout.table.TableLayout
import javax.swing.JButton
import javax.swing.JPanel
import kotlin.math.roundToInt

class Color4fChooser : JPanel()
{
    private val colorPreview = Color4fPreview()
    private val alphaPart = ColorPartSelector("alpha")
    private val redPart = ColorPartSelector("red")
    private val greenPart = ColorPartSelector("green")
    private val bluePart = ColorPartSelector("blue")
    private var color = GRAY
    private var promise : Promise<Color4f>? = null

    init
    {
        this.alphaPart.valueObservable.observedBy(TaskContext.INDEPENDENT) { part ->
            this.color = Color4f(this.color.red, this.color.green, this.color.blue, part.toFloat() / 255f)
            this.updateColor()
        }

        this.redPart.valueObservable.observedBy(TaskContext.INDEPENDENT) { part ->
            this.color = Color4f(part.toFloat() / 255f, this.color.green, this.color.blue, this.color.alpha)
            this.updateColor()
        }

        this.greenPart.valueObservable.observedBy(TaskContext.INDEPENDENT) { part ->
            this.color = Color4f(this.color.red, part.toFloat() / 255f, this.color.blue, this.color.alpha)
            this.updateColor()
        }

        this.bluePart.valueObservable.observedBy(TaskContext.INDEPENDENT) { part ->
            this.color = Color4f(this.color.red, this.color.green, part.toFloat() / 255f, this.color.alpha)
            this.updateColor()
        }

        val chooseAction = GenericAction(Editor.resourcesText, "choose", TaskContext.INDEPENDENT) {
            this.promise?.result(this.color)
            this.promise = null
        }

        val cancelAction = GenericAction(Editor.resourcesText, "cancel", TaskContext.INDEPENDENT) {
            this.promise?.fail(Exception("Cancel"))
            this.promise = null
        }

        constraintLayout {
            panel("colorParts",
                  {
                      layout = TableLayout(3)
                      alphaPart.pushInside(this, 0)
                      redPart.pushInside(this, 1)
                      greenPart.pushInside(this, 2)
                      bluePart.pushInside(this, 3)
                  },
                  {
                      horizontalSize = ConstraintsSize.WRAPPED
                      verticalSize = ConstraintsSize.EXPANDED
                      topAtParent
                      bottomAtParent
                      leftAtParent
                      rightFree
                  })
            colorPreview("preview") {
                horizontalSize = ConstraintsSize.EXPANDED
                verticalSize = ConstraintsSize.EXPANDED
                topAtParent
                bottomAtParent
                leftAtRight = "colorParts"
                rightAtLeft = "chooseButtons"
            }
            panel("chooseButtons",
                  {
                      verticalLayout(marginHorizontal = 8, marginLeft = 8) {
                          + JButton(chooseAction)
                          + JButton(cancelAction)
                      }
                  },
                  {
                      horizontalSize = ConstraintsSize.WRAPPED
                      verticalSize = ConstraintsSize.EXPANDED
                      topAtParent
                      bottomAtParent
                      leftFree
                      rightAtParent
                  })

        }
        this.updateColor()
    }

    fun chooseColor(color : Color4f) : FutureResult<Color4f>
    {
        this.color = color
        this.updateColor()
        this.promise = Promise()
        return this.promise !!.futureResult
    }

    private fun updateColor()
    {
        this.alphaPart.value = (this.color.alpha * 255f).roundToInt()
        this.redPart.value = (this.color.red * 255f).roundToInt()
        this.greenPart.value = (this.color.green * 255f).roundToInt()
        this.bluePart.value = (this.color.blue * 255f).roundToInt()
        this.colorPreview.color = this.color
    }
}