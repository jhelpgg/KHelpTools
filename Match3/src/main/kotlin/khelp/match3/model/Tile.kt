package khelp.match3.model

import khelp.match3.ui.obtainImage
import khelp.ui.extensions.drawImageCenter
import java.awt.Graphics2D

class Tile(baseImageName : String)
{
    private val images = ArrayList<String>()
    var pingPong = false
    var rotated = false

    init
    {
        this.images.add(baseImageName)
    }

    fun addImage(imageName : String)
    {
        synchronized(this.images)
        {
            this.images.add(imageName)
        }
    }

    fun draw(xCenter : Int, yCenter : Int, frame : Int, graphics : Graphics2D)
    {
        val image =
            synchronized(this.images)
            {
                val index =
                    if (this.pingPong)
                    {
                        val size = this.images.size
                        val max = 2 * size - 1
                        val reference = frame % max

                        if (reference >= size)
                        {
                            max - reference - 1
                        }
                        else
                        {
                            reference
                        }
                    }
                    else
                    {
                        frame % this.images.size
                    }

                obtainImage(this.images[index])
            }

        if (this.rotated)
        {
            val transform = graphics.transform
            graphics.translate(xCenter.toDouble(), yCenter.toDouble())
            graphics.rotate(Math.PI / 2.0)
            graphics.drawImageCenter(- image.height / 2, image.width / 2, image)
            graphics.transform = transform
        }
        else
        {
            graphics.drawImageCenter(xCenter, yCenter, image)
        }
    }
}



