package khelp.engine3d.gui.component

import khelp.engine3d.gui.GUIMargin
import khelp.engine3d.resource.Resources3D
import khelp.ui.events.MouseState
import khelp.ui.events.MouseStatus
import khelp.ui.extensions.grayVersion
import khelp.ui.game.GameImage
import khelp.utilities.extensions.bounds
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D
import kotlin.math.max
import kotlin.math.min

class GUIComponentScroll(private val viewPort : GUIComponentPanel<*, *>) : GUIComponent()
{
    companion object
    {
        private val left = GameImage.loadThumbnail("icons/button_arrow_left.png", Resources3D.resources, 32, 32)
        private val leftDisabled = GUIComponentScroll.left.grayVersion()
        private val up = GUIComponentScroll.left.rotate270()
        private val upDisabled = GUIComponentScroll.up.grayVersion()
        private val right = GUIComponentScroll.left.rotate180()
        private val rightDisabled = GUIComponentScroll.right.grayVersion()
        private val down = GUIComponentScroll.left.rotate90()
        private val dowDisabled = GUIComponentScroll.down.grayVersion()
        private const val SCROLL_SPEED = 16
    }

    private var scrollX = 0
    private var scrollY = 0
    private var mouseX = 0
    private var mouseY = 0
    private var insideViewPort = false

    init
    {
        val size = this.viewPort.preferredSize()
        this.viewPort.x = 0
        this.viewPort.y = 0
        this.viewPort.width = size.width
        this.viewPort.height = size.height
        this.viewPort.relayoutWithPreferred()
        this.margin = GUIMargin(4, 0, 0, 4)
    }

    override fun drawIntern(graphics2D : Graphics2D, margin : GUIMargin)
    {
        this.viewPort.relayoutWithPreferred()
        val width = this.width - margin.width
        val height = this.height - margin.height
        val viewPortWidth = width - 64
        val viewPortHeight = height - 64
        val x = margin.left
        val y = margin.right
        val left = if (this.viewPort.x < 0) GUIComponentScroll.left else GUIComponentScroll.leftDisabled
        val up = if (this.viewPort.y < 0) GUIComponentScroll.up else GUIComponentScroll.upDisabled
        val right =
            if (this.viewPort.width + this.viewPort.x > viewPortWidth) GUIComponentScroll.right else GUIComponentScroll.rightDisabled
        val down =
            if (this.viewPort.height + this.viewPort.y > viewPortHeight) GUIComponentScroll.down else GUIComponentScroll.dowDisabled
        graphics2D.color = Color.GRAY

        graphics2D.fillRoundRect(x + 32, y, viewPortWidth, 32, 16, 16)
        up.drawOn(graphics2D, (this.width - 32) / 2, y)

        graphics2D.fillRoundRect(x + 32, y + height - 32, viewPortWidth, 32, 16, 16)
        down.drawOn(graphics2D, (this.width - 32) / 2, y + height - 32)

        graphics2D.fillRoundRect(x, y + 32, 32, viewPortHeight, 16, 16)
        left.drawOn(graphics2D, x, (this.height - 32) / 2)

        graphics2D.fillRoundRect(x + width - 32, y + 32, 32, viewPortHeight, 16, 16)
        right.drawOn(graphics2D, x + width - 32, (this.height - 32) / 2)

        val transform = graphics2D.transform
        val clip = graphics2D.clip
        graphics2D.clipRect(x + 32, y + 32, viewPortWidth, viewPortHeight)
        graphics2D.translate(x + 32 + this.viewPort.x, y + 32 + this.viewPort.y)
        this.viewPort.draw(graphics2D)
        graphics2D.transform = transform
        graphics2D.clip = clip

        if (this.scrollX < 0 && this.viewPort.width + this.viewPort.x + this.scrollX <= viewPortWidth)
        {
            this.scrollX = viewPortWidth - this.viewPort.width - this.viewPort.x + 1
        }
        else if (this.scrollX > 0 && this.viewPort.x + this.scrollX >= 0)
        {
            this.scrollX = - this.viewPort.x - 1
        }

        if (this.scrollX != 0 && this.viewPort.x + this.scrollX < 0 && this.viewPort.width + this.viewPort.x + this.scrollX > viewPortWidth)
        {
            this.viewPort.x += this.scrollX
        }

        if (this.scrollY < 0 && this.viewPort.height + this.viewPort.y + this.scrollY <= viewPortHeight)
        {
            this.scrollY = viewPortHeight - this.viewPort.height - this.viewPort.y + 1
        }
        else if (this.scrollY > 0 && this.viewPort.y + this.scrollY >= 0)
        {
            this.scrollY = - this.viewPort.y - 1
        }

        if (this.scrollY != 0 && this.viewPort.y + this.scrollY < 0 && this.viewPort.height + this.viewPort.y + this.scrollY > viewPortHeight)
        {
            this.viewPort.y += this.scrollY
        }
    }

