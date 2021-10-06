package khelp.editor.ui.components.color

import khelp.engine3d.render.Color4f
import khelp.engine3d.render.GRAY
import khelp.ui.game.GameImage
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JComponent

class Color4fPreview(small : Boolean = false) : JComponent()
{
    var color : Color4f = GRAY
        set(value)
        {
            field = value
            this.repaint()
        }

    init
    {
        val size = if (small) 64 else 128
        val dimension = Dimension(size, size)
        this.size = dimension
        this.preferredSize = dimension
        this.minimumSize = dimension
    }

    override fun paintComponent(graphics : Graphics)
    {
        val margin = this.insets
        val x = margin.left
        val y = margin.top
        val width = this.width - margin.left - margin.right
        val height = this.height - margin.top - margin.bottom
        graphics as Graphics2D
        val paint = graphics.paint
        graphics.paint = GameImage.DARK_LIGHT_PAINT
        graphics.fillRect(x, y, width, height)
        graphics.paint = paint
        graphics.color = this.color.color
        graphics.fillRect(x, y, width, height)
    }
}
