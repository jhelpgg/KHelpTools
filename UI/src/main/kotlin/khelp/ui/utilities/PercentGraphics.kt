package khelp.ui.utilities

import khelp.ui.TextAlignment
import khelp.ui.extensions.drawText
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.Image

class PercentGraphics internal constructor(private val graphics2D : Graphics2D,
                                           private val width : Int, private val height : Int)
{
    private val Double.width : Int get() = (this * this@PercentGraphics.width).toInt()
    private val Double.height : Int get() = (this * this@PercentGraphics.height).toInt()

    var color : Color
        get() = this.graphics2D.color
        set(value)
        {
            this.graphics2D.color = value
        }

    var font : Font
        get() = this.graphics2D.font
        set(value)
        {
            this.graphics2D.font = font
        }

    fun drawLine(xPercent1 : Double, yPercent1 : Double, xPercent2 : Double, yPercent2 : Double)
    {
        this.graphics2D.drawLine(xPercent1.width, yPercent1.height, xPercent2.width, yPercent2.height)
    }

    fun drawRectangle(xPercent : Double, yPercent : Double, widthPercent : Double, heightPercent : Double)
    {
        this.graphics2D.drawRect(xPercent.width, yPercent.height, widthPercent.width, heightPercent.height)
    }

    fun drawRoundRectangle(xPercent : Double, yPercent : Double,
                           widthPercent : Double, heightPercent : Double,
                           arcWidthPercent : Double, arcHeightPercent : Double)
    {
        this.graphics2D.drawRoundRect(xPercent.width, yPercent.height,
                                      widthPercent.width, heightPercent.height,
                                      arcWidthPercent.width, arcHeightPercent.height)
    }

    fun drawOval(xPercent : Double, yPercent : Double, widthPercent : Double, heightPercent : Double)
    {
        this.graphics2D.drawOval(xPercent.width, yPercent.height, widthPercent.width, heightPercent.height)
    }

    fun drawText(xPercent : Double, yPercent : Double, text : String,
                 textAlignment : TextAlignment = TextAlignment.LEFT)
    {
        this.graphics2D.drawText(xPercent.width, yPercent.height, text, textAlignment)
    }

    fun drawImage(xPercent : Double, yPercent : Double, image : Image)
    {
        this.graphics2D.drawImage(image, xPercent.width, yPercent.height, null)
    }

    fun drawImageFit(xPercent : Double, yPercent : Double, image : Image,
                     widthPercent : Double, heightPercent : Double)
    {
        this.graphics2D.drawImage(image,
                                  xPercent.width, yPercent.height,
                                  widthPercent.width, heightPercent.height,
                                  null)
    }

    fun drawImagePart(xImagePercent1 : Double, yImagePercent1 : Double,
                      xImagePercent2 : Double, yImagePercent2 : Double,
                      image : Image,
                      xDrawPercent1 : Double, yDrawPercent1 : Double,
                      xDrawPercent2 : Double, yDrawPercent2 : Double)
    {
        val width = image.getWidth(null)
        val height = image.getHeight(null)
        this.graphics2D.drawImage(image,
                                  xDrawPercent1.width, yDrawPercent1.height,
                                  xDrawPercent2.width, yDrawPercent2.height,
                                  (xImagePercent1 * width).toInt(), (yImagePercent1 * height).toInt(),
                                  (xImagePercent2 * width).toInt(), (yImagePercent2 * height).toInt(),
                                  null)
    }

    fun drawImagePart(xImage1 : Int, yImage1 : Int, xImage2 : Int, yImage2 : Int,
                      image : Image,
                      xDrawPercent1 : Double, yDrawPercent1 : Double, xDrawPercent2 : Double, yDrawPercent2 : Double)
    {
        this.graphics2D.drawImage(image,
                                  xDrawPercent1.width, yDrawPercent1.height, xDrawPercent2.width, yDrawPercent2.height,
                                  xImage1, yImage1, xImage2, yImage2,
                                  null)
    }

    fun fillRectangle(xPercent : Double, yPercent : Double, widthPercent : Double, heightPercent : Double)
    {
        this.graphics2D.fillRect(xPercent.width, yPercent.height, widthPercent.width, heightPercent.height)
    }

    fun fillRoundRectangle(xPercent : Double, yPercent : Double,
                           widthPercent : Double, heightPercent : Double,
                           arcWidthPercent : Double, arcHeightPercent : Double)
    {
        this.graphics2D.fillRoundRect(xPercent.width, yPercent.height,
                                      widthPercent.width, heightPercent.height,
                                      arcWidthPercent.width, arcHeightPercent.height)
    }

    fun fillOval(xPercent : Double, yPercent : Double, widthPercent : Double, heightPercent : Double)
    {
        this.graphics2D.fillOval(xPercent.width, yPercent.height, widthPercent.width, heightPercent.height)
    }
}