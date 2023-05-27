package khelp.engine3d.gui.layout.constraint.bottom

import khelp.engine3d.gui.component.GUIComponent

val GUIBottomConstraint.attached : GUIComponent?
    get() =
        when (this)
        {
            is GUIBottomAtParent, is GUIBottomFree -> null
            is GUIBottomToBottomOf                 -> this.component
            is GUIBottomToTopOf                    -> this.component
        }