package khelp.ui.layout.constraints

import java.awt.Component

sealed class TopConstraint

object TopAtParent : TopConstraint()

object TopFree : TopConstraint()

class TopToTopOf(val component : Component) : TopConstraint()

class TopToCenterOf(val component : Component) : TopConstraint()

class TopToBottomOf(val component : Component) : TopConstraint()

internal val TopConstraint.attached : Component?
    get() =
        when (this)
        {
            is TopAtParent, is TopFree -> null
            is TopToTopOf              -> this.component
            is TopToCenterOf           -> this.component
            is TopToBottomOf           -> this.component
        }