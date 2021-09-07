package khelp.ui.layout.constraints

import java.awt.Component

sealed class LeftConstraint

object LeftAtParent : LeftConstraint()

object LeftFree : LeftConstraint()

class LeftToLeftOf(val component : Component) : LeftConstraint()

class LeftToCenterOf(val component : Component) : LeftConstraint()

class LeftToRightOf(val component : Component) : LeftConstraint()

internal val LeftConstraint.attached : Component?
    get() =
        when (this)
        {
            is LeftAtParent, is LeftFree -> null
            is LeftToLeftOf              -> this.component
            is LeftToCenterOf            -> this.component
            is LeftToRightOf             -> this.component
        }