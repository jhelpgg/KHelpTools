package khelp.ui.layout.constraints

import java.awt.Component

sealed class BottomConstraint

object BottomAtParent : BottomConstraint()

object BottomFree : BottomConstraint()

class BottomToTopOf(val component : Component) : BottomConstraint()

class BottomToCenterOf(val component : Component) : BottomConstraint()

class BottomToBottomOf(val component : Component) : BottomConstraint()

internal val BottomConstraint.attached : Component?
    get() =
        when (this)
        {
            is BottomAtParent, is BottomFree -> null
            is BottomToTopOf                 -> this.component
            is BottomToCenterOf              -> this.component
            is BottomToBottomOf              -> this.component
        }