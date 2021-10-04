package khelp.editor.ui.patheditor

import fr.jhelp.utilities.random
import khelp.editor.ui.VERY_LIGHT_GRAY
import khelp.engine3d.geometry.path.Path
import khelp.engine3d.geometry.path.PathClose
import khelp.engine3d.geometry.path.PathCubic
import khelp.engine3d.geometry.path.PathElement
import khelp.engine3d.geometry.path.PathLine
import khelp.engine3d.geometry.path.PathLineTo
import khelp.engine3d.geometry.path.PathMove
import khelp.engine3d.geometry.path.PathQuadratic
import khelp.engine3d.render.TwoSidedRule
import khelp.engine3d.render.prebuilt.Revolution
import khelp.thread.TaskContext
import khelp.thread.delay
import khelp.thread.future.FutureResult
import khelp.ui.dsl.borderLayout
import khelp.ui.events.MouseManager
import khelp.ui.events.MouseState
import khelp.ui.events.MouseStatus
import khelp.ui.game.GameComponent
import khelp.utilities.extensions.bounds
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Point
import javax.swing.JPanel
import kotlin.math.abs
import kotlin.math.roundToInt

class PathView : JPanel()
{
    companion object
    {
        private const val GRID_SIZE = 16
        private val dashStroke =
            BasicStroke(1.25f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 10.0f, floatArrayOf(10f, 5f), 0f)
    }

    private val gameComponent = GameComponent(256, 512)
    private val gameImage = this.gameComponent.gameImage
    private val mouseManager = MouseManager.attachTo(this.gameComponent)
    private var lines : List<PathLine> = emptyList()
    private var controlPoints : List<ControlPoint> = emptyList()
    private var overControlIndex = - 1
    private var overElement : PathElement? = null
    private var selectControlIndex = - 1
    private var rightDownTime = 0L
    private var middleDownTime = 0L
    val revolution : Revolution = Revolution("EditedRevolution")

    var pathPrecision : Int = 7
        set(value)
        {
            val oldValue = field
            field = value.bounds(2, 12)

            if (field != oldValue)
            {
                this.postRefresh()
                this.refreshPath()
            }
        }
    var rotationPrecision : Int = 12
        set(value)
        {
            val oldValue = field
            field = value.bounds(3, 32)

            if (field != oldValue)
            {
                this.postRefresh()
            }
        }

    private val path = Path()
    private var postRefreshTimeOut : FutureResult<Unit>? = null

    init
    {
        borderLayout { center(gameComponent) }
        this.mouseManager.mouseStateObservable.observedBy(TaskContext.INDEPENDENT) { mouseState ->
            this.mouseState(mouseState, false)
        }

        this.revolution.twoSidedRule = TwoSidedRule.FORCE_TWO_SIDE
        this.revolution.showWire = true
    }

    fun addLine()
    {
        if (this.path.size == 0)
        {
            this.path.move(64f, 64f)
            this.path.line(64f, 512f - 64f)
        }
        else
        {
            this.path.line(random(10f, 200f), random(10f, 500f))
        }

        this.refreshPath()
        this.postRefresh()
    }

