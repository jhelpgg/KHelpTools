package khelp.mobile.game.editor.ui.screens

import khelp.engine3d.event.ActionCode
import khelp.engine3d.gui.GUIMargin
import khelp.engine3d.gui.component.GUIComponentScroll
import khelp.engine3d.gui.component.GUIComponentText
import khelp.engine3d.gui.component.GUIGridComponent
import khelp.engine3d.gui.dsl.constraintLayout
import khelp.engine3d.gui.dsl.scrollProportion
import khelp.engine3d.gui.layout.proprtion.GUIProportionConstraint
import khelp.engine3d.gui.model.GUIGridModel
import khelp.engine3d.render.Window3D
import khelp.resources.standardText
import khelp.thread.TaskContext
import khelp.thread.observable.Observer
import khelp.thread.parallel
import khelp.ui.TextAlignment
import khelp.ui.VerticalAlignment
import khelp.ui.events.MouseState
import khelp.ui.style.background.StyleBackgroundColor
import khelp.ui.style.shape.StyleShapeRoundRectangle
import khelp.utilities.comparators.ComparableNaturalOrderComparator
import khelp.utilities.extensions.percent
import khelp.utilities.log.debug
import khelp.utilities.math.random
import java.awt.Color

class MainScreen : Screen
{
    private var observerActionsCodes : Observer<List<ActionCode>>? = null
    private var observerMouseState : Observer<MouseState>? = null

    override fun attach(window3D : Window3D)
    {
        this.observerActionsCodes =
            window3D.actionManager.actionObservable.observedBy(TaskContext.INDEPENDENT, this::actionCodes)
        this.observerMouseState =
            window3D.mouseManager.mouseStateObservable.observedBy(TaskContext.INDEPENDENT, this::mouseState)


        val removeModel = GUIGridModel<String>()
        val randomString : () -> String = {
            val stringBuilder = StringBuilder()
            stringBuilder.append(random('A', 'Z'))
            val number = random(5, 9)

            for (time in 0 until number)
            {
                stringBuilder.append(random('a', 'z'))
            }

            stringBuilder.toString()
        }

        for (index in 0 until 30)
        {
            removeModel.add("${randomString()} : Test_$index")
        }
        val removeList = GUIGridComponent(removeModel,
                                          { string ->
                                              val text = GUIComponentText()
                                              text.keyText = string.standardText
                                              text.shape = StyleShapeRoundRectangle
                                              text.background = StyleBackgroundColor(Color.CYAN)
                                              text.borderColor = Color.BLACK
                                              text.textAlignment = TextAlignment.CENTER
                                              text.verticalAlignment = VerticalAlignment.CENTER
                                              text.margin = GUIMargin(4, 4, 4, 4)
                                              text
                                          },
                                          2)


        window3D.gui.constraintLayout {
            scrollProportion{ removeList with GUIProportionConstraint(0.percent,0.percent,100.percent,100.percent) } with {
                this.horizontalWrapped
                this.verticalExpanded

                this.topAtParent
                this.bottomAtParent
                this.leftAtParent
                this.rightFree
            }
        }

        parallel {
            for (index in 10 until 30)
            {
                removeModel.add("${randomString()} : Test_$index")
                Thread.sleep(8)
            }

            Thread.sleep(2048)
            removeModel.sort(ComparableNaturalOrderComparator<String>())
            Thread.sleep(4096)

            for (index in 9 downTo 5)
            {
                removeModel.remove(random(0, index))
                Thread.sleep(64)
            }

            Thread.sleep(2048)
            removeModel.filter = { string -> string.endsWith("0") }
            Thread.sleep(4096)

            removeModel.filter = { true }
        }

        // TODO
    }

    override fun detach()
    {
        this.observerActionsCodes?.stopObserve()
        this.observerMouseState?.stopObserve()

        this.observerActionsCodes = null
        this.observerMouseState = null
    }

    private fun actionCodes(actionCodes : List<ActionCode>)
    {
        if (actionCodes.isNotEmpty())
        {
            debug(actionCodes)
        }
        // Something TODO ?
    }

    private fun mouseState(mouseState : MouseState)
    {
        //   debug(mouseState)
        // Something TODO ?
    }
}
