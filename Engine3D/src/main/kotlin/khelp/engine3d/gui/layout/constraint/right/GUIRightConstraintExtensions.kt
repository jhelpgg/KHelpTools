package khelp.engine3d.gui.layout.constraint.right

import khelp.engine3d.gui.component.GUIComponent

val GUIRightConstraint.attached : GUIComponent?
    get() =
        when (this)
        {
            is GUIRightAtParent, is GUIRightFree -> null
            is GUIRightToLeftOf                  -> this.component
            is GUIRightToRightOf                 -> this.component
        }
