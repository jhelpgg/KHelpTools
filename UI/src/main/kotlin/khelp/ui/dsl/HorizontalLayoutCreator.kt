package khelp.ui.dsl

import khelp.ui.layout.constraints.ConstraintsSize
import java.util.concurrent.atomic.AtomicInteger
import javax.swing.JComponent

class HorizontalLayoutCreator(private val constraintsLayoutCreator : ConstraintsLayoutCreator)
{
    companion object
    {
        private const val BASE = "HorizontalLayoutCreator_"
        private val nextID = AtomicInteger(0)
        private val nextName : String get() = HorizontalLayoutCreator.BASE + HorizontalLayoutCreator.nextID.getAndIncrement()
    }

    private var previousName = ""

    operator fun JComponent.unaryPlus()
    {
        val name = HorizontalLayoutCreator.nextName

        constraintsLayoutCreator.add(this, name) {
            horizontalSize = ConstraintsSize.WRAPPED
            verticalSize = ConstraintsSize.EXPANDED
            topAtParent
            bottomAtParent

            if (previousName.isEmpty())
            {
                leftAtParent
            }
            else
            {
                leftAtRight = previousName
            }

            rightFree
        }

        previousName = name
    }
}