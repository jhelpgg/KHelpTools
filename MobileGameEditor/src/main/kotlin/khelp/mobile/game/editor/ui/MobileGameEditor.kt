package khelp.mobile.game.editor.ui

import khelp.engine3d.render.window3DFull
import khelp.mobile.game.editor.ui.screens.ScreenManager

object MobileGameEditor
{
    fun show()
    {
        window3DFull("Mobile game editor") { ScreenManager(this) }
    }
}