    private fun mouseState(mouseState : MouseState, fromRefreshPath : Boolean)
    {
        this.overControlIndex = - 1
        this.overElement = null
        var refreshPath = false
        val mousePoint = this.gameComponent.convertMousePositionToImagePosition(mouseState.x, mouseState.y)

        if (mouseState.mouseStatus == MouseStatus.DRAG && this.selectControlIndex >= 0)
        {
            val x = mousePoint.x.bounds(2, 254)
                .toFloat()
            val y = mousePoint.y.bounds(2, 510)
                .toFloat()
            refreshPath = true
            this.postRefresh()
            val (_, element, number, index) = this.controlPoints[this.selectControlIndex]

            if (number == 0)
            {
                if (index > 0)
                {
                    val previous = this.path[index - 1]

                    when (previous)
                    {
                        is PathClose     -> Unit
                        is PathMove      ->
                        {
                            previous.x = x
                            previous.y = y
                        }
                        is PathLineTo    ->
                        {
                            previous.x = x
                            previous.y = y
                        }
                        is PathQuadratic ->
                        {
                            previous.x = x
                            previous.y = y
                        }
                        is PathCubic     ->
                        {
                            previous.x = x
                            previous.y = y
                        }
                    }
                }
            }
            else
            {
                when (element)
                {
                    is PathClose     -> Unit
                    is PathMove      ->
                    {
                        element.x = x
                        element.y = y
                    }
                    is PathLineTo    ->
                    {
                        element.x = x
                        element.y = y
                    }
                    is PathQuadratic ->
                        when (number)
                        {
                            1    ->
                            {
                                element.controlX = x
                                element.controlY = y
                            }
                            else ->
                            {
                                element.x = x
                                element.y = y
                            }
                        }
                    is PathCubic     ->
                        when (number)
                        {
                            1    ->
                            {
                                element.control1X = x
                                element.control1Y = y
                            }
                            2    ->
                            {
                                element.control2X = x
                                element.control2Y = y
                            }
                            else ->
                            {
                                element.x = x
                                element.y = y
                            }
                        }
                }
            }
        }
        else if (mouseState.mouseStatus != MouseStatus.OUTSIDE)
        {
            val x = mousePoint.x
            val y = mousePoint.y

            for ((index, controlPoint) in this.controlPoints.withIndex())
            {
                val (point, element, _, indexElement) = controlPoint

                if (abs(x - point.x) <= 2 && abs(y - point.y) <= 2)
                {
                    this.overControlIndex = index
                    this.overElement = element

                    if (mouseState.leftButtonDown)
                    {
                        this.selectControlIndex = this.overControlIndex
                    }
                    else
                    {
                        this.selectControlIndex = - 1
                    }

                    if (mouseState.rightButtonDown)
                    {
                        if (System.currentTimeMillis() - this.rightDownTime > 256L)
                        {
                            this.rightDownTime = System.currentTimeMillis()

                            val newElement =
                                when (element)
                                {
                                    is PathLineTo    -> PathQuadratic(element.x - 32f, element.y - 32f,
                                                                      element.x, element.y)
                                    is PathQuadratic -> PathCubic(element.controlX - 16f, element.controlY - 16f,
                                                                  element.controlX + 16f, element.controlY + 16f,
                                                                  element.x, element.y)
                                    else             -> element
                                }

                            if (newElement != element)
                            {
                                this.path[indexElement] = newElement
                                this.refreshPath()
                                this.postRefresh()
                            }
                        }

                        this.rightDownTime = System.currentTimeMillis()
                    }

                    if (mouseState.middleButtonDown)
                    {
                        if (System.currentTimeMillis() - this.middleDownTime > 256L)
                        {
                            this.middleDownTime = System.currentTimeMillis()
                            var toRemove = false

                            val newElement =
                                when (element)
                                {
                                    is PathLineTo    ->
                                    {
                                        toRemove = true
                                        element
                                    }
                                    is PathQuadratic -> PathLineTo(element.x, element.y)
                                    is PathCubic     -> PathQuadratic((element.control1X + element.control2X) / 2f,
                                                                      (element.control1Y + element.control2Y) / 2f,
                                                                      element.x, element.y)
                                    else             -> element
                                }

                            if (toRemove)
                            {
                                this.path.remove(index)
                                this.refreshPath()
                                this.postRefresh()
                            }
                            else if (newElement != element)
                            {
                                this.path[indexElement] = newElement
                                this.refreshPath()
                                this.postRefresh()
                            }
                        }

                        this.middleDownTime = System.currentTimeMillis()
                    }

                    break
                }
            }
        }

        if (refreshPath && ! fromRefreshPath)
        {
            this.refreshPath()
        }
        else
        {
            this.refreshImage()
        }
    }

