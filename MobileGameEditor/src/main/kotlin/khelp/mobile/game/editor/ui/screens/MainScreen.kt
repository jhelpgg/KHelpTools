package khelp.mobile.game.editor.ui.screens

import khelp.engine3d.event.ActionCode
import khelp.engine3d.gui.dsl.buttonText
import khelp.engine3d.gui.dsl.constraintLayout
import khelp.engine3d.gui.dsl.scrollVertical
import khelp.engine3d.render.Window3D
import khelp.resources.standardText
import khelp.thread.TaskContext
import khelp.thread.observable.Observer
import khelp.ui.events.MouseState
import khelp.utilities.log.debug

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


        val removeList = scrollVertical {
            this.margin = 4

            for (number in 1 .. 100)
            {
                val text = buttonText("Project number $number".standardText)
                text.click = { debug("Project number $number") }
                text.center
            }
        }

        window3D.gui.constraintLayout {
            removeList with {
                this.horizontalWrapped
                this.verticalExpanded

                this.topAtParent
                this.bottomAtParent
                this.leftAtParent
                this.rightFree
            }
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
