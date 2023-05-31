package khelp.mobile.game.editor.extensions

import khelp.engine3d.gui.dsl.GUIMenuCreator
import khelp.mobile.game.editor.EDITOR_TEXTS
import khelp.mobile.game.editor.editorImage
import khelp.thread.TaskContext
import khelp.ui.GenericAction

fun GUIMenuCreator.add(keyText : String, imageName : String, action : () -> Unit)
{
    + GenericAction(EDITOR_TEXTS, keyText, editorImage(imageName), TaskContext.INDEPENDENT, action)
}
