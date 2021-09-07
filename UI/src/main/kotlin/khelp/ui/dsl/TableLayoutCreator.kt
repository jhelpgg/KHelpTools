package khelp.ui.dsl

import khelp.ui.layout.table.TableLayout
import khelp.ui.layout.table.TableLayoutConstraint
import java.awt.Component
import java.awt.Container
import javax.swing.JPanel

class TableLayoutCreator internal constructor(private val container : Container, tableAspect : Boolean = false)
{
    private val tableLayout = TableLayout(tableAspect)

    init
    {
        this.container.layout = this.tableLayout
    }

    val dynamicStructure get() = this.tableLayout.dynamicStructure()

    var marginTop : Int
        get() = this.tableLayout.getMargins().top
        set(value)
        {
            val margin = this.tableLayout.getMargins()
            margin.top = value
            this.tableLayout.setMargins(margin)
        }

    var marginBottom : Int
        get() = this.tableLayout.getMargins().bottom
        set(value)
        {
            val margin = this.tableLayout.getMargins()
            margin.bottom = value
            this.tableLayout.setMargins(margin)
        }

    var marginLeft : Int
        get() = this.tableLayout.getMargins().left
        set(value)
        {
            val margin = this.tableLayout.getMargins()
            margin.left = value
            this.tableLayout.setMargins(margin)
        }

    var marginRight : Int
        get() = this.tableLayout.getMargins().right
        set(value)
        {
            val margin = this.tableLayout.getMargins()
            margin.right = value
            this.tableLayout.setMargins(margin)
        }

    fun fixStructure(numberOfColumns : Int, numberOfRows : Int) =
        this.tableLayout.fixStructure(numberOfColumns, numberOfRows)

    fun Component.cell(x : Int, y : Int, numberCellWidth : Int = 1, numberCellHeight : Int = 1)
    {
        this@TableLayoutCreator.container.add(this, TableLayoutConstraint(x, y, numberCellWidth, numberCellHeight))
    }

    fun panel(x : Int, y : Int, numberCellWidth : Int = 1, numberCellHeight : Int = 1, creator : JPanel.() -> Unit)
    {
        val panel = JPanel()
        creator(panel)
        this.container.add(panel, TableLayoutConstraint(x, y, numberCellWidth, numberCellHeight))
    }
}