    override fun preferredSize(margin : GUIMargin) : Dimension
    {
        val size = this.viewPort.preferredSize()
        return Dimension(size.width + 64 + this.margin.width, size.height + 64 + this.margin.height)
    }

    override fun mouseState(mouseState : MouseState) : Boolean
    {
        if (mouseState.mouseStatus == MouseStatus.EXIT || mouseState.mouseStatus == MouseStatus.ENTER)
        {
            // Those events cause troubles, we do them in another way just for view port (see bellow)
            return true
        }

        val width = this.width - this.margin.width
        val height = this.height - this.margin.height
        val xx = this.margin.left
        val yy = this.margin.top
        val viewPortWidth = width - 64
        val viewPortHeight = height - 64

        val x = mouseState.x
        val y = mouseState.y

        if (mouseState.mouseStatus == MouseStatus.DRAG)
        {
            this.viewPort.x = (this.viewPort.x - this.mouseX + x).bounds(viewPortWidth - this.viewPort.width, 0)
            this.viewPort.y = (this.viewPort.y - this.mouseY + y).bounds(viewPortHeight - this.viewPort.height, 0)
        }

        if (mouseState.mouseStatus == MouseStatus.DRAG || mouseState.mouseStatus == MouseStatus.MOVE)
        {
            this.mouseX = x
            this.mouseY = y
        }

        if (mouseState.mouseStatus == MouseStatus.DRAG)
        {
            // Drag consume the event
            return true
        }

        if (x > xx + 32 && y > yy + 32 && x < width - 32 && y < height - 32)
        {
            if (! this.insideViewPort)
            {
                this.insideViewPort = true
                this.viewPort.mouseState(MouseState(MouseStatus.ENTER,
                                                    x - xx - 32 - this.viewPort.x - this.margin.left,
                                                    y - yy - 32 - this.viewPort.y - this.margin.top,
                                                    mouseState.leftButtonDown, mouseState.middleButtonDown,
                                                    mouseState.rightButtonDown,
                                                    mouseState.shiftDown, mouseState.controlDown, mouseState.altDown,
                                                    mouseState.clicked))
            }

            val state = MouseState(mouseState.mouseStatus,
                                   x - xx - 32 - this.viewPort.x - this.margin.left,
                                   y - yy - 32 - this.viewPort.y - this.margin.top,
                                   mouseState.leftButtonDown, mouseState.middleButtonDown, mouseState.rightButtonDown,
                                   mouseState.shiftDown, mouseState.controlDown, mouseState.altDown,
                                   mouseState.clicked)
            return this.viewPort.mouseState(state)
        }

        if (this.insideViewPort)
        {
            this.insideViewPort = false
            this.viewPort.mouseState(MouseState(MouseStatus.EXIT,
                                                x - xx - 32 - this.viewPort.x - this.margin.left,
                                                y - yy - 32 - this.viewPort.y - this.margin.top,
                                                mouseState.leftButtonDown, mouseState.middleButtonDown,
                                                mouseState.rightButtonDown,
                                                mouseState.shiftDown, mouseState.controlDown, mouseState.altDown,
                                                mouseState.clicked))
        }


        if (mouseState.leftButtonDown)
        {
            when
            {
                x > xx + 32 && x < xx + viewPortWidth && y < 32 && this.viewPort.y < 0                           ->
                    this.scrollY = min(GUIComponentScroll.SCROLL_SPEED, - this.viewPort.y)

                x > xx + 32 && x < xx + viewPortWidth && this.viewPort.height + this.viewPort.y > viewPortHeight ->
                    this.scrollY = max(- GUIComponentScroll.SCROLL_SPEED, viewPortHeight - this.viewPort.height)

                x < 32 && y > yy + 32 && y < yy + viewPortHeight && this.viewPort.x < 0                          ->
                    this.scrollX = min(GUIComponentScroll.SCROLL_SPEED, - this.viewPort.x)

                y > yy + 32 && y < yy + viewPortHeight && this.viewPort.width + this.viewPort.x > viewPortWidth  ->
                    this.scrollX = max(- GUIComponentScroll.SCROLL_SPEED, viewPortWidth - this.viewPort.width)
            }
        }
        else if (mouseState.mouseStatus == MouseStatus.STAY)
        {
            this.scrollX = 0
            this.scrollY = 0
        }

        return true
    }

}