    private fun refreshPath()
    {
        this.lines = this.path.path(this.pathPrecision)
        val controlPoints = ArrayList<ControlPoint>()
        var previousElement : PathElement = PathClose

        for (line in this.lines)
        {
            val element = line.pathElement

            if (element != previousElement)
            {
                previousElement = element

                when (element)
                {
                    is PathClose     -> Unit
                    is PathMove      -> Unit
                    is PathLineTo    ->
                    {
                        controlPoints.add(ControlPoint(Point(line.x1.roundToInt(), line.y1.roundToInt()), element, 0,
                                                       line.elementIndex))
                        controlPoints.add(
                            ControlPoint(Point(element.x.roundToInt(), element.y.roundToInt()), element, 1,
                                         line.elementIndex))
                    }
                    is PathQuadratic ->
                    {
                        controlPoints.add(ControlPoint(Point(line.x1.roundToInt(), line.y1.roundToInt()), element, 0,
                                                       line.elementIndex))
                        controlPoints.add(
                            ControlPoint(Point(element.controlX.roundToInt(), element.controlY.roundToInt()),
                                         element, 1, line.elementIndex))
                        controlPoints.add(
                            ControlPoint(Point(element.x.roundToInt(), element.y.roundToInt()), element, 2,
                                         line.elementIndex))
                    }
                    is PathCubic     ->
                    {
                        controlPoints.add(ControlPoint(Point(line.x1.roundToInt(), line.y1.roundToInt()), element, 0,
                                                       line.elementIndex))
                        controlPoints.add(ControlPoint(Point(element.control1X.roundToInt(),
                                                             element.control1Y.roundToInt()),
                                                       element, 1, line.elementIndex))
                        controlPoints.add(ControlPoint(Point(element.control2X.roundToInt(),
                                                             element.control2Y.roundToInt()),
                                                       element, 2, line.elementIndex))
                        controlPoints.add(
                            ControlPoint(Point(element.x.roundToInt(), element.y.roundToInt()), element, 3,
                                         line.elementIndex))
                    }
                }
            }
        }

        this.controlPoints = controlPoints
        this.mouseState(this.mouseManager.mouseStateObservable.value(), true)
    }

    private fun refreshImage()
    {
        this.gameImage.clear(Color.WHITE)

        this.gameImage.draw { graphics2D ->
            // Grid
            graphics2D.color = VERY_LIGHT_GRAY
            graphics2D.stroke = BasicStroke(0.5f)

            for (y in PathView.GRID_SIZE .. 512 - PathView.GRID_SIZE step PathView.GRID_SIZE)
            {
                graphics2D.drawLine(0, y, 256, y)
            }

            for (x in PathView.GRID_SIZE .. 256 - PathView.GRID_SIZE step PathView.GRID_SIZE)
            {
                graphics2D.drawLine(x, 0, x, 512)
            }

            // Y = 0
            graphics2D.color = Color.LIGHT_GRAY
            graphics2D.stroke = PathView.dashStroke
            graphics2D.drawLine(0, 256, 256, 256)

            // Path
            for (line in this.lines)
            {
                if (line.pathElement == this.overElement)
                {
                    graphics2D.color = Color.BLUE
                    graphics2D.stroke = BasicStroke(2f)
                }
                else
                {
                    graphics2D.color = Color.BLACK
                    graphics2D.stroke = BasicStroke(1f)
                }

                graphics2D.drawLine(line.x1.roundToInt(), line.y1.roundToInt(),
                                    line.x2.roundToInt(), line.y2.roundToInt())

            }

            // Control pints
            for ((index, pair) in this.controlPoints.withIndex())
            {
                val (point, element) = pair
                val size =
                    if (element == this.overElement)
                    {
                        if (index == this.overControlIndex)
                        {
                            graphics2D.color = Color.GREEN
                            8
                        }
                        else
                        {
                            graphics2D.color = Color.BLUE
                            6
                        }
                    }
                    else
                    {
                        graphics2D.color = Color.RED
                        4
                    }

                val miSize = size / 2
                graphics2D.fillRect(point.x - miSize, point.y - miSize, size, size)
            }
        }
    }

    private fun postRefresh()
    {
        this.postRefreshTimeOut?.cancel("repost")
        this.postRefreshTimeOut = delay(128) { this.refreshRevolution() }
    }

    private fun refreshRevolution()
    {
        this.revolution.path(precision = this.pathPrecision, rotationPrecision = this.rotationPrecision) {
            for (index in 0 until path.size)
            {
                val element = path[index]

                when (element)
                {
                    is PathClose     ->
                        close()
                    is PathMove      ->
                        move(element.x / 256f, (256f - element.y) / 256f)
                    is PathLineTo    ->
                        line(element.x / 256f, (256f - element.y) / 256f)
                    is PathQuadratic ->
                        quadratic(element.controlX / 256f, (256f - element.controlY) / 256f,
                                  element.x / 256f, (256f - element.y) / 256f)
                    is PathCubic     ->
                        cubic(element.control1X / 256f, (256f - element.control1Y) / 256f,
                              element.control2X / 256f, (256f - element.control2Y) / 256f,
                              element.x / 256f, (256f - element.y) / 256f)
                }
            }

        }
    }
}