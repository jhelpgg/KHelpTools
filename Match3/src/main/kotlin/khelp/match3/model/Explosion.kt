package khelp.match3.model

import khelp.utilities.math.random
import khelp.match3.ui.obtainImage
import khelp.ui.extensions.drawImageCenter
import java.awt.Graphics2D

class Explosion
{
    companion object
    {
        private const val MAXIMUM_INDEX = 5
        private val COLORS = arrayOf("blue", "green", "pink", "red")
        private const val NAME_TAIL = ".png"
        private fun baseName() : String = "explosion${COLORS.random()}0"
    }

    private var index = 1
    private val baseName = Explosion.baseName()

    fun drawExplosion(xCenter : Int, yCenter : Int, graphics : Graphics2D) : Boolean
    {
        if (this.index > Explosion.MAXIMUM_INDEX)
        {
            return false
        }

        val name = "${this.baseName}${this.index}${Explosion.NAME_TAIL}"
        graphics.drawImageCenter(xCenter, yCenter, obtainImage(name))
        this.index ++
        return this.index <= Explosion.MAXIMUM_INDEX
    }
}
