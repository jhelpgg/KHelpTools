package khelp.engine3d.utils.delaunay

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.JComponent
import khelp.thread.Locker
import khelp.ui.TextAlignment
import khelp.ui.extensions.drawText
import kotlin.math.ceil
import kotlin.math.round

class DelaunaySpyComponent(delaunay : Delaunay) : JComponent(),
                                                  MouseListener
{
    companion object
    {
        const val RAY = 16
        const val DIAMETER = DelaunaySpyComponent.RAY * 2
    }

    private var points : List<PointIndexed> = emptyList()
    private var triangles : List<TriangleIndexed> = emptyList()
    private val locked = AtomicBoolean(false)
    private val locker = Locker()
    private val circle : EnclosingCircle

    init
    {
        val dimension = Dimension(1024, 1024)
        this.preferredSize = dimension
        this.size = dimension
        this.minimumSize = dimension
        this.maximumSize = dimension

        val circle = EnclosingCircle()

        for (point in delaunay.points())
        {
            circle.addPoint(point.x, point.y)
        }

        this.circle = circle

        Thread { delaunay.triangles(this::step) }.start()
        this.addMouseListener(this)
    }

    override fun paintComponent(g : Graphics)
    {
        val graphics = g as Graphics2D
        graphics.color = Color.WHITE
        graphics.fillRect(0, 0, this.width, this.height)

        synchronized(this.locker)
        {
            for (point in this.points)
            {
                graphics.color = Color.RED
                val x = round(point.x).toInt()
                val y = round(point.y).toInt()
                graphics.fillOval(x - DelaunaySpyComponent.RAY,
                                  y - DelaunaySpyComponent.RAY,
                                  DelaunaySpyComponent.DIAMETER,
                                  DelaunaySpyComponent.DIAMETER)
                graphics.color = Color.WHITE

                for (j in -1..1)
                {
                    for (i in -1..1)
                    {
                        graphics.drawText(x + i, y + j, "${point.index}", TextAlignment.CENTER)
                    }
                }

                graphics.color = Color.BLACK
                graphics.drawText(x, y, "${point.index}", TextAlignment.CENTER)
            }

            graphics.color = Color.BLUE
            graphics.stroke = BasicStroke(1.5f)
            val ray = ceil(this.circle.ray).toInt()
            val diameter = ray * 2
            graphics.drawOval(round(circle.centerX).toInt() - ray,
                              round(circle.centerY).toInt() - ray,
                              diameter,
                              diameter)

            graphics.color = Color.BLACK
            graphics.stroke = BasicStroke(3f)

            for (triangle in this.triangles)
            {
                val x1 = round(triangle.point1.x).toInt()
                val y1 = round(triangle.point1.y).toInt()
                val x2 = round(triangle.point2.x).toInt()
                val y2 = round(triangle.point2.y).toInt()
                val x3 = round(triangle.point3.x).toInt()
                val y3 = round(triangle.point3.y).toInt()
                graphics.drawLine(x1, y1, x2, y2)
                graphics.drawLine(x2, y2, x3, y3)
                graphics.drawLine(x3, y3, x1, y1)
            }
        }
    }

    override fun mouseClicked(mouseEvent : MouseEvent)
    {
        if (this.locked.compareAndSet(true, false))
        {
            this.locker.unlock()
        }
    }

    override fun mousePressed(mouseEvent : MouseEvent) = Unit

    override fun mouseReleased(mouseEvent : MouseEvent) = Unit

    override fun mouseEntered(mouseEvent : MouseEvent) = Unit

    override fun mouseExited(mouseEvent : MouseEvent) = Unit

    private fun step(points : List<PointIndexed>, triangles : List<TriangleIndexed>)
    {
        synchronized(this.locker)
        {
            this.points = points
            this.triangles = triangles
        }

        this.repaint()
        this.validate()
        this.locked.set(true)
        this.locker.lock()
    }
}