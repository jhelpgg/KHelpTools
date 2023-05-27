package khelp.engine3d.gui.dsl

import khelp.engine3d.gui.component.GUIComponent
import khelp.engine3d.gui.layout.constraint.GUIConstraintConstraint
import khelp.engine3d.gui.layout.constraint.GUIConstraintsSize
import khelp.engine3d.gui.layout.constraint.bottom.GUIBottomAtParent
import khelp.engine3d.gui.layout.constraint.bottom.GUIBottomFree
import khelp.engine3d.gui.layout.constraint.bottom.GUIBottomToBottomOf
import khelp.engine3d.gui.layout.constraint.bottom.GUIBottomToTopOf
import khelp.engine3d.gui.layout.constraint.bottom.attached
import khelp.engine3d.gui.layout.constraint.left.GUILeftAtParent
import khelp.engine3d.gui.layout.constraint.left.GUILeftFree
import khelp.engine3d.gui.layout.constraint.left.GUILeftToLeftOf
import khelp.engine3d.gui.layout.constraint.left.GUILeftToRightOf
import khelp.engine3d.gui.layout.constraint.left.attached
import khelp.engine3d.gui.layout.constraint.right.GUIRightAtParent
import khelp.engine3d.gui.layout.constraint.right.GUIRightFree
import khelp.engine3d.gui.layout.constraint.right.GUIRightToLeftOf
import khelp.engine3d.gui.layout.constraint.right.GUIRightToRightOf
import khelp.engine3d.gui.layout.constraint.right.attached
import khelp.engine3d.gui.layout.constraint.top.GUITopAtParent
import khelp.engine3d.gui.layout.constraint.top.GUITopFree
import khelp.engine3d.gui.layout.constraint.top.GUITopToBottomOf
import khelp.engine3d.gui.layout.constraint.top.GUITopToTopOf
import khelp.engine3d.gui.layout.constraint.top.attached

class GUIConstraintParameterFiller internal constructor()
{
    internal val constraint : GUIConstraintConstraint = GUIConstraintConstraint()

    var marginTop : Int
        get() = this.constraint.margin.top
        set(value)
        {
            this.constraint.margin = this.constraint.margin.top(value)
        }

    var marginBottom : Int
        get() = this.constraint.margin.bottom
        set(value)
        {
            this.constraint.margin = this.constraint.margin.bottom(value)
        }

    var marginLeft : Int
        get() = this.constraint.margin.left
        set(value)
        {
            this.constraint.margin = this.constraint.margin.left(value)
        }

    var marginRight : Int
        get() = this.constraint.margin.right
        set(value)
        {
            this.constraint.margin = this.constraint.margin.right(value)
        }

    val horizontalWrapped : Unit
        get()
        {
            this.constraint.horizontalSize = GUIConstraintsSize.WRAPPED
        }

    val horizontalExpanded : Unit
        get()
        {
            this.constraint.horizontalSize = GUIConstraintsSize.EXPANDED
        }

    val verticalWrapped : Unit
        get()
        {
            this.constraint.verticalSize = GUIConstraintsSize.WRAPPED
        }

    val verticalExpanded : Unit
        get()
        {
            this.constraint.verticalSize = GUIConstraintsSize.EXPANDED
        }

    val topAtParent : Unit
        get()
        {
            this.constraint.topConstraint = GUITopAtParent
        }

    val topFree : Unit
        get()
        {
            this.constraint.topConstraint = GUITopFree
        }

    var topAtTop : GUIComponent?
        get() = this.constraint.topConstraint.attached
        set(value)
        {
            if (value != null)
            {
                this.constraint.topConstraint = GUITopToTopOf(value)
            }
        }

    var topAtBottom : GUIComponent?
        get() = this.constraint.topConstraint.attached
        set(value)
        {
            if (value != null)
            {
                this.constraint.topConstraint = GUITopToBottomOf(value)
            }
        }

    val bottomAtParent : Unit
        get()
        {
            this.constraint.bottomConstraint = GUIBottomAtParent
        }

    val bottomFree : Unit
        get()
        {
            this.constraint.bottomConstraint = GUIBottomFree
        }

    var bottomAtTop : GUIComponent?
        get() = this.constraint.bottomConstraint.attached
        set(value)
        {
            if (value != null)
            {
                this.constraint.bottomConstraint = GUIBottomToTopOf(value)
            }
        }

    var bottomAtBottom : GUIComponent?
        get() = this.constraint.bottomConstraint.attached
        set(value)
        {
            if (value != null)
            {
                this.constraint.bottomConstraint = GUIBottomToBottomOf(value)
            }
        }

    val leftAtParent : Unit
        get()
        {
            this.constraint.leftConstraint = GUILeftAtParent
        }

    val leftFree : Unit
        get()
        {
            this.constraint.leftConstraint = GUILeftFree
        }

    var leftAtLeft : GUIComponent?
        get() = this.constraint.leftConstraint.attached
        set(value)
        {
            if (value != null)
            {
                this.constraint.leftConstraint = GUILeftToLeftOf(value)
            }
        }

    var leftAtRight : GUIComponent?
        get() = this.constraint.leftConstraint.attached
        set(value)
        {
            if (value != null)
            {
                this.constraint.leftConstraint = GUILeftToRightOf(value)
            }
        }

    val rightAtParent : Unit
        get()
        {
            this.constraint.rightConstraint = GUIRightAtParent
        }

    val rightFree : Unit
        get()
        {
            this.constraint.rightConstraint = GUIRightFree
        }

    var rightAtLeft : GUIComponent?
        get() = this.constraint.rightConstraint.attached
        set(value)
        {
            if (value != null)
            {
                this.constraint.rightConstraint = GUIRightToLeftOf(value)
            }
        }

    var rightAtRight : GUIComponent?
        get() = this.constraint.rightConstraint.attached
        set(value)
        {
            if (value != null)
            {
                this.constraint.rightConstraint = GUIRightToRightOf(value)
            }
        }
}
