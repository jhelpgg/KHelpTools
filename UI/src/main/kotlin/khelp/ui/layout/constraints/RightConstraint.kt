package khelp.ui.layout.constraints

import java.awt.Component

sealed class RightConstraint

object RightAtParent : RightConstraint()

object RightFree : RightConstraint()

class RightToLeftOf(val component : Component) : RightConstraint()

class RightToCenterOf(val component : Component) : RightConstraint()

class RightToRightOf(val component : Component) : RightConstraint()

internal val RightConstraint.attached : Component?
    get() =
        when (this)
        {
            is RightAtParent, is RightFree -> null
            is RightToLeftOf               -> this.component
            is RightToCenterOf             -> this.component
            is RightToRightOf              -> this.component
        }