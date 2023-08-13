package khelp.uno.ui

import khelp.engine3d.event.ActionCode
import khelp.engine3d.render.Window3D
import khelp.thread.TaskContext

class UnoScreenManager(private val window3D : Window3D)
{
    init
    {
        this.window3D.actionManager.actionObservable.observedBy(TaskContext.INDEPENDENT, this::actionCodes)
    }

    private fun actionCodes(actionCodes : List<ActionCode>)
    {
        if (actionCodes.contains(ActionCode.ACTION_EXIT))
        {
            this.window3D.close()
        }
    }
}