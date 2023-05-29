package khelp.mobile.game.editor.ui.screens

import khelp.engine3d.event.ActionCode
import khelp.engine3d.gui.component.GUIComponentEmpty
import khelp.engine3d.gui.component.GUIComponentTextImage
import khelp.engine3d.gui.dsl.buttonText
import khelp.engine3d.gui.dsl.constraintLayout
import khelp.engine3d.render.Window3D
import khelp.resources.SAVE_AS
import khelp.resources.defaultResources
import khelp.thread.TaskContext
import khelp.thread.observable.Observer
import khelp.ui.events.MouseState
import khelp.ui.extensions.color
import khelp.ui.extensions.semiVisible
import khelp.ui.game.GameImage
import khelp.ui.style.ImageTextRelativePosition
import khelp.ui.style.background.StyleBackgroundColor
import khelp.ui.style.shape.StyleShapeRoundRectangle
import khelp.ui.utilities.colors.Blue
import khelp.ui.utilities.colors.Red
import khelp.utilities.log.debug

class MainScreen : Screen
{
    private var observerActionsCodes : Observer<List<ActionCode>>? = null
    private var observerMouseState : Observer<MouseState>? = null
    private val empty : GUIComponentEmpty by lazy {
        val component = GUIComponentEmpty()
        component.background = StyleBackgroundColor(Red.RED_0500.color.semiVisible)
        component
    }
    private val removeMeButton = buttonText(keyText = "ok")
    private val label : GUIComponentTextImage by lazy {
        val component = GUIComponentTextImage()
        component.keyText = SAVE_AS
        component.image = GameImage.load("error.png", defaultResources)
        component.background = StyleBackgroundColor(Blue.BLUE_0500.color.semiVisible)
        component.shape = StyleShapeRoundRectangle
        component
    }

    override fun attach(window3D : Window3D)
    {
        this.observerActionsCodes =
            window3D.actionManager.actionObservable.observedBy(TaskContext.INDEPENDENT, this::actionCodes)
        this.observerMouseState =
            window3D.mouseManager.mouseStateObservable.observedBy(TaskContext.INDEPENDENT, this::mouseState)

        removeMeButton.click = {
            val values = ImageTextRelativePosition.values()
            this.label.imageTextRelativePosition =
                values[(this.label.imageTextRelativePosition.ordinal + 1) % values.size]
        }

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

            this@MainScreen.label with {
                this.horizontalWrapped
                this.verticalWrapped

                this.topAtParent
                this bottomAtTopOf this@MainScreen.removeMeButton
                this.leftAtParent
                this.rightFree
            }

            this@MainScreen.empty with {
                this.horizontalExpanded
                this.verticalExpanded

                this.topAtParent
                this bottomAtTopOf this@MainScreen.removeMeButton
                this leftAtRightOf this@MainScreen.label
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
