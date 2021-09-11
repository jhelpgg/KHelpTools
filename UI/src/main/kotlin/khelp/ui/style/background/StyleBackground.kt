package khelp.ui.style.background

import java.awt.Graphics2D
import java.awt.Shape

interface StyleBackground
{
    fun applyOnShape(graphics2D : Graphics2D, shape : Shape)
}
