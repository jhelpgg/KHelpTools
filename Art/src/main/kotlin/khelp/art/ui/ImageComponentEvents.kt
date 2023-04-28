package khelp.art.ui

import khelp.art.ARC_SIZE
import khelp.image.BLACK_ALPHA_MASK
import khelp.image.BLUE
import khelp.image.JHelpImage
import khelp.image.JHelpSprite
import khelp.image.RED
import khelp.utilities.math.radianToDegree
import khelp.utilities.math.square
import java.awt.Point
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

internal class ImageComponentEvents(private val mainImage : JHelpImage,
                                    private val pathSprite : JHelpSprite) : MouseListener,
                                                                            MouseMotionListener
{
    val points = ArrayList<Point>()
    private var x = Int.MIN_VALUE
    private var y = Int.MIN_VALUE
    private val sprite = this.pathSprite.image()
        .createSprite(0, 0, this.pathSprite.width(), this.pathSprite.height())

    override fun mouseClicked(mouseEvent : MouseEvent)
    {
        if (this.x < 0 && this.y < 0)
        {
            this.x = mouseEvent.x
            this.y = mouseEvent.y
            this.points.add(Point(this.x, this.y))

            this.draw { image -> image.fillCircle(this@ImageComponentEvents.x, this@ImageComponentEvents.y, 3, RED) }

            return
        }

        var xx = mouseEvent.x.toDouble()
        var yy = mouseEvent.y.toDouble()
        val diffX = xx - this.x
        val diffY = yy - this.y
        val distance = sqrt(square(diffX) + square(diffY))

        if (distance < 1.0)
        {
            return
        }

        val angle = atan2(diffY, diffX)
        xx = this.x + ARC_SIZE * cos(angle)
        yy = this.y + ARC_SIZE * sin(angle)
        val x = xx.toInt()
        val y = yy.toInt()

        this.draw { image -> image.drawLine(this.x, this.y, x, y, RED)

            val minX = min(this.x, x)
            val minY = min(this.y, y)
            val w = max(this.x, x) - minX
            val h = max(this.y,y) - minY
            val m = max(w,h)
            image.drawCircle((this.x+ x)/2,(this.y+y)/2, ARC_SIZE/ 2, BLACK_ALPHA_MASK)
         //   image.drawArc(minX, minY, ARC_SIZE, ARC_SIZE, BLACK_ALPHA_MASK, radianToDegree(angle + PI/2.0), 180.0)
        }

        this.x = x
        this.y = y
        this.points.add(Point(x, y))

        this.drawSprite { image -> image.clear() }
    }

    override fun mousePressed(mouseEvent : MouseEvent)
    {
    }

    override fun mouseReleased(mouseEvent : MouseEvent)
    {
    }

    override fun mouseEntered(mouseEvent : MouseEvent)
    {
    }

    override fun mouseExited(mouseEvent : MouseEvent)
    {
    }

    override fun mouseDragged(mouseEvent : MouseEvent)
    {
    }

    override fun mouseMoved(mouseEvent : MouseEvent)
    {
        if (this.x < 0 && this.y < 0)
        {
            return
        }

        var xx = mouseEvent.x.toDouble()
        var yy = mouseEvent.y.toDouble()
        val diffX = xx - this.x
        val diffY = yy - this.y
        val distance = sqrt(square(diffX) + square(diffY))

        if (distance < 1.0)
        {
            return
        }

        val angle = atan2(diffY, diffX)
        xx = this.x + ARC_SIZE * cos(angle)
        yy = this.y + ARC_SIZE * sin(angle)
        val x = xx.toInt()
        val y = yy.toInt()

        this.drawSprite { image ->
            image.clear()
            image.drawLine(this.x - 1, this.y - 1, x - 1, y - 1, BLUE)
            image.drawLine(this.x, this.y, x, y, BLUE)
            image.drawLine(this.x + 1, this.y + 1, x + 1, y + 1, BLUE)
        }
    }

    private fun draw(drawer : (JHelpImage) -> Unit)
    {
        this.pathSprite.visible(false)
        val image = this.pathSprite.image()
        image.startDrawMode()
        drawer(image)
        image.endDrawMode()
        this.pathSprite.visible(true)
        this.mainImage.update()
    }

    private fun drawSprite(drawer : (JHelpImage) -> Unit)
    {
        this.pathSprite.visible(false)
        this.sprite.visible(false)
        val image = this.sprite.image()
        image.startDrawMode()
        drawer(image)
        image.endDrawMode()
        this.sprite.visible(true)
        this.pathSprite.image()
            .update()
        this.pathSprite.visible(true)
        this.mainImage.update()
    }
}
