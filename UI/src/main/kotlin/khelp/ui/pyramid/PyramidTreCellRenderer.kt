package khelp.ui.pyramid

import java.awt.Color
import java.awt.Component
import java.awt.Graphics
import javax.swing.JLabel
import javax.swing.JTree
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.tree.TreeCellRenderer
import khelp.ui.border.RoundedBorder
import khelp.ui.utilities.TITLE_FONT
import khelp.ui.utilities.TRANSPARENT
import khelp.utilities.collections.tree.Tree

@Suppress("UNCHECKED_CAST")
class PyramidTreCellRenderer :      DefaultTreeCellRenderer()
{
    init
    {
        this.font = TITLE_FONT.font
        this.border = RoundedBorder(color = Color.BLUE, thickness = 0)
    }

    override fun getTreeCellRendererComponent(tree : JTree, value : Any,
                                              selected : Boolean, expanded : Boolean, leaf : Boolean,
                                              row : Int, hasFocus : Boolean) : Component
    {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus)
        val valueInt = (value as Tree<Int>).value

        this.foreground =
            when
            {
                valueInt < 0  -> Color.RED
                valueInt == 0 -> Color.BLUE
                else          -> Color.BLACK
            }

        this.background = if (selected) Color.GREEN else TRANSPARENT

        this.text = valueInt.toString()
        return this
    }

    override fun paintComponent(g : Graphics)
    {
        g.color = this.background
        g.fillRect(0, 0, this.width, this.height)
        super.paintComponent(g)
    }
}