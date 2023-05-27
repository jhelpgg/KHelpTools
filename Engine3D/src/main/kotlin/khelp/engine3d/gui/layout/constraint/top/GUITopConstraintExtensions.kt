package khelp.engine3d.gui.layout.constraint.top

import khelp.engine3d.gui.component.GUIComponent

val GUITopConstraint.attached : GUIComponent?
    get() =
        when (this)
        {
            is GUITopAtParent, is GUITopFree -> null
            is GUITopToTopOf                 -> this.component
            is GUITopToBottomOf              -> this.component
        }