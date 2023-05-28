package khelp.mobile.game.editor.ui.screens

import khelp.engine3d.event.ActionCode
import khelp.engine3d.gui.dsl.buttonText
import khelp.engine3d.gui.dsl.constraintLayout
import khelp.engine3d.render.Window3D
import khelp.thread.TaskContext
import khelp.thread.observable.Observer
import khelp.ui.events.MouseState
import khelp.utilities.log.debug

class MainScreen : Screen
{
    private var observerActionsCodes : Observer<List<ActionCode>>? = null
    private var observerMouseState : Observer<MouseState>? = null
    private val removeMeButton = buttonText(keyText = "ok")

    override fun attach(window3D : Window3D)
    {
        this.observerActionsCodes =
            window3D.actionManager.actionObservable.observedBy(TaskContext.INDEPENDENT, this::actionCodes)
        this.observerMouseState =
            window3D.mouseManager.mouseStateObservable.observedBy(TaskContext.INDEPENDENT, this::mouseState)

        val scene = window3D.scene
        scene.root.removeAllChildren()
        scene.root {
            this.z = - 2f
            this.box("Box") {
                this.angleX = 12.5f
                this.angleY = 12.5f
            }
        }

        window3D.gui.constraintLayout {
            this@MainScreen.removeMeButton with {
                this.horizontalExpanded
                this.verticalWrapped

                this.marginLeft = 32
                this.marginRight = 32
                this.marginBottom = 16

                this.topFree
                this.bottomAtParent
                this.leftAtParent
                this.rightAtParent
            }
        }
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
        debug(mouseState)
        // Something TODO ?
    }
}
