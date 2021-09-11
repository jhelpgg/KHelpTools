package khelp.ui.components

import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JComponent

class EmptyComponent : JComponent()
{
    init
    {
        val size = Dimension(1, 1)
        this.size = size
        this.preferredSize = size
        this.minimumSize = size
        this.maximumSize = size
        this.isVisible = false
    }

    override fun paintComponent(ignored : Graphics) = Unit
}