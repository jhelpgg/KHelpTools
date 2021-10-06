package khelp.ui.components

import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JComponent

class EmptyComponent(width:Int=1,height:Int=1) : JComponent()
{
    init
    {
        val size = Dimension(width, height)
        this.size = size
        this.preferredSize = size
        this.minimumSize = size
        this.maximumSize = size
        this.isVisible = false
    }

    override fun paintComponent(ignored : Graphics) = Unit
}