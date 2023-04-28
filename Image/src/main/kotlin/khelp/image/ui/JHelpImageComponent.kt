package khelp.image.ui

import khelp.image.JHelpImage
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JComponent
import kotlin.math.max

class JHelpImageComponent(width : Int, height : Int) : JComponent()
{
    val image = JHelpImage(max(1, width), max(1, height))

    init
    {
        val size = Dimension(width, height)
        this.size = size
        this.preferredSize = size
        this.minimumSize = size
        this.maximumSize = size
        this.image.register(this)
    }

    override fun paintComponent(graphics : Graphics)
    {
        graphics.drawImage(this.image.image, 0, 0, this.width, this.height, null)
    }
}