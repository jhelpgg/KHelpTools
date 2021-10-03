package khelp.editor.ui.patheditor

import khelp.engine3d.geometry.path.PathElement
import java.awt.Point

data class ControlPoint(val point:Point,val element:PathElement, val number:Int, val index:Int)
