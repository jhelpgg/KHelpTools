package khelp.ui.game

import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JComponent
import kotlin.math.max

class GameComponent(width : Int, height : Int) : JComponent()
{
    val gameImage = GameImage(max(1, width), max(1, height))

    init
    {
        val dimension = Dimension(this.gameImage.width, this.gameImage.height)
        this.size = dimension
        this.preferredSize = dimension
        this.maximumSize = dimension
        this.minimumSize = dimension
        this.gameImage.refreshListener = this::repaint
    }

    override fun paintComponent(graphics : Graphics)
    {
        graphics.drawImage(this.gameImage.image, 0, 0, this.width, this.height, this)
    }
}
