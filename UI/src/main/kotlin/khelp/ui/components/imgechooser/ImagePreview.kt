package khelp.ui.components.imgechooser

import khelp.ui.extensions.drawImage
import khelp.ui.game.GameImage
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JComponent

class ImagePreview : JComponent()
{
    init
    {
        val dimension = Dimension(128, 128)
        this.size = dimension
        this.preferredSize = dimension
        this.minimumSize = dimension
    }

    var image : GameImage = GameImage.DUMMY
        set(value)
        {
            field = value
            this.repaint()
        }

    override fun paintComponent(graphics : Graphics)
    {
        val margin = this.insets
        val x = margin.left
        val y = margin.top
        val width = this.width - margin.left - margin.right
        val height = this.height - margin.top - margin.bottom
        graphics as Graphics2D
        graphics.paint = GameImage.DARK_LIGHT_PAINT
        graphics.fillRect(x, y, width, height)
        graphics.drawImage(x, y, width, height, this.image)
    }
}