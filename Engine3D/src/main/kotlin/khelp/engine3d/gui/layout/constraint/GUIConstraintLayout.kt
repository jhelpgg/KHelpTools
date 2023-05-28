package khelp.engine3d.gui.layout.constraint

import khelp.engine3d.gui.component.GUIComponent
import khelp.engine3d.gui.layout.GUILayout
import khelp.engine3d.gui.layout.constraint.bottom.GUIBottomAtParent
import khelp.engine3d.gui.layout.constraint.bottom.GUIBottomToBottomOf
import khelp.engine3d.gui.layout.constraint.bottom.GUIBottomToTopOf
import khelp.engine3d.gui.layout.constraint.bottom.attached
import khelp.engine3d.gui.layout.constraint.left.GUILeftAtParent
import khelp.engine3d.gui.layout.constraint.left.GUILeftToLeftOf
import khelp.engine3d.gui.layout.constraint.left.GUILeftToRightOf
import khelp.engine3d.gui.layout.constraint.left.attached
import khelp.engine3d.gui.layout.constraint.right.GUIRightAtParent
import khelp.engine3d.gui.layout.constraint.right.GUIRightToLeftOf
import khelp.engine3d.gui.layout.constraint.right.GUIRightToRightOf
import khelp.engine3d.gui.layout.constraint.right.attached
import khelp.engine3d.gui.layout.constraint.top.GUITopAtParent
import khelp.engine3d.gui.layout.constraint.top.GUITopToBottomOf
import khelp.engine3d.gui.layout.constraint.top.GUITopToTopOf
import khelp.engine3d.gui.layout.constraint.top.attached
import khelp.utilities.collections.queue.Queue
import java.awt.Dimension
import kotlin.math.max
import kotlin.math.min

class GUIConstraintLayout : GUILayout<GUIConstraintConstraint>()
{
    override fun layout(parentWidth : Int, parentHeight : Int,
                        components : List<Pair<GUIComponent, GUIConstraintConstraint>>)
    {
        val queue = Queue<GUIConstraintConstraint>()
        val collectedConstraints = ArrayList<Pair<GUIComponent, GUIConstraintConstraint>>()
        val parentConstraint = GUIConstraintConstraint(0, 0, parentWidth, parentHeight)

        for ((component, constraint) in components)
        {
            val dimension = component.preferredSize()
            constraint.x = 0
            constraint.y = 0
            constraint.xMax = constraint.margin.width +
                              if (constraint.horizontalSize == GUIConstraintsSize.WRAPPED) dimension.width else parentWidth
            constraint.yMax = constraint.margin.height +
                              if (constraint.verticalSize == GUIConstraintsSize.WRAPPED) dimension.height else parentHeight
            constraint.computed = false

            constraint.constraintsLeft =
                if (constraint.leftConstraint == GUILeftAtParent)
                {
                    parentConstraint
                }
                else
                {
                    constraint.leftConstraint.attached?.let { componentSearched -> components.firstOrNull { (componentTested, _) -> componentTested == componentSearched }?.second }
                }

            constraint.constraintsRight =
                if (constraint.rightConstraint == GUIRightAtParent)
                {
                    parentConstraint
                }
                else
                {
                    constraint.rightConstraint.attached?.let { componentSearched -> components.firstOrNull { (componentTested, _) -> componentTested == componentSearched }?.second }
                }

            constraint.constraintsTop =
                if (constraint.topConstraint == GUITopAtParent)
                {
                    parentConstraint
                }
                else
                {
                    constraint.topConstraint.attached?.let { componentSearched -> components.firstOrNull { (componentTested, _) -> componentTested == componentSearched }?.second }
                }

            constraint.constraintsBottom =
                if (constraint.bottomConstraint == GUIBottomAtParent)
                {
                    parentConstraint
                }
                else
                {
                    constraint.bottomConstraint.attached?.let { componentSearched -> components.firstOrNull { (componentTested, _) -> componentTested == componentSearched }?.second }
                }

            queue.inQueue(constraint)
            collectedConstraints.add(Pair(component, constraint))
        }

        var constraintLoop : GUIConstraintConstraint? = null
        var sizeLoop = queue.size

        while (queue.notEmpty)
        {
            val constraint = queue.outQueue()

            if (constraint.allDependencyComputed || (constraintLoop == constraint && queue.size == sizeLoop))
            {
                constraintLoop = null
                this.placeLeftConstraints(constraint, constraint.horizontalSize == GUIConstraintsSize.EXPANDED)
                this.placeRightConstraints(constraint, constraint.horizontalSize == GUIConstraintsSize.EXPANDED)
                this.placeTopConstraints(constraint, constraint.verticalSize == GUIConstraintsSize.EXPANDED)
                this.placeBottomConstraints(constraint, constraint.verticalSize == GUIConstraintsSize.EXPANDED)
                constraint.computed = true
            }
            else
            {
                if (constraintLoop == null || sizeLoop != queue.size)
                {
                    constraintLoop = constraint
                    sizeLoop = queue.size
                }

                queue.inQueue(constraint)
            }
        }

        for ((component, constraint) in collectedConstraints)
        {
            component.x = constraint.x + constraint.margin.left
            component.y = constraint.y + constraint.margin.top
            component.width = constraint.xMax - constraint.x - constraint.margin.width
            component.height = constraint.yMax - constraint.y - constraint.margin.height
        }
    }

