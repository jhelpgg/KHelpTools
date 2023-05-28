package khelp.mobile.game.editor.ui.screens

import khelp.engine3d.event.ActionCode
import khelp.engine3d.render.Window3D
import khelp.mobile.game.editor.models.shared.NavigatorModel
import khelp.mobile.game.editor.models.shared.Screens
import khelp.thread.TaskContext
import khelp.utilities.provider.provided

class ScreenManager(private val window3D : Window3D)
{
    private val navigatorModel : NavigatorModel by provided<NavigatorModel>()
    private val mainScreen = MainScreen()
    private var previousScreen : Screen? = null

    init
    {
        this.navigatorModel.screen.observedBy(TaskContext.INDEPENDENT, this::screenChange)
        this.window3D.actionManager.actionObservable.observedBy(TaskContext.INDEPENDENT, this::actionCodes)
    }

    private fun screenChange(screens : Screens)
    {
        val screen =
            when (screens)
            {
                Screens.MAIN_SCREEN -> this.mainScreen
            }

        if (screen != this.previousScreen)
        {
            this.previousScreen?.detach()
            screen.attach(this.window3D)
            this.previousScreen = screen
        }
    }

    private fun actionCodes(actionCodes : List<ActionCode>)
    {
        if (actionCodes.contains(ActionCode.ACTION_EXIT) && this.navigatorModel.back())
        {
            this.window3D.close()
        }
    }
}
