package khelp.ui.dsl

import khelp.ui.utilities.computePreferredDimension
import javax.swing.JComponent
import kotlin.math.max

class VerticalLayoutCreator(private val tableLayoutCreator : TableLayoutCreator,
                            marginVertical : Int, marginLeft : Int)
{
    private var y = 0

    init
    {
        this.tableLayoutCreator.marginLeft = max(0, marginLeft)
        this.tableLayoutCreator.marginBottom = max(0, marginVertical)
    }

    operator fun JComponent.unaryPlus()
    {
        this@VerticalLayoutCreator.addComponent {
            val size = computePreferredDimension(this@unaryPlus)
            this@unaryPlus.cell(0, this@VerticalLayoutCreator.y, 1, size.height)
            this@VerticalLayoutCreator.y += size.height
        }
    }

    private fun addComponent(tableLayoutCreator : TableLayoutCreator.() -> Unit)
    {
        tableLayoutCreator(this.tableLayoutCreator)
    }
}