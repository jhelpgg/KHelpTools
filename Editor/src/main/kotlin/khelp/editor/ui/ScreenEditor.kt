package khelp.editor.ui

import khelp.engine3d.render.Node
import javax.swing.JPanel

interface ScreenEditor
{
    val manipulatedNode : Node

    fun applyInside(panel : JPanel)
}