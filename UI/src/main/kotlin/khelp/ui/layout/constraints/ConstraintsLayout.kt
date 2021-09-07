package khelp.ui.layout.constraints

import khelp.ui.components.iterator
import khelp.ui.utilities.computeMaximumDimension
import khelp.ui.utilities.computeMinimumDimension
import khelp.ui.utilities.computePreferredDimension
import khelp.utilities.collections.queue.Queue
import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.LayoutManager2
import kotlin.math.max
import kotlin.math.min

class ConstraintsLayout : LayoutManager2
{
    private val componentsConstraints = HashMap<Component, ConstraintsLayoutConstraint>()

    override fun addLayoutComponent(component : Component, constraints : Any)
    {
        if (constraints is ConstraintsLayoutConstraint)
        {
            this.componentsConstraints[component] = constraints
        }
    }

    override fun addLayoutComponent(name : String, component : Component) = Unit

    override fun removeLayoutComponent(component : Component)
    {
        this.componentsConstraints.remove(component)
    }

    override fun preferredLayoutSize(parent : Container) : Dimension =
        this.computeSize(parent, ::computePreferredDimension)

    override fun minimumLayoutSize(parent : Container) : Dimension =
        this.computeSize(parent, ::computeMinimumDimension)

    override fun maximumLayoutSize(parent : Container) : Dimension =
        this.computeSize(parent, ::computeMaximumDimension)

