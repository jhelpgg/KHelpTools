package khelp.engine3d.gui.component.dialogs

import khelp.engine3d.gui.GUI
import khelp.engine3d.gui.component.GUIComponentEmpty
import khelp.engine3d.gui.component.GUIComponentToggleButton
import khelp.engine3d.gui.component.GUIDialog
import khelp.engine3d.gui.dsl.buttonText
import khelp.engine3d.gui.dsl.dialogConstraint
import khelp.engine3d.gui.dsl.panelAbsolute
import khelp.engine3d.gui.layout.absolute.GUIAbsoluteConstraint
import khelp.engine3d.gui.layout.constraint.GUIConstraintConstraint
import khelp.engine3d.gui.layout.constraint.GUIConstraintLayout
import khelp.thread.TaskContext
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.ui.extensions.color
import khelp.ui.extensions.invert
import khelp.ui.style.background.StyleBackgroundColor
import khelp.ui.utilities.TITLE_FONT
import khelp.ui.utilities.colors.Amber
import khelp.ui.utilities.colors.BaseColor
import khelp.ui.utilities.colors.Blue
import khelp.ui.utilities.colors.BlueGrey
import khelp.ui.utilities.colors.Brown
import khelp.ui.utilities.colors.Cyan
import khelp.ui.utilities.colors.DeepOrange
import khelp.ui.utilities.colors.DeepPurple
import khelp.ui.utilities.colors.Green
import khelp.ui.utilities.colors.Grey
import khelp.ui.utilities.colors.Indigo
import khelp.ui.utilities.colors.LightBlue
import khelp.ui.utilities.colors.LightGreen
import khelp.ui.utilities.colors.Lime
import khelp.ui.utilities.colors.Orange
import khelp.ui.utilities.colors.Pink
import khelp.ui.utilities.colors.Purple
import khelp.ui.utilities.colors.Red
import khelp.ui.utilities.colors.Teal
import khelp.ui.utilities.colors.Yellow
import java.util.concurrent.atomic.AtomicBoolean

class DialogColorChooser internal constructor(gui : GUI)
{
    companion object
    {
        private val order : Array<Class<out BaseColor<*>>> =
            arrayOf(
                Grey::class.java, Red::class.java, Green::class.java, Blue::class.java,
                Amber::class.java, BlueGrey::class.java, Brown::class.java, Cyan::class.java,
                DeepOrange::class.java, DeepPurple::class.java, Indigo::class.java, LightBlue::class.java,
                LightGreen::class.java, Lime::class.java, Orange::class.java, Pink::class.java,
                Purple::class.java, Teal::class.java, Yellow::class.java
            )

        private val colors : List<List<BaseColor<*>>> by lazy {
            val columns = ArrayList<List<BaseColor<*>>>()

            for (colorClass in DialogColorChooser.order)
            {
                val line = ArrayList<BaseColor<*>>()
                var color = colorClass.enumConstants[0].lightest
                line.add(color)

                while (color != color.darker)
                {
                    color = color.darker
                    line.add(color)
                }

                columns.add(line)
            }

            columns
        }

        private const val COLOR_SIZE = 48
    }

    private val dialog : GUIDialog<GUIConstraintConstraint, GUIConstraintLayout>
    private val colorObservableData : ObservableData<BaseColor<*>> = ObservableData<BaseColor<*>>(Grey.WHITE)
    private val colorComponents = ArrayList<Pair<GUIComponentToggleButton, BaseColor<*>>>()
    val color : Observable<BaseColor<*>> = this.colorObservableData.observable
    val showing : Observable<Boolean>

    init
    {
        val colorStart = this.colorObservableData.value()
        val panel = panelAbsolute {
            var x = 0

            for (column in DialogColorChooser.colors)
            {
                var y = 0

                for (baseColor in column)
                {
                    val emptyUp = GUIComponentEmpty(DialogColorChooser.COLOR_SIZE)
                    emptyUp.background = StyleBackgroundColor(baseColor.color)
                    val emptyDown = GUIComponentEmpty(DialogColorChooser.COLOR_SIZE)
                    emptyDown.background = StyleBackgroundColor(baseColor.color)
                    emptyDown.borderColor = baseColor.color.invert
                    val toggle = GUIComponentToggleButton(emptyUp, emptyUp, emptyDown, emptyDown)
                    toggle.select(baseColor == colorStart)
                    val firstTime = AtomicBoolean(true)
                    toggle.selected.observedBy(TaskContext.INDEPENDENT) { selected ->
                        if (! firstTime.compareAndSet(true, false) && selected)
                        {
                            this@DialogColorChooser.select(baseColor)
                        }
                    }
                    this@DialogColorChooser.colorComponents.add(Pair(toggle, baseColor))

                    toggle with GUIAbsoluteConstraint(x, y,
                                                      DialogColorChooser.COLOR_SIZE, DialogColorChooser.COLOR_SIZE)

                    y += DialogColorChooser.COLOR_SIZE
                }

                x += DialogColorChooser.COLOR_SIZE
            }
        }

        val buttonOk = buttonText(keyText = "ok",
                                  font = TITLE_FONT)

        this.dialog = gui.dialogConstraint {
            panel with {
                this.horizontalWrapped
                this.verticalWrapped

                this.topAtParent
                this.bottomFree
                this.leftAtParent
                this.rightAtParent
            }

            buttonOk with {
                this.horizontalExpanded
                this.verticalWrapped

                this.marginTop = 8

                this.topAtBottom = panel
                this.bottomAtParent
                this.leftAtParent
                this.rightAtParent
            }
        }

        buttonOk.click = { this.dialog.close() }
        this.showing = this.dialog.showing
    }

    fun show()
    {
        this.dialog.show()
    }

    private fun select(color : BaseColor<*>)
    {
        if (this.colorObservableData.valueIf(color) { value -> value != color })
        {
            for ((component, baseColor) in this.colorComponents)
            {
                component.select(color == baseColor)
            }
        }
    }
}
