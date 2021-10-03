package khelp.editor.ui

import khelp.engine3d.render.Window3D
import khelp.engine3d.render.window3DFix
import khelp.thread.TaskContext
import khelp.thread.future.FutureResult
import khelp.thread.future.Promise
import khelp.thread.parallel
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.geom.Point2D
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.JComponent
import kotlin.math.roundToInt

object Component3D : JComponent()
{
    init
    {
        this.minimumSize = Dimension(320, 200)
        this.preferredSize = Dimension(800, 600)
    }

    private val alreadyCreated = AtomicBoolean(false)
    private val promise = Promise<Window3D>()
    val window3D : FutureResult<Window3D> = this.promise.futureResult

    override fun paintComponent(graphics : Graphics)
    {
        graphics.color = Color.BLACK
        graphics.fillRect(0, 0, this.width, this.height)

        if (this.alreadyCreated.compareAndSet(false, true))
        {
            val transform = (graphics as Graphics2D).transform
            val start = transform.transform(Point2D.Double(), Point2D.Double())
            val end = transform.transform(Point2D.Double(this.width.toDouble(), this.height.toDouble()),
                                          Point2D.Double())
            val x = start.x.roundToInt()
            val y = start.y.roundToInt()
            val width = (end.x - start.x).roundToInt()
            val height = (end.y - start.y).roundToInt()

            parallel(TaskContext.INDEPENDENT, this.promise, Rectangle(x, y, width, height)) { promise, bounds ->
                window3DFix(bounds.x, bounds.y, bounds.width, bounds.height,
                            "Editor") { promise.result(this) }
            }
        }
    }
}
