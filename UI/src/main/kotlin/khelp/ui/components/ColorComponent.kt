package khelp.ui.components

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JComponent

class ColorComponent(var color : Color) : JComponent()
{
    init
    {
        this.minimumSize = Dimension(16, 16)
        this.preferredSize = Dimension(64, 64)
    }

    override fun paintComponent(graphics : Graphics)
    {
        graphics.color = this.color
        val margin = this.insets
        graphics.fillRect(margin.left, margin.top,
                          this.width - margin.left - margin.right,
                          this.height - margin.top - margin.bottom)
    }
}
