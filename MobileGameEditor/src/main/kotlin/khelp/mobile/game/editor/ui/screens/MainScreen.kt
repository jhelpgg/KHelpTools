package khelp.mobile.game.editor.ui.screens

import khelp.engine3d.event.ActionCode
import khelp.engine3d.gui.component.GUIComponentEmpty
import khelp.engine3d.gui.component.GUIComponentPanel
import khelp.engine3d.gui.component.GUIComponentTextImage
import khelp.engine3d.gui.dsl.buttonText
import khelp.engine3d.gui.dsl.constraintLayout
import khelp.engine3d.gui.dsl.panelVertical
import khelp.engine3d.render.Window3D
import khelp.mobile.game.editor.EDITOR_TEXTS
import khelp.resources.LOAD
import khelp.resources.NO
import khelp.resources.OTHER
import khelp.resources.SAVE
import khelp.resources.SAVE_AS
import khelp.resources.YES
import khelp.resources.defaultResources
import khelp.resources.defaultTexts
import khelp.resources.standardText
import khelp.thread.TaskContext
import khelp.thread.observable.Observer
import khelp.ui.GenericAction
import khelp.ui.components.JHelpFrame
import khelp.ui.components.JLabel
import khelp.ui.dsl.borderLayout
import khelp.ui.dsl.frame
import khelp.ui.events.MouseState
import khelp.ui.extensions.color
import khelp.ui.extensions.nearInvisible
import khelp.ui.extensions.semiVisible
import khelp.ui.game.GameImage
import khelp.ui.style.ImageTextRelativePosition
import khelp.ui.style.background.StyleBackgroundColor
import khelp.ui.style.shape.StyleShapeRoundRectangle
import khelp.ui.utilities.colors.Blue
import khelp.ui.utilities.colors.Green
import khelp.ui.utilities.colors.Red
import khelp.utilities.log.debug
import khelp.utilities.log.mark
import khelp.utilities.math.random

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
