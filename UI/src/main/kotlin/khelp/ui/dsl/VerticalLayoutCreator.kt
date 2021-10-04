package khelp.ui.dsl

import khelp.ui.layout.constraints.ConstraintsSize
import java.util.concurrent.atomic.AtomicInteger
import javax.swing.JComponent

class VerticalLayoutCreator(private val constraintsLayoutCreator : ConstraintsLayoutCreator)
{
    companion object
    {
        private const val BASE = "VerticalLayoutCreator_"
        private val nextID = AtomicInteger(0)
        private val nextName : String get() = VerticalLayoutCreator.BASE + VerticalLayoutCreator.nextID.getAndIncrement()
    }

    private var previousName = ""

    operator fun JComponent.unaryPlus()
    {
        val name = VerticalLayoutCreator.nextName

        constraintsLayoutCreator.add(this, name) {
            horizontalSize = ConstraintsSize.WRAPPED
            verticalSize = ConstraintsSize.WRAPPED

            if (previousName.isEmpty())
            {
                topAtParent
            }
            else
            {
                topAtBottom = previousName
            }

            bottomFree
            leftAtParent
            rightFree
        }

        previousName = name
    }

    operator fun JComponent.unaryMinus()
    {
        val name = VerticalLayoutCreator.nextName

        constraintsLayoutCreator.add(this, name) {
            horizontalSize = ConstraintsSize.WRAPPED
            verticalSize = ConstraintsSize.WRAPPED

            if (previousName.isEmpty())
            {
                topAtParent
            }
            else
            {
                topAtBottom = previousName
            }

            bottomFree
            leftAtLeft = previousName
            rightAtRight = previousName
        }

        previousName = name
    }
}