    override fun layoutContainer(parent : Container)
    {
        val parentSize = parent.size
        val parentWidth = parentSize.width
        val parentHeight = parentSize.height
        val queue = Queue<ConstraintsLayoutConstraint>()
        val collectedConstraints = ArrayList<Pair<Component, ConstraintsLayoutConstraint>>()
        val parentConstraint = ConstraintsLayoutConstraint()
        parentConstraint.x = 0
        parentConstraint.y = 0
        parentConstraint.xMax = parentWidth
        parentConstraint.yMax = parentHeight
        parentConstraint.computed = true

        for (component in parent.iterator())
        {
            if (component.isVisible)
            {
                val dimension = computePreferredDimension(component)

                this.componentsConstraints[component]?.let { constraint ->
                    constraint.x = 0
                    constraint.y = 0
                    constraint.xMax =
                        if (constraint.horizontalSize == ConstraintsSize.WRAPPED) dimension.width else parentWidth
                    constraint.yMax =
                        if (constraint.verticalSize == ConstraintsSize.WRAPPED) dimension.height else parentHeight
                    constraint.computed = false
                    constraint.constraintsLeft =
                        if (constraint.leftConstraint == LeftAtParent)
                        {
                            parentConstraint
                        }
                        else
                        {
                            constraint.leftConstraint.attached?.let { component ->
                                if (component.isVisible)
                                {
                                    this.componentsConstraints[component]
                                }
                                else
                                {
                                    null
                                }
                            }
                        }
                    constraint.constraintsRight =
                        if (constraint.rightConstraint == RightAtParent)
                        {
                            parentConstraint
                        }
                        else
                        {
                            constraint.rightConstraint.attached?.let { component ->
                                if (component.isVisible)
                                {
                                    this.componentsConstraints[component]
                                }
                                else
                                {
                                    null
                                }
                            }
                        }
                    constraint.constraintsTop =
                        if (constraint.topConstraint == TopAtParent)
                        {
                            parentConstraint
                        }
                        else
                        {
                            constraint.topConstraint.attached?.let { component ->
                                if (component.isVisible)
                                {
                                    this.componentsConstraints[component]
                                }
                                else
                                {
                                    null
                                }
                            }
                        }
                    constraint.constraintsBottom =
                        if (constraint.bottomConstraint == BottomAtParent)
                        {
                            parentConstraint
                        }
                        else
                        {
                            constraint.bottomConstraint.attached?.let { component ->
                                if (component.isVisible)
                                {
                                    this.componentsConstraints[component]
                                }
                                else
                                {
                                    null
                                }
                            }
                        }
                    queue.inQueue(constraint)
                    collectedConstraints.add(Pair(component, constraint))
                }
            }
        }

        var constraintLoop : ConstraintsLayoutConstraint? = null
        var sizeLoop = queue.size

        while (queue.isNotEmpty())
        {
            val constraint = queue.outQueue()

            if (constraint.allDependencyComputed || (constraintLoop == constraint && queue.size == sizeLoop))
            {
                constraintLoop = null
                this.placeLeftConstraints(constraint, constraint.horizontalSize == ConstraintsSize.EXPANDED)
                this.placeRightConstraints(constraint, constraint.horizontalSize == ConstraintsSize.EXPANDED)
                this.placeTopConstraints(constraint, constraint.verticalSize == ConstraintsSize.EXPANDED)
                this.placeBottomConstraints(constraint, constraint.verticalSize == ConstraintsSize.EXPANDED)
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



        synchronized(parent.treeLock)
        {
            for ((component, constraint) in collectedConstraints)
            {
                component.setLocation(constraint.x, constraint.y)
                component.setSize(constraint.xMax - constraint.x, constraint.yMax - constraint.y)
            }
        }
    }

    override fun getLayoutAlignmentX(target : Container) : Float = 0f

    override fun getLayoutAlignmentY(target : Container) : Float = 0f

    override fun invalidateLayout(target : Container) = Unit

    private fun computeSize(parent : Container, componentSize : (Component) -> Dimension) : Dimension
    {
        val queue = Queue<ConstraintsLayoutConstraint>()
        val collectedConstraints = ArrayList<ConstraintsLayoutConstraint>()

        for (component in parent.iterator())
        {
            if (component.isVisible)
            {
                val dimension = componentSize(component)

                this.componentsConstraints[component]?.let { constraint ->
                    constraint.x = 0
                    constraint.y = 0
                    constraint.xMax = dimension.width
                    constraint.yMax = dimension.height
                    constraint.computed = false
                    constraint.constraintsLeft =
                        constraint.leftConstraint.attached?.let { component -> if (component.isVisible) this.componentsConstraints[component] else null }
                    constraint.constraintsRight =
                        constraint.rightConstraint.attached?.let { component -> if (component.isVisible) this.componentsConstraints[component] else null }
                    constraint.constraintsTop =
                        constraint.topConstraint.attached?.let { component -> if (component.isVisible) this.componentsConstraints[component] else null }
                    constraint.constraintsBottom =
                        constraint.bottomConstraint.attached?.let { component -> if (component.isVisible) this.componentsConstraints[component] else null }
                    queue.inQueue(constraint)
                    collectedConstraints.add(constraint)
                }
            }
        }

        var constraintLoop : ConstraintsLayoutConstraint? = null
        var sizeLoop = queue.size

        while (queue.isNotEmpty())
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

        for (constraint in collectedConstraints)
        {
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

    private fun placeLeftConstraints(constraint : ConstraintsLayoutConstraint, resize : Boolean = false)
    {
        val atLeft = constraint.constraintsLeft ?: return
        val width = constraint.xMax - constraint.x

        when (constraint.leftConstraint)
        {
            is LeftToLeftOf, is LeftAtParent -> constraint.x = atLeft.x
            is LeftToCenterOf                -> constraint.x = (atLeft.xMax + atLeft.x) / 2
            is LeftToRightOf                 -> constraint.x = atLeft.xMax
            else                             -> Unit
        }

        if (! resize)
        {
            constraint.xMax = constraint.x + width
        }
    }

    private fun placeRightConstraints(constraint : ConstraintsLayoutConstraint, resize : Boolean = false)
    {
        val atRight = constraint.constraintsRight ?: return
        val width = constraint.xMax - constraint.x

        when (constraint.rightConstraint)
        {
            is RightToLeftOf                    -> constraint.xMax = atRight.x
            is RightToCenterOf                  -> constraint.xMax = (atRight.xMax + atRight.x) / 2
            is RightToRightOf, is RightAtParent -> constraint.xMax = atRight.xMax
            else                                -> Unit
        }

        if (! resize)
        {
            val constraintsLeft = constraint.constraintsLeft

            if (constraintsLeft != null)
            {
                constraint.x =
                    when (constraint.leftConstraint)
                    {
                        is LeftToLeftOf, is LeftAtParent -> (constraintsLeft.x + constraint.xMax - width) / 2
                        is LeftToCenterOf                -> ((constraintsLeft.x + constraintsLeft.xMax) / 2 + constraint.xMax - width) / 2
                        is LeftToRightOf                 -> (constraintsLeft.xMax + constraint.xMax - width) / 2
                        else                             -> constraint.xMax - width
                    }

                constraint.xMax = constraint.x + width
            }
            else
            {
                constraint.x = constraint.xMax - width
            }
        }
    }

    private fun placeTopConstraints(constraint : ConstraintsLayoutConstraint, resize : Boolean = false)
    {
        val atTop = constraint.constraintsTop ?: return
        val height = constraint.yMax - constraint.y

        when (constraint.topConstraint)
        {
            is TopToTopOf, is TopAtParent -> constraint.y = atTop.y
            is TopToCenterOf              -> constraint.y = (atTop.yMax + atTop.y) / 2
            is TopToBottomOf              -> constraint.y = atTop.yMax
            else                          -> Unit
        }

        if (! resize)
        {
            constraint.yMax = constraint.y + height
        }
    }

    private fun placeBottomConstraints(constraint : ConstraintsLayoutConstraint, resize : Boolean = false)
    {
        val atBottom = constraint.constraintsBottom ?: return
        val height = constraint.yMax - constraint.y

        when (constraint.bottomConstraint)
        {
            is BottomToTopOf                       -> constraint.yMax = atBottom.y
            is BottomToCenterOf                    -> constraint.yMax = (atBottom.yMax + atBottom.y) / 2
            is BottomToBottomOf, is BottomAtParent -> constraint.yMax = atBottom.yMax
            else                                   -> Unit
        }

        if (! resize)
        {
            val constraintsTop = constraint.constraintsTop

            if (constraintsTop != null)
            {
                constraint.y =
                    when (constraint.topConstraint)
                    {
                        is TopToTopOf, is TopAtParent -> (constraintsTop.y + constraint.yMax - height) / 2
                        is TopToCenterOf              -> ((constraintsTop.y + constraintsTop.yMax) / 2 + constraint.yMax - height) / 2
                        is TopToBottomOf              -> (constraintsTop.yMax + constraint.yMax - height) / 2
                        else                          -> constraint.yMax - height
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