    override fun preferredSize(components : List<Pair<GUIComponent, GUIConstraintConstraint>>) : Dimension
    {
        val queue = Queue<GUIConstraintConstraint>()
        val collectedConstraints = ArrayList<GUIConstraintConstraint>()

        for ((component, constraint) in components)
        {
            val dimension = component.preferredSize()
            constraint.x = 0
            constraint.y = 0
            constraint.xMax = dimension.width + constraint.margin.width
            constraint.yMax = dimension.height + constraint.margin.height
            constraint.computed = false
            constraint.constraintsLeft =
                constraint.leftConstraint.attached?.let { componentSearched -> components.firstOrNull { (componentTested, _) -> componentTested == componentSearched }?.second }

            constraint.constraintsRight =
                constraint.rightConstraint.attached?.let { componentSearched -> components.firstOrNull { (componentTested, _) -> componentTested == componentSearched }?.second }

            constraint.constraintsTop =
                constraint.topConstraint.attached?.let { componentSearched -> components.firstOrNull { (componentTested, _) -> componentTested == componentSearched }?.second }

            constraint.constraintsBottom =
                constraint.bottomConstraint.attached?.let { componentSearched -> components.firstOrNull { (componentTested, _) -> componentTested == componentSearched }?.second }

            queue.inQueue(constraint)
            collectedConstraints.add(constraint)
        }

        var constraintLoop : GUIConstraintConstraint? = null
        var sizeLoop = queue.size

        while (queue.notEmpty)
        {
            val constraint = queue.outQueue()

            if (constraint.allDependencyComputed || (constraintLoop == constraint && queue.size == sizeLoop))
            {
                constraintLoop = null
                this.placeLeftConstraints(constraint)
                this.placeRightConstraints(constraint)
                this.placeTopConstraints(constraint)
                this.placeBottomConstraints(constraint)
                constraint.computed = true
            }
            else
            {
                if (constraintLoop == null || sizeLoop != queue.size)
                {
                    constraintLoop = constraint
                    sizeLoop = queue.size
                }

                queue.inQueue(constraint)
            }
        }

        var minX = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var minY = Int.MAX_VALUE
        var maxY = Int.MIN_VALUE

        for (index in 0 until collectedConstraints.size)
        {
            val constraint = collectedConstraints[index]

            minX = min(minX, constraint.x)
            maxX = max(maxX, constraint.xMax)
            minY = min(minY, constraint.y)
            maxY = max(maxY, constraint.yMax)
        }

        if (minX <= maxX && minY <= maxY)
        {
            return Dimension(maxX - minX, maxY - minY)
        }

        return Dimension(16, 16)
    }

    private fun placeLeftConstraints(constraint : GUIConstraintConstraint, resize : Boolean = false)
    {
        val atLeft = constraint.constraintsLeft ?: return
        val width = constraint.xMax - constraint.x

        when (constraint.leftConstraint)
        {
            is GUILeftToLeftOf, is GUILeftAtParent -> constraint.x = atLeft.x
            is GUILeftToRightOf                    -> constraint.x = atLeft.xMax
            else                                   -> Unit
        }

        if (! resize)
        {
            constraint.xMax = constraint.x + width
        }
    }

    private fun placeRightConstraints(constraint : GUIConstraintConstraint, resize : Boolean = false)
    {
        val atRight = constraint.constraintsRight ?: return
        val width = constraint.xMax - constraint.x

        when (constraint.rightConstraint)
        {
            is GUIRightToLeftOf                       -> constraint.xMax = atRight.x
            is GUIRightToRightOf, is GUIRightAtParent -> constraint.xMax = atRight.xMax
            else                                      -> Unit
        }

        if (! resize)
        {
            val constraintsLeft = constraint.constraintsLeft

            if (constraintsLeft != null)
            {
                constraint.x =
                    when (constraint.leftConstraint)
                    {
                        is GUILeftToLeftOf, is GUILeftAtParent -> (constraintsLeft.x + constraint.xMax - width) / 2
                        is GUILeftToRightOf                    -> (constraintsLeft.xMax + constraint.xMax - width) / 2
                        else                                   -> constraint.xMax - width
                    }

                constraint.xMax = constraint.x + width
            }
            else
            {
                constraint.x = constraint.xMax - width
            }
        }
    }

    private fun placeTopConstraints(constraint : GUIConstraintConstraint, resize : Boolean = false)
    {
        val atTop = constraint.constraintsTop ?: return
        val height = constraint.yMax - constraint.y

        when (constraint.topConstraint)
        {
            is GUITopToTopOf, is GUITopAtParent -> constraint.y = atTop.y
            is GUITopToBottomOf                 -> constraint.y = atTop.yMax
            else                                -> Unit
        }

        if (! resize)
        {
            constraint.yMax = constraint.y + height
        }
    }

    private fun placeBottomConstraints(constraint : GUIConstraintConstraint, resize : Boolean = false)
    {
        val atBottom = constraint.constraintsBottom ?: return
        val height = constraint.yMax - constraint.y

        when (constraint.bottomConstraint)
        {
            is GUIBottomToTopOf                          -> constraint.yMax = atBottom.y
            is GUIBottomToBottomOf, is GUIBottomAtParent -> constraint.yMax = atBottom.yMax
            else                                         -> Unit
        }

        if (! resize)
        {
            val constraintsTop = constraint.constraintsTop

            if (constraintsTop != null)
            {
                constraint.y =
                    when (constraint.topConstraint)
                    {
                        is GUITopToTopOf, is GUITopAtParent -> (constraintsTop.y + constraint.yMax - height) / 2
                        is GUITopToBottomOf                 -> (constraintsTop.yMax + constraint.yMax - height) / 2
                        else                                -> constraint.yMax - height
                    }

                constraint.yMax = constraint.y + height
            }
            else
            {
                constraint.y = constraint.yMax - height
            }
        }
    }

}